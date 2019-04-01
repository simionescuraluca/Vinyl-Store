package com.vinyl.service.validation;

import com.vinyl.model.Token;
import com.vinyl.repository.TokenRepository;
import com.vinyl.service.exception.UnauthorizedException;
import org.springframework.beans.factory.annotation.Autowired;

public class AdminValidator implements Validator<Token> {

    @Autowired
    private TokenRepository tokenRepository;

    public AdminValidator(TokenRepository tokenRepository) {
        this.tokenRepository = tokenRepository;
    }

    @Override
    public void validate(Token token) {
        if (!token.getUser().getRole().getRoleName().equals("ADMIN")) {
            throw new UnauthorizedException("Only an admin can do this!");
        }
    }
}