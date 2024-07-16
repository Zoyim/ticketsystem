package af.cmr.loti.ticketsystem.ws.controller;

import java.nio.file.AccessDeniedException;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import af.cmr.loti.ticketsystem.business.dto.TicketBasicDTO;
import af.cmr.loti.ticketsystem.business.dto.TicketFullDTO;
import af.cmr.loti.ticketsystem.business.entity.ApiResponse;
import af.cmr.loti.ticketsystem.business.exception.GestTicketBusinessException;
import af.cmr.loti.ticketsystem.business.service.ITicketService;
import af.cmr.loti.ticketsystem.business.utils.ConstsValues;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.annotation.Resource;

/**
 * RESTful controller to manage operations related to ticket.
*/
@RestController
@RequestMapping("/tickets")
public class TicketController {
	
	@Resource(name = ConstsValues.ServiceKeys.TICKET_SERVICE_KEY)
	private ITicketService ticketService;
	
	/**
	 * Retrieve the list of all tickets.
	 *
	 * @return ApiResponse containing the list of tickets.
	 */
	@GetMapping
	@Operation(summary = "Rechercher tous les tickets", description = "Cette methode permet de chercher et renvoyer la liste des tickets qui existe dans la BDD")
	public ApiResponse<List<TicketBasicDTO>> getAllTickets() {
		return new ApiResponse<List<TicketBasicDTO>>(HttpStatus.OK.value(), "Ticket list fetched successfully", this.ticketService.findAll());
	}
	
	/**
	 * Retrieve a ticket by its ID.
	 *
	 * @param id The identifier of the ticket.
	 * @return ApiResponse containing information about the ticket.
	 * @throws GestTicketBusinessException If a business exception occurs.
	 */
	@GetMapping("/{id}")
	@Operation(summary = "Rechercher un ticket par ID", description = "Cette methode permet de chercher et renvoyer un ticket par son ID")
	public ApiResponse<TicketFullDTO> getOneTicket(@PathVariable int id) throws GestTicketBusinessException {
		return new ApiResponse<TicketFullDTO>(HttpStatus.OK.value(), "Ticket fetched sucessfully", this.ticketService.findById(id));
	}
	
	/**
	 * Create a new ticket.
	 *
	 * @param dto Information about the ticket to create.
	 * @return ApiResponse containing information about the newly created
	 *         ticket.
	 * @throws GestTicketBusinessException If a business exception occurs.
	 */
	@PostMapping
	@Operation(summary = "Enregistrer un ticket", description = "Cette methode permet d'enregistrer un nouveau ticket")
	public ApiResponse<TicketBasicDTO> create(@RequestBody TicketBasicDTO dto) throws GestTicketBusinessException {
		return new ApiResponse<TicketBasicDTO>(HttpStatus.OK.value(), "Ticket saved successfully", this.ticketService.create(dto));
	}
	
	/**
	 * Update an existing ticket.
	 *
	 * @param id  The identifier of the ticket to update.
	 * @param dto The new information about the ticket.
	 * @return ResponseEntity containing the updated information of the ticket.
	 * @throws GestTicketBusinessException If a business exception occurs.
	 * @throws AccessDeniedException    If access is denied.
	 */
	@PutMapping("/{id}")
	@Operation(summary = "Modifier un ticket", description = "Cette methode permet de modifier un ticket existant dans la BDD")
	public ApiResponse<TicketBasicDTO> update(@PathVariable int id, @RequestBody TicketBasicDTO dto)
			throws GestTicketBusinessException, AccessDeniedException {
		return new ApiResponse<TicketBasicDTO>(HttpStatus.OK.value(), "Ticket updated successfully", this.ticketService.update(dto));
	}
	
	/**
	 * Assign a ticket to a user.
	 *
	 * @param ticketId  The identifier of the ticket.
	 * @param userId  The identifier of the user.
	 * @return ResponseEntity containing information about the ticket's assignment to the user.
	 */
	@PutMapping("/{ticketId}/assign/{userId}")
	@Operation(summary = "Assigner un ticket à un utilisateur", description = "Cette methode permet d'assigner un ticket à un utilisateur")
	public ResponseEntity<?> assignTicketToUser(@PathVariable Integer ticketId, @PathVariable Integer userId) {
	    try {
	        TicketFullDTO ticketDTO = this.ticketService.assignTicketToUser(ticketId, userId);
	        ApiResponse<TicketFullDTO> response = new ApiResponse<>(HttpStatus.OK.value(), "Ticket successfully assigned to user", ticketDTO);
	        return ResponseEntity.ok(response);
	    } catch (GestTicketBusinessException e) {
	        ApiResponse<String> errorResponse = new ApiResponse<>(HttpStatus.NOT_FOUND.value(), e.getMessage(), null);
	        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
	    }
	}
	
	/**
	 * Delete a ticket by its ID.
	 *
	 * @param id The identifier of the ticket to delete.
	 * @return ApiResponse with an empty body indicating the ticket has been
	 *         successfully deleted.
	 * @throws GestTicketBusinessException If a business exception occurs.
	 * @throws AccessDeniedException    If access is denied.
	 */
	@DeleteMapping("/{id}")
	@Operation(summary = "Supprimer un ticket", description = "Cette methode permet de supprimer un ticket par son ID")
	public ApiResponse<?> delete(@PathVariable int id) throws GestTicketBusinessException, AccessDeniedException {
		this.ticketService.deleteById(id);
		return new ApiResponse<Void>(HttpStatus.OK.value(), "Ticket deleted sucessfully", null);
	}
}
