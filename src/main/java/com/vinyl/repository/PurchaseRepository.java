package com.vinyl.repository;

import org.springframework.data.repository.CrudRepository;

import com.vinyl.model.Purchase;

public interface PurchaseRepository extends CrudRepository<Purchase, Integer> {

}
