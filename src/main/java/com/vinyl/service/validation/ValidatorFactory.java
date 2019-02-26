package com.vinyl.service.validation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import com.vinyl.model.User;
import com.vinyl.modelDTO.EmailPassDTO;
import com.vinyl.repository.UserRepository;

@Component
public class ValidatorFactory {

	private final UserRepository userRepository;
	private BCryptPasswordEncoder passwordEncoder;
	
	@Autowired
	public ValidatorFactory(UserRepository userRepository, BCryptPasswordEncoder passwordEncoder) {
		this.userRepository = userRepository;
		this.passwordEncoder = passwordEncoder;
	}

	public Validator<User> getUserNameValidator() {
		return new UserNameValidator();
	}

	public Validator<User> getUserEmailValidator() {
		return new UserEmailValidator(userRepository);
	}

	public Validator<User> getUserPasswordValidator() {
		return new UserPasswordValidator();
	}
	
	public Validator<EmailPassDTO> getEmailAndPasswordValidator() {
		return new EmailAndPasswordValidator(userRepository, passwordEncoder);
	}

	public void setPasswordEncoder(BCryptPasswordEncoder passwordEncoder) {
		this.passwordEncoder = passwordEncoder;
	}
}
