package af.cmr.loti.ticketsystem.ws.controller;

import java.nio.file.AccessDeniedException;
import java.security.Principal;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import af.cmr.loti.ticketsystem.business.dto.TicketBasicDTO;
import af.cmr.loti.ticketsystem.business.dto.UserBasicDTO;
import af.cmr.loti.ticketsystem.business.dto.UserFullDTO;
import af.cmr.loti.ticketsystem.business.entity.ApiResponse;
import af.cmr.loti.ticketsystem.business.exception.GestTicketBusinessException;
import af.cmr.loti.ticketsystem.business.service.IUserService;
import af.cmr.loti.ticketsystem.business.utils.ConstsValues;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.annotation.Resource;

/**
 * RESTful controller to manage operations related to users.
*/
@RestController
@RequestMapping("/users")
@Validated
public class UserController {

	@Resource(name = ConstsValues.ServiceKeys.USER_SERVICE_KEY)
	private IUserService userService;
	
	/**
	 * Retrieve the list of all users.
	 *
	 * @return ApiResponse containing the list of users.
	 */
	@GetMapping
	@Operation(summary = "Rechercher tous les utilisateurs", description = "Cette methode permet de chercher et renvoyer la liste des utilisateurs qui existe dans la BDD")
	public ApiResponse<List<UserBasicDTO>> getAllUsers() {
		return new ApiResponse<List<UserBasicDTO>>(HttpStatus.OK.value(), "User list fetched successfully", this.userService.findAll());
	}

	/**
	 * Retrieve information about a user by their identifier.
	 *
	 * @param id The identifier of the user.
	 * @return ApiResponse containing information about the user.
	 * @throws GestTicketBusinessException If a business exception occurs.
	 */
	@GetMapping("/{id}")
	@Operation(summary = "Rechercher un utilisateur par ID", description = "Cette methode permet de chercher et renvoyer un utilisateur par son ID")
	public ApiResponse<UserFullDTO> getOneUser(@PathVariable int id) throws GestTicketBusinessException {
		return new ApiResponse<UserFullDTO>(HttpStatus.OK.value(), "User fetched sucessfully", this.userService.findById(id));
	}
	
	/**
	 * Retrieve tickets assigned to the user.
	 * @param id The identifier of the user.
	 * @throws GestTicketBusinessException 
	 * 
	 */
	@GetMapping("/{userId}/tickets")
	@Operation(summary = "Rechercher les tickets assignés à l'utilisateur", description = "Cette methode permet de chercher et renvoyer la liste des tickets assignés à un utilisateur")
    public ApiResponse<List<TicketBasicDTO>> getUserTickets(@PathVariable Integer userId, Principal principal) throws GestTicketBusinessException {
		 if (principal == null) {
		     throw new GestTicketBusinessException("Principal is null, you must be authenticated to view tickets");
		 }
		List<TicketBasicDTO> tickets = this.userService.getUserTickets(userId, principal.getName());
		return new ApiResponse<List<TicketBasicDTO>>(HttpStatus.OK.value(), "The list of tickets assigned to the user has been successfully retrieved", tickets);
    }

	/**
	 * Create a new user.
	 *
	 * @param dto Information about the user to create.
	 * @return ApiResponse containing information about the newly created
	 *         user.
	 * @throws GestTicketBusinessException If a business exception occurs.
	 */
	@PostMapping
	@Operation(summary = "Enregistrer un utilisateur", description = "Cette methode permet d'enregistrer un nouveau utilisateur")
	public ApiResponse<UserBasicDTO> create(@RequestBody UserBasicDTO dto) throws GestTicketBusinessException {
		return new ApiResponse<UserBasicDTO>(HttpStatus.OK.value(), "User saved successfully", this.userService.create(dto));
	}
	
	/**
	 * Update information about a user.
	 *
	 * @param id  The identifier of the user to update.
	 * @param dto The new information about the user.
	 * @return ApiResponse containing the updated information of the user.
	 * @throws GestTicketBusinessException If a business exception occurs.
	 * @throws AccessDeniedException    If access is denied.
	 */
	@PutMapping("/{id}")
	@Operation(summary = "Modifier un utilisateur", description = "Cette methode permet de modifier un utilisateur existant dans la BDD")
	public ApiResponse<UserBasicDTO> update(@PathVariable int id, @RequestBody UserBasicDTO dto)
			throws GestTicketBusinessException, AccessDeniedException {
		return new ApiResponse<UserBasicDTO>(HttpStatus.OK.value(), "User updated successfully", this.userService.update(dto));
	}


	/**
	 * Delete a user by their identifier.
	 *
	 * @param id The identifier of the user to delete.
	 * @return ApiResponse with an empty body indicating the user has been
	 *         successfully deleted.
	 * @throws GestTicketBusinessException If a business exception occurs.
	 * @throws AccessDeniedException    If access is denied.
	 */
	@DeleteMapping("/{id}")
	@Operation(summary = "Supprimer un utilisateur", description = "Cette methode permet de supprimer un utilisateur par son ID")
	public ApiResponse<?> delete(@PathVariable int id) throws GestTicketBusinessException, AccessDeniedException {
		this.userService.deleteById(id);
		return new ApiResponse<Void>(HttpStatus.OK.value(), "User deleted sucessfully", null);
	}

}
