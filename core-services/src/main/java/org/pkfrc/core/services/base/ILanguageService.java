package org.pkfrc.core.services.base;


import org.pkfrc.core.entities.base.Language;
import org.pkfrc.core.persistence.model.SearchCriteria;

import java.util.List;

public interface ILanguageService extends IBaseService<Language, Long> {

    ServiceData<Language> findAllActive(List<SearchCriteria> criterias, String lang) throws Exception;
}
