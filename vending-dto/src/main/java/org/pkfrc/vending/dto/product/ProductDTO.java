package org.pkfrc.vending.dto.product;

import java.io.Serializable;

import org.pkfrc.core.dto.base.AbstractDTO;
import org.pkfrc.core.entities.security.User;
import org.pkfrc.vending.entities.product.Product;

import lombok.Data;
import lombok.NoArgsConstructor;



@Data
@NoArgsConstructor
public class ProductDTO extends AbstractDTO<Product, Long> implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private Integer quantity;
	private Double cost;
	private User seller;

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
}
