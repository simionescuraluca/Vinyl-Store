package com.vinyl.service.validation;

import com.vinyl.model.Token;
import com.vinyl.repository.TokenRepository;
import com.vinyl.service.exception.BadRequestException;
import com.vinyl.service.exception.UnauthorizedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;

public class TokenValidator implements Validator<String> {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserNameValidator.class);

    private final TokenRepository tokenRepository;

    public TokenValidator(TokenRepository tokenRepository) {

        this.tokenRepository = tokenRepository;
    }

    @Override
    public void validate(String auth) {
        if (auth == null) {
            LOGGER.error("Missing Token Error!");
            throw new UnauthorizedException("Missing token!");
        }

        Token token = tokenRepository.findByHash(auth);
        if (token == null) {
            LOGGER.error("Invalid Token Error!");
            throw new BadRequestException("Invalid token!");
        }

        if (LocalDate.now().compareTo(token.getValidUntil()) > 0) {
            LOGGER.error("Expired Token Error!");
            throw new UnauthorizedException("Token is expired!");
        }
    }
}