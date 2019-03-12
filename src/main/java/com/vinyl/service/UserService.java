package com.vinyl.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.vinyl.model.*;
import com.vinyl.modelDTO.CartDetailsDTO;
import com.vinyl.modelDTO.ProductDTO;
import com.vinyl.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.vinyl.modelDTO.EmailPassDTO;
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

	@Autowired
	private ProductCartRepository productCartRepository;

	@Autowired
	private CartRepository cartRepository;

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

	public CartDetailsDTO getCartDetails(String tokenHash) {

		validatorFactory.getTokenValidator().validate(tokenHash);
		Token token = tokenRepository.findByHash(tokenHash);
		User user = token.getUser();

		Cart cart = cartRepository.findByUser(user);
		List<ProductCart> productCartList = productCartRepository.findByCart(cart);
		List<ProductDTO> productDetails = new ArrayList<>();
		double cost = 0.0;
		for (ProductCart product : productCartList) {
			String name = product.getProduct().getProductName();
			productDetails.add(new ProductDTO(name, product.getNrItems(), product.getProductPrice()));

			cost = cost + (product.getProductPrice() * product.getNrItems());
		}
		CartDetailsDTO cartDetails = new CartDetailsDTO();
		cartDetails.setNrProducts(productDetails.size());
		cartDetails.setProducts(productDetails);
		cartDetails.setTotalCost(cost);

		return cartDetails;
	}
}