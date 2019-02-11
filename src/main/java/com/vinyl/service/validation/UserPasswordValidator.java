package com.vinyl.service.validation;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.vinyl.model.User;
import com.vinyl.service.exception.BadRequestException;

public class UserPasswordValidator implements Validator<User> {

	private static final String VALID_PASS_REGEX = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{6,}$";
	private static final Pattern PASS_PATTERN = Pattern.compile(VALID_PASS_REGEX);

	@Override
	public void validate(User user) {

		Matcher matcher = PASS_PATTERN.matcher(user.getPass());

		if (!matcher.find()) {
			throw new BadRequestException(
					"The password must contain at least 6 no-whitespace characters, one digit and a special character");
		}
	}
}
