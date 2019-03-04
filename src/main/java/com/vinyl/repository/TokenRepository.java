package com.vinyl.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.vinyl.model.Token;
import com.vinyl.model.User;

@Repository
public interface TokenRepository extends CrudRepository<Token, Integer>{

	Token findFirstByUserOrderByValidUntilDesc(User user);
}
