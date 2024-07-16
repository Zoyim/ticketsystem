package af.cmr.loti.ticketsystem.ws.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import af.cmr.loti.ticketsystem.business.dto.UserAuthDTO;
import af.cmr.loti.ticketsystem.business.dto.UserBasicDTO;
import af.cmr.loti.ticketsystem.business.entity.ApiResponse;
import af.cmr.loti.ticketsystem.business.exception.GestTicketBusinessException;
import af.cmr.loti.ticketsystem.business.service.IUserService;
import af.cmr.loti.ticketsystem.ws.form.AuthToken;
import af.cmr.loti.ticketsystem.ws.security.JwtTokenUtil;
import io.swagger.v3.oas.annotations.Operation;

@RestController
@RequestMapping("/token")
public class AuthenticationController {
	
	@Autowired
    private AuthenticationManager authenticationManager;
	
	@Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired 
    private IUserService userService;
    
    @PostMapping("/generateToken")
    @Operation(summary = "Authentifier l'utilisateur", description = "Cette methode permet de générer et renvoyer l'utilisateur connecté avec son token")
    public ApiResponse<AuthToken> authenticateAndGetToken(@RequestBody UserAuthDTO loginUser) throws AuthenticationException, GestTicketBusinessException {

        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginUser.getUsername(), loginUser.getPassword()));
        final UserBasicDTO user = this.userService.findByUsername(loginUser.getUsername());
        final String token = jwtTokenUtil.generateToken(user);
        return new ApiResponse<AuthToken>(200, "success",new AuthToken(token, user.getUsername()));
    }

}
