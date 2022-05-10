package pointRegister.api.dtos;

import java.util.List;

import lombok.Getter;
import lombok.Setter;
import pointRegister.api.entities.Authority;

@Getter
@Setter
public class UserDto {
	private int id;
		
	private String name;
	
	private String userName;
	
	private String password;
	
	private String email;
	
	private List<Authority> authorities;
	
	private Object roles;
	
}