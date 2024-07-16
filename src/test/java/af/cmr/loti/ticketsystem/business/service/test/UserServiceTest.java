package af.cmr.loti.ticketsystem.business.service.test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.nio.file.AccessDeniedException;
import java.util.ArrayList;
import java.util.Arrays;
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
import org.springframework.boot.test.context.SpringBootTest;

import af.cmr.loti.ticketsystem.boot.TicketsystemApplication;
import af.cmr.loti.ticketsystem.business.dao.UserRepository;
import af.cmr.loti.ticketsystem.business.dto.TicketBasicDTO;
import af.cmr.loti.ticketsystem.business.dto.UserBasicDTO;
import af.cmr.loti.ticketsystem.business.dto.UserFullDTO;
import af.cmr.loti.ticketsystem.business.entity.Ticket;
import af.cmr.loti.ticketsystem.business.entity.User;
import af.cmr.loti.ticketsystem.business.exception.GestTicketBusinessException;
import af.cmr.loti.ticketsystem.business.service.impl.UserServiceImpl;
import jakarta.validation.Validator;

@ExtendWith(MockitoExtension.class)
@SpringBootTest(classes = {TicketsystemApplication.class})
public class UserServiceTest {
	
	@InjectMocks
	private UserServiceImpl userService;
	
	@Mock
    private UserRepository userRepository;
	
	@Mock
    private Validator validator;
	
	@Mock
    private ModelMapper modelMapper;
	
	private UserBasicDTO userForAllTest = null;
    private Integer idCreatedUser = null;
    
    
    @SuppressWarnings("unchecked")
	@BeforeEach
    public void setUp() throws GestTicketBusinessException {
        MockitoAnnotations.openMocks(this);
        
        when(modelMapper.map(any(), any(Class.class))).thenAnswer(invocation -> {
            Object source = invocation.getArgument(0);
            Class<?> destinationType = invocation.getArgument(1);
            return new ModelMapper().map(source, destinationType);
        });
        
        this.userService.setModelMapper(modelMapper);

        User createdUserEntity = getSampleUserEntity(1, "Emmanuel", "emmanuelletiko@gmail.com", "pass1234");

        when(this.userRepository.saveAndFlush(any(User.class))).thenReturn(createdUserEntity);

        UserBasicDTO userDTO = getSampleUser();
        this.userForAllTest = this.userService.create(userDTO);

        assertNotNull(this.userForAllTest);
    }
    
    @Test
    public void testCreate() throws GestTicketBusinessException {
        User createdUserEntity = getSampleUserEntity(2, "Loti", "lotizoyim@gmail.com", "pass1234");
        
        UserBasicDTO user = getSampleUser();
        user.setEmail("abdou.zoyim@indyli-services.com");
        
        when(this.userRepository.saveAndFlush(any(User.class))).thenReturn(createdUserEntity);
        this.idCreatedUser = this.userService.create(user).getId();
        
        assertNotNull(this.idCreatedUser);
        assertEquals(2, this.idCreatedUser);
    }
    
    @Test
    public void testFindAll() {
    	List<User> userEntities = new ArrayList<>();
    	userEntities.add(getSampleUserEntity(1, "Loti", "lotizoyim@gmail.com", "pass1234"));
    	
    	when(this.userRepository.findAll()).thenReturn(userEntities);
    	List<UserBasicDTO> resultUsers = this.userService.findAll();
    	
    	assertEquals(1, resultUsers.size());
    	assertEquals(this.userForAllTest.getUsername(), resultUsers.get(0).getUsername());
    }
    
    @Test
    void testFindById() throws GestTicketBusinessException {
    	User sampleUserEntity = getSampleUserEntity(this.userForAllTest.getId(), this.userForAllTest.getUsername(), this.userForAllTest.getEmail(), this.userForAllTest.getPassword());
    	
    	when(this.userRepository.findById(this.userForAllTest.getId())).thenReturn(Optional.of(sampleUserEntity));
    	UserFullDTO user = this.userService.findById(this.userForAllTest.getId());
    	
    	assertNotNull(user);
        assertEquals(user.getUsername(), this.userForAllTest.getUsername());
    }
    
    @Test
    public void testGetUserTickets() throws GestTicketBusinessException {

    	User userWithTickets = getSampleUserEntity(this.userForAllTest.getId(), this.userForAllTest.getUsername(),
                this.userForAllTest.getEmail(), this.userForAllTest.getPassword());
        Ticket ticket = new Ticket();
        ticket.setId(1);
        ticket.setTitle("Example Ticket");
        userWithTickets.setTickets(Arrays.asList(ticket));

        when(userRepository.findById(anyInt())).thenReturn(Optional.of(userWithTickets));
        when(modelMapper.map(any(Ticket.class), any())).thenReturn(new TicketBasicDTO(ticket.getId(), ticket.getTitle()));

        List<TicketBasicDTO> userTickets = userService.getUserTickets(userForAllTest.getId(), "Loti");

        assertNotNull(userTickets);
        assertEquals(1, userTickets.size());
        assertEquals(ticket.getId(), userTickets.get(0).getId());

        verify(userRepository).findById(anyInt());
        verify(modelMapper).map(any(Ticket.class), any());
    }
    
    @Test
    void testUpdate() throws GestTicketBusinessException, AccessDeniedException {
    	
    	User sampleUserEntity = getSampleUserEntity(this.userForAllTest.getId(), "Emmanuel", "emmanuelletiko@gmail.com", "pass1234");
    	
    	String updateUsername = "Macron";
    	User updatedUserEntity = getSampleUserEntity(this.userForAllTest.getId(), updateUsername, sampleUserEntity.getEmail(), sampleUserEntity.getPassword());
    	
    	UserBasicDTO userToUpdate = getSampleUser();
        userToUpdate.setId(this.userForAllTest.getId());
        userToUpdate.setUsername(updateUsername);
        
        when(this.userRepository.findById(this.userForAllTest.getId())).thenReturn(Optional.of(sampleUserEntity));
        when(this.userRepository.saveAndFlush(any(User.class))).thenReturn(updatedUserEntity);
    	
        UserBasicDTO resultUser = this.userService.update(userToUpdate);

        assertEquals(updateUsername, resultUser.getUsername());
    }
    
    @Test 
    public void testDelete() throws GestTicketBusinessException, AccessDeniedException {
    	Mockito.doNothing().when(this.userRepository).deleteById(this.userForAllTest.getId());
    	this.userService.deleteById(this.userForAllTest.getId());
    	verify(this.userRepository, times(1)).deleteById(this.userForAllTest.getId());
    	when(this.userRepository.findById(this.userForAllTest.getId())).thenReturn(Optional.empty());
    	UserFullDTO deletedTicket = this.userService.findById(this.userForAllTest.getId());
    	assertNull(deletedTicket);
    }
    
    @AfterEach
    public void rollback() throws GestTicketBusinessException, AccessDeniedException {
    	if (this.userForAllTest != null) {
            this.userService.deleteById(this.userForAllTest.getId());
        }
        if (idCreatedUser != null) {
            this.userService.deleteById(idCreatedUser);
        }
    }
    
    private UserBasicDTO getSampleUser() {
        UserBasicDTO user = new UserBasicDTO();
        user.setUsername("Loti");
        user.setEmail("lotizoyim@gmail.com");
        user.setPassword("pass1234");
        return user;
    }
    
    private User getSampleUserEntity(Integer id, String username, String email, String password) {
        User user = new User();
        user.setId(id);
        user.setUsername(username);
        user.setEmail(email);
        user.setPassword(password);
        return user;
    }

}
