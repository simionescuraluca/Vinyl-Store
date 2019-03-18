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

    @SuppressWarnings("serial")
    public static class PurchaseProductId implements Serializable {

        private Purchase purchase;
        private Product product;

        public PurchaseProductId(Purchase purchase, Product product) {
            this.purchase = purchase;
            this.product = product;
        }

        public PurchaseProductId() {

        }

        @Override
        public int hashCode() {
            final int prime = 31;
            int result = 1;
            result = prime * result + ((product == null) ? 0 : product.hashCode());
            result = prime * result + ((purchase == null) ? 0 : purchase.hashCode());
            return result;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj)
                return true;
            if (obj == null)
                return false;
            if (getClass() != obj.getClass())
                return false;
            PurchaseProductId other = (PurchaseProductId) obj;
            if (product == null) {
                if (other.product != null)
                    return false;
            } else if (!product.equals(other.product))
                return false;
            if (purchase == null) {
                if (other.purchase != null)
                    return false;
            } else if (!purchase.equals(other.purchase))
                return false;
            return true;
        }

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

    }
}
