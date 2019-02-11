package com.vinyl.service.validation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.vinyl.model.User;
import com.vinyl.repository.UserRepository;

@Component
public class ValidatorFactory {

	private final UserRepository userRepository;
	
	@Autowired
	public ValidatorFactory(UserRepository userRepository) {
		this.userRepository = userRepository;
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

}
