package com.vinyl.repository;

import org.springframework.data.repository.CrudRepository;

import com.vinyl.model.User;

public interface UserRepository extends CrudRepository<User, Integer> {

}
