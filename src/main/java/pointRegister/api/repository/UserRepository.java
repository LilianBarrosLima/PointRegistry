package pointRegister.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import pointRegister.api.entities.User;

@Repository
public interface UserRepository extends JpaRepository <User, Long> {
	
	boolean existsByUserName (String userName);	
}