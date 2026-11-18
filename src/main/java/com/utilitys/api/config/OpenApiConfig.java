package com.utilitys.api.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Utilitys API - Central de Utilitários")
                        .version("1.0.0")
                        .description("API REST em Java 20 + Spring Boot 3 para Conversão de Unidades, " +
                                "Conversão e Cálculos de Moedas, Números por Extenso (PT-BR) e Criptografia/Geração de Senhas (BCrypt).")
                        .contact(new Contact()
                                .name("Utilitys Team")
                                .email("contato@utilitys.com"))
                        .license(new License()
                                .name("MIT License")
                                .url("https://opensource.org/licenses/MIT")));
    }
}
