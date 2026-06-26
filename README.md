# Links de Estudo (Multitenancy API)

Uma API REST desenvolvida em Spring Boot para gerenciamento e compartilhamento de links de estudo (bookmarks). O sistema extrai metadados das URLs (como título, descrição e imagens) automaticamente usando Web Scraping e associa os links a usuários autenticados via JWT (JSON Web Tokens).

## 🚀 Tecnologias Utilizadas

* **Java 21**
* **Spring Boot 4.1.0** (com Spring MVC e JPA/Hibernate)
* **Spring Security 7.1.0** (Autenticação e Autorização)
* **JSON Web Tokens (JWT)** (via biblioteca `jjwt`)
* **PostgreSQL** (Banco de dados de desenvolvimento/produção)
* **H2 Database** (Banco de dados em memória para testes)
* **Jsoup 1.17.2** (Web scraping e extração de metadados de páginas HTML)
* **Lombok** (Produtividade e redução de código boilerplate)
* **Maven** (Gerenciamento de dependências e automação de builds)

---

## 🛠️ Pré-requisitos

* Java JDK 21 instalado.
* Banco de dados PostgreSQL rodando localmente.
* Uma ferramenta para testar requisições (como Postman, Insomnia ou extensão REST Client do VS Code).

---

## ⚙️ Configuração do Banco de Dados

Crie um banco de dados no PostgreSQL com o nome `multitenancy_db`.
No arquivo `src/main/resources/application.properties`, configure as credenciais de acesso se forem diferentes das padrão:

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/multitenancy_db
spring.datasource.username=seu_usuario
spring.datasource.password=sua_senha
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
```

---

## 🔑 Fluxo de Autenticação e Endpoints

Todas as rotas sob `/api/bookmarks/**` são protegidas e requerem o token JWT no cabeçalho HTTP da seguinte forma:
`Authorization: Bearer <seu_token>`

### 1. Rotas de Autenticação (Públicas)

#### 📝 Cadastro de Usuário
Cria uma nova conta criptografando a senha em BCrypt.
* **POST** `/api/auth/register`
* **JSON de Envio**:
```json
{
  "email": "seu_email@email.com",
  "senha": "sua_senha_secreta"
}
```

#### 🔑 Login
Autentica o usuário e retorna o Token JWT.
* **POST** `/api/auth/login`
* **JSON de Envio**:
```json
{
  "email": "seu_email@email.com",
  "senha": "sua_senha_secreta"
}
```
* **Resposta (Text/Plain)**: Um token longo criptografado em JWT (ex: `eyJhbGciOiJIUzI1NiJ9...`).

---

### 2. Rotas de Bookmarks (Protegidas)

*Lembre-se de enviar o cabeçalho `Authorization: Bearer <token_recebido_no_login>` em todas as requisições abaixo.*

#### ➕ Criar Bookmark
Recebe uma URL, extrai as meta-tags (título, descrição) de forma automatizada e salva o link vinculado ao seu usuário.
* **POST** `/api/bookmarks`
* **JSON de Envio**:
```json
{
  "url": "https://spring.io"
}
```

#### 📋 Listar Todos os Bookmarks
Retorna todos os links registrados.
* **GET** `/api/bookmarks`

#### 🔍 Buscar por ID
Retorna os detalhes de um link específico.
* **GET** `/api/bookmarks/{id}`

#### 📝 Atualizar Bookmark
Edita os dados de título ou URL de um link existente.
* **PUT** `/api/bookmarks/{id}`
* **JSON de Envio**:
```json
{
  "url": "https://spring.io/projects",
  "title": "Spring Projects"
}
```

#### ❌ Excluir Bookmark
Remove um link do sistema.
* **DELETE** `/api/bookmarks/{id}`

---

## 🏃 Como Executar a Aplicação

### Via Terminal
Execute o comando Maven na raiz do projeto:
```powershell
# Windows PowerShell
.\mvnw.cmd spring-boot:run

# Linux/macOS
./mvnw spring-boot:run
```

### Via IDE (IntelliJ IDEA)
1. Certifique-se de realizar o **Reload do Maven** para sincronizar as dependências.
2. Dê um **Build** -> **Rebuild Project** para compilar as classes.
3. Clique em **Run** na classe principal `Multitenancy2Application.java`.

---

## 🧪 Como Executar a Suíte de Testes

O projeto conta com testes unitários e de integração completos com banco H2 em memória e contexto Spring MockMvc.
Para rodar os testes:
```powershell
# Windows PowerShell
.\mvnw.cmd test

# Linux/macOS
./mvnw test
```
