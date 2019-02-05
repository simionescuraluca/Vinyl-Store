package com.vinyl.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.vinyl.model.Role;

@Repository
public interface RoleRepository extends CrudRepository<Role, Integer> {

}
