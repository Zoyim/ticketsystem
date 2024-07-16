package af.cmr.loti.ticketsystem.ws.security;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import af.cmr.loti.ticketsystem.ws.utils.Constants;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * Custom JWT authentication filter to process JWT tokens in the incoming requests.
 */
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
    	String header = request.getHeader(Constants.HEADER_STRING);
		String token = null;
		String username = null;
		
		response.addHeader("Access-Control-Allow-Origin", "*");
		response.addHeader("Access-Control-Allow-Methods", "GET, HEAD, POST, PUT, DELETE, OPTIONS");
		response.addHeader("Access-Control-Allow-Headers", "Access-Control-Allow-Headers, Origin, Accept," + "X-Request-With, Content-Type, Access-Control-Request-Method" + "Access-Control-Allow-Headers, Authorization");
		response.addHeader("Access-Control-Expose-Headers", "Authorization, Access-ControlAllow-Origin");
		response.setHeader("Access-Control-Allow-Headers", "*");
		response.setHeader("Access-Control-Max-Age", "86400");
		
		if(request.getMethod().equals("OPTIONS")) {
			response.setStatus(HttpServletResponse.SC_OK);
			return ;
		}

		if (header != null && header.startsWith(Constants.TOKEN_PREFIX)) {
			token = header.replace(Constants.TOKEN_PREFIX,"");
			username = jwtTokenUtil.getUsernameFromToken(token);
		}

		if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
			UserDetails userDetails = userDetailsService.loadUserByUsername(username);
			if (jwtTokenUtil.validateToken(token, userDetails)) {
				UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(userDetails,
						null, userDetails.getAuthorities());
				authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
				SecurityContextHolder.getContext().setAuthentication(authToken);
			}
		}

		filterChain.doFilter(request, response);
	}
}