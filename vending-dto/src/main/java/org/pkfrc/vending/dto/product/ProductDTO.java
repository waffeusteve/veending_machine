package org.pkfrc.vending.dto.product;

import java.io.Serializable;

import org.pkfrc.core.dto.base.AbstractParamDTO;
import org.pkfrc.core.dto.security.UserResume;
import org.pkfrc.vending.entities.product.Product;

import lombok.NoArgsConstructor;




@NoArgsConstructor
public class ProductDTO extends AbstractParamDTO<Product, Long> implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private Integer quantity;
	private Double cost;
	private UserResume seller;

	public ProductDTO(Product entity) {
		super(entity);
		mapper.map(entity, this);
	}

	@Override
	public Product toEntity(Product entity) {
		entity = super.toEntity(entity);
		mapper.map(this, entity);
		return entity;
	}

	public Integer getQuantity() {
		return quantity;
	}

	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}

	public Double getCost() {
		return cost;
	}

	public void setCost(Double cost) {
		this.cost = cost;
	}

	public UserResume getSeller() {
		return seller;
	}

	public void setSeller(UserResume seller) {
		this.seller = seller;
	}
	
	
}
