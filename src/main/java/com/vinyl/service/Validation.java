package com.vinyl.service;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.vinyl.model.User;
import com.vinyl.service.exception.BadRequestException;

public class Validation {

	public static final String nameRegex = "^[A-Z][a-zA-Z]+$";
	public static final Pattern VALID_NAME_REGEX = Pattern.compile(nameRegex);

	public void validateNames(User user) {

		Matcher matcher1 = VALID_NAME_REGEX.matcher(user.getFirstName());
		Matcher matcher2 = VALID_NAME_REGEX.matcher(user.getSecondName());

		if (!matcher1.find() || !matcher2.find()) {
			throw new BadRequestException("The names should start with capital letter and have more than 1 character!");
		}
	}
}
