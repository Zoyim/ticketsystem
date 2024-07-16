package af.cmr.loti.ticketsystem.business.exception;

import java.util.Arrays;

import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import af.cmr.loti.ticketsystem.business.entity.ApiResponse;

@ControllerAdvice
public class CustomGlobalExceptionHandler {
    
    @ExceptionHandler(GestTicketBusinessException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ApiResponse handleGestTicketBusinessException(GestTicketBusinessException ex) {
        String[] errorMessages = ex.getMessage().split("\n");
        return new ApiResponse(HttpStatus.BAD_REQUEST.value(), "Validation Error", Arrays.asList(errorMessages));
    }
    
    @ExceptionHandler(UsernameNotFoundException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ResponseBody
    public ApiResponse<String> handleUsernameNotFoundException(UsernameNotFoundException ex) {
        return new ApiResponse<>(HttpStatus.UNAUTHORIZED.value(), ex.getMessage(), null);
    }
    
    @ExceptionHandler(BadCredentialsException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ResponseBody
    public ApiResponse<String> handleBadCredentialsException(BadCredentialsException ex) {
        return new ApiResponse<>(HttpStatus.UNAUTHORIZED.value(), "Authentication error", "Invalid Username or Password.");
    }
}
