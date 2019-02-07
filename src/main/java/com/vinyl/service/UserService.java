package com.vinyl.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.vinyl.model.Role;
import com.vinyl.model.User;
import com.vinyl.repository.RoleRepository;
import com.vinyl.repository.UserRepository;

@Service("userService")
public class UserService {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private RoleRepository roleRepository;

	public User addUser(User user) {

		if (userRepository.findByEmail(user.getEmail()) != null) {
			throw new RuntimeException("User already exists!");
		}

		Role r = new Role("BASIC_USER");
		r = roleRepository.save(r);
		user.setAddress("Defaul Address");
		user.setRole(r);

		Validation accountValidation = new Validation();
		accountValidation.validateNames(user);

		return userRepository.save(user);

	}

	public void deleteUser(Integer id) {
		userRepository.deleteById(id);
	}

	public User findByEmail(String email) {
		return userRepository.findByEmail(email);
	}
}
