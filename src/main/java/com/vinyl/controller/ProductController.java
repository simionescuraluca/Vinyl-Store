package com.vinyl.controller;

import com.vinyl.helper.AuthenticationHeaderHelper;
import com.vinyl.modelDTO.AddProductToCartDTO;
import com.vinyl.service.ProductService;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Api(value="Vinyl Management", description="Operations pertaining to vinyls in Vinyl Management System")
@RestController
public class ProductController {

    @Autowired
    ProductService productService;

    @ApiOperation(value = "Add a vinyl to the shopping cart", response = ResponseEntity.class)
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Successfully added vinyl to cart"),
            @ApiResponse(code = 401, message = "You are not authorized to make this request"),
            @ApiResponse(code = 400, message = "You made a bad request!")
    })
    @RequestMapping(value="/products/{productId}/cart", method= RequestMethod.POST, consumes= MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<?> addProductToCart(@ApiParam(value = "AddProductToCartDTO object to send in the request body", required = true)@RequestBody AddProductToCartDTO info,
                                              @ApiParam(value = "Product id of the product which will be added to cart", required = true) @PathVariable Integer productId,
                                              @ApiParam(value = "Token hash to sent in the request header", required = true) @RequestHeader(value = "Authorization", required = false) String auth ){

        String token = AuthenticationHeaderHelper.getTokenHashOrNull(auth);
        productService.addProductToCart(info,productId,token);

        return new ResponseEntity<>(HttpStatus.CREATED);
    }
}