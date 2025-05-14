package com.takarub.AuthJwtTemplate.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.servers.Server;

@OpenAPIDefinition(
        info = @Info(
                title = "Security system platform",
                description = "one day i wll be top one jordan via java",
                contact = @Contact(
                        name = "AL-Selawe",
                        email = "mahmoodselawe5@gmail.com",
                        url = "https://www.youtube.com/watch?v=cZju8qjSdsk&t=739s"
                ),
                version = "4.1",
                license = @License(
                        name = "License name",
                        url = "https://www.youtube.com/watch?v=cZju8qjSdsk&t=739s"
                ),
                termsOfService = "terms Of Service"
        ),
        servers = {
                @Server(
                        url = "http://localhost:8080",
                        description = "dev level"
                )
        },
        security = {
                @SecurityRequirement(
                        name = "bearerAuth"
                )
        }

)
@SecurityScheme(
        name = "bearerAuth",
        description = "JWt Auth description",
        scheme = "bearer",
        type = SecuritySchemeType.HTTP,
        bearerFormat = "JWT",
        in = SecuritySchemeIn.HEADER

)
public class OpenApiConfig {
}
