package af.cmr.loti.ticketsystem.business.dto;

import af.cmr.loti.ticketsystem.business.entity.Status;

/**
 * This class represents a full Data Transfer Object (DTO) for a {@link Ticket}
 * entity, it extends {@link TicketMediumDTO}.
*/
public class TicketFullDTO extends TicketMediumDTO {

	/**
	 * 
	 */
	private static final long serialVersionUID = 162556586179803425L;

	public TicketFullDTO() {
		super();
	}

	public TicketFullDTO(Integer id, String title, String description, Status status) {
		super(id, title, description, status);
	}
	
}
