package com.vinyl.service;

import org.springframework.stereotype.Component;

@Component
public class ValidatorFactory {

	public Validator getValidator(String validatorType) {
		if (validatorType == null) {
			return null;
		}
		if (validatorType.equalsIgnoreCase("NAME_VALIDATOR")) {
			return new NameValidator();

		} else if (validatorType.equalsIgnoreCase("EMAIL_VALIDATOR")) {
			return new EmailValidator();
		}
		return null;
	}
}