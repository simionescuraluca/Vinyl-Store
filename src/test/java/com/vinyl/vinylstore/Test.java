package com.vinyl.vinylstore;

import com.vinyl.model.Address;
import com.vinyl.model.Role;
import com.vinyl.model.User;
import com.vinyl.repository.AddressRepository;
import com.vinyl.repository.RoleRepository;
import com.vinyl.repository.UserRepository;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureTestDatabase
@RunWith(SpringRunner.class)
@ActiveProfiles("test")
public abstract class Test {
    @Autowired
    TestRestTemplate trt;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    AddressRepository addressRepository;

    @Autowired
    UserRepository userRepository;

    abstract Address createAddress();
    abstract Role createRole();
    abstract User createUser();

}
