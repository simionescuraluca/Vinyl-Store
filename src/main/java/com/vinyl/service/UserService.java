package com.vinyl.service;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

//import javax.ws.rs.BadRequestException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.vinyl.service.exception.BadRequestException;
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

		Pattern p = Pattern.compile("^[A-Z][a-zA-Z]+$");
		Matcher matcher1 = p.matcher(user.getFirstName());
		Matcher matcher2 = p.matcher(user.getSecondName());

		if (!matcher1.find() || !matcher2.find()) {
			throw new BadRequestException("The names should start with capital letter and have more than 1 character!");
		}

		return userRepository.save(user);

	}

	public void deleteUser(Integer id) {
		userRepository.deleteById(id);
	}

	public User findByEmail(String email) {
		return userRepository.findByEmail(email);
	}

}
