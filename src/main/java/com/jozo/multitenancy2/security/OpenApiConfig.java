package com.jozo.multitenancy2.security;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Multitenancy Bookmarks API")
                        .version("1.0")
                        .description("API para gerenciamento de bookmarks com multitenancy e autenticação JWT"));

        /*
        // Para reativar o botão 'Authorize' (JWT Bearer Auth) no Swagger UI,
        // comente o retorno acima e descomente o bloco abaixo:
        final String securitySchemeName = "bearerAuth";
        return new OpenAPI()
                .info(new Info()
                        .title("Multitenancy Bookmarks API")
                        .version("1.0")
                        .description("API para gerenciamento de bookmarks com autenticação JWT"))
                .addSecurityItem(new SecurityRequirement()
                        .addList(securitySchemeName))
                .components(new Components()
                        .addSecuritySchemes(securitySchemeName, new SecurityScheme()
                                .name(securitySchemeName)
                                .type(SecurityScheme.Type.HTTP)
                                .scheme("bearer")
                                .bearerFormat("JWT")));
        */
    }
}
