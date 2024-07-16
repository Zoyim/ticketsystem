package af.cmr.loti.ticketsystem.business.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * This class represents a full Data Transfer Object (DTO) for a {@link User}
 * entity, it extends {@link UserMediumDTO}.
*/ 
public class UserFullDTO extends UserMediumDTO {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3626564471012595035L;
	
	@JsonIgnore
	private List<TicketBasicDTO> tickets;

	public UserFullDTO() {}

	public List<TicketBasicDTO> getTickets() {
		return tickets;
	}

	public void setTickets(List<TicketBasicDTO> tickets) {
		this.tickets = tickets;
	}
}
