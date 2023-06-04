package com.ahmeric.store.config;

import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import lombok.NoArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * This class provides configuration for the OpenAPI. It uses a Bearer Authentication scheme with
 * JWT format. Annotated as a Spring configuration class.
 */
@Configuration
@SecurityScheme(
    name = "Bearer Authentication",
    type = SecuritySchemeType.HTTP,
    bearerFormat = "JWT",
    scheme = "bearer"
)
@NoArgsConstructor
public class OpenApiConfig {

  /**
   * The method is responsible for creating the OpenAPI instance.
   *
   * @return an OpenAPI instance with predefined information.
   */
  @Bean
  public OpenAPI myOpenApi() {

    Contact contact = new Contact();
    contact.setEmail("ahmeric@gmail.com");
    contact.setName("Ahmeric");

    License mitLicense = new License().name("MIT License")
        .url("https://choosealicense.com/licenses/mit/");

    Info info = new Info()
        .title("Retail Store API")
        .version("1.0.0")
        .contact(contact)
        .description("This API exposes endpoints to manage user, product and bill")
        .license(mitLicense);

    return new OpenAPI().info(info);
  }


}
