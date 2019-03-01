package com.vinyl.vinylstore;

import java.time.LocalDate;

import com.vinyl.repository.TokenRepository;
import com.vinyl.service.validation.ValidatorFactory;
import org.assertj.core.api.Assertions;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.vinyl.model.Token;
import com.vinyl.model.User;
import com.vinyl.modelDTO.EmailPassDTO;
import com.vinyl.modelDTO.TokenDTO;

import static org.mockito.ArgumentMatchers.anyString;

public class LoginTest extends com.vinyl.vinylstore.Test {

	@Autowired
	private TokenRepository tokenRepository;

	@Autowired
	private BCryptPasswordEncoder passwordEncoder;

	@Autowired
	private ValidatorFactory validatorFactory;

	private BCryptPasswordEncoder mockedPasswordEncoder;

	private User user;

	EmailPassDTO request = new EmailPassDTO();

	@Before
	public void setUp() {
		user = createUser();
		mockedPasswordEncoder = Mockito.mock(BCryptPasswordEncoder.class);
		Mockito.when(mockedPasswordEncoder.matches(anyString(), anyString())).thenReturn(true);
		validatorFactory.setPasswordEncoder(mockedPasswordEncoder);
	}

	@After
	public void tearDown() {
		tokenRepository.deleteAll();
		userRepository.deleteAll();
		addressRepository.deleteAll();
		roleRepository.deleteAll();
		validatorFactory.setPasswordEncoder(passwordEncoder);
	}

	@Test
	public void loginWithValidEmailAndPasswordWhenTokenExists() {
		Token token = createToken(user);
		setRequest(request);

		ResponseEntity<TokenDTO> tdo = trt.postForEntity("/users/login", request, TokenDTO.class);

		Assertions.assertThat(tdo.getStatusCode()).isEqualTo(HttpStatus.OK);
		Assertions.assertThat(tdo.getBody().getHash()).isEqualTo(token.getHash());
	}

	@Test
	public void loginWithValidEmailAndPasswordWhenTokenDoesNotExist() {
		setRequest(request);

		ResponseEntity<TokenDTO> tdo = trt.postForEntity("/users/login", request, TokenDTO.class);

		Assertions.assertThat(tdo.getStatusCode()).isEqualTo(HttpStatus.OK);
		Assertions.assertThat(tokenRepository.findByUserId(user.getId()));
	}

	@Test
	public void loginWithInvalidEmail()  {
		EmailPassDTO request = new EmailPassDTO();
		request.setEmail("thisisnottheemail@yahoo.com");
		request.setPass(user.getPass());

		ResponseEntity<TokenDTO> tdo = trt.postForEntity("/users/login", request, TokenDTO.class);
		Assertions.assertThat(tdo.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
	}

	@Test
	public void loginWithInvalidPass() {
		validatorFactory.setPasswordEncoder(passwordEncoder);
		EmailPassDTO request = new EmailPassDTO();
		request.setEmail(user.getEmail());
		request.setPass("thisisnotthepassword");

		ResponseEntity<TokenDTO> tdo = trt.postForEntity("/users/login", request, TokenDTO.class);
		Assertions.assertThat(tdo.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
	}

	private Token createToken(User user) {
		Token token = new Token();
		token.setHash("5");
		token.setValidUntil(LocalDate.now().plusMonths(1));
		token.setUser(user);
		return tokenRepository.save(token);
	}

	public void setRequest(EmailPassDTO request){
		request.setEmail(user.getEmail());
		request.setPass(user.getPass());
	}
}