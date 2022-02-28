package org.pkfrc.vending.services.product;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.pkfrc.core.entities.security.User;
import org.pkfrc.core.services.base.BaseServiceImpl;
import org.pkfrc.core.services.base.ServiceData;
import org.pkfrc.core.services.base.Validation;
import org.pkfrc.core.utilities.enumerations.ETransactionalOperation;
import org.pkfrc.projurise.repo.product.ProductRepository;
import org.pkfrc.vending.entities.product.Product;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProductServiceImpl extends BaseServiceImpl<Product, Long> implements IProductService {

	@Autowired
	ProductRepository repository;

	Logger logger = LoggerFactory.getLogger(ProductServiceImpl.class);

	@Override
	protected Logger getLogger() {
		return logger;
	}

	@Override
	protected Class<Product> getClazz() {
		return Product.class;
	}

	@Override
	protected List<Validation> validateRecord(Product record, ETransactionalOperation operation) {
		List<Validation> result = new ArrayList<>();
		if (record == null) {
			result.add(new Validation(getClazz().getSimpleName(), "No_product_selected", "No_product_selected"));
			return result;
		}

		if (record.getCost().intValue() % 5 != 0)
			result.add(new Validation(getClazz().getSimpleName(), "should_be_in_multiples_of_5",
					"should_be_in_multiples_of_5"));

		if (record.getCost() == null || record.getCost() <= 0)
			result.add(new Validation(getClazz().getSimpleName(), "Invalid_cost", "Invalid_cost"));

		if (record.getQuantity() == null || record.getQuantity() <= 0)
			result.add(new Validation(getClazz().getSimpleName(), "Invalid_quantity", "Invalid_quantity"));
		if (record.getDesignation() == null)
			result.add(new Validation(getClazz().getSimpleName(), "Assigne_designation", "Assigne_designation"));

		return result;
	}

	@Override
	protected List<Validation> validateRecords(Collection<Product> record, ETransactionalOperation operation) {
		return null;
	}

	@Override
	public ServiceData<Product> delete(User user, Long recordId) throws Exception {
		List<Validation> val = new ArrayList<>(0);
		if (recordId == null) {
			val.add(new Validation(getClazz().getSimpleName(), "No_product_selected", "No_product_selected"));
			return getInvalidResult(val);
		}
		Product product = repository.findById(recordId).orElse(null);
		if (product == null) {
			val.add(new Validation(getClazz().getSimpleName(), "Product_not_exit", "Product_not_exit"));
			return getInvalidResult(val);
		}
		if (product.getSeller().getId() != user.getId()) {
			val.add(new Validation(getClazz().getSimpleName(), "Unauthorised", "Unauthorised"));
			return getInvalidResult(val);
		}
		return super.delete(user, recordId, false);
	}

	@Override
	public ServiceData<Product> update(User user, Product record) throws Exception {
		List<Validation> val = validateRecord(record, ETransactionalOperation.Update);
		if (val.isEmpty() || record.getSeller().getId() != user.getId()) {
			val.add(new Validation(getClazz().getSimpleName(), "Unauthorised", "Unauthorised"));
			return getInvalidResult(val);
		}
		record.setSeller(user);
		return super.update(user, record);
	}

	@Override
	public ServiceData<Product> create(User user, Product record) throws Exception {
		ServiceData<Product> serviceResult = super.createServiceData();
		List<Validation> valRes = validateRecord(record, ETransactionalOperation.Create);
		if (!valRes.isEmpty()) {
			return getInvalidResult(valRes);
		}
		String originale = repository.getMaxID() != null
				? repository.findById(repository.getMaxID()).orElse(null).getCode()
				: "00000";
		String incre = String.format("%0" + originale.length() + "d", Integer.parseInt(originale) + 1);
		record.setCode(incre);
		record.setSeller(user);
		serviceResult = super.create(user, record, false);
		return serviceResult;

	}

}
