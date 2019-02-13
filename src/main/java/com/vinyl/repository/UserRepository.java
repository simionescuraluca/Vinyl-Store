package com.vinyl.repository;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.vinyl.model.User;

@Repository("userRepository")
public interface UserRepository extends CrudRepository<User, Integer> {

	Optional<User> findByEmail(String email);
}
