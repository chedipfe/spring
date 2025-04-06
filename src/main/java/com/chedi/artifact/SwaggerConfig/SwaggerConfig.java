package com.chedi.artifact.SwaggerConfig;

import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityScheme;

@Configuration
public class SwaggerConfig {

   // Seulement les routes correspondant à /api/** seront incluses dans la documentation générée par Swagger
    // /api/** inclut toutes les niveaux des routes sous /api/ (par exemple : /api/x/y/..., /api/z/....)
    // /api/* inclut seulement les routes de niveau 1 sous /api/ (par exemple : /api/x, /api/y)
    // Le nom du groupe des APIs dans la documentation générée par Swagger s'appelle "test"
    @Bean
    GroupedOpenApi customApi() {
        return GroupedOpenApi.builder().group("test").pathsToMatch("/api/sim-card-subscriptions/*").build();
    }

 // @Value permet d'injecter des valeurs provenant de variables d'environnement, de fichiers de configuration, d'arguments de ligne de commande
    @Value("${spring.application.name}")
    private String applicationName;

// Pour tester les API dans la documentation Swagger, un JWT access token valide doit être fourni 
// via le bouton "Authorize" dans l'interface Swagger. Sans ce token, les appels API protégés échoueront.
@Bean
    OpenAPI customOpenAPI() {
        return new OpenAPI()
                .components(
                        new Components().addSecuritySchemes(BEARER_KEY_SECURITY_SCHEME,
                                new SecurityScheme().type(SecurityScheme.Type.HTTP).scheme("bearer").bearerFormat("JWT")))
                .info(new Info().title(applicationName));
    }

    public static final String BEARER_KEY_SECURITY_SCHEME = "bearer-key";
}
