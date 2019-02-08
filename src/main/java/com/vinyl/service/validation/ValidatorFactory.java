package com.vinyl.service.validation;

import org.springframework.stereotype.Component;

import com.vinyl.model.User;

@Component
public class ValidatorFactory {

	public Validator<User> getUserNameValidator() {
		return new UserNameValidator();
	}

	public Validator<User> getUserEmailValidator() {
		return new UserEmailValidator();
	}

	public Validator<User> getUserPasswordValidator() {
		return new UserPasswordValidator();
	}
}
