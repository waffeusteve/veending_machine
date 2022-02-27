package org.pkfrc.projurise.repo.product;

import org.pkfrc.vending.entities.product.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ProductRepository extends JpaRepository<Product, Long>{
	
	
	@Query("SELECT max(id) FROM Product")
	Long getMaxID();

}
