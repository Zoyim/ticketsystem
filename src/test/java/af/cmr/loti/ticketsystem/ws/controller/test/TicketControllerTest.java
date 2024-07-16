package af.cmr.loti.ticketsystem.ws.controller.test;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.nio.file.AccessDeniedException;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.fasterxml.jackson.databind.ObjectMapper;

import af.cmr.loti.ticketsystem.boot.TicketsystemApplication;
import af.cmr.loti.ticketsystem.business.dto.TicketBasicDTO;
import af.cmr.loti.ticketsystem.business.dto.TicketFullDTO;
import af.cmr.loti.ticketsystem.business.dto.UserBasicDTO;
import af.cmr.loti.ticketsystem.business.entity.Status;
import af.cmr.loti.ticketsystem.business.exception.GestTicketBusinessException;
import af.cmr.loti.ticketsystem.business.service.ITicketService;

@SpringBootTest(classes = TicketsystemApplication.class)
@AutoConfigureMockMvc
public class TicketControllerTest {
	
	@Autowired
    private WebApplicationContext webApplicationContext;

	@Autowired
    private MockMvc mockMvc;

    @MockBean
    private ITicketService ticketService;

    private ObjectMapper objectMapper = new ObjectMapper();

    private TicketBasicDTO ticketForAllTest;
    private Integer idCreatedTicket;
    
    @BeforeEach
    public void setup() throws Exception {
        MockitoAnnotations.openMocks(this);
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.webApplicationContext).build();

        TicketBasicDTO ticket = new TicketBasicDTO();
        ticket.setTitle("Sample Ticket");

        TicketBasicDTO createdTicket = new TicketBasicDTO();
        createdTicket.setId(1);
        createdTicket.setTitle("Sample Ticket");

        given(this.ticketService.create(any(TicketBasicDTO.class))).willReturn(createdTicket);

        this.ticketForAllTest = this.ticketService.create(new TicketBasicDTO());
        this.idCreatedTicket = this.ticketForAllTest.getId();
    }

    @Test
    public void testGetAllTickets() throws Exception {
        TicketBasicDTO ticket = new TicketBasicDTO();
        ticket.setId(1);
        ticket.setTitle("Test Ticket");
        ticket.setStatus(Status.IN_PROGRESS);

        List<TicketBasicDTO> allTickets = Arrays.asList(ticket);

        given(this.ticketService.findAll()).willReturn(allTickets);

        mockMvc.perform(get("/tickets"))
        	.andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.result[0].title").value("Test Ticket"));
    }
    
    @Test
    public void testGetOneTicket() throws Exception {
        TicketFullDTO ticket = new TicketFullDTO();
        ticket.setId(1);
        ticket.setTitle("Test Ticket");
        ticket.setStatus(Status.IN_PROGRESS);

        given(this.ticketService.findById(1)).willReturn(ticket);

        mockMvc.perform(get("/tickets/1"))
        	.andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.result.title").value("Test Ticket"));
    }
    
    @Test
    public void testCreateTicket() throws Exception {
        TicketBasicDTO ticket = new TicketBasicDTO();
        ticket.setTitle("New Ticket");

        TicketBasicDTO createdTicket = new TicketBasicDTO();
        createdTicket.setId(1);
        createdTicket.setTitle("New Ticket");
        createdTicket.setDescription("Example ticket description");
        createdTicket.setStatus(Status.IN_PROGRESS);

        given(this.ticketService.create(any(TicketBasicDTO.class))).willReturn(createdTicket);

        mockMvc.perform(post("/tickets")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(ticket)))
        	.andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.result.title").value("New Ticket"));
    }
    
    @Test
    public void testUpdateTicket() throws Exception {
        TicketBasicDTO ticket = new TicketBasicDTO();
        ticket.setTitle("Updated Ticket");

        TicketBasicDTO updatedTicket = new TicketBasicDTO();
        updatedTicket.setId(1);
        updatedTicket.setTitle("Updated Ticket");
        updatedTicket.setDescription("Example ticket description");
        updatedTicket.setStatus(Status.IN_PROGRESS);

        given(this.ticketService.update(any(TicketBasicDTO.class))).willReturn(updatedTicket);

        mockMvc.perform(put("/tickets/1")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(ticket)))
        	.andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.result.title").value("Updated Ticket"));
    }
    
    @Test
    public void testAssignTicketToUser() throws Exception {
        TicketFullDTO ticket = new TicketFullDTO();
        ticket.setId(1);
        ticket.setTitle("Assigned Ticket");
        ticket.setDescription("Example ticket description");
        ticket.setStatus(Status.IN_PROGRESS);

        UserBasicDTO user = new UserBasicDTO();
        user.setId(1);
        user.setUsername("Emmanuel");
        user.setEmail("emmanuel@gmail.com");
        user.setPassword("pass1234");

        ticket.setUser(user);

        given(this.ticketService.assignTicketToUser(1, 1)).willReturn(ticket);

        mockMvc.perform(put("/tickets/1/assign/1"))
        	.andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.result.title").value("Assigned Ticket"))
            .andExpect(jsonPath("$.result.user.username").value("Emmanuel"));
    }

    
    @Test
    public void testDeleteTicket() throws Exception {
        mockMvc.perform(delete("/tickets/1"))
            .andExpect(status().isOk());

        verify(this.ticketService, times(1)).deleteById(1);
    }
    
    @AfterEach
    public void rollback() throws GestTicketBusinessException, AccessDeniedException {
        if (this.ticketForAllTest != null) {
            this.ticketService.deleteById(this.ticketForAllTest.getId());
        }
        if (this.idCreatedTicket != null) {
            this.ticketService.deleteById(this.idCreatedTicket);
        }
    }
}
