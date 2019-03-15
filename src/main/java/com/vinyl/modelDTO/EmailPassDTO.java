package com.vinyl.modelDTO;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(description = "User Credentials")
public class EmailPassDTO {

	@ApiModelProperty(notes = "User Email")
	private String email;

	@ApiModelProperty(notes = "Password")
	private String pass;

	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getPass() {
		return pass;
	}
	public void setPass(String pass) {
		this.pass = pass;
	}


}
