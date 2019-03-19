package com.vinyl.service.validation;

import com.vinyl.model.User;
import com.vinyl.service.exception.BadRequestException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UserNameValidator implements Validator<User> {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserNameValidator.class);

    private static final String VALID_NAME_REGEX = "^[A-Z][a-zA-Z]+$";
    private static final Pattern NAME_PATTERN = Pattern.compile(VALID_NAME_REGEX);

    @Override
    public void validate(User user) {

        Matcher matcher1 = NAME_PATTERN.matcher(user.getFirstName());
        Matcher matcher2 = NAME_PATTERN.matcher(user.getSecondName());

        if (!matcher1.find() || !matcher2.find()) {
            LOGGER.error("Invalid Name Error");
            throw new BadRequestException("The names should start with capital letter and have more than 1 character!");
        }
    }

}
