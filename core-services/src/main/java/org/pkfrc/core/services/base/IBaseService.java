package org.pkfrc.core.services.base;

import java.io.Serializable;
import java.util.List;
import java.util.Set;

import org.pkfrc.core.entities.base.BaseEntity;
import org.pkfrc.core.entities.security.User;
import org.pkfrc.core.persistence.model.SearchCriteria;
import org.pkfrc.core.persistence.tools.AliasesContainer;
import org.pkfrc.core.persistence.tools.OrderContainer;
import org.pkfrc.core.persistence.tools.RestrictionsContainer;

/**
 * @author Steve Waffeu
 */
public interface IBaseService<E extends BaseEntity<ID>, ID extends Serializable> {
 

	ServiceData<E> create(User user, E record) throws Exception;

	ServiceData<E> create(User user, E record, boolean validate) throws Exception;

	ServiceData<E> createAll(User user, Set<E> records) throws Exception;

	ServiceData<E> createAll(User user, Set<E> records, boolean validate) throws Exception;

	ServiceData<E> update(User user, E record) throws Exception;

	ServiceData<E> update(User user, E record, boolean validate) throws Exception;

	ServiceData<E> updateAll(User user, Set<E> records) throws Exception;

	ServiceData<E> updateAll(User user, Set<E> records, boolean validate) throws Exception;

	ServiceData<E> delete(User user, ID recordId) throws Exception;

	ServiceData<E> delete(User user, ID recordId, boolean validate) throws Exception;

	ServiceData<E> deleteAll(User user, Set<ID> recordIds) throws Exception;
	
	ServiceData<E> deleteAll(User user, Set<ID> recordIds, boolean validate) throws Exception;

	ServiceData<E> findById(ID recordId);

	ServiceData<E> findAll() throws Exception;

	Integer count() throws Exception;

	ServiceData<E> findAll(List<SearchCriteria> criterias) throws Exception;

	ServiceData<E> findPage(List<SearchCriteria> criterias, int page, int size);

	ServiceData<E> findOne(List<SearchCriteria> criterias) throws Exception;

	ServiceData<E> findOneByAttribute(String attributeName, Object value) throws Exception;

	ServiceData<E> findAllByAttribute(String attributeName, Object value) throws Exception;

    ServiceData<E> findByCode(Object value) throws Exception;

    ServiceData<E> findOneBy2Attributes(String attributeName1, Object value1, String attributeName2, Object value2)  throws Exception;

	Integer count(List<SearchCriteria> criterias, int first, int size) throws Exception;

	Integer count(AliasesContainer alias, RestrictionsContainer restrictions, OrderContainer order, int first, int size)
			throws Exception;
}
