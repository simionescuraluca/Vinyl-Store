package com.vinyl.service;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.vinyl.model.User;
import com.vinyl.service.exception.BadRequestException;

public class Validation {

	public static final String nameRegex = "^[A-Z][a-zA-Z]+$";
	public static final Pattern VALID_NAME_REGEX = Pattern.compile(nameRegex);

	public static final String emailRegex = "^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\\\.[A-Z]{2,6}$";
	public static final Pattern VALID_EMAIL_REGEX = Pattern.compile(emailRegex);

	public void validateNames(User user) {

		Matcher matcher1 = VALID_NAME_REGEX.matcher(user.getFirstName());
		Matcher matcher2 = VALID_NAME_REGEX.matcher(user.getSecondName());

		if (!matcher1.find() || !matcher2.find()) {
			throw new BadRequestException("The names should start with capital letter and have more than 1 character!");
		}
	}

	public void validateEmail(User user) {

		Matcher matcher = VALID_EMAIL_REGEX.matcher(user.getEmail());

		if (!matcher.find()) {
			throw new BadRequestException("The email address is invalid!");
		}
	}
}
