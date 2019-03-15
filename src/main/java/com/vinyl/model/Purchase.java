package com.vinyl.model;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "PURCHASE")
public class Purchase {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
	@GenericGenerator(name = "native", strategy = "native")
	private Integer id;

	@NotNull
	@Column(nullable = false)
	private LocalDate dateCreated;

	@NotEmpty
	@Column(nullable = false)
	private String status;

	@NotNull
	@ManyToOne
	@JoinColumn(name = "user_id", nullable = false)
	private User user;

	@OneToMany(mappedBy = "product")
	public List<PurchaseProduct> products;

	public Purchase() {
	}

	public Purchase(@NotNull LocalDate dateCreated, @NotEmpty String status, @NotNull User user, List<PurchaseProduct> products) {
		this.dateCreated = dateCreated;
		this.status = status;
		this.user = user;
		this.products = products;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public LocalDate getDateCreated() {
		return dateCreated;
	}

	public void setDateCreated(LocalDate dateCreated) {
		this.dateCreated = dateCreated;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public List<PurchaseProduct> getPurchaseProducts() {
		return products;
	}

	public void sePurchasetProducts(List<PurchaseProduct> products) {
		this.products = products;
	}
}
