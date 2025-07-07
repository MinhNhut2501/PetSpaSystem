package com.petspa.booking_service.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenAPIConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Pet Spa Booking API")
                        .description("API quản lý đặt lịch và dịch vụ thú cưng")
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("Your Name")
                                .email("your@email.com")
                                .url("https://your-website.com"))
                        .license(new License()
                                .name("Apache 2.0")
                                .url("http://springdoc.org")));
    }
}
