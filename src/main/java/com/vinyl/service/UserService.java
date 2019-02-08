package com.vinyl.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.vinyl.model.Role;
import com.vinyl.model.User;
import com.vinyl.repository.RoleRepository;
import com.vinyl.repository.UserRepository;
import com.vinyl.service.validation.ValidatorFactory;

@Service("userService")
public class UserService {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private RoleRepository roleRepository;

	@Autowired
	private BCryptPasswordEncoder passwordEncoder;

	@Autowired
	private ValidatorFactory validatorFactory;

	public User addUser(User user) {

		if (userRepository.findByEmail(user.getEmail()) != null) {
			throw new RuntimeException("User already exists!");
		}

		Role r = new Role("BASIC_USER");
		r = roleRepository.save(r);
		user.setAddress("Defaul Address");
		user.setRole(r);

		validatorFactory.getUserNameValidator().validate(user);
		validatorFactory.getUserEmailValidator().validate(user);
		validatorFactory.getUserPasswordValidator().validate(user);

		user.setPass(passwordEncoder.encode(user.getPass()));
		return userRepository.save(user);

	}

	public void deleteUser(Integer id) {
		userRepository.deleteById(id);
	}

	public User findByEmail(String email) {
		return userRepository.findByEmail(email);
	}
}
