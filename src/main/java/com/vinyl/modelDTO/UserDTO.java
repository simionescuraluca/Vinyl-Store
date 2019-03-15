package com.vinyl.modelDTO;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(description = "Details about user")
public class UserDTO {

	@ApiModelProperty(notes = "User first name")
	private String firstName;

	@ApiModelProperty(notes = "User second name")
	private String secondName;

	@ApiModelProperty(notes = "User email")
	private String email;

	@ApiModelProperty(notes = "User Password")
	private String pass;

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getSecondName() {
		return secondName;
	}

	public void setSecondName(String secondName) {
		this.secondName = secondName;
	}

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
