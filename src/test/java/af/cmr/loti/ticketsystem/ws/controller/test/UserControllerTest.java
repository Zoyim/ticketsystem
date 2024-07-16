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
import java.security.Principal;
import java.util.ArrayList;
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
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.fasterxml.jackson.databind.ObjectMapper;

import af.cmr.loti.ticketsystem.boot.TicketsystemApplication;
import af.cmr.loti.ticketsystem.business.dto.TicketBasicDTO;
import af.cmr.loti.ticketsystem.business.dto.UserBasicDTO;
import af.cmr.loti.ticketsystem.business.dto.UserFullDTO;
import af.cmr.loti.ticketsystem.business.entity.Status;
import af.cmr.loti.ticketsystem.business.exception.GestTicketBusinessException;
import af.cmr.loti.ticketsystem.business.service.IUserService;

@SpringBootTest(classes = TicketsystemApplication.class)
@AutoConfigureMockMvc
public class UserControllerTest {
    
    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private IUserService userService;

    private ObjectMapper objectMapper = new ObjectMapper();

    private UserBasicDTO userForAllTest;
    private Integer idCreatedUser;
    
    @BeforeEach
    public void setup() throws Exception {
        MockitoAnnotations.openMocks(this);
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.webApplicationContext).build();

        UserBasicDTO user = new UserBasicDTO();
        user.setId(1);
        user.setUsername("Abdou");
        user.setEmail("lotizoyim@gmail.com");
        user.setPassword("pass1234");
        
        List<UserBasicDTO> allUsers = Arrays.asList(user);
        
        given(this.userService.findAll()).willReturn(allUsers);

        given(this.userService.create(any(UserBasicDTO.class))).willReturn(user);
        
        this.userForAllTest = this.userService.create(new UserBasicDTO());
        this.idCreatedUser = this.userForAllTest.getId();
    }
    
    @Test
    public void testCreateUser() throws Exception {

    	UserBasicDTO user = new UserBasicDTO();
        user.setId(1);
        user.setUsername("Aliou");
        user.setEmail("garga.aliou@indyli-services.com");
        user.setPassword("pass1234");

        given(this.userService.create(any(UserBasicDTO.class))).willReturn(user);

        mockMvc.perform(post("/users")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(user)))
            .andExpect(status().isOk())
            .andDo(print())
            .andExpect(jsonPath("$.result.username").value("Aliou"));
    }
    
    @Test
    public void testGetAllUsers() throws Exception {

    	UserBasicDTO user = new UserBasicDTO();
        user.setId(1);
        user.setUsername("Abdou");
        user.setEmail("lotizoyim@gmail.com");
        user.setPassword("pass1234");

        List<UserBasicDTO> allUsers = Arrays.asList(user);

        given(this.userService.findAll()).willReturn(allUsers);
        
        mockMvc.perform(get("/users"))
        	.andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.result[0].username").value("Abdou"));
    }
    
    @Test
    public void testGetOneUser() throws Exception {

    	UserFullDTO user = new UserFullDTO();
        user.setId(1);
        user.setUsername("Abdou");
        user.setEmail("lotizoyim@gmail.com");
        user.setPassword("pass1234");
        
        TicketBasicDTO ticket = new TicketBasicDTO();
        ticket.setId(1);
        ticket.setTitle("Exemple ticket");
        ticket.setDescription("Exemple description");
        ticket.setStatus(Status.IN_PROGRESS);
        
        List<TicketBasicDTO> tickets = new ArrayList<>();
        tickets.add(ticket);
        
        user.setTickets(tickets);

        given(this.userService.findById(1)).willReturn(user);

        mockMvc.perform(get("/users/1"))
        	.andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.result.username").value("Abdou"));
    }
    
    @Test
    @WithMockUser(username = "loti", roles = {"ADMIN"})
    public void testGetUserTickets() throws Exception {

    	Principal mockPrincipal = () -> "loti";

        TicketBasicDTO ticket1 = new TicketBasicDTO();
        ticket1.setId(1);
        ticket1.setTitle("Sample Ticket 1");
        ticket1.setDescription("Example description 1");
        ticket1.setStatus(Status.IN_PROGRESS);

        TicketBasicDTO ticket2 = new TicketBasicDTO();
        ticket2.setId(2);
        ticket2.setTitle("Sample Ticket 2");
        ticket2.setDescription("Example description 2");
        ticket2.setStatus(Status.IN_PROGRESS);

        List<TicketBasicDTO> userTickets = Arrays.asList(ticket1, ticket2);

        given(userService.getUserTickets(1, "loti")).willReturn(userTickets);

        mockMvc.perform(MockMvcRequestBuilders.get("/users/1/tickets")
                .principal(mockPrincipal)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.result[0].title").value("Sample Ticket 1"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.result[1].title").value("Sample Ticket 2"))
                .andDo(MockMvcResultHandlers.print());
    }
    
    @Test
    public void testUpdateUser() throws Exception {

    	UserBasicDTO user = new UserBasicDTO();
        user.setUsername("Garga");

        given(this.userService.update(any(UserBasicDTO.class))).willReturn(user);

        mockMvc.perform(put("/users/1")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(user)))
            .andExpect(status().isOk())
            .andDo(print())
            .andExpect(jsonPath("$.result.username").value("Garga"));
    }
    
    @Test
    public void testDeleteUser() throws Exception {

    	mockMvc.perform(delete("/users/1"))
            .andExpect(status().isOk());

        verify(this.userService, times(1)).deleteById(1);
    }
    
    @AfterEach
    public void rollback() throws GestTicketBusinessException, AccessDeniedException {

    	if (this.userForAllTest != null) {
            this.userService.deleteById(this.userForAllTest.getId());
        }
        if (this.idCreatedUser != null) {
            this.userService.deleteById(this.idCreatedUser);
        }
    }
}