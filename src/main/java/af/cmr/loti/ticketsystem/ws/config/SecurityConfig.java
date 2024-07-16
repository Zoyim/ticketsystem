package af.cmr.loti.ticketsystem.ws.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import af.cmr.loti.ticketsystem.business.utils.ConstsValues;
import af.cmr.loti.ticketsystem.ws.security.JwtAuthenticationFilter;
import jakarta.annotation.Resource;


/**
 * Security configuration class for TicketSystem Web Services.
 */
@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {
	
	@Autowired
	private JwtAuthenticationFilter authFilter;

	@Resource(name = ConstsValues.ServiceKeys.USER_SERVICE_KEY)
	private UserDetailsService userDetailsService;

	public SecurityConfig(JwtAuthenticationFilter authFilter) {
		this.authFilter = authFilter;
	}

    // Configuring HttpSecurity
    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		return http.csrf(AbstractHttpConfigurer::disable).authorizeHttpRequests((request) -> request
				.requestMatchers("/token/generateToken", "/swagger-ui/**", "/v3/api-docs/**", "/error/**").permitAll()
				.requestMatchers(HttpMethod.POST, "/users").permitAll()
				.anyRequest().authenticated())
				.addFilterBefore(authFilter, UsernamePasswordAuthenticationFilter.class).build();
	}

    // Password Encoding
    @Bean
    BCryptPasswordEncoder bcryptEncoder() {
        return new BCryptPasswordEncoder();
    }

	@Bean
	AuthenticationProvider authenticationProvider() {
		DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
		authenticationProvider.setUserDetailsService(this.userDetailsService);
		authenticationProvider.setPasswordEncoder(bcryptEncoder());
		return authenticationProvider;
	}

	@Bean
	AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
		return config.getAuthenticationManager();
	}

}
