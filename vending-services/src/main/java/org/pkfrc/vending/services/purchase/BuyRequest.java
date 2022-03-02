package org.pkfrc.vending.services.purchase;

import java.io.Serializable;

public class BuyRequest implements Serializable{
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Long productId;
	private Integer quantity;
	
	public Long getProductId() {
		return productId;
	}
	public void setProductId(Long productId) {
		this.productId = productId;
	}
	public Integer getQuantity() {
		return quantity;
	}
	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}
	public BuyRequest(Long productId, Integer quantity) {
		super();
		this.productId = productId;
		this.quantity = quantity;
	}
	
	

}
