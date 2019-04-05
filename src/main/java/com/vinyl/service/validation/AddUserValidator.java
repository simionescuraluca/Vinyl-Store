package com.vinyl.service.validation;

import com.vinyl.modelDTO.UserDTO;
import com.vinyl.repository.UserRepository;
import com.vinyl.service.exception.BadRequestException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AddUserValidator implements Validator<UserDTO> {

    private static final Logger LOGGER = LoggerFactory.getLogger(AddUserValidator.class);

    private static final String VALID_NAME_REGEX = "^[A-Z][a-zA-Z]+$";
    private static final Pattern NAME_PATTERN = Pattern.compile(VALID_NAME_REGEX);

    private static final String VALID_EMAIL_REGEX = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
            + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

    private static final Pattern EMAIL_PATTERN = Pattern.compile(VALID_EMAIL_REGEX);

    private static final String VALID_PASS_REGEX = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{6,}$";
    private static final Pattern PASS_PATTERN = Pattern.compile(VALID_PASS_REGEX);

    private final UserRepository userRepository;

    AddUserValidator(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public void validate(UserDTO userDto) {
        validateName(userDto);
        validatePassword(userDto);
        validateEmail(userDto);
    }

    public void validateName(UserDTO userDto) {
        Matcher firstNameMatcher = NAME_PATTERN.matcher(userDto.getFirstName());
        Matcher secondNameMatcher = NAME_PATTERN.matcher(userDto.getSecondName());
        if (!firstNameMatcher.find() || !secondNameMatcher.find()) {
            LOGGER.error("Invalid Name Error");
            throw new BadRequestException("The names should start with capital letter and have more than 1 character!");
        }
    }

    public void validateEmail(UserDTO userDto) {
        Matcher emailMatcher = EMAIL_PATTERN.matcher(userDto.getEmail());
        if (!emailMatcher.find()) {
            LOGGER.error("Invalid Email Error");
            throw new BadRequestException("The email address is invalid!");
        }
        userRepository.findByEmail(userDto.getEmail()).ifPresent((e) -> {
            throw new BadRequestException("Email address already exists!");
        });
    }

    public void validatePassword(UserDTO userDto) {
        Matcher passwordMatcher = PASS_PATTERN.matcher(userDto.getPass());
        if (!passwordMatcher.find()) {
            LOGGER.error("Invalid Password Error");
            throw new BadRequestException(
                    "The password must contain at least 6 no-whitespace characters, one digit and a special character");
        }
    }
}
