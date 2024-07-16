package af.cmr.loti.ticketsystem.business.entity;

import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "GEST_USER")
public class User implements IEntity {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 5767432454027691732L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ID")
	private Integer id;
	
	@Column(name = "USERNAME")
	private String username;
	
	@Column(name = "EMAIL")
    private String email;
	
	@Column(name = "PASSWORD")
	private String password;
	
	@OneToMany(mappedBy = "user", fetch = FetchType.EAGER)
    private List<Ticket> tickets;

	public User() {
		super();
	}

	@Override
	public Integer getId() {
		return this.id;
	}

	@Override
	public void setId(Integer id) {
		this.id = id;
	}

	public String getUsername() {
		return this.username;
	}
 
	public void setUsername(String username) {
		this.username = username;
	}

	public String getEmail() {
		return this.email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
 
	public List<Ticket> getTickets() {
		return this.tickets;
	}

	public void setTickets(List<Ticket> tickets) {
		this.tickets = tickets;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
}
