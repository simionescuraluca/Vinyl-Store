package com.vinyl.service.validation;

import com.vinyl.modelDTO.EmailPassDTO;
import com.vinyl.modelDTO.ProductManagementDTO;
import com.vinyl.modelDTO.UserDTO;
import com.vinyl.repository.TokenRepository;
import com.vinyl.repository.UserRepository;
import com.vinyl.service.PasswordEncoder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ValidatorFactory {

    private final UserRepository userRepository;
    private final TokenRepository tokenRepository;
    private PasswordEncoder passwordEncoder;

    @Autowired
    public ValidatorFactory(UserRepository userRepository, PasswordEncoder passwordEncoder, TokenRepository tokenRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.tokenRepository = tokenRepository;
    }

    public Validator<UserDTO> getAddUserValidator() {
        return new AddUserValidator(userRepository);
    }

    public Validator<EmailPassDTO> getEmailAndPasswordValidator() {
        return new EmailAndPasswordValidator(userRepository, passwordEncoder);
    }

    public void setPasswordEncoder(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    public Validator<String> getTokenValidator() {
        return new TokenValidator(tokenRepository);
    }

    public Validator<ProductManagementDTO> getProductManagementValidator() {
        return new ProductManagementValidator();
    }

    public Validator<String> getAdminValidator() {
        return new AdminValidator(tokenRepository);
    }
}