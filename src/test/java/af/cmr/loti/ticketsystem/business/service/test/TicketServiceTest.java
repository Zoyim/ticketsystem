package af.cmr.loti.ticketsystem.business.service.test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.nio.file.AccessDeniedException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import af.cmr.loti.ticketsystem.business.dao.TicketRepository;
import af.cmr.loti.ticketsystem.business.dao.UserRepository;
import af.cmr.loti.ticketsystem.business.dto.TicketBasicDTO;
import af.cmr.loti.ticketsystem.business.dto.TicketFullDTO;
import af.cmr.loti.ticketsystem.business.dto.UserBasicDTO;
import af.cmr.loti.ticketsystem.business.entity.Status;
import af.cmr.loti.ticketsystem.business.entity.Ticket;
import af.cmr.loti.ticketsystem.business.entity.User;
import af.cmr.loti.ticketsystem.business.exception.GestTicketBusinessException;
import af.cmr.loti.ticketsystem.business.service.impl.TicketServiceImpl;
import jakarta.validation.Validator;

@ExtendWith(MockitoExtension.class)
public class TicketServiceTest {

	@InjectMocks
    private TicketServiceImpl ticketService;
	
	@Mock
	private TicketRepository ticketRepository;
	
	@Mock
    private UserRepository userRepository;
	
	@Mock
    private Validator validator;
	
	@Mock
    private ModelMapper modelMapper;

    private TicketBasicDTO ticketForAllTest = null;
    private Integer idCreatedTicket = null;

    @SuppressWarnings("unchecked")
	@BeforeEach
    public void setUp() throws GestTicketBusinessException {
        MockitoAnnotations.openMocks(this);
        
        when(modelMapper.map(any(), any(Class.class))).thenAnswer(invocation -> {
            Object source = invocation.getArgument(0);
            Class<?> destinationType = invocation.getArgument(1);
            return new ModelMapper().map(source, destinationType);
        });
        
        this.ticketService.setModelMapper(modelMapper);

        Ticket createdTicketEntity = getSampleTicketEntity(1, "Example Ticket", "Description de l'exemple", Status.IN_PROGRESS);

        when(this.ticketRepository.saveAndFlush(any(Ticket.class))).thenReturn(createdTicketEntity);

        TicketBasicDTO ticketDTO = getSampleTicket();
        this.ticketForAllTest = this.ticketService.create(ticketDTO);

        assertNotNull(this.ticketForAllTest);
    }

    @Test
    public void testCreate() throws GestTicketBusinessException {
        Ticket createdTicketEntity = getSampleTicketEntity(2, "Example Ticket Create", "Example ticket description", Status.IN_PROGRESS);
        
        TicketBasicDTO ticket = getSampleTicket();
        ticket.setTitle("Petits carreaux");
        
        when(this.ticketRepository.saveAndFlush(any(Ticket.class))).thenReturn(createdTicketEntity);
        this.idCreatedTicket = this.ticketService.create(ticket).getId();
        
        assertNotNull(this.idCreatedTicket);
        assertEquals(2, this.idCreatedTicket);
    }

    @Test
    public void testFindAll() {
    	List<Ticket> ticketEntities = new ArrayList<>();
    	ticketEntities.add(getSampleTicketEntity(1, "Example Ticket", "Description de l'exemple", Status.IN_PROGRESS));
    	
    	when(this.ticketRepository.findAll()).thenReturn(ticketEntities);
    	List<TicketBasicDTO> resultTickets = this.ticketService.findAll();
    	
    	assertEquals(1, resultTickets.size());
        assertEquals(this.ticketForAllTest.getTitle(), resultTickets.get(0).getTitle());
    }

    @Test
    void testFindById() throws GestTicketBusinessException {
    	Ticket sampleTicketEntity = getSampleTicketEntity(this.ticketForAllTest.getId(), this.ticketForAllTest.getTitle(), this.ticketForAllTest.getDescription(), this.ticketForAllTest.getStatus());
    	
    	when(this.ticketRepository.findById(this.ticketForAllTest.getId())).thenReturn(Optional.of(sampleTicketEntity));
    	TicketFullDTO ticket = this.ticketService.findById(this.ticketForAllTest.getId());
    	
    	assertNotNull(ticket);
        assertEquals(ticket.getTitle(), this.ticketForAllTest.getTitle());
    }

