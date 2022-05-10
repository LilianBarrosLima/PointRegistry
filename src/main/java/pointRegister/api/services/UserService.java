package pointRegister.api.services;

import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import pointRegister.api.entities.User;
import pointRegister.api.repository.UserRepository;

@Service
public class UserService {
	
	@Autowired
    private PasswordEncoder encoder;
		
	final UserRepository userRepository;
	
	public UserService (UserRepository userRepository) {
		this.userRepository = userRepository;
	}
	
	@Transactional 
	public User saveUser (User user) {	
		String pass = user.getPassword();
        user.setPassword(encoder.encode(pass)); 
		return userRepository.save(user);
	}

	public List<User> findAll() {	
		return userRepository.findAll();
	}

	public Optional<User> findById(Long id) {
		return userRepository.findById(id);
	}
	
	@Transactional
	public void deleteUser(User user) {
		userRepository.delete(user);		
	}
	
	public User updateUser(User user) {
		return userRepository.save(user);
	}

	public boolean existsByUserName(String userName) {
		return userRepository.existsByUserName(userName);
		
	}
	
}
