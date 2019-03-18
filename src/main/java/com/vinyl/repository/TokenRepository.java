package com.vinyl.repository;

import com.vinyl.model.Token;
import com.vinyl.model.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TokenRepository extends CrudRepository<Token, Integer> {

    Token findFirstByUserOrderByValidUntilDesc(User user);

    Token findByHash(String hash);
}
