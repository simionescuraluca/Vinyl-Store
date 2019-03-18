package com.vinyl.service.validation;

import com.vinyl.model.User;
import com.vinyl.modelDTO.EmailPassDTO;
import com.vinyl.repository.TokenRepository;
import com.vinyl.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class ValidatorFactory {

    private final UserRepository userRepository;
    private final TokenRepository tokenRepository;
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    public ValidatorFactory(UserRepository userRepository, BCryptPasswordEncoder passwordEncoder, TokenRepository tokenRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.tokenRepository = tokenRepository;
    }

    public Validator<User> getUserNameValidator() {
        return new UserNameValidator();
    }

    public Validator<User> getUserEmailValidator() {
        return new UserEmailValidator(userRepository);
    }

    public Validator<User> getUserPasswordValidator() {
        return new UserPasswordValidator();
    }

    public Validator<EmailPassDTO> getEmailAndPasswordValidator() {
        return new EmailAndPasswordValidator(userRepository, passwordEncoder);
    }

    public void setPasswordEncoder(BCryptPasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    public Validator<String> getTokenValidator() {
        return new TokenValidator(tokenRepository);
    }
}
