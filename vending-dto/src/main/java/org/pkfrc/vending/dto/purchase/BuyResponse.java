package org.pkfrc.vending.dto.purchase;

import java.io.Serializable;

import org.pkfrc.vending.entities.purchase.Purchase;

public class BuyResponse implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Integer totalSpent;
	private String productName;
	private Double accountBalance;
	
	public BuyResponse(Purchase entity) {
		this.totalSpent = entity.getProduct().getCost().intValue()* entity.getQuantity();
		this.productName = entity.getProduct().getDesignation();
		this.accountBalance = entity.getAccountBalance();
	}

	public Integer getTotalSpent() {
		return totalSpent;
	}

	public void setTotalSpent(Integer totalSpent) {
		this.totalSpent = totalSpent;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public Double getAccountBalance() {
		return accountBalance;
	}

	public void setAccountBalance(Double accountBalance) {
		this.accountBalance = accountBalance;
	}

}
