package af.cmr.loti.ticketsystem.business.service;

import java.nio.file.AccessDeniedException;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import af.cmr.loti.ticketsystem.business.exception.GestTicketBusinessException;

/**
 * Interface defining basic operations for an Ticket System service, allowing
 * manipulation and management of entities with different types of DTOs (Data
 * Transfer Objects). This interface provides methods to create, update, delete,
 * and retrieve entities, as well as to check the existence of an entity by its
 * identifier.
 *
 * @param <Entity>     The type of the entity manipulated by the service.
 * @param <BasicDTO>   The basic type of the DTO used to represent the entity.
 * @param <FullDTO>    The complete type of the DTO, extending the base type,
 *                     used to represent the entity with more details.
 * @param <IEntityDAO> The type of the DAO (Data Access Object) used to interact
 *                     with the database.
 */


public interface IAbstractTicketSystemService <Entity, BasicDTO, FullDTO extends BasicDTO, IEntityDAO extends JpaRepository<Entity, Integer>> {

	public BasicDTO create(BasicDTO ent) throws GestTicketBusinessException;

	public BasicDTO update(BasicDTO entToUpdate) throws GestTicketBusinessException, AccessDeniedException;

	public void deleteById(int id) throws GestTicketBusinessException, AccessDeniedException;

	public List<BasicDTO> findAll();

	public FullDTO findById(int id) throws GestTicketBusinessException;

	public IEntityDAO getDAO();

}
