package com.vinyl;

import com.vinyl.model.User;
import com.vinyl.modelDTO.UserDTO;
import com.vinyl.repository.UserRepository;
import com.vinyl.service.PasswordEncoder;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import static org.mockito.ArgumentMatchers.any;

public class AddUserTest extends BaseIntegration {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private BCryptPasswordEncoder originalPasswordEncoder;

    private BCryptPasswordEncoder mockedPasswordEncoder;

    UserDTO userDTO = new UserDTO();

    @Override
    public void setUp() {
        super.setUp();
        setUserDTO(userDTO);
        mockedPasswordEncoder = Mockito.mock(BCryptPasswordEncoder.class);
        Mockito.when(mockedPasswordEncoder.encode(any())).thenReturn(userDTO.getPass());
        passwordEncoder.setPasswordEncoder(mockedPasswordEncoder);
    }

    @Override
    public void tearDown() {
        passwordEncoder.setPasswordEncoder(originalPasswordEncoder);
        super.tearDown();
    }

    @Test
    public void testWhenOk() {
        ResponseEntity<User> response = trt.exchange("/users", HttpMethod.POST, new HttpEntity<>(userDTO), User.class);
        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        User savedUser = userRepository.findByEmail(userDTO.getEmail()).get();
        Assertions.assertThat(savedUser).isNotNull();
        Assertions.assertThat(savedUser.getFirstName()).isEqualTo(userDTO.getFirstName());
        Assertions.assertThat(savedUser.getSecondName()).isEqualTo(userDTO.getSecondName());
        Assertions.assertThat(savedUser.getPass()).isEqualTo(userDTO.getPass());
    }

    @Test
    public void testWhenFirstNameIsInvalid() {
        userDTO.setFirstName("invalid_first_name");
        assertResponseWhenInvalidInput();
    }


    @Test
    public void testwhenSecondNameIsInvalid() {
        userDTO.setSecondName("invalid_second_name");
        assertResponseWhenInvalidInput();
    }

    @Test
    public void testWhenEmailIsInvalid() {
        userDTO.setEmail("invalid_email");
        assertResponseWhenInvalidInput();
    }

    @Test
    public void testWhenPassIsInvalid() {
        userDTO.setPass("invalid_pass");
        assertResponseWhenInvalidInput();
    }

    private void assertResponseWhenInvalidInput() {
        ResponseEntity<User> response = trt.exchange("/users", HttpMethod.POST, new HttpEntity<>(userDTO), User.class);
        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        Assertions.assertThat(userRepository.findByEmail(userDTO.getEmail())).isNotNull();
    }

    private void setUserDTO(UserDTO userDTO) {
        userDTO.setEmail("test1@email.com");
        userDTO.setFirstName("Raluca");
        userDTO.setSecondName("Simionescu");
        userDTO.setPass("P@ssword1");
    }
}