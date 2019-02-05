package com.vinyl.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import com.vinyl.model.ProductCart.ProductCartId;

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

		private Cart cart;
		private Product product;

		public ProductCartId(Cart cart, Product product) {

			this.cart = cart;
			this.product = product;
		}

		public ProductCartId() {

		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + ((cart == null) ? 0 : cart.hashCode());
			result = prime * result + ((product == null) ? 0 : product.hashCode());
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
			ProductCartId other = (ProductCartId) obj;
			if (cart == null) {
				if (other.cart != null)
					return false;
			} else if (!cart.equals(other.cart))
				return false;
			if (product == null) {
				if (other.product != null)
					return false;
			} else if (!product.equals(other.product))
				return false;
			return true;
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

	}
}
