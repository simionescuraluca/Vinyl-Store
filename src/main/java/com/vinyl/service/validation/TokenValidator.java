package com.vinyl.service.validation;

import com.vinyl.model.Token;
import com.vinyl.repository.TokenRepository;
import com.vinyl.service.exception.BadRequestException;
import com.vinyl.service.exception.UnauthorizedException;

import java.time.LocalDate;

public class TokenValidator implements Validator<String> {

    private final TokenRepository tokenRepository;

    public TokenValidator(TokenRepository tokenRepository) {

        this.tokenRepository = tokenRepository;
    }

    @Override
    public void validate(String auth) {
        if (auth == null) {
            throw new UnauthorizedException("Missing token!");
        }

        Token token = tokenRepository.findByHash(auth);
        if (token == null) {
            throw new BadRequestException("Invalid token!");
        }

        if (LocalDate.now().compareTo(token.getValidUntil()) > 0) {
            throw new UnauthorizedException("Token is expired!");
        }
    }
}
