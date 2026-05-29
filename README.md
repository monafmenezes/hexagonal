# Hexagonal Customer API

Projeto de estudos para aplicar e compreender os conceitos de arquitetura hexagonal, também conhecida como Ports and Adapters, usando Java e Spring Boot.

O objetivo principal não é entregar uma API completa de produção, mas exercitar a separação entre domínio, casos de uso, portas de entrada, portas de saída e adapters de infraestrutura.

## Objetivos de Estudo

- Separar regras de aplicação de detalhes de framework e infraestrutura.
- Modelar portas de entrada para casos de uso chamados por adapters HTTP.
- Modelar portas de saída para persistência e integrações externas.
- Implementar adapters para controller REST, MongoDB e cliente HTTP com Feign.
- Integrar Apache Kafka como adapter de saída para envio de CPF para validação e como adapter de entrada para receber o resultado da validação.
- Testar unidades isoladas sem depender do contexto completo do Spring.
- Validar as regras da arquitetura hexagonal com testes automatizados usando ArchUnit.
- Implementar tratamento de erros global com exceções de domínio e respostas HTTP adequadas.
- Escrever testes de integração com banco de dados real usando Testcontainers.
- Validar entrada com Bean Validation nos DTOs e retornar erros descritivos por campo.
- Paginar resultados com `Page<T>` e `Pageable` sem carregar todos os registros em memória.
- Aplicar cache in-memory nos adapters de saída, mantendo o núcleo da aplicação livre de dependências de framework.
- Usar CI para validar a suíte de testes a cada push ou pull request.

## Arquitetura

O projeto segue a ideia central da arquitetura hexagonal: o core da aplicação não deve conhecer detalhes de entrada e saída.

```text
adapters/in        -> entrada da aplicação: controllers REST e consumers Kafka
application/core   -> domínio e casos de uso
application/ports  -> contratos de entrada e saída
adapters/out       -> persistência, Feign clients, Kafka producer e mappers externos
config             -> wiring dos casos de uso com Spring
```

Fluxo de cadastro de cliente:

```text
HTTP Controller
  -> InsertCustomerInputPort
  -> InsertCustomerUseCase
  -> FindAddresByZipCodeOutputPort (Feign)
  -> InsertCustomerOutputPort
  -> MongoDB Adapter
  -> SendCpfForValidationOutputPort
  -> SendCpfForValidationAdapter (Kafka Producer → tp-cpf-validation)
```

Fluxo de validação de CPF via Kafka:

```text
ReceiveValidatedCpfConsumer (Kafka Consumer ← tp-cpf-validated)
  -> UpdateCustomerInputPort
  -> UpdateCustomerUseCase
  -> FindAddresByZipCodeOutputPort (Feign)
  -> UpdateCustomerOutputPort
  -> MongoDB Adapter
```

Fluxo de consulta de cliente por ID (com cache):

```text
HTTP Controller
  -> FindCustomerByIdInputPort
  -> FindCustomerByIdUseCase
  -> FindCustomerByIdOutputPort
  -> FindCustomerByIdAdapter (@Cacheable — retorna do cache se já consultado)
  -> MongoDB Adapter (consultado apenas no primeiro acesso)
```

Fluxo de listagem paginada:

```text
HTTP Controller (GET /api/v1/customers?page=0&size=10)
  -> FindAllCustomersInputPort
  -> FindAllCustomersUseCase
  -> FindAllCustomersOutputPort
  -> FindAllCustomersAdapter
  -> MongoDB Adapter (com skip e limit)
```

## Tecnologias

- Java 17
- Spring Boot
- Spring Web MVC
- Spring Data MongoDB
- Spring Cloud OpenFeign
- Apache Kafka (Spring Kafka)
- Spring Cache (ConcurrentMapCacheManager)
- Bean Validation (Jakarta Validation + Hibernate Validator)
- MapStruct
- Lombok
- JUnit 5
- Mockito
- ArchUnit
- Testcontainers
- Maven
- GitHub Actions

## Como Rodar os Testes

```bash
mvn test
```

Os testes de integração sobem um container MongoDB via Testcontainers, portanto é necessário ter o **Docker rodando** na máquina.

A suíte atual contém testes unitários para:

