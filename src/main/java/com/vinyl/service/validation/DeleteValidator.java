package com.vinyl.service.validation;

import java.util.Optional;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.vinyl.model.User;
import com.vinyl.modelDTO.DeleteUserDTO;
import com.vinyl.repository.UserRepository;
import com.vinyl.service.exception.BadRequestException;

public class DeleteValidator implements Validator<DeleteUserDTO> {

	private final UserRepository userRepository;
	private final BCryptPasswordEncoder passwordEncoder;

	public DeleteValidator(UserRepository userRepository, BCryptPasswordEncoder passwordEncoder) {
		this.userRepository = userRepository;
		this.passwordEncoder = passwordEncoder;

	}

	@Override
	public void validate(DeleteUserDTO credentials) {

		Optional<User> toBeDeleted = userRepository.findByEmail(credentials.getEmail());
		toBeDeleted.orElseThrow(() -> new BadRequestException("Cannot validate email during delete process!"));
		toBeDeleted.map(e -> e.getPass()).filter(e -> passwordEncoder.matches(credentials.getPass(), e))
				.orElseThrow((() -> new BadRequestException("Cannot validate password during delete process!")));

	}

}
