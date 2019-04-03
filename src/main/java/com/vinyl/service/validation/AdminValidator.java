package com.vinyl.service.validation;

import com.vinyl.repository.TokenRepository;
import com.vinyl.service.exception.UnauthorizedException;

public class AdminValidator extends TokenValidator {

    public AdminValidator(TokenRepository tokenRepository) {
        super(tokenRepository);
    }

    @Override
    public void validate(String token) {
        super.validate(token);
        if (!tokenRepository.findByHash(token).getUser().getRole().getRoleName().equals("ADMIN")) {
            throw new UnauthorizedException("Only an admin can do this!");
        }
    }
}