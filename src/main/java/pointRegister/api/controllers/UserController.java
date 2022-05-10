package pointRegister.api.controllers;


import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.var;
import pointRegister.api.dtos.UserDto;
import pointRegister.api.entities.User;
import pointRegister.api.services.UserService;


@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api")
public class UserController{
	final UserService userService;
	
	public UserController(UserService userService){	
	
	this.userService = userService;
}	

	
	@PreAuthorize("hasRole('USER')")
	@PutMapping("/user/{id}") //Profile Screen
	public ResponseEntity <Object> updateUser(@PathVariable(value = "id") Long id,
												@RequestBody @Valid UserDto userDto){
		Optional<User> userOptional = userService.findById(id);
		if (!userOptional.isPresent()) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found!"); 
		}		
		var user = userOptional.get();
		user.setName(userDto.getName());
		user.setUserName(userDto.getUserName());
		user.setPassword(userDto.getPassword());	
		user.setEmail(userDto.getEmail());
		return ResponseEntity.status(HttpStatus.OK).body(userService.saveUser(user));
	}			

	
	@PreAuthorize("hasRole('USER')")
	@GetMapping("/")
	public ResponseEntity <List<User>> findAll(){
		return ResponseEntity.status(HttpStatus.OK).body(userService.findAll());
	}

	
	@PreAuthorize("hasRole('ADMIN')")
	@GetMapping("/{id}")
	public ResponseEntity <Object> findById(@PathVariable(value = "id") Long id){
		Optional<User> userOptional = userService.findById(id);
		if (!userOptional.isPresent()) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found"); 
		}
		return ResponseEntity.status(HttpStatus.OK).body(userOptional.get());
	}	

	
	@DeleteMapping("/{id}")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity <Object> deleteUser(@PathVariable(value = "id") Long id){
		Optional<User> userOptional = userService.findById(id);
		if (!userOptional.isPresent()) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found"); 
		}
		userService.deleteUser(userOptional.get());
		return ResponseEntity.status(HttpStatus.OK).body("User deleted!");
	}	
	
	
 	@PostMapping("/cadastro")	
  	public ResponseEntity <Object> saveUser(@RequestBody @Valid UserDto userDto){
 		
  		if(userService.existsByUserName (userDto.getUserName())) { 
  			return ResponseEntity.status(HttpStatus.CONFLICT).body("Username already exists!");			
  		}  	
  		var user = new User();
  		BeanUtils.copyProperties(userDto, user); 
  		return ResponseEntity.status(HttpStatus.CREATED).body(userService.saveUser(user));		
  	}	
 	
  		
}