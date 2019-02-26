package com.vinyl.service;

import java.time.LocalDate;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.vinyl.model.Address;
import com.vinyl.model.Role;
import com.vinyl.model.Token;
import com.vinyl.model.User;
import com.vinyl.modelDTO.EmailPassDTO;
import com.vinyl.repository.AddressRepository;
import com.vinyl.repository.RoleRepository;
import com.vinyl.repository.TokenRepository;
import com.vinyl.repository.UserRepository;
import com.vinyl.service.validation.ValidatorFactory;

@Service("userService")
public class UserService {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private RoleRepository roleRepository;

	@Autowired
	private AddressRepository addressRepository;

	@Autowired
	private BCryptPasswordEncoder passwordEncoder;

	@Autowired
	private ValidatorFactory validatorFactory;

	@Autowired
	private TokenRepository tokenRepository;

	public User addUser(User user) {

		Role r = new Role("BASIC_USER");
		r = roleRepository.save(r);
		user.setRole(r);

		Address defaultAddress = addressRepository.findByCountryAndCityAndStreetAndNumber("Romania", "Iasi",
				"Strada Palat", 1);
		user.setAddress(defaultAddress);

		validatorFactory.getUserNameValidator().validate(user);
		validatorFactory.getUserEmailValidator().validate(user);
		validatorFactory.getUserPasswordValidator().validate(user);

		user.setPass(passwordEncoder.encode(user.getPass()));
		return userRepository.save(user);

	}

	public void deleteUser(EmailPassDTO credentials) {

		validatorFactory.getEmailAndPasswordValidator().validate(credentials);
		Optional<User> user = userRepository.findByEmail(credentials.getEmail());
		user.ifPresent(userRepository::delete);
	}

	public Token loginUser(EmailPassDTO info) {

		validatorFactory.getEmailAndPasswordValidator().validate(info);

		User user=userRepository.findByEmail(info.getEmail()).get();
		Token token = tokenRepository.findFirstByUserOrderByValidUntilDesc(user);
		
		if (token == null) {
			token=new Token();
			token.setUser(userRepository.findByEmail(info.getEmail()).get());
			token.setHash(String.valueOf(Math.abs(info.hashCode())));

			LocalDate tokenCreateDate = LocalDate.now();
			LocalDate tokenValidUntil = tokenCreateDate.plusMonths(1);
			token.setValidUntil(tokenValidUntil);
		}
		else
		{
			if (LocalDate.now().compareTo(token.getValidUntil()) < 0) {
				return token;
			}
		}
		
		return tokenRepository.save(token);
	}
}
