package com.vinyl.model;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@Entity
@Table(name="CART")
public class Cart {

	@Id
	@NotNull
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	@NotNull
	@ManyToOne
	@JoinColumn(name = "user_id", nullable=false)
	private User user;
	
	 @ManyToMany
	 @JoinTable(name="PRODUCT_CART",
			 joinColumns=
			 				@JoinColumn(name="cart_id", referencedColumnName="id"),
			 inverseJoinColumns=
			 				@JoinColumn(name="product_id", referencedColumnName="id")
	 )
	 public List<Product> products;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public List<Product> getProducts() {
		return products;
	}

	public void setProducts(List<Product> products) {
		this.products = products;
	}
	 
	 
}
