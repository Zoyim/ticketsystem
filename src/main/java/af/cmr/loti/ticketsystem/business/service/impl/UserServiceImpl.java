package af.cmr.loti.ticketsystem.business.service.impl;

import java.nio.file.AccessDeniedException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import af.cmr.loti.ticketsystem.business.dao.UserRepository;
import af.cmr.loti.ticketsystem.business.dto.TicketBasicDTO;
import af.cmr.loti.ticketsystem.business.dto.UserBasicDTO;
import af.cmr.loti.ticketsystem.business.dto.UserFullDTO;
import af.cmr.loti.ticketsystem.business.entity.Ticket;
import af.cmr.loti.ticketsystem.business.entity.User;
import af.cmr.loti.ticketsystem.business.exception.GestTicketBusinessException;
import af.cmr.loti.ticketsystem.business.service.IUserService;
import af.cmr.loti.ticketsystem.business.utils.ConstBusinessRules;
import af.cmr.loti.ticketsystem.business.utils.ConstsValues;
import jakarta.annotation.Resource;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;

/**
 * Service implementation class for managing {@link Ticket} entity, extending
 * the AbstractTicketSystemServiceImpl class. This class provides specific
 * functionality for managing sessions, including CRUD operations and
 * validation.
 *
 * @see AbstractTicketSystemServiceImpl 
*/
@Service(ConstsValues.ServiceKeys.USER_SERVICE_KEY)
public class UserServiceImpl 
				extends AbstractTicketSystemServiceImpl<User, UserBasicDTO, UserFullDTO, UserRepository>
				implements IUserService, UserDetailsService {
	
	@Resource(name = ConstsValues.ConstsDAO.USER_DAO_KEY)
	private UserRepository userRepository;
	
	private BCryptPasswordEncoder bcryptEncoder;
	
	private final Validator validator;

	public UserServiceImpl(Validator validator) {
		super(User.class, UserBasicDTO.class, UserFullDTO.class);
		this.validator = validator;
		this.bcryptEncoder = new BCryptPasswordEncoder();
	}

	@Override
	public UserRepository getDAO() {
		return this.userRepository;
	}
	
	@Override
    public UserBasicDTO findByUsername(String username) throws GestTicketBusinessException {
		if (StringUtils.isBlank(username)) {
	        throw new GestTicketBusinessException("Vous devez renseigner le username");
	    }
		
		User user = this.getDAO().findByUsername(username);
	    if (user == null) {
	        throw new GestTicketBusinessException("Utilisateur non trouvé pour le username: " + username);
	    }
	    
	    return this.getModelMapper().map(user, UserBasicDTO.class);
    }
	 
	private List<SimpleGrantedAuthority> getAuthority() {
	   return Arrays.asList(new SimpleGrantedAuthority("ROLE_ADMIN"),
	        new SimpleGrantedAuthority("ROLE_ANONYMOUS")); 
	}
	
	@Override
	public UserBasicDTO create(UserBasicDTO dto) throws GestTicketBusinessException {
		Set<ConstraintViolation<UserBasicDTO>> violations = this.validator.validate(dto);
	    if (!violations.isEmpty()) {
	        List<String> errorMessages = new ArrayList<>();
	        for (ConstraintViolation<UserBasicDTO> violation : violations) {
	            String errorMessage = String.format("%s: %s", violation.getPropertyPath(), violation.getMessage());
	            errorMessages.add(errorMessage);
	        }
	        throw new GestTicketBusinessException(String.join("\n", errorMessages));
	    }
	    
	    User getOneUserEmail = this.getDAO().findByEmail(dto.getEmail());
	    if (getOneUserEmail != null) {
	    	throw new GestTicketBusinessException(ConstBusinessRules.RG01);
	    }
	    
	    User getOneUserUsername = this.getDAO().findByUsername(dto.getUsername());
	    if (getOneUserUsername != null) {
	    	throw new GestTicketBusinessException(ConstBusinessRules.RG02);
	    }
	    
	    String encryptPassword = bcryptEncoder.encode(dto.getPassword());
	    dto.setPassword(encryptPassword);
	    User user = this.getDAO().saveAndFlush(this.getModelMapper().map(dto, User.class));
	    dto.setId(user.getId());
	    return dto; 
	}
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
	    User user = this.getDAO().findByUsername(username);
	    if (user == null) {
	      throw new UsernameNotFoundException("Invalid Username or Password.");
	    }
	    return new org.springframework.security.core.userdetails.User(user.getUsername(),
	        user.getPassword(), getAuthority());
	}
	
	@Override
	public UserBasicDTO update(UserBasicDTO dtoToUpdate) throws GestTicketBusinessException, AccessDeniedException {
		
		Set<ConstraintViolation<UserBasicDTO>> violations = this.validator.validate(dtoToUpdate);
	    if (!violations.isEmpty()) {
	        List<String> errorMessages = new ArrayList<>();
	        for (ConstraintViolation<UserBasicDTO> violation : violations) {
	            String errorMessage = String.format("%s: %s", violation.getPropertyPath(), violation.getMessage());
	            errorMessages.add(errorMessage);
	        }
	        throw new GestTicketBusinessException(String.join("\n", errorMessages));
	    }
	    
	    boolean isUserEmailExist = this.findAll().stream().anyMatch(p -> dtoToUpdate.getEmail().equals(p.getEmail()) && !dtoToUpdate.getId().equals(p.getId()));
	    boolean isUserUserExist = this.findAll().stream().anyMatch(p -> dtoToUpdate.getUsername().equals(p.getUsername()) && !dtoToUpdate.getId().equals(p.getId()));
		
	    if (!isUserEmailExist || !isUserUserExist) {
	    	User user = this.getDAO().findById(dtoToUpdate.getId()).orElse(null);
	    	if (user != null) {
	    		String encryptPassword = bcryptEncoder.encode(dtoToUpdate.getPassword());
	    		dtoToUpdate.setPassword(encryptPassword);
	    		this.getDAO().saveAndFlush(this.getModelMapper().map(dtoToUpdate, User.class));
			}else {
                throw new GestTicketBusinessException("L'objet à modifier N'existe pas en Base...");
            }
	    	
	    }
	    if(isUserEmailExist) {
	    	throw new GestTicketBusinessException(ConstBusinessRules.RG01);
	    }
	    
	    if(isUserUserExist) {
	    	throw new GestTicketBusinessException(ConstBusinessRules.RG02);
	    }
	    return dtoToUpdate;
	}
	
	@Override
	public List<TicketBasicDTO> getUserTickets(Integer id, String authenticatedUsername) throws GestTicketBusinessException {
	    User user = this.getDAO().findById(id).orElseThrow(() -> new GestTicketBusinessException("User not found for this id :: " + id));
	    
	    if (!user.getUsername().equals(authenticatedUsername)) {
	        throw new GestTicketBusinessException("You are not authorized to view another user's tickets");
	    }

	    return user.getTickets().stream()
	            .map(ticket -> this.getModelMapper().map(ticket, TicketBasicDTO.class))
	            .collect(Collectors.toList());
	}
}
