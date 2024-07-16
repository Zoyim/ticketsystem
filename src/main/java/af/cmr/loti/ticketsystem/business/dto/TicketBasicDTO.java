package af.cmr.loti.ticketsystem.business.dto;

import af.cmr.loti.ticketsystem.business.entity.Status;
import af.cmr.loti.ticketsystem.business.entity.Ticket;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/**
 * This class represents a basic Data Transfer Object (DTO) for a
 * {@link Ticket} entity.
*/

public class TicketBasicDTO implements IDTO {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5593461543955599297L;
	
	private Integer id;
	
	@NotBlank(message = "Le titre ne peut pas être vide")
	@Size(max = 50, message = "Le titre ne peut pas dépasser {max} caractères")
	private String title;
	
	@NotBlank(message = "La description ne peut pas être vide")
	private String description;
	
	@NotNull(message = "Le statut ne peut pas être vide")
	private Status status;
	
	public TicketBasicDTO() {}

	public TicketBasicDTO(Integer id, String title, String description, Status status) {
		this.id = id;
		this.title = title;
		this.description = description;
		this.status = status;
	}

	public TicketBasicDTO(Integer id, String title) {
		super();
		this.id = id;
		this.title = title;
	}

	@Override
	public Integer getId() {
		return this.id;
	}

	@Override
	public void setId(Integer id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}
}
