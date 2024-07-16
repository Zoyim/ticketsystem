package af.cmr.loti.ticketsystem.business.service.impl;

import java.nio.file.AccessDeniedException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.stereotype.Service;

import af.cmr.loti.ticketsystem.business.dao.TicketRepository;
import af.cmr.loti.ticketsystem.business.dao.UserRepository;
import af.cmr.loti.ticketsystem.business.dto.TicketBasicDTO;
import af.cmr.loti.ticketsystem.business.dto.TicketFullDTO;
import af.cmr.loti.ticketsystem.business.dto.UserBasicDTO;
import af.cmr.loti.ticketsystem.business.entity.Ticket;
import af.cmr.loti.ticketsystem.business.entity.User;
import af.cmr.loti.ticketsystem.business.exception.GestTicketBusinessException;
import af.cmr.loti.ticketsystem.business.service.ITicketService;
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
@Service(ConstsValues.ServiceKeys.TICKET_SERVICE_KEY)
public class TicketServiceImpl 
			extends AbstractTicketSystemServiceImpl<Ticket, TicketBasicDTO, TicketFullDTO, TicketRepository>
			implements ITicketService {
	
	@Resource(name = ConstsValues.ConstsDAO.TICKET_DAO_KEY)
	private TicketRepository ticketRepository;
	
	@Resource(name = ConstsValues.ConstsDAO.USER_DAO_KEY)
	private UserRepository userRepository;
	
	private final Validator validator;
	

	public TicketServiceImpl(Validator validator) {
		super(Ticket.class, TicketBasicDTO.class, TicketFullDTO.class);
		this.validator = validator;
	}

	@Override
	public TicketRepository getDAO() {
		return this.ticketRepository;
	}
	
	@Override
	public TicketBasicDTO create(TicketBasicDTO dto) throws GestTicketBusinessException {
	    Set<ConstraintViolation<TicketBasicDTO>> violations = this.validator.validate(dto);
	    if (!violations.isEmpty()) {
	        List<String> errorMessages = new ArrayList<>();
	        for (ConstraintViolation<TicketBasicDTO> violation : violations) {
	            String errorMessage = String.format("%s: %s", violation.getPropertyPath(), violation.getMessage());
	            errorMessages.add(errorMessage);
	        }
	        throw new GestTicketBusinessException(String.join("\n", errorMessages));
	    }
	    
	    Ticket getOneTicket = this.getDAO().findByTitle(dto.getTitle());
	    if (getOneTicket == null) {
	    	Ticket ticket = this.getDAO().saveAndFlush(this.getModelMapper().map(dto, Ticket.class));
		    dto.setId(ticket.getId());
		    return dto;
        }
	    throw new GestTicketBusinessException(ConstBusinessRules.RG03);
	}
	
	@Override
	public TicketBasicDTO update(TicketBasicDTO dtoToUpdate)
			throws GestTicketBusinessException, AccessDeniedException {
		
		Set<ConstraintViolation<TicketBasicDTO>> violations = this.validator.validate(dtoToUpdate);
	    if (!violations.isEmpty()) {
	        List<String> errorMessages = new ArrayList<>();
	        for (ConstraintViolation<TicketBasicDTO> violation : violations) {
	            String errorMessage = String.format("%s: %s", violation.getPropertyPath(), violation.getMessage());
	            errorMessages.add(errorMessage);
	        }
	        throw new GestTicketBusinessException(String.join("\n", errorMessages));
	    }
	    
	    boolean isTicketExist = this.findAll().stream().anyMatch(p -> dtoToUpdate.getTitle().equals(p.getTitle()) && !dtoToUpdate.getId().equals(p.getId()));
	    if (!isTicketExist) {
	    	Ticket ticket = this.getDAO().findById(dtoToUpdate.getId()).orElse(null);
	    	if (ticket != null) {
	    		this.getDAO().saveAndFlush(this.getModelMapper().map(dtoToUpdate, Ticket.class));
			}else {
                throw new GestTicketBusinessException("L'objet à modifier N'existe pas en Base...");
            }
	    	return dtoToUpdate;
	    }
	    throw new GestTicketBusinessException(ConstBusinessRules.RG03);
	}

	@Override
	public TicketFullDTO assignTicketToUser(Integer ticketId, Integer userId) throws GestTicketBusinessException {
		
		Optional<Ticket> optionalTicket = this.getDAO().findById(ticketId);
		if (!optionalTicket.isPresent()) {
			throw new GestTicketBusinessException("Ticket not found with id: " + ticketId);
	    }
		Ticket ticket = optionalTicket.get();
		if (ticket.getUser() != null) {
	        throw new GestTicketBusinessException("Ticket with id: " + ticketId + " is already assigned to a user.");
	    }
		User assignedUser = this.userRepository.findById(userId)
	            .orElseThrow(() -> new GestTicketBusinessException("User not found with id: " + userId));
	    ticket.setUser(assignedUser);
        
        Ticket updatedTicket = this.getDAO().save(ticket);
        
        UserBasicDTO userBasicDTO = this.getModelMapper().map(assignedUser, UserBasicDTO.class);
        TicketFullDTO ticketDTO = this.getModelMapper().map(updatedTicket, TicketFullDTO.class);
        ticketDTO.setUser(userBasicDTO);
		
        return ticketDTO;
	}
}
