package com.vinyl.service.validation;

import com.google.common.base.Strings;
import com.vinyl.modelDTO.ProductManagementDTO;
import com.vinyl.service.exception.BadRequestException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ProductManagementValidator implements Validator<ProductManagementDTO> {

    private static final Logger LOGGER = LoggerFactory.getLogger(ProductManagementValidator.class);

    @Override
    public void validate(ProductManagementDTO product) {

        if (Strings.isNullOrEmpty(product.getProductName())) {
            LOGGER.error("Invalid product name error!");
            throw new BadRequestException("The product name is invalid!");
        }

        if (product.getStock() < 0) {
            LOGGER.error("Invalid stock error!");
            throw new BadRequestException("The stock is invalid!");
        }

        if (product.getPrice() <= 0.0) {
            LOGGER.error("Invalid price error!");
            throw new BadRequestException("The product price is invalid!");
        }
    }
}