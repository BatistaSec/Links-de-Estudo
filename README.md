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

> ⚡ **Acesso Simplificado para Testes:**  
> Para facilitar a avaliação por recrutadores e visitantes, as rotas de bookmarks `/api/bookmarks/**` estão configuradas em **modo permissivo** (abertas ao público sem necessidade de cabeçalhos ou tokens de login). Toda requisição anônima utilizará automaticamente um usuário de testes padrão (`teste@email.com`).
> 
> 🔒 **Demonstração de Segurança & Cadastro (JWT / LGPD):**  
> Toda a arquitetura de segurança (Spring Security 7, filtros de autenticação, geração e validação de tokens JWT, registro e login de usuários) está **100% implementada** no código do projeto (nos pacotes `security` e `user`).  
> Por questões de **privacidade e conformidade com a LGPD**, o cadastro e o login de usuários reais foram desativados na demonstração pública hospedada no Render (retornando o código `405 Method Not Allowed`).  
> Caso deseje reativar o fluxo completo em seu fork:
> 1. No arquivo `SecuritySecurity.java`, descomente a linha do filtro JWT: `.addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)`.
> 2. No arquivo `BookmarkController.java`, dentro do método `getEmail`, descomente a linha: `return principal.getName()`.
> 3. No arquivo `UserController.java`, comente o retorno de teste e descomente o corpo original dos métodos `register` e `login`.
> 4. No arquivo `SecuritySecurity.java`, configure as rotas protegidas (por exemplo, removendo a linha de liberação pública `/api/bookmarks/**`).

---

## 🔑 Endpoints Principais

### Autenticação (Opcional - JWT)
*Estes endpoints estão desativados na demonstração pública por motivos de conformidade com a LGPD.*

| Método | Endpoint | Descrição | Exemplo de Payload (Request) |
| :--- | :--- | :--- | :--- |
| `POST` | `/api/auth/register` | Cria uma nova conta de usuário. | `{"email": "teste@email.com", "senha": "123"}` |
| `POST` | `/api/auth/login` | Autentica e retorna o Token JWT. | `{"email": "teste@email.com", "senha": "123"}` |

### Bookmarks (Acesso Público para Testes)
| Método | Endpoint | Descrição | Exemplo de Payload (Request) |
| :--- | :--- | :--- | :--- |
| `GET` | `/api/bookmarks` | Lista todos os bookmarks cadastrados. | *Nenhum* |
| `GET` | `/api/bookmarks/{id}` | Busca os detalhes de um bookmark pelo ID. | *Nenhum* |
| `POST` | `/api/bookmarks` | Salva um link e extrai seus metadados. | `{"url": "https://spring.io"}` |
| `PUT` | `/api/bookmarks/{id}` | Atualiza o título e URL de um bookmark. | `{"url": "https://spring.io/projects", "title": "Spring"}` |
| `DELETE` | `/api/bookmarks/{id}` | Deleta um bookmark existente. | *Nenhum* |

---

## 💻 Exemplos de Consumo (Como consumir a API)

Abaixo estão trechos de código demonstrando como interagir com os endpoints de bookmarks usando **JavaScript**, **Python** e **Java**.

### 1. JavaScript (Fetch API / Frontend)
```javascript
const API_URL = "https://links-de-estudo.onrender.com";

// Envia uma URL para a API extrair metadados e salvar o bookmark
async function cadastrarBookmark(url) {
  const response = await fetch(`${API_URL}/api/bookmarks`, {
    method: "POST",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify({ url })
  });
  
  const data = await response.json();
  console.log("✅ Bookmark cadastrado:", data);
  return data;
}

// Lista todos os bookmarks cadastrados no sistema
async function obterBookmarks() {
  const response = await fetch(`${API_URL}/api/bookmarks`);
  const data = await response.json();
  console.log("Bookmarks cadastrados:", data);
  return data;
}
```

### 2. Python (Requests Library)
```python
import requests

API_URL = "https://links-de-estudo.onrender.com"

# Envia uma URL para a API extrair metadados e cadastrar
def cadastrar_bookmark(url):
    payload = {"url": url}
    response = requests.post(f"{API_URL}/api/bookmarks", json=payload)
    return response.json()

# Lista todos os bookmarks cadastrados
def listar_bookmarks():
    response = requests.get(f"{API_URL}/api/bookmarks")
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

    public static void main(String[] args) throws Exception {
        HttpClient client = HttpClient.newHttpClient();
        String payload = "{\"url\":\"https://spring.io\"}";
        
        System.out.println("Enviando requisição de cadastro de bookmark...");
        
        // Requisição POST para criar o Bookmark
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(API_URL + "/api/bookmarks"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(payload))
                .build();
        
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        
        System.out.println("✅ Resposta do Servidor:");
        System.out.println(response.body());
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
