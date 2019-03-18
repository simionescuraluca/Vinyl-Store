package com.vinyl.repository;

import com.vinyl.model.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository("userRepository")
public interface UserRepository extends CrudRepository<User, Integer> {

    Optional<User> findByEmail(String email);
}