    @Test
    void testUpdate() throws GestTicketBusinessException, AccessDeniedException {
    	
    	Ticket sampleTicketEntity = getSampleTicketEntity(this.ticketForAllTest.getId(), "Example Ticket", "Example ticket description", Status.IN_PROGRESS);
    	
    	String updateTitle = "Example Ticket Updated";
    	Ticket updatedTicketEntity = getSampleTicketEntity(this.ticketForAllTest.getId(), updateTitle, sampleTicketEntity.getDescription(), sampleTicketEntity.getStatus());
    	
    	TicketBasicDTO ticketToUpdate = getSampleTicket();
        ticketToUpdate.setId(this.ticketForAllTest.getId());
        ticketToUpdate.setTitle(updateTitle);
        
        when(this.ticketRepository.findById(this.ticketForAllTest.getId())).thenReturn(Optional.of(sampleTicketEntity));
        when(this.ticketRepository.saveAndFlush(any(Ticket.class))).thenReturn(updatedTicketEntity);
    	
        TicketBasicDTO resultTicket = this.ticketService.update(ticketToUpdate);

        assertEquals(updateTitle, resultTicket.getTitle());
    }
    
    @Test
    public void testAssignTicketToUser() throws GestTicketBusinessException {

    	Integer ticketId = 1;
        Integer userId = 1;

        Ticket ticket = getSampleTicketEntity(ticketId, "Sample Ticket", "Sample Description", Status.IN_PROGRESS);
        User user = new User();
        user.setId(userId);
        user.setUsername("Loti");

        UserBasicDTO userBasicDTO = new UserBasicDTO();
        userBasicDTO.setId(userId);
        userBasicDTO.setUsername("Loti");

        TicketFullDTO ticketFullDTO = new TicketFullDTO(ticketId, ticket.getTitle(), ticket.getDescription(), ticket.getStatus());
        ticketFullDTO.setUser(userBasicDTO);

        when(this.ticketRepository.findById(ticketId)).thenReturn(Optional.of(ticket));
        when(this.userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(this.ticketRepository.save(any(Ticket.class))).thenReturn(ticket);
        when(this.modelMapper.map(any(Ticket.class), eq(TicketFullDTO.class))).thenReturn(ticketFullDTO);
        when(this.modelMapper.map(any(User.class), eq(UserBasicDTO.class))).thenReturn(userBasicDTO);

        // Act
        TicketFullDTO result = this.ticketService.assignTicketToUser(ticketId, userId);

        // Assert
        assertNotNull(result);
        assertEquals(ticketId, result.getId());
        assertEquals(userId, result.getUser().getId());
        verify(this.ticketRepository).findById(ticketId);
        verify(this.userRepository).findById(userId);
        verify(this.ticketRepository).save(ticket);
        verify(this.modelMapper).map(ticket, TicketFullDTO.class);
        verify(this.modelMapper).map(user, UserBasicDTO.class);
    }

    @Test
    public void testDelete() throws GestTicketBusinessException, AccessDeniedException {
    	Mockito.doNothing().when(this.ticketRepository).deleteById(this.ticketForAllTest.getId());
    	this.ticketService.deleteById(this.ticketForAllTest.getId());
    	verify(this.ticketRepository, times(1)).deleteById(this.ticketForAllTest.getId());
    	when(this.ticketRepository.findById(this.ticketForAllTest.getId())).thenReturn(Optional.empty());
    	TicketFullDTO deletedTicket = this.ticketService.findById(this.ticketForAllTest.getId());
    	assertNull(deletedTicket);
    }

    @AfterEach
    public void rollback() throws GestTicketBusinessException, AccessDeniedException {
    	if (this.ticketForAllTest != null) {
            this.ticketService.deleteById(this.ticketForAllTest.getId());
        }
        if (idCreatedTicket != null) {
            this.ticketService.deleteById(idCreatedTicket);
        }
    }

    private TicketBasicDTO getSampleTicket() {
        TicketBasicDTO ticket = new TicketBasicDTO();
        ticket.setTitle("Example Ticket");
        ticket.setDescription("Example ticket description");
        ticket.setStatus(Status.IN_PROGRESS);
        return ticket;
    }
    
    private Ticket getSampleTicketEntity(Integer id, String title, String description, Status status) {
        Ticket ticket = new Ticket();
        ticket.setId(id);
        ticket.setTitle(title);
        ticket.setDescription(description);
        ticket.setStatus(status);
        return ticket;
    } 
}
