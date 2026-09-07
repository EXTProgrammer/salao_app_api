# 💇‍♂️ API - Sistema de Agendamento para Salões

![Status](https://img.shields.io/badge/Status-Em_Desenvolvimento-yellow)
![Java](https://img.shields.io/badge/Java-ED8B00?style=for-the-badge&logo=java&logoColor=white)
![Spring Boot](https://img.shields.io/badge/Spring_Boot-F2F4F9?style=for-the-badge&logo=spring-boot)

Esta é uma API RESTful desenvolvida para gerenciar o backend de um sistema de agendamentos para salão de beleza. O projeto tem como foco o fornecimento de endpoints seguros e eficientes para o gerenciamento de horários, clientes e avaliações.

## 🚀 Tecnologias Utilizadas
* **Java** (JDK 21)
* **Spring Boot** (Web, Data JPA, Security, JWT)
* **Banco de Dados** (PostgreSQL)
* **Maven** para gerenciamento de dependências

## ⚙️ Funcionalidades Principais
* **Gerenciamento de Serviços:** Endpoints para criação, listagem e cancelamento de agendamentos, clientes, profissionais e serviços.
* **Segurança e Autenticação:** Acesso a recursos protegido através da implementação de **Spring Security** (autenticação/autorização).
* **Validação de Dados:** Entradas da API consistentes utilizando o sistema de validações do Spring (ex: `@Valid`, tratamento de exceções).
* **Confiabilidade:** Lógicas principais cobertas por **Testes Unitários**, garantindo a estabilidade das regras de negócio.
* **Sistema de Avaliações:** Lógica para recebimento e processamento de avaliações (reviews) dos clientes após os serviços.
* **Integração com Client-Side:** API estruturada para consumo pelo aplicativo mobile (React Native/Expo).

## 🛠️ Como rodar o projeto localmente

### Pré-requisitos
* Java 21+ instalado
* Maven instalado
* Banco de dados configurado localmente

### Passos
1. Clone este repositório:
   ```bash
   git clone [https://github.com/EXTProgrammer/salao_app_api.git]

2. Configure as credenciais do seu banco de dados no arquivo
   src/main/resources/application.properties.

3. Execute o projeto usando o Maven:
   mvn spring-boot:run

4. A API estará disponível em http://localhost:8080.

## 📌 Próximos Passos
- [ ] Implementação de Dashboard Administrativo
- [ ] Implementação de CI/CD via GitHub Actions
- [ ] Documentação da API com Swagger/OpenAPI
- [ ] Testes de Integração
