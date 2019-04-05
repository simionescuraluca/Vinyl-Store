package com.vinyl.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class PasswordEncoder {

    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    public PasswordEncoder(BCryptPasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    public String encode(String toEncode) {
        return passwordEncoder.encode(toEncode);
    }

    public void setPasswordEncoder(BCryptPasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    public boolean matches(String rawPass, String encodedPass) {
        return passwordEncoder.matches(rawPass, encodedPass);
    }
}