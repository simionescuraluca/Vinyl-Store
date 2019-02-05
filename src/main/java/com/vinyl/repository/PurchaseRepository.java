package com.vinyl.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.vinyl.model.Purchase;

@Repository
public interface PurchaseRepository extends CrudRepository<Purchase, Integer> {

}
