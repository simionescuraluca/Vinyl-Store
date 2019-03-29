package com.vinyl.controller;

import com.vinyl.helper.AuthenticationHeaderHelper;
import com.vinyl.modelDTO.AddProductToCartDTO;
import com.vinyl.modelDTO.ProductManagementDTO;
import com.vinyl.service.ProductService;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.vinyl.controller.Consts.BAD_REQUEST_MESSAGE;
import static com.vinyl.controller.Consts.NOT_AUTHORIZED_MESSAGE;

@Api(value = "Vinyl Management", description = "Operations pertaining to vinyls in Vinyl Management System")
@RestController
public class ProductController {

    @Autowired
    ProductService productService;

    @ApiOperation(value = "Add a vinyl to the shopping cart", response = ResponseEntity.class)
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Successfully added vinyl to cart"),
            @ApiResponse(code = 401, message = NOT_AUTHORIZED_MESSAGE),
            @ApiResponse(code = 400, message = BAD_REQUEST_MESSAGE)
    })
    @RequestMapping(value = "/products/{productId}/cart", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<?> addProductToCart(@ApiParam(value = "AddProductToCartDTO object to send in the request body", required = true) @RequestBody AddProductToCartDTO info,
                                              @ApiParam(value = "Product id of the product which will be added to cart", required = true) @PathVariable Integer productId,
                                              @ApiParam(value = "Token hash to send in the request header", required = true) @RequestHeader(value = "Authorization", required = false) String auth) {

        String token = AuthenticationHeaderHelper.getTokenHashOrNull(auth);
        productService.addProductToCart(info, productId, token);

        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @ApiOperation(value = "As a manager, add a vinyl to the vinyl store", response = ResponseEntity.class)
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Successfully added vinyl to store"),
            @ApiResponse(code = 401, message = NOT_AUTHORIZED_MESSAGE),
            @ApiResponse(code = 400, message = BAD_REQUEST_MESSAGE)
    })
    @RequestMapping(value = "/products", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<?> addProductToStore(@ApiParam(value = "AddProductToStoreDTO object to send in the request body", required = true) @RequestBody ProductManagementDTO productToAdd,
                                               @ApiParam(value = "Token hash to send in the request header", required = true) @RequestHeader(value = "Authorization", required = false) String auth) {
        String token = AuthenticationHeaderHelper.getTokenHashOrNull(auth);
        productService.addProductToStore(productToAdd, token);

        return new ResponseEntity<>(HttpStatus.CREATED);
    }

   @ApiOperation(value = "As a manager, update a vinyl", response = ResponseEntity.class)
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Successfully updated vinyl"),
            @ApiResponse(code = 401, message = NOT_AUTHORIZED_MESSAGE),
            @ApiResponse(code = 400, message = BAD_REQUEST_MESSAGE)
    })
    @RequestMapping(value = "/products/{productId}", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<?> updateProduct(@ApiParam(value = "Vinyl object to send in the request body", required = true) @RequestBody ProductManagementDTO updatedProduct,
                                           @ApiParam(value = "Token hash to send in the request header", required = true) @RequestHeader(value = "Authorization", required = false) String auth,
                                           @ApiParam(value = "Vinyl id of the vinyl which will be updated", required = true) @PathVariable Integer productId) {
        String token = AuthenticationHeaderHelper.getTokenHashOrNull(auth);
        productService.updateProduct(updatedProduct, token, productId);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @ApiOperation(value = "As a manager, update a vinyl", response = ResponseEntity.class)
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Successfully updated vinyl"),
            @ApiResponse(code = 401, message = NOT_AUTHORIZED_MESSAGE),
            @ApiResponse(code = 400, message = BAD_REQUEST_MESSAGE)
    })
    @RequestMapping(value = "/products/{productId}", method = RequestMethod.DELETE)
    public ResponseEntity<?> removeProductFromStore(@ApiParam(value = "Token hash to send in the request header", required = true) @RequestHeader(value = "Authorization", required = false) String auth,
                                                    @ApiParam(value = "Vinyl id of the vinyl which will be removed", required = true) @PathVariable Integer productId) {
        String token = AuthenticationHeaderHelper.getTokenHashOrNull(auth);
        productService.removeProductFromStore(token,productId);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}