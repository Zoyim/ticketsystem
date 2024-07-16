package af.cmr.loti.ticketsystem.business.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import af.cmr.loti.ticketsystem.business.entity.Ticket;
import af.cmr.loti.ticketsystem.business.utils.ConstsValues;

@Repository(value = ConstsValues.ConstsDAO.TICKET_DAO_KEY)
public interface TicketRepository extends JpaRepository<Ticket, Integer> {
	
	public List<Ticket> findByUserId(Integer userId);
	
	Ticket findByTitle(String title);
}
