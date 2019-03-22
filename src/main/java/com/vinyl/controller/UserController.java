package com.vinyl.controller;

import com.vinyl.helper.AuthenticationHeaderHelper;
import com.vinyl.model.Token;
import com.vinyl.model.User;
import com.vinyl.modelDTO.CartDetailsDTO;
import com.vinyl.modelDTO.EmailPassDTO;
import com.vinyl.modelDTO.TokenDTO;
import com.vinyl.modelDTO.UserDTO;
import com.vinyl.service.UserService;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.vinyl.controller.Consts.BAD_REQUEST_MESSAGE;
import static com.vinyl.controller.Consts.NOT_AUTHORIZED_MESSAGE;

@Api(value = "User Management", description = "Operations pertaining to users in User Management System")
@RestController
public class UserController {

    @Autowired
    UserService userService;

    @ApiOperation(value = "Create an account", response = ResponseEntity.class)
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Successfully created user"),
            @ApiResponse(code = 400, message = BAD_REQUEST_MESSAGE)
    })
    @RequestMapping(value = "/users", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<?> addUser(@ApiParam(value = "UserDTO object to send in the request body", required = true) @RequestBody UserDTO userDTO) {

        User user = new User();
        user.setFirstName(userDTO.getFirstName());
        user.setSecondName(userDTO.getSecondName());
        user.setEmail(userDTO.getEmail());
        user.setPass(userDTO.getPass());

        userService.addUser(user);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @ApiOperation(value = "Delete an account", response = ResponseEntity.class)
    @ApiResponses(value = {
            @ApiResponse(code = 204, message = "Successfully deleted user"),
            @ApiResponse(code = 400, message = BAD_REQUEST_MESSAGE)
    })
    @RequestMapping(value = "/users", method = RequestMethod.DELETE, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<?> deleteUser(@ApiParam(value = "EmailPassDTO object to send in the request body", required = true) @RequestBody EmailPassDTO credentials) {

        userService.deleteUser(credentials);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @ApiOperation(value = "User logs in", response = ResponseEntity.class)
    @ApiResponses(value = {
            @ApiResponse(code = 401, message = NOT_AUTHORIZED_MESSAGE),
            @ApiResponse(code = 400, message = BAD_REQUEST_MESSAGE),
            @ApiResponse(code = 200, message = "You successfully logged in")

    })
    @RequestMapping(value = "/users/login", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<?> userLogin(@ApiParam(value = "EmailPassDTO object to send in the request body", required = true) @RequestBody EmailPassDTO loginInfo) {

        Token token = userService.loginUser(loginInfo);

        TokenDTO tokenDTO = new TokenDTO();
        tokenDTO.setHash(token.getHash());
        tokenDTO.setValidUntil(token.getValidUntil());

        return new ResponseEntity<>(tokenDTO, HttpStatus.OK);
    }

    @ApiOperation(value = "User gets cart details", response = String.class)
    @ApiResponses(value = {
            @ApiResponse(code = 401, message = NOT_AUTHORIZED_MESSAGE),
            @ApiResponse(code = 400, message = BAD_REQUEST_MESSAGE),
            @ApiResponse(code = 200, message = "You successfully retrieved cart details")

    })
    @RequestMapping(value = "/users/cart", method = RequestMethod.GET)
    public ResponseEntity<?> getCartDetails(@ApiParam(value = "Token hash to send in the request header", required = true) @RequestHeader(value = "Authorization", required = false) String auth) {

        String token = AuthenticationHeaderHelper.getTokenHashOrNull(auth);

        CartDetailsDTO cartDetails = userService.getCartDetails(token);
        if (cartDetails.getNrProducts() == 0) {
            return new ResponseEntity<>("No items in cart!", HttpStatus.OK);
        }

        return new ResponseEntity<>(cartDetails, HttpStatus.OK);
    }

    @RequestMapping(value = "/users/{userId}/{productId}", method = RequestMethod.POST)
    public ResponseEntity<?> deleteProductFromCart(@RequestHeader(value = "Authorization", required = false) String auth, @PathVariable Integer userId, @PathVariable Integer productId) {

        String token = AuthenticationHeaderHelper.getTokenHashOrNull(auth);
        userService.deleteProductFromCart(token, productId, userId);

        return new ResponseEntity<>(HttpStatus.OK);
    }
}
