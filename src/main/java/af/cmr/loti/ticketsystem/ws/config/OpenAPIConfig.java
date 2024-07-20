package af.cmr.loti.ticketsystem.ws.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.security.SecuritySchemes;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import io.swagger.v3.oas.models.OpenAPI;


@Configuration
@OpenAPIDefinition(info = @Info(title = "TicketSystem", version = "1.0",
		description = "TicketSystem est un système de gestion de tickets qui permet à un utilisateur de faire des opérations de CRUD. Un utilisateur peut avoir plusieurs tickets et un ticket ne peut être affecté qu'à un utilisateur.",
		contact = @Contact(name = "ticketSystem")),
		security = {@SecurityRequirement(name = "bearerToken")}
)
@SecuritySchemes({
		@SecurityScheme(name = "bearerToken", type = SecuritySchemeType.HTTP,
				scheme = "bearer", bearerFormat = "JWT")
})
public class OpenAPIConfig {
	@Bean
	public OpenAPI customOpenAPI() {
		return new OpenAPI()
				.info(new io.swagger.v3.oas.models.info.Info()
						.title("TICKET SYSTEM")
						.version("1.0")
						.description("TicketSystem est un système de gestion de tickets qui permet à un utilisateur de faire des opérations de CRUD. Un utilisateur peut avoir plusieurs tickets et un ticket ne peut être affecté qu'à un utilisateur.")
						.contact(new io.swagger.v3.oas.models.info.Contact().name("ticketSystem")))
				        .addSecurityItem(new io.swagger.v3.oas.models.security.SecurityRequirement().addList("bearerToken"));
	}
}
