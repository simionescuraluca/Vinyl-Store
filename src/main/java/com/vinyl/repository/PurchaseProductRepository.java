package com.vinyl.repository;

import com.vinyl.model.Purchase;
import com.vinyl.model.PurchaseProduct;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PurchaseProductRepository extends CrudRepository<PurchaseProduct, Integer> {

    PurchaseProduct findByPurchase(Purchase purchase);
}
