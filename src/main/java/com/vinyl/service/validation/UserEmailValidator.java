package com.vinyl.service.validation;

import com.vinyl.model.User;
import com.vinyl.repository.UserRepository;
import com.vinyl.service.exception.BadRequestException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UserEmailValidator implements Validator<User> {

	private static final Logger LOGGER = LoggerFactory.getLogger(UserEmailValidator.class);

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
			LOGGER.error("Invalid Email Error");
			throw new BadRequestException("The email address is invalid!");
		}
		userRepository.findByEmail(user.getEmail()).ifPresent((e) -> {throw new BadRequestException("Email address already exists!");});
	}
}