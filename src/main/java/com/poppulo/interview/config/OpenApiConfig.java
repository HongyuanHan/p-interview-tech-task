package com.poppulo.interview.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {
    @Bean
    public OpenAPI customOpenAPI(@Value("${app.swagger.info.description}") String description,
                                 @Value("${app.swagger.info.version}") String version,
                                 @Value("${app.swagger.info.title}") String title,
                                 @Value("${app.swagger.info.termsOfService}") String termsOfService,
                                 @Value("${app.swagger.info.contact.email}") String contactEmail,
                                 @Value("${app.swagger.info.license.name}") String licenseName,
                                 @Value("${app.swagger.info.license.url}") String licenseUrl) {
        return new OpenAPI()
                .info(new Info()
                        .description(description)
                        .version(version)
                        .title(title)
                        .termsOfService(termsOfService)
                        .contact(new Contact().email(contactEmail))
                        .license(new License().name(licenseName).url(licenseUrl)));
    }
}
