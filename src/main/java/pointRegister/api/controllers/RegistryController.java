package pointRegister.api.controllers;

import java.util.Collection;
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
import pointRegister.api.dtos.RegistryDto;
import pointRegister.api.entities.Registry;
import pointRegister.api.services.RegistryService;


@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api/registros")
public class RegistryController {
	final RegistryService registryService;
	
	public RegistryController(RegistryService registryService){	
	
	this.registryService = registryService;	
	}
	

	@PreAuthorize("hasRole('ADMIN')")	
	@GetMapping("/")
	public ResponseEntity <List<Registry>> findAll(){
		return ResponseEntity.status(HttpStatus.OK).body(registryService.findAll());
	}
	

	@PreAuthorize("hasRole('USER')")
	@GetMapping(value = "/{id}")
	public ResponseEntity <Object> findRegistryById(@PathVariable(value = "id") Long id){
		Optional<Registry> registryOptional = registryService.findRegistryById(id);
		if (!registryOptional.isPresent()) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Registry not found!"); 
		}
		return ResponseEntity.status(HttpStatus.OK).body(registryOptional.get());
	}
	
	
	@PreAuthorize("hasRole('ADMIN')")
	@DeleteMapping(value = "/{id}")
	public ResponseEntity <Object> deleteRegistry(@PathVariable(value = "id") Long id){
		Optional<Registry> registryOptional = registryService.findRegistryById(id);
		if (!registryOptional.isPresent()) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Registry not found!"); 
		}
		registryService.deleteRegistry(registryOptional.get());
		return ResponseEntity.status(HttpStatus.OK).body("Registry deleted!");
	}
	

	@PreAuthorize("hasRole('USER')")
	@PutMapping(value = "/{id}") //edit
	public ResponseEntity <Object> updateRegistry(@PathVariable(value = "id") Long id,
												@RequestBody @Valid RegistryDto registryDto){		
		return registryService.updateRegistry(id, registryDto);
	}	
	
	
	@PreAuthorize("hasRole('USER')")
	@PostMapping(value = "/incluir") //Add registry
	public ResponseEntity <Object> saveRegistry(@RequestBody @Valid RegistryDto registryDto){		
		var registry = new Registry();
		BeanUtils.copyProperties(registryDto, registry); 
		return ResponseEntity.status(HttpStatus.CREATED).body(registryService.saveRegistry(registry));		
	}
	
	
	@PreAuthorize("hasRole('USER')")
	@GetMapping(value = "/usuario/{user_id}") 
	public List<Registry> findRegistryByUser_Id (@PathVariable(value = "user_id")Long user_id){
		return registryService.findByUser_Id(user_id);
	}
	
	@PreAuthorize("hasRole('USER')")
	@GetMapping(value = "/mensagem/{user_id}")
	public ResponseEntity <String> registriesMessage(@PathVariable(value = "user_id")Long user_id) {
		return registryService.registriesMessage(user_id);
	}	

	@PreAuthorize("hasRole('USER')")
	@GetMapping(value = "/dia/{user_id}")//Registries by day
	public ResponseEntity<Collection<Registry>> findRegistriesByDay(@PathVariable(value = "user_id")Long user_id) {
		Collection <Registry> registriesByDay = registryService.findByUserIdByDay(user_id);
		return ResponseEntity.ok().body(registriesByDay);		
	}		
	
	
	@PreAuthorize("hasRole('USER')")
	@GetMapping(value = "/mes/{user_id}") //Registries by month
	public ResponseEntity<Collection<Registry>> findByUserIdByMonth(@PathVariable(value = "user_id")Long user_id) {
		Collection <Registry> registriesByMonth = registryService.findByUserIdByMonth(user_id);
		return ResponseEntity.ok().body(registriesByMonth);
	}	
	
	
	@PreAuthorize("hasRole('USER')")
	@GetMapping(value = "/horas/{user_id}") //Worked hours - HH:mm
	public ResponseEntity<String> showHours(@PathVariable(value = "user_id")Long user_id) {
		return registryService.showHours(user_id);
	} 
	
	
	@PreAuthorize("hasRole('ADMIN')")
	@GetMapping(value = "/minutos/{user_id}")//Minutes by day - Long
	public ResponseEntity<Long> showMinutesWorked(@PathVariable(value = "user_id")Long user_id) {
		return registryService.showMinutesWorked(user_id);	 		
	}
	
}