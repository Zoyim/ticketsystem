package af.cmr.loti.ticketsystem.ws.security;

import java.io.IOException;
import java.io.Serializable;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

@Component("authentEntryPoint")
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint, Serializable {

    /**
	 * 
	 */
	private static final long serialVersionUID = -1039146914341430233L;

    public void commence(HttpServletRequest request,
                         HttpServletResponse response,
                         AuthenticationException authException) throws IOException {

        response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized");
    }
}