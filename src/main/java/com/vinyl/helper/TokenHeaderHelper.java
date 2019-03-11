package com.vinyl.helper;

import com.vinyl.model.Token;

import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;

@Component
public class TokenHeaderHelper {

    public HttpHeaders setupToken(String token) {

        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + token);

        return headers;
    }
}
