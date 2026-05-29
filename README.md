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

Fluxo de consulta de cliente por ID:

```text
HTTP Controller
  -> FindCustomerByIdInputPort
  -> FindCustomerByIdUseCase
  -> FindCustomerByIdOutputPort
  -> MongoDB Adapter
```

## Tecnologias

- Java 17
- Spring Boot
- Spring Web MVC
- Spring Data MongoDB
- Spring Cloud OpenFeign
- Apache Kafka (Spring Kafka)
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
- Controller de cadastro e consulta.
- Mappers MapStruct.
- Adapters de persistência, consulta por ID e busca de endereço.

Testes de integração com Testcontainers para:

- Operações de salvar, buscar, atualizar e deletar no `CustomerRepository` com MongoDB real (`CustomerRepositoryIT`).

E testes de arquitetura com ArchUnit para:

- Garantir que a camada `Application` não acessa `Adapters` nem `Config`.
- Garantir que a camada `Adapters` não é acessada diretamente por outras camadas além de `Config`.
- Garantir que a camada `Config` não é acessada por nenhuma outra camada.

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
  "message": "Customer not found"
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

Este projeto ainda está em evolução. Alguns pontos naturais para próximos estudos são testes de integração com Kafka, validação de entrada com Bean Validation nos DTOs, paginação no `GET /customers`, cache com Spring Cache no `FindCustomerByIdUseCase`, e expansão dos testes de arquitetura com regras mais granulares por subcamada.