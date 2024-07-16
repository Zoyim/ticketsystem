package af.cmr.loti.ticketsystem.business.dto;

import jakarta.validation.constraints.NotBlank;

public class UserAuthDTO {
	
	@NotBlank(message = "Username ne peut pas être vide")
	private String username;
	
	@NotBlank(message = "Password ne peut pas être vide")
	private String password;
	
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}

}
