package com.api.projeto.senai.swagger;

import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;

@Configuration
public class SwaggerConfig implements WebMvcConfigurer {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Anotações")
                        .version("1.0.5")
                        .description("Este software foi desenvolvido para criação de tarefas personalizadas"));
    }

    @Bean
    public GroupedOpenApi clienteApi() {
        return GroupedOpenApi.builder()
                .group("tarefa")
                .pathsToMatch("/**")
                .build();
    }

}
