package com.vinyl.service.validation;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.vinyl.model.User;
import com.vinyl.repository.UserRepository;
import com.vinyl.service.exception.BadRequestException;

@Component
public class UserEmailValidator implements Validator<User> {

	@Autowired
	private UserRepository userRepository;

	private static final String VALID_EMAIL_REGEX = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
			+ "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
	private static final Pattern EMAIL_PATTERN = Pattern.compile(VALID_EMAIL_REGEX);

	@Override
	public void validate(User user) {

		Matcher matcher = EMAIL_PATTERN.matcher(user.getEmail());

		if (!matcher.find()) {
			throw new BadRequestException("The email address is invalid!");
		}

		if (userRepository.findByEmail(user.getEmail()) != null) {
			throw new BadRequestException("Email address already exists!");
		}
	}
}