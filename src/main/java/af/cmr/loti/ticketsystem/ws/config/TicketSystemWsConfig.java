package af.cmr.loti.ticketsystem.ws.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import af.cmr.loti.ticketsystem.business.config.TicketSystemBusinessConfig;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;

/**
 * Configuration class for TicketSystem Web Services.
 */
@Configuration
@OpenAPIDefinition(info = @Info(title = "TicketSystem", version = "1.0", description = "TicketSystem est un système de gestion de tickets qui permet à un utilisateur de faire des opérations de CRUD. Un utilisateur peut avoir plusieurs tickets et un ticket ne peut être affecté qu'à un utilisateur.", contact = @Contact(name = "ticketSystem")))
@Import(TicketSystemBusinessConfig.class)
@ComponentScan(basePackages = {"af.cmr.loti.ticketsystem.ws.*"})
public class TicketSystemWsConfig {

}
