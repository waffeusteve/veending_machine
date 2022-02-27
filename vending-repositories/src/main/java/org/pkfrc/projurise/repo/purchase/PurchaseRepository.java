package org.pkfrc.projurise.repo.purchase;

import org.pkfrc.vending.entities.purchase.Purchase;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface PurchaseRepository extends JpaRepository<Purchase, Long>{
	
	
	@Query("SELECT max(id) FROM Purchase")
	Long getMaxID();

}
