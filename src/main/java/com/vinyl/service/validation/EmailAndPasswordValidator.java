package com.vinyl.service.validation;

import java.util.Optional;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.vinyl.model.User;
import com.vinyl.modelDTO.EmailPassDTO;
import com.vinyl.repository.UserRepository;
import com.vinyl.service.exception.BadRequestException;

public class EmailAndPasswordValidator implements Validator<EmailPassDTO> {

	private final UserRepository userRepository;
	private final BCryptPasswordEncoder passwordEncoder;
	


	public EmailAndPasswordValidator(UserRepository userRepository, BCryptPasswordEncoder passwordEncoder) {
		this.userRepository = userRepository;
		this.passwordEncoder = passwordEncoder;
	}

	@Override
	public void validate(EmailPassDTO credentials) {

		Optional<User> toBeDeleted = userRepository.findByEmail(credentials.getEmail());
		toBeDeleted.orElseThrow(() -> new BadRequestException("Cannot validate email during process!"));
		toBeDeleted.map(e -> e.getPass()).filter(e -> passwordEncoder.matches(credentials.getPass(), e))
				.orElseThrow((() -> new BadRequestException("Cannot validate password during process!")));

	}
}
