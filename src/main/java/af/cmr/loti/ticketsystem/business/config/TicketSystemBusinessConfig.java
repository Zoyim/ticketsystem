package af.cmr.loti.ticketsystem.business.config;

import org.modelmapper.ModelMapper;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import jakarta.validation.Validation;
import jakarta.validation.Validator;

/**
 * This class represents the configuration for the TicketSystem business module. It
 * enables JPA auditing, scans for entity classes, repository interfaces, and
 * components within the specified base packages, and defines a singleton bean
 * for the ModelMapper instance.
 */
@Configuration
@EnableJpaAuditing
@EntityScan("af.cmr.loti.ticketsystem.business.entity")
@EnableJpaRepositories("af.cmr.loti.ticketsystem.business.dao")
@ComponentScan(basePackages = {"af.cmr.loti.ticketsystem.business.*"})
public class TicketSystemBusinessConfig {
	
	@Bean(value = "ticketsystem-modelmapper")
    @Scope(value = "singleton")
    ModelMapper modelMapper() {
        return new ModelMapper();
    }
	
	@Bean
    Validator validator() {
        return Validation.buildDefaultValidatorFactory().getValidator();
    }
	
	@Bean(value = "BCRYPT_ENCODER")
	BCryptPasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
}
