package com.vinyl.modelDTO;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.time.LocalDate;

@ApiModel(description = "Details about the token")
public class TokenDTO {

    @ApiModelProperty(notes = "The hash of the token")
    private String hash;

    @ApiModelProperty(notes = "Expiring date of the token")
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
