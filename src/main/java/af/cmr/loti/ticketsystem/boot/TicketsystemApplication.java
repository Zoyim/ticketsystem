package af.cmr.loti.ticketsystem.boot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

import af.cmr.loti.ticketsystem.ws.config.TicketSystemWsConfig;

@SpringBootApplication 
@Import(TicketSystemWsConfig.class)
public class TicketsystemApplication {

	public static void main(String[] args) {
		SpringApplication.run(TicketsystemApplication.class, args);
	}

}
