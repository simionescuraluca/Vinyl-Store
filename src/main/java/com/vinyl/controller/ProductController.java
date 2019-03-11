package com.vinyl.controller;

import com.vinyl.helper.AuthenticationHeaderHelper;
import com.vinyl.modelDTO.AddProductToCartDTO;
import com.vinyl.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class ProductController {

    @Autowired
    ProductService productService;

    @RequestMapping(value="/products/{productId}/cart", method= RequestMethod.POST, consumes= MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<?> addProductToCart(@RequestBody AddProductToCartDTO info, @PathVariable Integer productId, @RequestHeader(value = "Authorization", required = false) String auth ){

        String token = AuthenticationHeaderHelper.getTokenHashOrNull(auth);
        productService.addProductToCart(info,productId,token);

        return new ResponseEntity<>(HttpStatus.CREATED);
    }
}