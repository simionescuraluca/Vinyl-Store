package com.vinyl;

import com.vinyl.helper.DefaultEntitiesHelper;
import com.vinyl.model.*;
import com.vinyl.repository.AddressRepository;
import com.vinyl.repository.ProductRepository;
import com.vinyl.repository.RoleRepository;
import com.vinyl.repository.UserRepository;
import org.junit.After;
import org.junit.Before;
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
public abstract class BaseIntegration {
    @Autowired
    protected TestRestTemplate trt;

    @Autowired
    protected RoleRepository roleRepository;

    @Autowired
    protected AddressRepository addressRepository;

    @Autowired
    protected UserRepository userRepository;

    protected User user;

    protected Token token;

    @Autowired
    protected DefaultEntitiesHelper defaultEntitiesHelper;

    private Address address;

    private Role role;

    @Autowired
    ProductRepository productRepository;

    @Before
    public void setUp() {
        createAddress();
        createRole();
        user = createUser("defaultuser@email.com");
        token = defaultEntitiesHelper.createToken(user);
    }

    @After
    public void tearDown() {
        defaultEntitiesHelper.tearDown();
    }

   public void createAddress(){
        address = new Address();
        address.setCity("Iasi");
        address.setCountry("Romaniaa");
        address.setNumber(1);
        address.setStreet("Strada Palat");
        address=addressRepository.save(address);
    }

    public void createRole(){
        role = new Role("BASIC_USER");
        role=roleRepository.save(role);
    }

    public User createUser(String email){
        User user = new User();
        user.setEmail(email);
        user.setFirstName("Raluca");
        user.setSecondName("Ioana");
        user.setPass("Raluca1@");
        user.setRole(role);
        user.setAddress(address);
        return userRepository.save(user);
    }
}
