package com.vinyl.model;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "PRODUCT")
public class Product {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
	@GenericGenerator(name = "native", strategy = "native")
	private Integer id;

	@NotEmpty
	@Column(nullable = false)
	private String productName;

	@NotEmpty
	@Column(nullable = false)
	private String description;

	@NotNull
	@Column(nullable = false)
	private Double price;

	@NotNull
	@Column(nullable = false)
	private Integer stock;

	@NotEmpty
	@Column(nullable = false)
	private String artist;

	@NotEmpty
	@Column(nullable = false)
	private String category;

	public Product() {

	}

	public Product(@NotEmpty String productName, @NotEmpty String description, @NotNull Double price, @NotNull Integer stock, @NotEmpty String artist, @NotEmpty String category) {
		this.productName = productName;
		this.description = description;
		this.price = price;
		this.stock = stock;
		this.artist = artist;
		this.category = category;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Double getPrice() {
		return price;
	}

	public void setPrice(Double price) {
		this.price = price;
	}

	public Integer getStock() {
		return stock;
	}

	public void setStock(Integer stock) {
		this.stock = stock;
	}

	public String getArtist() {
		return artist;
	}

	public void setArtist(String artist) {
		this.artist = artist;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}
}
