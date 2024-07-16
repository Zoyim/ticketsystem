package af.cmr.loti.ticketsystem.business.dto;

import af.cmr.loti.ticketsystem.business.entity.Status;
import af.cmr.loti.ticketsystem.business.entity.Ticket;

/**
 * This class represents a medium Data Transfer Object (DTO) for a
 * {@link Ticket} entity. It extends {@link TicketBasicDTO} and inherits basic
 * information about a ticket. Medium DTOs typically include additional details
 * beyond the basic DTO but exclude complex associations like lists.
*/
public class TicketMediumDTO extends TicketBasicDTO {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3119724265423067385L;
	
	private UserBasicDTO user;

	

	public TicketMediumDTO() {
		super();
	}

	public TicketMediumDTO(Integer id, String title, String description, Status status) {
		super(id, title, description, status);
	}

	public UserBasicDTO getUser() {
		return user;
	}

	public void setUser(UserBasicDTO user) {
		this.user = user;
	}
}
