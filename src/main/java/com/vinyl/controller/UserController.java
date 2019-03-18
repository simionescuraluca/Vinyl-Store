package com.vinyl.controller;

import com.vinyl.helper.AuthenticationHeaderHelper;
import com.vinyl.model.Token;
import com.vinyl.model.User;
import com.vinyl.modelDTO.CartDetailsDTO;
import com.vinyl.modelDTO.EmailPassDTO;
import com.vinyl.modelDTO.TokenDTO;
import com.vinyl.modelDTO.UserDTO;
import com.vinyl.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class UserController {

    @Autowired
    UserService userService;

    @RequestMapping(value = "/users", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<?> addUser(@RequestBody UserDTO userDTO) {

        User user = new User();
        user.setFirstName(userDTO.getFirstName());
        user.setSecondName(userDTO.getSecondName());
        user.setEmail(userDTO.getEmail());
        user.setPass(userDTO.getPass());

        userService.addUser(user);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @RequestMapping(value = "/users", method = RequestMethod.DELETE, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<?> deleteUser(@RequestBody EmailPassDTO credentials) {

        userService.deleteUser(credentials);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @RequestMapping(value = "/users/login", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<?> userLogin(@RequestBody EmailPassDTO loginInfo) {

        Token token = userService.loginUser(loginInfo);

        TokenDTO tokenDTO = new TokenDTO();
        tokenDTO.setHash(token.getHash());
        tokenDTO.setValidUntil(token.getValidUntil());

        return new ResponseEntity<>(tokenDTO, HttpStatus.OK);
    }

    @RequestMapping(value = "/users/cart", method = RequestMethod.GET)
    public ResponseEntity<?> getCartDetails(@RequestHeader(value = "Authorization", required = false) String auth) {

        String token = AuthenticationHeaderHelper.getTokenHashOrNull(auth);

        CartDetailsDTO cartDetails = userService.getCartDetails(token);
        if (cartDetails.getNrProducts() == 0) {
            return new ResponseEntity<>("No items in cart!", HttpStatus.OK);
        }

        return new ResponseEntity<>(cartDetails, HttpStatus.OK);
    }

    @RequestMapping(value="/users/{userId}/{productId}", method=RequestMethod.POST)
	public ResponseEntity<?> deleteProductFromCart(@RequestHeader(value = "Authorization", required = false) String auth, @PathVariable Integer userId, @PathVariable Integer productId){

		String token = AuthenticationHeaderHelper.getTokenHashOrNull(auth);
		userService.deleteProductFromCart(token,productId,userId);

		return new ResponseEntity<>(HttpStatus.OK);
	}
}
