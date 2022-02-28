package org.pkfrc.vending.entities.product;

import java.io.Serializable;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.pkfrc.core.entities.base.AbstractParamEntity;
import org.pkfrc.core.entities.security.User;


@Entity
@Table(name = "VEND_PRODUCT")
@SequenceGenerator(name = "VEND_PRODUCT_SEQ", sequenceName = "VEND_PRODUCT_SEQ", allocationSize = 1, initialValue = 1)
@AttributeOverrides({
		@AttributeOverride(name = "id", column = @Column(name = "PRODUCT_ID", insertable = false, updatable = false)),
		@AttributeOverride(name = "code", column = @Column(name = "PRODUCT_CODE")),
		@AttributeOverride(name = "title", column = @Column(name = "PRODUCT_TITLE")) })
public class Product extends AbstractParamEntity<Long> implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "id", unique = true, updatable = false)
	@GeneratedValue(generator = "VEND_PRODUCT_SEQ", strategy = GenerationType.SEQUENCE)
	private Long id;
	
	
	@Column(name = "PRODUCT_QUANTITY")
	private Double quantity;
	
	@Column(name = "PRODUCT_COST")
	private Double cost;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "SELLER_ID")
	private User seller;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Double getQuantity() {
		return quantity;
	}

	public void setQuantity(Double quantity) {
		this.quantity = quantity;
	}

	public Double getCost() {
		return cost;
	}

	public void setCost(Double cost) {
		this.cost = cost;
	}

	public User getSeller() {
		return seller;
	}

	public void setSeller(User seller) {
		this.seller = seller;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((code == null) ? 0 : code.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		Product other = (Product) obj;
		if (code == null) {
			if (other.code != null)
				return false;
		} else if (!code.equals(other.code))
			return false;
		return true;
	}
	
	
	
}