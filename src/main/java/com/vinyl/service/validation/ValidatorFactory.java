package com.vinyl.service.validation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.vinyl.model.User;

@Component
public class ValidatorFactory {

	@Autowired
	private UserEmailValidator emailValidator;
	
	public Validator<User> getUserNameValidator() {
		return new UserNameValidator();
	}

	public Validator<User> getUserEmailValidator() {
		return emailValidator;
	}

	public Validator<User> getUserPasswordValidator() {
		return new UserPasswordValidator();
	}

}
