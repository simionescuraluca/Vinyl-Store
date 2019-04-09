package com.vinyl.service;

import com.vinyl.modelDTO.AddProductToCartDTO;
import com.vinyl.modelDTO.InventoryListDTO;
import com.vinyl.modelDTO.ProductManagementDTO;

public interface ProductService {

    void addProductToCart(AddProductToCartDTO addProductToCartDTO, Integer productId, String tokenHash);

    void addProductToStore(ProductManagementDTO productToAdd, String tokenHash);

    void updateProduct(ProductManagementDTO updatedProduct, String tokenHash, Integer productId);

    void removeProductFromStore(String tokenHash, Integer productId);

    InventoryListDTO getInventory(String tokenHash);
}
