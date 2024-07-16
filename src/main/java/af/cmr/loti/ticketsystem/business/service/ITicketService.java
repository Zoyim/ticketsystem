package af.cmr.loti.ticketsystem.business.service;

import af.cmr.loti.ticketsystem.business.dao.TicketRepository;
import af.cmr.loti.ticketsystem.business.dto.TicketBasicDTO;
import af.cmr.loti.ticketsystem.business.dto.TicketFullDTO;
import af.cmr.loti.ticketsystem.business.entity.Ticket;
import af.cmr.loti.ticketsystem.business.exception.GestTicketBusinessException;

public interface ITicketService extends IAbstractTicketSystemService<Ticket, TicketBasicDTO, TicketFullDTO, TicketRepository> {
	
	public TicketFullDTO assignTicketToUser(Integer ticketId, Integer userId) throws GestTicketBusinessException;
		
}
