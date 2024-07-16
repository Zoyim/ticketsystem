package af.cmr.loti.ticketsystem.business.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * This class represents a basic Data Transfer Object (DTO) for a
 * {@link User} entity. 
*/

public class UserBasicDTO implements IDTO {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5892723298880586490L;
	
	private Integer id;
	
	@NotBlank(message = "Username ne peut pas être vide")
	@Size(max = 20, message = "Username ne peut pas dépasser {max} caractères")
	private String username;
	
	@NotBlank(message = "Email ne peut pas être vide")
	private String email;
	
	@NotBlank(message = "Password ne peut pas être vide")
	private String password;

	public UserBasicDTO() {}

	@Override
	public Integer getId() {
		return this.id;
	}

	@Override
	public void setId(Integer id) {
		this.id =  id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	
}
