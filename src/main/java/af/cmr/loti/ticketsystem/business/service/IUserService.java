package af.cmr.loti.ticketsystem.business.service;

import java.util.List;

import org.springframework.security.core.userdetails.UserDetailsService;

import af.cmr.loti.ticketsystem.business.dao.UserRepository;
import af.cmr.loti.ticketsystem.business.dto.TicketBasicDTO;
import af.cmr.loti.ticketsystem.business.dto.UserBasicDTO;
import af.cmr.loti.ticketsystem.business.dto.UserFullDTO;
import af.cmr.loti.ticketsystem.business.entity.User;
import af.cmr.loti.ticketsystem.business.exception.GestTicketBusinessException;

/**
 * Interface extending the IAbstractTicketSystemService interface for managing
 * user, providing specific operations for {@link User} entity.
 *
 * @see IAbstractTicketSystemService 
*/

public interface IUserService extends IAbstractTicketSystemService<User, UserBasicDTO, UserFullDTO, UserRepository>, UserDetailsService  {
	public List<TicketBasicDTO> getUserTickets(Integer id, String authenticatedUsername) throws GestTicketBusinessException ;
	
	public UserBasicDTO findByUsername(String username) throws GestTicketBusinessException ;
}
