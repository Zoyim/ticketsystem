package af.cmr.loti.ticketsystem.business.service.impl;

import java.nio.file.AccessDeniedException;
import java.util.ArrayList;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.data.jpa.repository.JpaRepository;

import af.cmr.loti.ticketsystem.business.dto.IDTO;
import af.cmr.loti.ticketsystem.business.entity.IEntity;
import af.cmr.loti.ticketsystem.business.exception.GestTicketBusinessException;
import af.cmr.loti.ticketsystem.business.service.IAbstractTicketSystemService;
import af.cmr.loti.ticketsystem.business.utils.ConstRejectBusinessMessage;
import jakarta.annotation.Resource;

/**
 * Abstract class implementing the IAbstractTicketSystemService interface, providing
 * common functionality for service implementations managing entities in the
 * Ticket System system. This class handles basic CRUD operations for entities and DTO
 * mapping.
 *
 * @param <Entity>     The type of the entity handled by the service.
 * @param <BasicDTO>   The basic DTO type used for representing the entity.
 * @param <FullDTO>    The complete DTO type, extending the base type, used for
 *                     detailed representation of the entity.
 * @param <IEntityDAO> The type of the DAO (Data Access Object) used to interact
 *                     with the database.
 */
public abstract class AbstractTicketSystemServiceImpl<Entity extends IEntity, BasicDTO extends IDTO, FullDTO extends BasicDTO, IEntityDAO extends JpaRepository<Entity, Integer>> implements IAbstractTicketSystemService<Entity, BasicDTO, FullDTO, IEntityDAO>{
	
	private final Class<Entity> entityClass;
	private final Class<BasicDTO> basicClass;
	private final Class<FullDTO> fullClass;
	@Resource(name = "ticketsystem-modelmapper")
	private ModelMapper mapper;
	
	public AbstractTicketSystemServiceImpl(Class<Entity> myEntityClass, Class<BasicDTO> basicClass,
			Class<FullDTO> fullViewClass) {
		this.entityClass = myEntityClass;
		this.basicClass = basicClass;
		this.fullClass = fullViewClass;
	}

	public BasicDTO create(BasicDTO view) throws GestTicketBusinessException {
		Entity entity = this.getDAO().saveAndFlush(this.getModelMapper().map(view, this.entityClass));
		view.setId(entity.getId());
		return view;
	}
	
	public BasicDTO update(BasicDTO viewToUpdate) throws GestTicketBusinessException, AccessDeniedException {
		Entity entity = this.getDAO().findById(viewToUpdate.getId()).orElse(null);
		if (entity != null) {
			BeanUtils.copyProperties(viewToUpdate, entity);
			this.getDAO().saveAndFlush(entity);
		} else {
			throw new GestTicketBusinessException(ConstRejectBusinessMessage.UPDATE_OBJECT_NOT_FOUND);
		}
		return viewToUpdate;
	}
	
	public void deleteById(int id) throws GestTicketBusinessException, AccessDeniedException {
		this.getDAO().deleteById(id);
	}

	public List<BasicDTO> findAll() {
		List<Entity> list = this.getDAO().findAll();
		List<BasicDTO> viewList = new ArrayList<BasicDTO>();
		for (Entity ent : list) {
			BasicDTO view = this.getModelMapper().map(ent, this.basicClass);
			viewList.add(view);
		}
		return viewList;
	}

	public FullDTO findById(int id) throws GestTicketBusinessException {
		Entity ent = this.getDAO().findById(id).orElse(null);
		if (ent == null) {
			return null;
		}
		return this.getModelMapper().map(ent, this.fullClass);
	}

	public boolean ifEntityExistById(int id) throws GestTicketBusinessException {
		return this.getDAO().existsById(id);
	}

	public abstract IEntityDAO getDAO();

	public ModelMapper getModelMapper() {
		return this.mapper;
	}
	
	public void setModelMapper(ModelMapper mapper) {
        this.mapper = mapper;
    }
}
