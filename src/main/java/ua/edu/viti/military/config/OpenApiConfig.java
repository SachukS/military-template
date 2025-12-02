package ua.edu.viti.military.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * Конфігурація OpenAPI для Swagger документації
 */
@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Military Warehouse API - Система МТЗ")
                        .version("1.0.0")
                        .description(
                                "REST API для системи управління матеріально-технічним забезпеченням.\n\n" +
                                        "**Функціональність:**\n" +
                                        "- Управління категоріями матеріалів\n" +
                                        "- CRUD операції з матеріалами на складі\n" +
                                        "- Управління складами\n" +
                                        "- Фільтрація та пошук матеріалів\n" +
                                        "- Контроль термінів придатності\n" +
                                        "- Облік кількості та статусів\n\n" +
                                        "**Технології:** Spring Boot 3.2, Spring Data JPA, PostgreSQL, Hibernate"
                        )
                        .contact(new Contact()
                                .name("VITI Military Warehouse System")
                                .email("support@viti.edu.ua")
                        )
                        .license(new License()
                                .name("Educational Use Only")
                                .url("https://viti.edu.ua")
                        )
                )
                .servers(List.of(
                        new Server()
                                .url("http://localhost:8080")
                                .description("Local Development Server"),
                        new Server()
                                .url("https://api.military-warehouse.example.com")
                                .description("Production Server (placeholder)")
                ));
    }
}
