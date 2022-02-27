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

	@Autowired
	IProductService ProductService;

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

		return result;
	}

	@Override
	protected List<Validation> validateRecords(Collection<Product> record, ETransactionalOperation operation) {
		return null;
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
		serviceResult = super.create(user, record, false);
		return serviceResult;

	}

	
}
