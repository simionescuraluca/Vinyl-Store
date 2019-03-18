package com.vinyl.modelDTO;

import java.time.LocalDate;

public class TokenDTO {

    private String hash;
    private LocalDate validUntil;

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    public LocalDate getValidUntil() {
        return validUntil;
    }

    public void setValidUntil(LocalDate validUntil) {
        this.validUntil = validUntil;
    }


}
