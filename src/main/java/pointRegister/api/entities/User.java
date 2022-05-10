package pointRegister.api.entities;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.NoArgsConstructor;


@NoArgsConstructor
@Table(name = "USER")
@Entity

public class User implements UserDetails {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;

	@Column(name = "user_name" )
	private String userName;

	@JsonIgnore
	@Column(name = "password") 
	private String password;

	@Column(name = "name")
	private String name;

	@Column(name = "email")
	private String email;

	@Column(name = "enabled")
	private boolean enabled=true;
	
	@OneToMany(mappedBy = "user")
	private List<Registry> registry;
	
	@ManyToMany(cascade = CascadeType.ALL,fetch = FetchType.EAGER)
	@JoinTable(name = "USER_AUTHORITY", joinColumns = @JoinColumn(referencedColumnName = "id"),
				inverseJoinColumns = @JoinColumn(referencedColumnName ="id")) //id
	private List<Authority> authorities = new ArrayList<>();
	
		
	public User(String userName, String password, String name, String email, boolean enabled,
			List<Authority> authorities) {
		this.userName = userName;
		this.password = password;
		this.name = name;
		this.email = email;
		this.enabled = enabled;
		this.authorities = authorities;
	}
		

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}
	
	@Override
	public String getUsername() {
		return this.userName;
	}	
	
	public void setUserName(String userName) {
		this.userName = userName;
	}
	
	@Override
	public String getPassword() {
		return this.password;
	}
	
	public void setPassword(String password) {
		this.password = password;
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}	
	
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		if (authorities==null) {
			List<GrantedAuthority> authorityListUser = AuthorityUtils.createAuthorityList("ROLE_USER");
			return authorityListUser;
		}else {
			return authorities;
		}		
	}			

	public void setAuthorities(List<Authority> authorities) {	
		this.authorities = authorities;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}
	
	@Override
	public boolean isAccountNonExpired() {
		return this.enabled;
	}

	@Override
	public boolean isAccountNonLocked() {
		return this.enabled;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return this.enabled;
	}

	@Override
	public boolean isEnabled() {
		return this.enabled;
	}

	@Override
	public int hashCode() {
		return Objects.hash(id);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		User other = (User) obj;
		return id == other.id;
	}

}
