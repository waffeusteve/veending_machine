package org.pkfrc.vending.services.purchase;

import org.pkfrc.core.entities.security.User;
import org.pkfrc.core.services.base.IBaseService;
import org.pkfrc.core.services.base.ServiceData;
import org.pkfrc.vending.entities.purchase.Purchase;


public interface IPurchaseService extends IBaseService<Purchase, Long> {

	ServiceData<Purchase> doBuy(User user, BuyRequest record) throws Exception;

	

}