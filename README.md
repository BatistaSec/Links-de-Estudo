# 🎯 Bookmarks Manager API (Links de Estudo)

Uma **API RESTful** robusta e segura desenvolvida com **Java 21** e **Spring Boot 4.1.0** para gerenciamento e categorização de links de estudo (bookmarks). O sistema extrai metadados de qualquer link (como título e descrição) automaticamente utilizando Web Scraping, associando-os a usuários autenticados de forma segura.

> ⚠️ **Aviso de Sandbox (Ambiente de Testes / LGPD):**  
> Este é um projeto estritamente acadêmico com fins demonstrativos de portfólio. Para testar os endpoints da API (seja pelo Swagger UI ou pelos clientes), **utilize e-mails fictícios e senhas simples de teste**. Não forneça ou envie dados pessoais reais sob nenhuma circunstância.

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

### 1. JavaScript (Fetch API / Frontend - Fluxo Completo)
```javascript
const API_URL = "https://links-de-estudo.onrender.com";

async function executarFluxoCompleto() {
  // Gera um e-mail único usando o timestamp para evitar erros de e-mail duplicado
  const email = `dev_${Date.now()}@email.com`;
  const senha = "minha_senha_123";

  console.log("1. Cadastrando novo usuário: ", email);
  
  // 1. Registrar o usuário
  const resRegister = await fetch(`${API_URL}/api/auth/register`, {
    method: "POST",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify({ email, senha })
  });

  if (resRegister.ok) {
    console.log("✅ Usuário cadastrado com sucesso!");
    
    // 2. Realizar o login para obter o Token JWT
    console.log("2. Realizando login...");
    const resLogin = await fetch(`${API_URL}/api/auth/login`, {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify({ email, senha })
    });
    
    const token = await resLogin.text();
    console.log("✅ Autenticado! Token JWT obtido:", token);
    return token;
  } else {
    console.error("❌ Falha no cadastro:", await resRegister.text());
  }
}
```

### 2. Python (Requests Library - Fluxo Completo)
```python
import requests
import time

API_URL = "https://links-de-estudo.onrender.com"

def testar_fluxo_completo():
    # Gera um e-mail único para evitar erro de usuário duplicado
    email = f"dev_{int(time.time())}@email.com"
    senha = "minha_senha_123"

    print(f"1. Cadastrando novo usuário: {email}")
    payload = {"email": email, "senha": senha}
    
    # 1. Registrar
    response_reg = requests.post(f"{API_URL}/api/auth/register", json=payload)
    if response_reg.status_code in [200, 201]:
        print("✅ Usuário cadastrado com sucesso!")
        
        # 2. Login
        print("2. Realizando login...")
        response_log = requests.post(f"{API_URL}/api/auth/login", json=payload)
        if response_log.status_code == 200:
            token = response_log.text
            print(f"✅ Autenticado! Token JWT obtido:\n{token}")
            return token
            
    print("❌ Falha no fluxo de testes.")
```

### 3. Java (HttpClient nativo - Fluxo Completo)
```java
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class ApiClient {
    private static final String API_URL = "https://links-de-estudo.onrender.com";

    public static void main(String[] args) throws Exception {
        // Gera um e-mail único baseado no timestamp atual
        String email = "dev_" + System.currentTimeMillis() + "@email.com";
        String senha = "minha_senha_123";
        String payload = String.format("{\"email\":\"%s\",\"senha\":\"%s\"}", email, senha);
        
        HttpClient client = HttpClient.newHttpClient();
        
        System.out.println("1. Cadastrando novo usuário: " + email);
        // 1. Registrar Usuário
        HttpRequest requestReg = HttpRequest.newBuilder()
                .uri(URI.create(API_URL + "/api/auth/register"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(payload))
                .build();
        client.send(requestReg, HttpResponse.BodyHandlers.discarding());
        System.out.println("✅ Usuário cadastrado!");

        System.out.println("2. Realizando login...");
        // 2. Fazer Login para obter o Token JWT
        HttpRequest requestLog = HttpRequest.newBuilder()
                .uri(URI.create(API_URL + "/api/auth/login"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(payload))
                .build();
        
        HttpResponse<String> response = client.send(requestLog, HttpResponse.BodyHandlers.ofString());
        System.out.println("✅ Autenticado! Token JWT: " + response.body());
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

---

## 💬 Feedback e Sugestões

Se você testou a API, tem alguma sugestão de melhoria, encontrou algum bug ou quer deixar um feedback, a sua contribuição é muito bem-vinda!

*   **Feedback & Bugs:** Abra uma [Issue no GitHub](https://github.com/BatistaSec/Links-de-Estudo/issues) para compartilhar suas sugestões ou relatar problemas.
*   **Novos Recursos:** Fique à vontade para fazer um fork do projeto e abrir um Pull Request com suas implementações!
