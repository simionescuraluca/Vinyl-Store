package com.vinyl.model;

import com.vinyl.model.ProductCart.ProductCartId;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Entity
@IdClass(ProductCartId.class)
@Table(name = "PRODUCT_CART")
public class ProductCart {

    @NotNull
    @Column(nullable = false)
    private Integer nrItems;

    @Id
    @NotNull
    @ManyToOne
    @JoinColumn(name = "cart_id", nullable = false)
    private Cart cart;

    @Id
    @NotNull
    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @NotNull
    @Column(nullable = false)
    private Double productPrice;

    public ProductCart() {
    }

    public ProductCart(Product product, Cart cart) {
        this.product = product;
        this.cart = cart;
    }

    public Double getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(Double productPrice) {
        this.productPrice = productPrice;
    }

    public Integer getNrItems() {
        return nrItems;
    }

    public void setNrItems(Integer nrItems) {
        this.nrItems = nrItems;
    }

    public Cart getCart() {
        return cart;
    }

    public void setCart(Cart cart) {
        this.cart = cart;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    @SuppressWarnings("serial")
    public static class ProductCartId implements Serializable {

        private int cart;
        private int product;

        public ProductCartId(int cart, int product) {

            this.cart = cart;
            this.product = product;
        }

        public ProductCartId() {
        }

        public int getCart() {
            return cart;
        }

        public void setCart(int cart) {
            this.cart = cart;
        }

        public int getProduct() {
            return product;
        }

        public void setProduct(int product) {
            this.product = product;
        }
    }
}
