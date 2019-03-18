package com.vinyl.repository;

import com.vinyl.model.Address;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AddressRepository extends CrudRepository<Address, Integer> {

    Address findByCountryAndCityAndStreetAndNumber(String country, String city, String street, int number);
}
