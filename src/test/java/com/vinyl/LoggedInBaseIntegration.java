package com.vinyl;

import com.vinyl.helper.TokenHeaderHelper;
import com.vinyl.repository.TokenRepository;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;

import java.time.LocalDate;

public abstract class LoggedInBaseIntegration extends BaseIntegration {

    @Autowired
    protected TokenHeaderHelper tokenHeaderHelper;

    @Autowired
    protected TokenRepository tokenRepository;

    @Test
    public void testWhenTokenIsMissing() {
        ResponseEntity<?> response = trt.exchange(getUrl(), getMethod(), HttpEntity.EMPTY, Void.class);
        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
    }

    @Test
    public void testWhenTokenIsInvalid() {
        HttpHeaders headers = tokenHeaderHelper.setupToken("INVALID_TOKEN");
        ResponseEntity<?> response = trt.exchange(getUrl(), getMethod(), new HttpEntity<>(headers), Void.class);
        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    public void testWhenTokenIsExpired() {
        token.setValidUntil(LocalDate.now().minusMonths(3));
        tokenRepository.save(token);

        HttpHeaders headers = tokenHeaderHelper.setupToken(token.getHash());
        ResponseEntity<?> response = trt.exchange(getUrl(), getMethod(), new HttpEntity<>(headers), Void.class);

        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
    }

    protected abstract String getUrl();
    protected abstract HttpMethod getMethod();
}
