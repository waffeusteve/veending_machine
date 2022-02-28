package org.pkfrc.vending.services.purchase;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.transaction.Transactional;

import org.pkfrc.core.entities.security.User;
import org.pkfrc.core.services.base.BaseServiceImpl;
import org.pkfrc.core.services.base.ServiceData;
import org.pkfrc.core.services.base.Validation;
import org.pkfrc.core.services.security.IUserService;
import org.pkfrc.core.utilities.enumerations.ETransactionalOperation;
import org.pkfrc.projurise.repo.product.ProductRepository;
import org.pkfrc.projurise.repo.purchase.PurchaseRepository;
import org.pkfrc.vending.entities.product.Product;
import org.pkfrc.vending.entities.purchase.Purchase;
import org.pkfrc.vending.services.product.IProductService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PurchaseServiceImpl extends BaseServiceImpl<Purchase, Long> implements IPurchaseService {

	@Autowired
	PurchaseRepository repository;

	@Autowired
	ProductRepository productRepo;
	
	@Autowired
	IProductService productService;

	@Autowired
	IPurchaseService PurchaseService;

	@Autowired
	IUserService userService;

	Logger logger = LoggerFactory.getLogger(PurchaseServiceImpl.class);

	@Override
	protected Logger getLogger() {
		return logger;
	}

	@Override
	protected Class<Purchase> getClazz() {
		return Purchase.class;
	}

	@Override
	protected List<Validation> validateRecord(Purchase record, ETransactionalOperation operation) {
		List<Validation> result = new ArrayList<>();
		if (record == null || record.getProduct() == null || record.getQuantity() == null) {
			result.add(new Validation(getClazz().getSimpleName(), "Invalid_record", "Invalid_record"));
			return result;
		}
		return result;
	}

	protected List<Validation> validateRecord(BuyRequest record, ETransactionalOperation operation) {
		List<Validation> result = new ArrayList<>();
		if (record == null || record.getProductId() == null || record.getQuantity() == null) {
			result.add(new Validation(getClazz().getSimpleName(), "Invalid_record", "Invalid_record"));
			return result;
		}
		return result;
	}

	@Override
	protected List<Validation> validateRecords(Collection<Purchase> record, ETransactionalOperation operation) {
		return null;
	}

	@Override
	@Transactional
	public ServiceData<Purchase> doBuy(User user, BuyRequest record) throws Exception {
//		ServiceData<Purchase> serviceResult = super.createServiceData();
		List<Validation> valRes = validateRecord(record, ETransactionalOperation.Create);
		if (!valRes.isEmpty()) {
			return getInvalidResult(valRes);
		}
		Product product = productRepo.getOne(record.getProductId());
		Double price = record.getQuantity() * product.getCost();
		if (price > user.getDeposit()) {
			valRes.add(new Validation(getClazz().getSimpleName(), "Insufficient_funds", "Insufficient_funds"));
			return getInvalidResult(valRes);
		}
		if (record.getQuantity() > product.getQuantity()) {
			valRes.add(new Validation(getClazz().getSimpleName(), "Insufficient_product", "Insufficient_product"));
			return getInvalidResult(valRes);
		}
		// create purchase Object START
		Purchase purchase = new Purchase();
		purchase.setBuyer(user);
		purchase.setQuantity(record.getQuantity());
		String originale = repository.getMaxID() != null
				? repository.findById(repository.getMaxID()).orElse(null).getCode()
				: "00000";
		String incre = String.format("%0" + originale.length() + "d", Integer.parseInt(originale) + 1);
		purchase.setCode(incre);
		purchase.setAccountBalance(user.getDeposit() - price);
		// create purchase Object END

		// Update user deposit START
		user.setDeposit(user.getDeposit() - price);
		userService.update(user, user, false);

		// Update product quantity end
		product.setQuantity(product.getQuantity() - purchase.getQuantity());
		productService.update(user, product, false);

		return super.create(user, purchase, false);

	}

}
