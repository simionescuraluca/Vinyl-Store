package com.vinyl.model;

import com.vinyl.model.PurchaseProduct.PurchaseProductId;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Entity
@IdClass(PurchaseProductId.class)
@Table(name = "PURCHASE_PRODUCT")
public class PurchaseProduct {

    @NotNull
    @Column(nullable = false)
    private Integer nrItems;

    @Id
    @NotNull
    @ManyToOne
    @JoinColumn(name = "purchase_id", nullable = false)
    private Purchase purchase;

    @Id
    @NotNull
    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    public Purchase getPurchase() {
        return purchase;
    }

    public void setPurchase(Purchase purchase) {
        this.purchase = purchase;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public Integer getNrItems() {
        return nrItems;
    }

    public void setNrItems(Integer nrItems) {
        this.nrItems = nrItems;
    }

    @SuppressWarnings("serial")
    public static class PurchaseProductId implements Serializable {

        private int purchase;
        private int product;

        public PurchaseProductId(int purchase, int product) {
            this.purchase = purchase;
            this.product = product;
        }

        public PurchaseProductId() {

        }

        public int getPurchase() {
            return purchase;
        }

        public void setPurchase(int purchase) {
            this.purchase = purchase;
        }

        public int getProduct() {
            return product;
        }

        public void setProduct(int product) {
            this.product = product;
        }
    }
}