- Casos de uso de cadastro, consulta por ID, atualização e remoção de cliente.
- Controller de cadastro, consulta, listagem paginada e validação de entrada com `@WebMvcTest` + `MockMvc`.
- Mappers MapStruct.
- Adapters de persistência, consulta por ID e busca de endereço.

Testes de integração com Testcontainers para:

- Operações de salvar, buscar, atualizar e deletar no `CustomerRepository` com MongoDB real (`CustomerRepositoryIT`).

Testes de comportamento de cache para:

- Verificar que o banco é consultado apenas uma vez para o mesmo ID após o resultado ser armazenado em cache.

E testes de arquitetura com ArchUnit para:

- Garantir que a camada `Application` não acessa `Adapters` nem `Config`.
- Garantir que a camada `Adapters` não é acessada diretamente por outras camadas além de `Config`.
- Garantir que a camada `Config` não é acessada por nenhuma outra camada.
- Garantir que `application.core` não depende de `org.springframework.cache`.
- Garantir que `@Cacheable` e `@CacheEvict` só existem em classes dentro de `adapters`.
- Garantir que o domínio não importa nada dos adapters.

## Como Rodar a Aplicação

Para executar a aplicação completa, é necessário ter:

- MongoDB disponível em `mongodb://localhost:27017/hexagonal`.
- Um serviço de endereços respondendo em `http://localhost:8082/addresses/{zipCode}`.
- Apache Kafka disponível em `localhost:9092`.

Com as dependências disponíveis:

```bash
mvn spring-boot:run
```

### Tópicos Kafka

| Tópico             | Direção | Descrição                                          |
|--------------------|---------|----------------------------------------------------|
| `tp-cpf-validation` | Saída   | CPF enviado para validação após cadastro do cliente |
| `tp-cpf-validated`  | Entrada | Resultado da validação recebido para atualizar o cliente |

### Endpoints

Listar clientes (paginado):

```http
GET /api/v1/customers?page=0&size=10&sort=name,asc
```

Resposta esperada:

```json
{
  "content": [
    { "name": "Maria", "cpf": "12345678901", "isValidCpf": true, "address": { ... } }
  ],
  "totalElements": 42,
  "totalPages": 5,
  "size": 10,
  "number": 0
}
```

Criar cliente:

```http
POST /api/v1/customers
Content-Type: application/json

{
  "name": "Maria",
  "cpf": "12345678901",
  "zipCode": "60100-000"
}
```

Consultar cliente por ID:

```http
GET /api/v1/customers/{id}
```

Resposta esperada:

```json
{
  "name": "Maria",
  "address": {
    "street": "Rua A",
    "city": "Fortaleza",
    "state": "CE"
  },
  "cpf": "12345678901",
  "isValidCpf": true
}
```

Resposta quando o cliente não existe (`404`):

```json
{
  "timestamp": "2026-05-29T18:00:00",
  "status": 404,
  "error": "Not Found",
  "message": "Cliente não encontrado com o id: abc",
  "path": "/api/v1/customers/abc"
}
```

Resposta quando a requisição tem campos inválidos (`400`):

```json
{
  "timestamp": "2026-05-29T18:00:00",
  "status": 400,
  "error": "Bad Request",
  "message": "cpf: O CPF deve ter 11 dígitos",
  "path": "/api/v1/customers"
}
```

Atualizar cliente:

```http
PUT /api/v1/customers/{id}
Content-Type: application/json

{
  "name": "Maria Silva",
  "cpf": "12345678901",
  "zipCode": "60100-000"
}
```

Remover cliente:

```http
DELETE /api/v1/customers/{id}
```

## CI

O projeto possui workflow em `.github/workflows/ci.yml`.

O CI roda em:

- `push` para `main`, `develop` e branches `feature/**`.
- `pull_request`.

Comando executado no pipeline:

```bash
mvn --batch-mode test
```

## Observações

Este projeto ainda está em evolução. Alguns pontos naturais para próximos estudos são testes de integração com Kafka, segundo agregado `Order` relacionado ao `Customer`, CQRS separando portas de leitura e escrita, observabilidade com Micrometer e Prometheus, e Outbox Pattern para consistência entre MongoDB e Kafka.