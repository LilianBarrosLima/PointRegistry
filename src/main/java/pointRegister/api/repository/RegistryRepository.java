package pointRegister.api.repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import pointRegister.api.entities.Registry;

@Repository
public interface RegistryRepository extends JpaRepository<Registry, Integer> {

	
	@Query(value = "select * from registry where user_id = :user_id and convert(point_registry, date) = convert(sysdate(), date)", nativeQuery = true)
	Collection<Registry> findByUser_IdByDay(Long user_id);			
	
	@Query(value = "select * from registry where user_id = ?1 and year(point_registry) = year(sysdate()) and month(point_registry) = month(sysdate())", nativeQuery = true)
	Collection<Registry> findByUser_IdByMonth(@Param("user_id")Long user_id );
	
	@Query(value = "select COUNT(*) from registry where convert(point_registry, date) = convert(sysdate(), date)", nativeQuery = true)
	long findRegistriesByDay(Long user_id);

	Optional<Registry> findById(Long id);

	List<Registry> findByUser_Id(Long user_id);	
	 
}