package org.pkfrc.vending.dto.purchase;

import java.io.Serializable;

import org.pkfrc.core.dto.base.AbstractDTO;
import org.pkfrc.core.entities.security.User;
import org.pkfrc.vending.dto.product.ProductDTO;
import org.pkfrc.vending.entities.purchase.Purchase;

import lombok.Data;
import lombok.NoArgsConstructor;



@Data
@NoArgsConstructor
public class PurchaseDTO extends AbstractDTO<Purchase, Long> implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private Double quantity;
	private User buyer;
	private ProductDTO product;

	public PurchaseDTO(Purchase entity) {
		super(entity);
		mapper.map(entity, this);
	}

	@Override
	public Purchase toEntity(Purchase entity) {
		entity = super.toEntity(entity);
		mapper.map(this, entity);
		return entity;
	}
}
