package com.vinyl.vinylstore;

import java.time.LocalDate;

import com.vinyl.service.validation.ValidatorFactory;
import org.assertj.core.api.Assertions;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import com.vinyl.model.Address;
import com.vinyl.model.Role;
import com.vinyl.model.Token;
import com.vinyl.model.User;
import com.vinyl.modelDTO.EmailPassDTO;
import com.vinyl.modelDTO.TokenDTO;
import com.vinyl.repository.AddressRepository;
import com.vinyl.repository.RoleRepository;
import com.vinyl.repository.TokenRepository;
import com.vinyl.repository.UserRepository;

import static org.mockito.ArgumentMatchers.anyString;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureTestDatabase
@RunWith(SpringRunner.class)
@ActiveProfiles("test")
public class VinylstoreApplicationTests {

	@Autowired
	private TestRestTemplate trt;

	@Autowired
	private TokenRepository tokenRepository;

	@Autowired
	private RoleRepository roleRepository;

	@Autowired
	private AddressRepository addressRepository;
	@Autowired
	private UserRepository userRepository;

	@Autowired
	private BCryptPasswordEncoder passwordEncoder;

	@Autowired
	private ValidatorFactory validatorFactory;

	private BCryptPasswordEncoder mockedEncoder;

	private User user;


	@Before
	public void setUp() {
		user = createUser();
		mockedEncoder = Mockito.mock(BCryptPasswordEncoder.class);
		Mockito.when(mockedEncoder.matches(anyString(), anyString())).thenReturn(true);
		validatorFactory.setPasswordEncoder(mockedEncoder);
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
	public void loginWithValidEmailAndPassword() {
		Token token = createToken(user);

		EmailPassDTO request = new EmailPassDTO();
		request.setEmail(user.getEmail());
		request.setPass(user.getPass());

		ResponseEntity<TokenDTO> tdo = trt.postForEntity("/users/login", request, TokenDTO.class);

		Assertions.assertThat(tdo.getStatusCode()).isEqualTo(HttpStatus.OK);
		Assertions.assertThat(tdo.getBody().getHash()).isEqualTo(token.getHash());
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

	private Address createAddress() {
		Address address = new Address();
		address.setCity("Iasi");
		address.setCountry("Romaniaa");
		address.setNumber(1);
		address.setStreet("Strada Palat");
		return addressRepository.save(address);
	}

	private Role createRole() {
		Role role = new Role("BASIC_USER");
		return roleRepository.save(role);
	}

	private User createUser() {
		User user = new User();
		user.setEmail("ralucaioana@yahoo.com");
		user.setFirstName("Raluca");
		user.setSecondName("Ioana");
		user.setPass("Raluca1@");
		user.setRole(createRole());
		user.setAddress(createAddress());
		return userRepository.save(user);
	}
}