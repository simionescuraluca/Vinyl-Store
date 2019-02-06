package com.vinyl.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.vinyl.model.User;

@Repository("userRepository")
public interface UserRepository extends CrudRepository<User, Integer> {

	User findByEmail(String email);
}
