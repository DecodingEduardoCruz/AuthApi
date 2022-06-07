package br.coop.integrada.auth.domain.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {
	@Bean
    public OpenAPI openApi() {
        return new OpenAPI()
	        .info(new Info()
                .title("AUTH 1.0")
                .description("Api Cooperado Auth")
                .version("v1.0")
                .contact(new Contact()
                        .name("Reginaldo Rocha")
                        .url("https://www.integrada.coop.br/")
                        .email("reginaldo.rocha@integrada.coop.br"))
                .license(new License().name("Apache License Version 2.0")
                .url("https://www.apache.org/licenses/LICENSE-2.0.txt"))
	        );
    }
}
