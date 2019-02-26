package com.vinyl.service.validation;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.vinyl.model.User;
import com.vinyl.repository.UserRepository;
import com.vinyl.service.exception.BadRequestException;

public class UserEmailValidator implements Validator<User> {

	private final UserRepository userRepository;

	private static final String VALID_EMAIL_REGEX = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
			+ "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
	private static final Pattern EMAIL_PATTERN = Pattern.compile(VALID_EMAIL_REGEX);

	
	public UserEmailValidator(UserRepository userRepository) {
		this.userRepository = userRepository;
	}

	@Override
	public void validate(User user) {

		Matcher matcher = EMAIL_PATTERN.matcher(user.getEmail());

		if (!matcher.find()) {
			throw new BadRequestException("The email address is invalid!");
		}
		userRepository.findByEmail(user.getEmail()).ifPresent((e) -> {throw new BadRequestException("Email address already exists!");});
	}
}