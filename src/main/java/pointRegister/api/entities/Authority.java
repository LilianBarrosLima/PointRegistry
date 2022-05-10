package pointRegister.api.entities;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import org.springframework.security.core.GrantedAuthority;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@Table(name = "AUTHORITY")
@Entity
//@NoArgsConstructor
public class Authority implements GrantedAuthority {
	
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(name = "role_code")
	private String roleCode; 	
	
	@JsonIgnore
	@ManyToMany(mappedBy = "authorities")
	private List <User> user;	
		
	@Override
	public String getAuthority() {
		return roleCode;
	}
	
}
