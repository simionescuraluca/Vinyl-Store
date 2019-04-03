package com.vinyl.controller;

import com.vinyl.helper.AuthenticationHeaderHelper;
import com.vinyl.modelDTO.StatusDTO;
import com.vinyl.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class OrderController {

    @Autowired
    private OrderService orderService;

    @RequestMapping(value = "/orders/{orderId}", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<?> changeOrderStatus(@RequestBody StatusDTO newStatus, @PathVariable Integer orderId, @RequestHeader(value = "Authorization", required = false) String auth) {
        String token = AuthenticationHeaderHelper.getTokenHashOrNull(auth);
        orderService.changeOrderStatus(newStatus, orderId, token);

        return new ResponseEntity<>(HttpStatus.OK);
    }
}