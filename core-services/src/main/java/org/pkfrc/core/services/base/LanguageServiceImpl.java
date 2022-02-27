package org.pkfrc.core.services.base;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.pkfrc.core.entities.base.Language;
import org.pkfrc.core.persistence.model.SearchCriteria;
import org.pkfrc.core.utilities.enumerations.ETransactionalOperation;
import org.slf4j.Logger;import org.slf4j.LoggerFactory; 
import org.pkfrc.core.persistence.model.ESearchOperationType;
import org.springframework.stereotype.Service;

@Service
public class LanguageServiceImpl extends BaseServiceImpl<Language, Long> implements ILanguageService {

	Logger logger = LoggerFactory.getLogger(LanguageServiceImpl.class);

	@Override
	protected Logger getLogger() {
		return logger;
	}

	@Override
	public ServiceData<Language> findAllActive(List<SearchCriteria> criterias, String lang) throws Exception {
		if (criterias == null) {
			criterias = new ArrayList<>(0);
		}
		criterias.add(new SearchCriteria("active", true, ESearchOperationType.EQUAL));
		return super.findAll(criterias);
	}

	@Override
	protected List<Validation> validateRecord(Language record, ETransactionalOperation operation) {
		return new ArrayList<>(0);
	}

	@Override
	protected List<Validation> validateRecords(Collection<Language> record, ETransactionalOperation operation) {
		return new ArrayList<>(0);
	}

	@Override
	protected Class<Language> getClazz() {
		return Language.class;
	}

}
