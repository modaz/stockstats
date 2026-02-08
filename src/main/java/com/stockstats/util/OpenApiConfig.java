package com.stockstats.util;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI stockStatsOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Stock Stats REST API")
                        .description("Rest API that takes a list of stock symbols (comma separated) and date range as input and returns the Min, Max and Avg closing prices by symbol.")
                        .version("v1")
                        .contact(new Contact()
                                .email("mohandas.kannan@gmail.com")
                                .name("Stock Stats API")));
    }
}
