package pointRegister.api.services;

import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

import javax.transaction.Transactional;
import javax.validation.Valid;

import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import lombok.var;
import pointRegister.api.dtos.RegistryDto;
import pointRegister.api.entities.Registry;
import pointRegister.api.repository.RegistryRepository;

@Service
public class RegistryService {
	
	final RegistryRepository registryRepository;
		
	
	public RegistryService (RegistryRepository registryRepository) {
		this.registryRepository = registryRepository;
	}
	
	
	@Transactional 
	public Registry saveRegistry (Registry registry) {		
		return registryRepository.save(registry);
	}
	
	
	public List<Registry> findAll() {	
		return registryRepository.findAll();
	}
	

	public Optional<Registry> findRegistryById(Long id) {
		return registryRepository.findById(id);
	}

	
	@Transactional
	public void deleteRegistry(Registry registry) {		
		registryRepository.delete(registry);		
	}	
	
	
	public Collection <Registry> findByUserIdByDay(Long user_id) {
		return registryRepository.findByUser_IdByDay(user_id);
	}
	

	public Collection<Registry> findByUserIdByMonth(Long user_id) {
		return registryRepository.findByUser_IdByMonth(user_id);
	}
	
	
	public long findRegistriesByDay(Long user_id) { 
		return registryRepository.findRegistriesByDay(user_id);
	}
	
	
	public List<Registry> findByUser_Id(Long user_id) {
		return registryRepository.findByUser_Id(user_id);
	}
	
	
	public ResponseEntity <Object> updateRegistry(@PathVariable(value = "id") Long id,
										@RequestBody @Valid RegistryDto registryDto){		
		Optional<Registry> registryOptional = findRegistryById(id);
		if (!registryOptional.isPresent()) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Registry not found!"); 
		}		
		var registry = new Registry(); 	
		BeanUtils.copyProperties(registryDto, registryOptional);
		registry.setId(registryOptional.get().getId());
		return ResponseEntity.status(HttpStatus.OK).body(saveRegistry(registry));
		}
	
	
	public ResponseEntity <String> registriesMessage(@PathVariable(value = "user_id")Long user_id) {
		Collection <Registry> registriesByDay = registryRepository.findByUser_IdByDay(user_id);
		long registryDay = registriesByDay.size();
		if (registriesByDay.isEmpty()) {
			return ResponseEntity.status(HttpStatus.ACCEPTED).body("Empty! There isn't registries today!"); 
		}else if (!(registryDay % 2 == 0)) {
			return ResponseEntity.status(HttpStatus.ACCEPTED).body("You must registry point today yet!"); 
		}else {
			return ResponseEntity.ok("Ok");
		}
	}	
	
	
	public ResponseEntity<String> showHours(@PathVariable(value = "user_id")Long user_id) {
		Collection <Registry> registriesByDay = registryRepository.findByUser_IdByDay(user_id);
		Registry[] registries = registriesByDay.toArray(new Registry[0]);	
		long registryDay = registriesByDay.size();	
		long total_minutes = 0;	
		Date start_point = null, end_point = null;		    
	    for(int i = 0; i < registryDay; i++) {		    	
	    	if (registryDay % 2 == 0) { //case have input and output 
		        if(i % 2 == 0) {//the first, input, the even		        
		        	start_point = registries[i].getPointRegistry();
		        }else{//output
		        	end_point = registries[i].getPointRegistry();
		        	long diff = end_point.getTime() - start_point.getTime();
		            total_minutes += TimeUnit.MILLISECONDS.toMillis(diff);			            	
		        }		      
		    }else{ //Odd - doesn't have output  
		    	if (registryDay > 2) {//3 or + registries
	    			do {	    			
	    				if(i % 2 == 0) {//it means that is the first, input, the even		        
	    		        	start_point = registries[i].getPointRegistry();
	    		        }else{//output
	    		        	end_point = registries[i].getPointRegistry();
	    		        	long diff = end_point.getTime() - start_point.getTime();
	    		            total_minutes += TimeUnit.MILLISECONDS.toMillis(diff);
	    		        } 			    				
	    			}while(registryDay <= (registryDay-1));   	
		    	}else if (registryDay == 0){//if doesn't have anyone registries
		    		total_minutes = 0;
	    		}else {//just 1 registry
	    			start_point = registries[i].getPointRegistry();	 
	    			Calendar cal = Calendar.getInstance();
	    			Date now = cal.getTime();
	    			long diff = now.getTime() - start_point.getTime();
    		        total_minutes += TimeUnit.MILLISECONDS.toMillis(diff); 	    			
	    		}
		    }
		}	    
	    long minutes  = ( total_minutes / 60000 ) % 60;     
        long hours    = total_minutes / 3600000;
	    String showH =  String.format("%02d:%02d", hours, minutes);	    	    
	    if (total_minutes < (480*60000)) {
    		System.out.println("Your working hours are 8 hours by day. You worked just " + showH + ", please justify."); 
       	}
		return ResponseEntity.ok().body(showH);
	} 
	

	@GetMapping(value = "/minutos/{user_id}")
	public ResponseEntity<Long> showMinutesWorked(@PathVariable(value = "user_id")Long user_id) {
		Collection <Registry> registriesByDay = registryRepository.findByUser_IdByDay(user_id);
		long registryDay = registriesByDay.size();	
		Registry[] registries = registriesByDay.toArray(new Registry[0]);		
		long total_minutes = 0;	 
	    Date start_point = null, end_point = null;		    
	    for(int i = 0; i < registryDay; i++) {	
	    	if (registryDay % 2 == 0) {   
		        if(i % 2 == 0) {		        
		        	start_point = registries[i].getPointRegistry();
		        }else{
		        	end_point = registries[i].getPointRegistry();
		        	long diff = end_point.getTime() - start_point.getTime();
		        	total_minutes += TimeUnit.MILLISECONDS.toMinutes(diff);		        	
		        }		      
		    }else{    
	    		if (registryDay > 2) {
	    			do {	    			
	    				if(i % 2 == 0) {		        
	    		        	start_point = registries[i].getPointRegistry();
	    		        }else{
	    		        	end_point = registries[i].getPointRegistry();
	    		        	long diff = end_point.getTime() - start_point.getTime();
	    		            total_minutes += TimeUnit.MILLISECONDS.toMinutes(diff);
	    		        } 			    				
	    			}while(registryDay <= (registryDay-1));	 	    		
	    		}else {
	    			System.out.println("You must justify!");
		    		start_point = registries[i].getPointRegistry();	 
		    		Calendar cal = Calendar.getInstance();
		    		Date now = cal.getTime();
		    		long diff = now.getTime() - start_point.getTime();
	    		    total_minutes += TimeUnit.MILLISECONDS.toMinutes(diff); 
		    	}     			      
		    }		    	
        }    	
		return ResponseEntity.ok().body(total_minutes);	 		
	}
		
}