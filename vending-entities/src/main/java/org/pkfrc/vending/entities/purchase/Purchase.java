package org.pkfrc.vending.entities.purchase;

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
import javax.persistence.Transient;

import org.pkfrc.core.entities.base.AbstractParamEntity;
import org.pkfrc.core.entities.security.User;
import org.pkfrc.vending.entities.product.Product;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Entity
@Table(name = "VEND_PURCHASE")
@SequenceGenerator(name = "VEND_PURCHASE_SEQ", sequenceName = "VEND_PURCHASE_SEQ", allocationSize = 1, initialValue = 1)
@AttributeOverrides({
		@AttributeOverride(name = "id", column = @Column(name = "PURCHASE_ID", insertable = false, updatable = false)),
		@AttributeOverride(name = "code", column = @Column(name = "PURCHASE_CODE")),
		@AttributeOverride(name = "title", column = @Column(name = "PURCHASE_TITLE")) })
public class Purchase extends AbstractParamEntity<Long> implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "id", unique = true, updatable = false)
	@GeneratedValue(generator = "VEND_PURCHASE_SEQ", strategy = GenerationType.SEQUENCE)
	private Long id;
	
	
	@Column(name = "PURCHASE_QUANTITY")
	private Integer quantity;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "BUYER_ID")
	private User buyer;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "PRODUCT_ID")
	private Product product;
	
	@Transient
	private Double accountBalance;
}