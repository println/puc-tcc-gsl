# Prova de conceito: GESTÃO DE SERVIÇOS DE LOGÍSTICA (GSL)

Esta é a prova de conceito para o trabalho de conclusão do curso (TCC) de Arquitetura de Software Distribuído, da PUC Minas. A ideia é, a partir de um problema, apresentar uma solução arquitetural, implementar a prova de conceito e gerar resultados que demonstram a eficácia da solução.
O TCC completo pode ser obtido aqui: [Download do TCC](https://github.com/println/puc-tcc-gsl/raw/microservices/.dev/doc/boa-entrega.pdf).

## O Problema
A empresa de logística Boa Entrega está passando por mudanças e deseja se manter relevante no mercado, que, atualmente, contém diversos novos concorrentes e desafios. A empresa contém uma stack tecnológica que necessita de atualização e da inclusão de novas ferramentas. Neste trabalho, apresentamos a proposta arquitetural GSL, que é escalável, distribuída, fácil de modificar e evoluir. Ela é baseada em microsserviços, interoperável, via API, com mobile e Web, e conta com ferramentas de BI e Big Data. Os serviços são autônomos e se comunicam via mensagens pelo Kafka. Adicionalmente, são pequenos, tolerantes a falha e simples de manter. A proposta arquitetural GSL busca resolver os problemas do negócio da Boa Entrega, mas deixa uma série de decisões em aberto, para o futuro, com o objetivo comportar a evolução do negócio.

## Visão Geral da Solução
Esta é a arquitetura proposta. Ela contém os componentes novos e os legados, em nuvens pública e privada, se comunicando via mensagens.

![Arquitetura](https://raw.githubusercontent.com/println/puc-tcc-gsl/microservices/.dev/doc/arquitetura.png)

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
   1. Konga
1. Padrões arquiteturais (ref: https://microservices.io/patterns)
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

## Execução da prova de conceito
> **_NOTA:_** Os arquivos do ambiente estão na pasta .dev

### Requisitos:
1. Linux
1. Docker com kubernetes ativado
1. Docker Compose
1. Postman

### Subir ambiente
1. Subir os serviços no docker compose: `.dev/environment/run-with-kafka.sh` (ele vai pegar o ip automaticamente)

Opcional:
1. Limpeza de ambiente sujo: `.dev/environment/run-clean.sh`

### Procedimentos
1. Docker: Gerar imagem de todos os serviços: `docker-build-all.sh`
1. Kubernetes: Implantar (deploy) todas as imagens do docker no kubernetes: `kubernetes-deploy-all.sh`

Opcional:
1. Docker: Executar todos os serviços via docker: `docker-run-all.sh`
1. Docker: Encerrar a execução de todos os serviços no docker: `docker-stop-all.sh`
1. Kubernetes: Remover todas imagens implantadas: `kubernetes-delete-all.sh`

### Teste de BDD (POSTMAN)
> **_NOTA:_** Importar os arquivos do Postman de `.dev/postman/`
1. Executar teste de como mostrado no video: Testabilidade
1. Incluir delay entre os testes de: 2000ms

## Resultados 
1. Apresentação cenário 1 - Desempenho: 
   - vídeo: https://drive.google.com/file/d/1iAyH8E8KhluQoNz_EnyCP2mbi-fh0-kG/view?usp=sharing
1. Apresentação cenário 2 - Manutenibilidade: 
   - vídeo: https://drive.google.com/file/d/1snEA8CDAozyKJIdkCz52Mid5DHE3Y17K/view?usp=sharing
1. Apresentação cenário 3 - Testabilidade: 
   - vídeo: https://drive.google.com/file/d/1YtuhUaq8U75TRdYqaXvL1yr6_qBO2dn3/view?usp=share_link
1. Apresentação cenário 4 – Tolerância a falhas: 
   - vídeo: https://drive.google.com/file/d/1WLMXLyUx_QeE9AcsTL-ynblHaNg_bcSB/view?usp=share_link
1. POC monolito implantado: https://boa-entrega.herokuapp.com/
