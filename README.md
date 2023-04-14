# Prova de conceito: GESTÃO DE SERVIÇOS DE LOGÍSTICA (GSL)


## Visão geral
Este código é uma prova de conceito para o TCC do curso Arquitetura de Software Distribuído - PUC Minas.

## O Problema
A empresa de logística Boa Entrega está passando por mudanças e deseja se manter relevante no mercado, que, atualmente, contém diversos novos concorrentes e desafios. A empresa contém uma stack tecnológica que necessita de atualização e da inclusão de novas ferramentas. Neste trabalho, apresentamos a proposta arquitetural GSL, que é escalável, distribuída, fácil de modificar e evoluir. Ela é baseada em microsserviços, interoperável, via API, com mobile e Web, e conta com ferramentas de BI e Big Data. Os serviços são autônomos e se comunicam via mensagens pelo Kafka. Adicionalmente, são pequenos, tolerantes a falha e simples de manter. A proposta arquitetural GSL busca resolver os problemas do negócio da Boa Entrega, mas deixa uma série de decisões em aberto, para o futuro, com o objetivo comportar a evolução do negócio.

## Proposta arquitetural
![Arquitetura](https://raw.githubusercontent.com/println/puc-tcc-gsl/microservices/.dev/doc/arquitetura.png)

[Download do TCC](https://github.com/println/puc-tcc-gsl/raw/microservices/.dev/doc/boa-entrega.pdf)

## Tecnologias
1. Java / Kotlin
1. Spring
   1. Spring boot
   1. Spring Cloud   Hibernate (HIBERNATE)
   1. Spring Data
   1. Spring Boot Test
   1. Spring Doc
1. Mensageria
   1. Kafka
   1. Avro
   1. RabbitMQ
1. API REST
   1. Json
   1. Spring DOC
   1. Swagger-UI
1. Armazenamento
   1. Spring Data 
   1. PostgreSQL
1. Testes
   1. H2
   1. Junit 
   1. Spring Boot Test 
   1. Postman (POSTMAN)
   1. Testes de unidade, integração e BDD (Postman)
1. Ambiente    
   1. Docker    
   1. Kong
   1. Kafka
   1. PostgreSQL
   1. Kubernetes
1. Devtools
   1. Docker: Docker Registry e Kubernetes
   1. Docker Compose (DEV): Kong, Kafka, PostgreSQL
   1. Gradle / Maven
   1. Shell Script
   1. GIT
   1. Postman
   1. SonarQube
   1. JMeter
   1. Kafka UI
1. Padrões arquiteturais
    1. Microservice Architecture
    1. Monolithic Architecture
    1. Transactional outbox
    1. Saga
       1. Choreography-based
       1. Orchestration-based
    1. Event sourcing
    1. Anti-corruption layer (ACL)
    1. Database per service
    1. API Composition + Pattern: API Gateway / Backends for Frontends
    1. Padrão de pipes e filtros
1. Técnicas de desenvolvimento
    1. Event Driven Architecture
    1. EVENT STORMING
    1. Domain-Driven Design
1. Métricas
    1. SonarQube
    1. JMeter

## Instalação e execução da prova de conceito
