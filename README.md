# Hexagonal Customer API

Projeto de estudos para aplicar e compreender os conceitos de arquitetura hexagonal, também conhecida como Ports and Adapters, usando Java e Spring Boot.

O objetivo principal não é entregar uma API completa de produção, mas exercitar a separação entre domínio, casos de uso, portas de entrada, portas de saída e adapters de infraestrutura.

## Objetivos de Estudo

- Separar regras de aplicação de detalhes de framework e infraestrutura.
- Modelar portas de entrada para casos de uso chamados por adapters HTTP.
- Modelar portas de saída para persistência e integrações externas.
- Implementar adapters para controller REST, MongoDB e cliente HTTP com Feign.
- Testar unidades isoladas sem depender do contexto completo do Spring.
- Usar CI para validar a suíte de testes a cada push ou pull request.

## Arquitetura

O projeto segue a ideia central da arquitetura hexagonal: o core da aplicação não deve conhecer detalhes de entrada e saída.

```text
adapters/in        -> entrada da aplicação, como controllers REST
application/core   -> domínio e casos de uso
application/ports  -> contratos de entrada e saída
adapters/out       -> persistência, Feign clients e mappers externos
config             -> wiring dos casos de uso com Spring
```

Fluxo principal de cadastro de cliente:

```text
HTTP Controller
  -> InsertCustomerInputPort
  -> InsertCustomerUseCase
  -> FindAddresByZipCodeOutputPort
  -> InsertCustomerOutputPort
  -> MongoDB Adapter
```

## Tecnologias

- Java 17
- Spring Boot
- Spring Web MVC
- Spring Data MongoDB
- Spring Cloud OpenFeign
- MapStruct
- Lombok
- JUnit 5
- Mockito
- Maven
- GitHub Actions

## Como Rodar os Testes

```bash
mvn test
```

A suíte atual contém testes unitários para:

- Caso de uso de cadastro de cliente.
- Controller de cadastro.
- Mappers MapStruct.
- Adapters de persistência e busca de endereço.

## Como Rodar a Aplicação

Para executar a aplicação completa, é necessário ter:

- MongoDB disponível em `mongodb://localhost:27017/hexagonal`.
- Um serviço de endereços respondendo em `http://localhost:8082/addresses/{zipCode}`.

Com as dependências disponíveis:

```bash
mvn spring-boot:run
```

Endpoint principal:

```http
POST /api/v1/customers
Content-Type: application/json

{
  "name": "Maria",
  "cpf": "12345678901",
  "zipCode": "60100-000"
}
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

Este projeto ainda está em evolução. Alguns pontos naturais para próximos estudos são validação de CPF/CEP, tratamento de erro em integrações externas, testes de integração com MongoDB, configuração correta de ambientes e proteção de dados pessoais como CPF.
