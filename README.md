# 🎯 Bookmarks Manager API (Links de Estudo)

Uma **API RESTful** robusta e segura desenvolvida com **Java 21** e **Spring Boot 4.1.0** para gerenciamento e categorização de links de estudo (bookmarks). O sistema extrai metadados de qualquer link (como título e descrição) automaticamente utilizando Web Scraping, associando-os a usuários autenticados de forma segura.

---

## 🌟 Principais Recursos
*   **Autenticação JWT (JSON Web Tokens):** Controle de acesso seguro a recursos e endpoints específicos.
*   **Web Scraping Automático:** Extração automática de metadados das páginas web (utilizando **Jsoup**).
*   **DTO Pattern:** Tráfego de dados seguro e limpo (impedindo o vazamento de hashes de senhas e dados internos do framework).
*   **CORS Habilitado:** API configurada para receber requisições de frontends externos (Vercel, Netlify, etc.).
*   **Ambiente Cloud Ready:** Configuração flexível usando variáveis de ambiente (`application.properties`).
*   **Dockerizado:** Setup rápido de build e execução local ou em nuvem por meio de containers Docker.
*   **Testes Automatizados:** Suíte de testes unitários e de integração completos com JUnit 5 e H2 database.

---

## 🌐 Documentação Interativa da API (Swagger UI)

A API conta com documentação interativa utilizando **Springdoc OpenAPI (Swagger UI)**. Nela, você pode visualizar todos os endpoints, schemas de retorno e fazer requisições de teste diretamente no navegador:

👉 **URL do Swagger UI:** [https://links-de-estudo.onrender.com/swagger-ui/index.html](https://links-de-estudo.onrender.com/swagger-ui/index.html)

> 💡 **Dica de Teste:** O Swagger suporta o fluxo completo. Cadastre um usuário em `/api/auth/register`, faça o login em `/api/auth/login`, copie o token gerado, clique no botão **"Authorize"** no topo da tela do Swagger, cole o token e teste os endpoints protegidos `/api/bookmarks/**`.

---

## 🔑 Endpoints Principais

### Autenticação (Públicos)
| Método | Endpoint | Descrição | Exemplo de Payload (Request) |
| :--- | :--- | :--- | :--- |
| `POST` | `/api/auth/register` | Cria uma nova conta de usuário. | `{"email": "teste@email.com", "senha": "123"}` |
| `POST` | `/api/auth/login` | Autentica e retorna o Token JWT. | `{"email": "teste@email.com", "senha": "123"}` |

### Bookmarks (Protegidos por Token JWT)
*É necessário enviar o cabeçalho `Authorization: Bearer <seu_token>` nas rotas abaixo.*

| Método | Endpoint | Descrição | Exemplo de Payload (Request) |
| :--- | :--- | :--- | :--- |
| `GET` | `/api/bookmarks` | Lista todos os bookmarks cadastrados. | *Nenhum* |
| `GET` | `/api/bookmarks/{id}` | Busca os detalhes de um bookmark pelo ID. | *Nenhum* |
| `POST` | `/api/bookmarks` | Salva um link e extrai seus metadados. | `{"url": "https://spring.io"}` |
| `PUT` | `/api/bookmarks/{id}` | Atualiza o título e URL de um bookmark. | `{"url": "https://spring.io/projects", "title": "Spring"}` |
| `DELETE` | `/api/bookmarks/{id}` | Deleta um bookmark existente. | *Nenhum* |

---

## 💻 Exemplos de Consumo (Como consumir a API)

Abaixo estão trechos de código prontos demonstrando como fazer login na API e consumir a lista de bookmarks usando **JavaScript**, **Python** e **Java**.

### 1. JavaScript (Fetch API / Frontend)
```javascript
const API_URL = "https://links-de-estudo.onrender.com";

// 1. Fazer login e salvar o Token JWT
async function login(email, senha) {
  const response = await fetch(`${API_URL}/api/auth/login`, {
    method: "POST",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify({ email, senha })
  });
  
  const token = await response.text();
  localStorage.setItem("token", token);
  console.log("Token salvo com sucesso!");
  return token;
}

// 2. Buscar Bookmarks autenticados
async function obterBookmarks() {
  const token = localStorage.getItem("token");
  const response = await fetch(`${API_URL}/api/bookmarks`, {
    method: "GET",
    headers: {
      "Authorization": `Bearer ${token}`,
      "Content-Type": "application/json"
    }
  });
  
  const data = await response.json();
  console.log("Bookmarks:", data);
}
```

### 2. Python (Requests Library)
```python
import requests

API_URL = "https://links-de-estudo.onrender.com"

# 1. Autenticar para obter o Token
def obter_token(email, senha):
    payload = {"email": email, "senha": senha}
    response = requests.post(f"{API_URL}/api/auth/login", json=payload)
    if response.status_code == 200:
        return response.text
    raise Exception("Falha na autenticação")

# 2. Criar um novo Bookmark
def cadastrar_bookmark(token, url):
    headers = {"Authorization": f"Bearer {token}"}
    payload = {"url": url}
    response = requests.post(f"{API_URL}/api/bookmarks", headers=headers, json=payload)
    return response.json()
```

### 3. Java (HttpClient nativo - Java 11+)
```java
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class ApiClient {
    private static final String API_URL = "https://links-de-estudo.onrender.com";

    public static String fazerLogin(String email, String senha) throws Exception {
        String payload = String.format("{\"email\":\"%s\",\"senha\":\"%s\"}", email, senha);
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(API_URL + "/api/auth/login"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(payload))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        return response.body(); // Retorna a String do Token JWT
    }
}
```

---

## 🐳 Execução Local (Maven & Docker)

### Requisitos locais:
*   Java JDK 21
*   Docker (opcional)
*   PostgreSQL local rodando na porta `5432` com usuário `root` e senha `root` (ou crie um banco local chamado `multitenancy_db`).

### Executando com Maven:
```bash
# Compilar e rodar a aplicação Spring Boot
./mvnw spring-boot:run
```

### Executando com Docker:
```bash
# Construir a imagem Docker localmente
docker build -t bookmarks-api .

# Rodar a imagem expondo na porta 8080
docker run -p 8080:8080 bookmarks-api
```

### Executando a suíte de testes:
```bash
./mvnw test
```

---

## 🚀 Deploy em Produção (Render + Neon)
Para publicar este projeto gratuitamente:
1.  Crie um banco de dados PostgreSQL gratuito no **[Neon.tech](https://neon.tech/)**.
2.  Crie um novo **Web Service** no **[Render.com](https://render.com/)** apontando para o seu repositório no GitHub.
3.  O Render detectará o `Dockerfile` automaticamente e selecionará o runtime **Docker**.
4.  Configure as variáveis de ambiente em **Advanced**:
    *   `DATABASE_URL`: `jdbc:postgresql://<seu-host-do-neon>/neondb?sslmode=require`
    *   `DATABASE_USERNAME`: `seu-usuario-do-neon`
    *   `DATABASE_PASSWORD`: `sua-senha-do-neon`
5.  Clique em **Create Web Service**.
