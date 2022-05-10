package pointRegister.api.dtos;

import java.util.Date;

import javax.validation.constraints.NotNull;

import lombok.Getter;
import lombok.Setter;
import pointRegister.api.entities.User;

@Getter
@Setter
public class RegistryDto {
	
	private Date pointRegistry; 
	
	private String justification;	
	
	@NotNull 
	private User user;
}