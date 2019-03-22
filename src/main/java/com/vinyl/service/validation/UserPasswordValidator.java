package com.vinyl.service.validation;

import com.vinyl.model.User;
import com.vinyl.service.exception.BadRequestException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UserPasswordValidator implements Validator<User> {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserNameValidator.class);

    private static final String VALID_PASS_REGEX = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{6,}$";
    private static final Pattern PASS_PATTERN = Pattern.compile(VALID_PASS_REGEX);

    @Override
    public void validate(User user) {

        Matcher matcher = PASS_PATTERN.matcher(user.getPass());

        if (!matcher.find()) {
            LOGGER.error("Invalid Password Error");
            throw new BadRequestException(
                    "The password must contain at least 6 no-whitespace characters, one digit and a special character");
        }
    }
}
