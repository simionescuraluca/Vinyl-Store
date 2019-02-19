package com.vinyl.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.vinyl.model.Token;

@Repository
public interface TokenRepository extends CrudRepository<Token, Integer>{

	Token findByHash(String hash);
}
