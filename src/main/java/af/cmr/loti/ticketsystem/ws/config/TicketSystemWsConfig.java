package af.cmr.loti.ticketsystem.ws.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import af.cmr.loti.ticketsystem.business.config.TicketSystemBusinessConfig;

/**
 * Configuration class for TicketSystem Web Services.
 */
@Configuration
@Import(TicketSystemBusinessConfig.class)
@ComponentScan(basePackages = {"af.cmr.loti.ticketsystem.ws.*"})
public class TicketSystemWsConfig {

}
