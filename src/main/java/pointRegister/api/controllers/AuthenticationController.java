package pointRegister.api.controllers;

import java.security.NoSuchAlgorithmException;
import java.security.Principal;
import java.security.spec.InvalidKeySpecException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import pointRegister.api.Security.AuthenticationRequest;
import pointRegister.api.Security.JWTTokenHelper;
import pointRegister.api.Security.LoginResponse;
import pointRegister.api.dtos.UserDto;
import pointRegister.api.entities.User;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("") 
public class AuthenticationController {

	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	JWTTokenHelper jWTTokenHelper;
	
	@Autowired
	private UserDetailsService userDetailsService;

	@PostMapping("/login")
	public ResponseEntity<?> login(@RequestBody AuthenticationRequest authenticationRequest) throws InvalidKeySpecException, NoSuchAlgorithmException {

		final Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
				authenticationRequest.getUserName(), authenticationRequest.getPassword()));
		
		SecurityContextHolder.getContext().setAuthentication(authentication);
		
		User user=(User)authentication.getPrincipal();
		String jwtToken=jWTTokenHelper.generateToken(user.getUsername());
		
		LoginResponse response=new LoginResponse();
		response.setToken(jwtToken);		

		return ResponseEntity.ok(response);
	}
	
	@GetMapping("/userinfo")
	public ResponseEntity<?> getUserDto(Principal user) {
		User userObj=(User) userDetailsService.loadUserByUsername(user.getName());
		
		UserDto userDto=new UserDto();
		userDto.setName(userObj.getName());
		userDto.setUserName(userObj.getUsername());
		userDto.setEmail(userObj.getEmail());
		userDto.setPassword(userObj.getPassword());
		userDto.setRoles(userObj.getAuthorities().toArray());		
		
		return ResponseEntity.ok(userDto);
	} 
}
