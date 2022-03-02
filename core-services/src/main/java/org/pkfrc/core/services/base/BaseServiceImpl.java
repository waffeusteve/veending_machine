package org.pkfrc.core.services.base;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

import org.hibernate.envers.Audited;
import org.pkfrc.core.entities.base.BaseEntity;
import org.pkfrc.core.entities.security.User;
import org.pkfrc.core.persistence.base.CoreDAO;
import org.pkfrc.core.persistence.base.SearchCriteriaManager;
import org.pkfrc.core.persistence.model.ESearchOperationType;
import org.pkfrc.core.persistence.model.SearchCriteria;
import org.pkfrc.core.persistence.tools.AliasesContainer;
import org.pkfrc.core.persistence.tools.OrderContainer;
import org.pkfrc.core.persistence.tools.RestrictionsContainer;
import org.pkfrc.core.persistence.vendors.Vendor;
import org.pkfrc.core.services.enums.EServiceDataType;
import org.pkfrc.core.utilities.enumerations.ETransactionalOperation;
import org.pkfrc.core.utilities.exceptions.SmartTechException;
import org.pkfrc.core.utilities.helper.StringHelper;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
/**
 * @author Steve Waffeu
 */
public abstract class BaseServiceImpl<E extends BaseEntity<ID>, ID extends Serializable> implements IBaseService<E, ID> {

	public static String DEFAULT_LANG = "en";
	protected static int DEFAULT_FETCH_SIZE = 10;
	protected static final String VALIDATION_FAILED = "Validation.failed";
	protected static final String CREATE_SUCCESSFUL ="Create.successful";
	protected static final String UPDATE_SUCCESSFUL ="Update.successful";
	protected static final String SYSTEM_ERROR ="System.error";
	protected static final String RECORD_NOT_FOUND= "Record.Not.Found";
    protected static final String DELETE_SUCCES=  "Delete.success";
    protected static final String DATA_INTEGRITY_VIOLATION_EXCEPTION=  "DataIntegrityViolationException";
    protected static final String OBJECT_OPTIMISTIC_LOCKING_FAILURE_EXCEPTION = "ObjectOptimisticLockingFailureException";
    protected static final String UPDATE_EXCEPTION= "Update.Exception";
	protected static String MULTIPLE_VALUES_FOUND = "Multiple.Values.Found";


	@Autowired
	protected CoreDAO dao;


	@PersistenceContext
	protected EntityManager manager;


    @Autowired
    protected Vendor vendor;



	protected abstract Logger getLogger();

	protected abstract Class<E> getClazz();

	protected abstract List<Validation> validateRecord(E record, ETransactionalOperation operation);

	protected abstract List<Validation> validateRecords(Collection<E> record, ETransactionalOperation operation);

	public BaseServiceImpl() {
	}

	protected ServiceData<E> createServiceData() {
		return createServiceData(null);
	}

	protected ServiceData<E> createServiceData(String lang) {
		return new ServiceData<E>();
	}

	@PostConstruct
	private void init() {
		dao.setManager(getManager());
//        refHelper.setParams(getVendor(), dao);
	}


    protected Vendor getVendor() {
        return vendor;
    }

	protected EntityManager getManager() {
		return manager;
	}

	protected void throwException(String message) {
			getLogger().error(message);
			throw new SmartTechException(message);
	}

	protected void throwException(String message, Exception e) {
		if (e != null) {
			String errorMessage = "";
			String exceptionMessage = "";
			if (e.getMessage() != null) {
				exceptionMessage = e.getMessage();
			}
			errorMessage = exceptionMessage + " Exception Type : " + e.getClass().getSimpleName();
			getLogger().error(message + " - " + errorMessage);
			throw new SmartTechException(message, e);

		} else {
			throwException(message);
		}
	}

	@Override
	public ServiceData<E> create(User user, E record) throws Exception {
		return create(user, record, true);
	}

	@Transactional
	@Override
	public ServiceData<E> create(User user, E record, boolean validate) throws Exception {
		ServiceData<E> serviceResult = createServiceData();
		try {
			serviceResult = save(user, record, ETransactionalOperation.Create, validate);
		} catch (Exception e) {
			throwException("Create.Failed", e);
		}
		return serviceResult;
	}

	@Override
	public ServiceData<E> createAll(User user, Set<E> records) throws Exception {
		return createAll(user, records, true);
	}

	@Transactional
	@Override
	public ServiceData<E> createAll(User user, Set<E> records, boolean validate) throws Exception {
		ServiceData<E> sData = validateList(records, ETransactionalOperation.Create, validate);
		Iterator<E> iter = records.iterator();
		boolean found = sData.isValid();
		Set<E> updated = new HashSet<>(0);
		while (iter.hasNext() && found) {
			E record = iter.next();
			try {
				sData = save(user, record, ETransactionalOperation.Create, validate);
			} catch (ObjectOptimisticLockingFailureException stexc) {
				stexc.printStackTrace();
				throwException(OBJECT_OPTIMISTIC_LOCKING_FAILURE_EXCEPTION, stexc);
			} catch (Exception e) {
				throwException("Create.All.Failed", e);
			}
			updated.add(sData.getRecord());
			found = sData.isValid();
		}
		sData.setRecords(updated);
		return sData;
	}

	protected ServiceData<E> validateList(Set<E> records, ETransactionalOperation operation, boolean validate) {
		ServiceData<E> sResult = createServiceData();
		if (validate) {
			List<Validation> validations = validateRecords(records, operation);
			if (validations == null) {
				validations = new ArrayList<>(0);
			}
			if (!validations.isEmpty()) {
				sResult = getInvalidResult(validations);
				return sResult;
			}
		}
		sResult.setValid(true);
		return sResult;
	}

	protected ServiceData<E> getInvalidResult(Collection<Validation> validations) {
		ServiceData<E> sData = createServiceData();
		sData.setValid(false);
		sData.getValidations().addAll(validations);
//		sData.setMessage(VALIDATION_FAILED);
		sData.setMessage(validations.iterator().next().getMessage());
		return sData;
	}

	
	protected ServiceData<E> save(User user, E record, ETransactionalOperation operation, boolean validate)
			throws Exception {
		ServiceData<E> sData = createServiceData();
		if (validate) {
			List<Validation> Validations = validateRecord(record, operation);
			if (Validations == null) {
				Validations = new ArrayList<>(0);
			}
			if (!Validations.isEmpty()) {
				sData = getInvalidResult(Validations);
				return sData;
			}
		}
		String message;
		if (record instanceof Audited) {
//			record = (E) ((Audited) record).setAudit(user, (Audited) record, operation);
		}
		if (operation.equals(ETransactionalOperation.Create)) {
			message = CREATE_SUCCESSFUL;
//            record = refHelper.generate(record);
            record = dao.saveAndFlush(record);
		} else {
			message = UPDATE_SUCCESSFUL;
            record = dao.updateAndFlush(record);
		}
		sData.setRecord(record);
		sData.setType(EServiceDataType.Record);
		sData.setValid(true);
		sData.setMessage(message);
		return sData;
	}

	@Override
	public ServiceData<E> update(User user, E record) throws Exception {
		return update(user, record, true);
	}

	@Transactional
	@Override
	public ServiceData<E> update(User user, E record, boolean validate) throws Exception {

		ServiceData<E> sData = createServiceData();
        E existingRecord = dao.findOne(getClazz(),record.getId());
        if (existingRecord == null) {
            sData.setValid(false);
            sData.setMessage(RECORD_NOT_FOUND);
            return sData;
        }
		try {
			sData = save(user, record, ETransactionalOperation.Update, validate);
		} catch (ObjectOptimisticLockingFailureException stexc) {
			stexc.printStackTrace();
			throwException(OBJECT_OPTIMISTIC_LOCKING_FAILURE_EXCEPTION, stexc);
		} catch (Exception e) {
			throwException(UPDATE_EXCEPTION, e);
		}
		return sData;
	}

	@Override
	public ServiceData<E> updateAll(User user, Set<E> records) throws Exception {
		return updateAll(user, records, true);
	}

	@Transactional
	@Override
	public ServiceData<E> updateAll(User user, Set<E> records, boolean validate) throws Exception {
		ServiceData<E> sData = validateList(records, ETransactionalOperation.Create, validate);
		Iterator<E> iter = records.iterator();
		boolean found = sData.isValid();
		Set<E> updated = new HashSet<>(0);
		while (iter.hasNext() && found) {
            sData = update(user, iter.next(), validate);
			updated.add(sData.getRecord());
			found = sData.isValid();
		}
		sData.setRecords(updated);
		return sData;
	}

	@Override
	public ServiceData<E> delete(User user, ID recordId) throws Exception {
		return delete(user, recordId, true);
	}

	@Transactional
	@Override
	public ServiceData<E> delete(User user, ID recordId, boolean validate) throws Exception {
		ServiceData<E> sData = createServiceData();

		ServiceData<E> findResult = findById(recordId);

		if (findResult.getRecord() == null) {
			sData.setValid(false);
			sData.setMessage(RECORD_NOT_FOUND);
			return sData;
		}

		try {
            dao.deleteAndFlush(findResult.getRecord());
			sData.setRecord(findResult.getRecord());
			sData.setValid(true);
			sData.setType(EServiceDataType.Record);

			sData.setMessage(DELETE_SUCCES);
		} catch (DataIntegrityViolationException de) {
			throwException(DATA_INTEGRITY_VIOLATION_EXCEPTION, de);
		} catch (Exception e) {
			throwException("Delete.Exception", e);
		}

		return sData;
	}

	@Override
	public ServiceData<E> deleteAll(User user, Set<ID> recordIds) throws Exception {
		return deleteAll(user, recordIds, false);
	}

	@Transactional
	@Override
	public ServiceData<E> deleteAll(User user, Set<ID> recordIds, boolean validate) throws Exception {
		ServiceData<E> sData = createServiceData();
		Iterator<ID> iter = recordIds.iterator();
		boolean found = true;
		while (iter.hasNext() && found) {
			ID recordId = iter.next();
			ServiceData<E> findResult = findById(recordId);

			if (findResult.getRecord() == null) {
				sData.setValid(false);
				sData.setMessage(RECORD_NOT_FOUND);
				return sData;
			}

			try {

                dao.delete(findResult.getRecord());
				sData.setValid(true);
				sData.setRecord(null);
				sData.setType(EServiceDataType.Record);
				sData.setMessage(DELETE_SUCCES);
			} catch (DataIntegrityViolationException de) {
				throwException(DATA_INTEGRITY_VIOLATION_EXCEPTION, de);
			} catch (Exception e) {
				throwException(SYSTEM_ERROR, e);
			}
			found = sData.isValid();
		}
		// In case of false, I need to throw an exception to rollback previous
		// delete
		return sData;
	}

	@Override
	public ServiceData<E> findById(ID recordId) {
		ServiceData<E> sData = createServiceData();
		try {
            E record = dao.findOne(getClazz(), recordId);
            sData.setRecord(record);
            sData.setType(EServiceDataType.Record);
            sData = setServiceData(sData);
		} catch (Exception e) {
			throwException("FindEntityById.Exception", e);
		}
		return sData;
	}

	@Override
	public ServiceData<E> findAll() throws Exception {
		return findAll(new ArrayList<>(0));
	}
	@Override
	public ServiceData<E> findAll(List<SearchCriteria> criterias) throws Exception {
		ServiceData<E> result = createServiceData();
		try {
            List<SearchCriteria> criteriasWithAlias = SearchCriteria.buildAliasFromSearchCriterias(criterias);
			RestrictionsContainer restrictions = SearchCriteriaManager.getRestrictions(getClazz(), criteriasWithAlias, true);
			AliasesContainer alias = SearchCriteriaManager.getAlias(criteriasWithAlias);
			OrderContainer order = SearchCriteriaManager.getOrder(criteriasWithAlias);

			result.getRecords().addAll(dao.filter(getClazz(), alias, restrictions, order, null, 0, -1));
			result.setTotalElements(result.getRecords().size());
			result.setTotalPages(0);
			result.setType(EServiceDataType.Set);

			result = this.setServiceData(result);

		} catch (Exception e) {
			throwException(SYSTEM_ERROR, e);
		}
		return result;
	}


	public ServiceData<E> getFindPage(List<SearchCriteria> criterias, int page, int size) {
		ServiceData<E> result = createServiceData();
		try {
            List<SearchCriteria> criteriasWithAlias = SearchCriteria.buildAliasFromSearchCriterias(criterias);
			RestrictionsContainer restrictions = SearchCriteriaManager.getRestrictions(getClazz(), criteriasWithAlias);
			AliasesContainer alias = SearchCriteriaManager.getAlias(criteriasWithAlias);
			OrderContainer order = SearchCriteriaManager.getOrder(criteriasWithAlias);
			result.getRecords()
					.addAll(dao.filter(getClazz(), alias, restrictions, order, null, getFirst(page, size), size));
			result.setTotalElements(count(alias, restrictions, order, 0, -1));

			// result.setTotalElements(result.getEntities().size());
			result.setTotalPages(nbPages(size, result.getTotalElements()));
			result.setType(EServiceDataType.Set);

			result = this.setServiceData(result);

		} catch (Exception e) {
			e.printStackTrace();
			throwException(SYSTEM_ERROR, e);
		}
		return result;
	}

	@Override
	public ServiceData<E> findPage(List<SearchCriteria> criterias, int page, int size) {
		ServiceData<E> data = createServiceData();
		ServiceData<E> result = createServiceData();
		if (criterias.isEmpty()|| criterias.size()==0){
			return getFindPage(criterias,page,size);
		}
		boolean defaul= false;
		for(SearchCriteria cret: criterias){
			if (cret.isApplyAnd()){	
				defaul = true;
				break;				
			}
		}
		if (defaul) {
			return getFindPage(criterias,page,size);	
		}
		
		boolean filter=false;
		for (SearchCriteria cret: criterias){
		    if(cret.getValue()!=null){
		        filter = true;
		        break;
            }
		}
		if (!filter){
            return getFindPage(criterias,page,size);
        }
        for (SearchCriteria cret: criterias){
            List<SearchCriteria> criteria = new ArrayList<>();
            criteria.add(cret);
            ServiceData<E> re = getFindPage(criteria, page, size);
            data.getRecords().addAll(re.getRecords());
            data.setMessage(re.getMessage());
            data.setValid(re.isValid());
        }
		result.setType(EServiceDataType.Page);
		result.setTotalElements(data.getRecords().size());
		List<E> stringsList = new ArrayList<>(data.getRecords());
		if (stringsList.size()<=size){
			result.getRecords().addAll(stringsList);
		}else {
			for (int i=page; i<size ; i++){
				result.getRecords().add(stringsList.get(i));
			}
		}
		try {
			result = this.setServiceData(result);
		} catch (Exception e) {
			e.printStackTrace();
			throwException(SYSTEM_ERROR, e);
		}
		return result;
	}




    @Override
    public Integer count() throws Exception {
        return dao.countObject(getClazz());
    }

	
	@Override
	public Integer count(List<SearchCriteria> criterias, int first, int size) throws Exception {
        List<SearchCriteria> criteriasWithAlias = SearchCriteria.buildAliasFromSearchCriterias(criterias);
		RestrictionsContainer restrictions = SearchCriteriaManager.getRestrictions(getClazz(), criteriasWithAlias);
		AliasesContainer alias = SearchCriteriaManager.getAlias(criteriasWithAlias);
		return count(alias, restrictions, null, first, size);
	}

	
	@Override
	public Integer count(AliasesContainer alias, RestrictionsContainer restrictions, OrderContainer order, int first,
						 int size) throws Exception {
		return dao.countObject(getClazz(), alias, restrictions, null, null, first, size);
	}

    protected Integer nbPages(int size, long count) {
        if (size == 0) {
            return 0;
        }
        int pages = (int) (count / size);
        if (count % size != 0) {
            pages++;
        }
        return pages;
    }


    protected Integer getFirst(int page, int size) {
        return page * size;
    }

    public static boolean stringEmpty(String text){
        return text == null || text.isEmpty();
    }

    protected void addValidationIfTextEmpty(Collection<Validation> validations, String text, String message){
        if(StringHelper.isEmpty(text)){
            validations.add(new Validation(message));
        }
    }
    protected void addValidationIfObjectIsNull(Collection<Validation> validations, Object object, String message){
        if(object == null){
            validations.add(new Validation(message));
        }
    }

    protected String getNoRecordsFoundMessage() {
        return RECORD_NOT_FOUND;
    }

    protected ServiceData<E> setServiceData(ServiceData<E> sData) throws Exception {
        if (sData.getType().equals(EServiceDataType.Set) || sData.getType().equals(EServiceDataType.Page)) {
            Set<E> records = sData.getRecords();
            if (records == null || records.isEmpty()) {
                sData.setMessage(getNoRecordsFoundMessage());
            } else {
                sData.setMessage("RecordsFound");
                sData.setRecords(records);
            }
            sData.setValid(true);
        } else {
            if (sData.getRecord() == null) {
                sData.setMessage(getNoRecordsFoundMessage());
            } else {
                sData.setMessage("RecordFound");
                sData.setValid(true);
            }
        }
        return sData;
    }

    protected Pageable buildPageable(int page, int size, String order, String... sortBy){
        Pageable pageable;
        if(sortBy.length>0){
            if(!stringEmpty(order) && order.trim().toLowerCase().contentEquals("desc") ){
                pageable = PageRequest.of(page, size, Sort.by(sortBy).descending());
            }else{
                pageable = PageRequest.of(page, size, Sort.by(sortBy).ascending());
            }
        }else{
            pageable  = PageRequest.of(page, size);
        }
        return pageable;
    }

    protected ServiceData<E> setServiceData(ServiceData<E> sData, Page<E> page) throws Exception {
        sData.getRecords().addAll(page.getContent());
        sData.setType(EServiceDataType.Page);
        sData.setTotalPages(page.getTotalPages());
        sData.setTotalElements(page.getTotalElements());
        return setServiceData(sData);
    }
    protected ServiceData<E> setServiceData(ServiceData<E> sData, Collection<E> records) throws Exception {
        sData.getRecords().addAll(records);
        sData.setType(EServiceDataType.Set);
        sData.setTotalElements(records.size());
        sData.setTotalPages(1);
        return setServiceData(sData);
    }

    protected ServiceData<E> setServiceData(ServiceData<E> sData, E record) throws Exception {
        sData.setRecord(record);
        sData.setType(EServiceDataType.Record);
        return setServiceData(sData);
    }

    protected ServiceData<E> setServiceData(ServiceData<E> sData, Optional<E> record) throws Exception {
        if(record.isPresent()){
            sData.setRecord(record.get());
        }
        sData.setType(EServiceDataType.Record);
        return setServiceData(sData);
    }

	@Override
	public ServiceData<E> findOne(List<SearchCriteria> criterias) throws Exception {
		ServiceData<E> result = findAll(criterias);
		result.setType(EServiceDataType.Record);
		try {
			if (result.getRecords().size() > 1) {
				result.setMessage(MULTIPLE_VALUES_FOUND);
				result.setValid(false);
				throwException(MULTIPLE_VALUES_FOUND, null);
				return result;
			} else {
				Iterator<E> iterator = result.getRecords().iterator();
				if (iterator.hasNext()) {
					result.setRecord(iterator.next());
				}
				result.getRecords().clear();
				result = setServiceData(result);
			}
		} catch (Exception e) {
			throwException("System.error", e);
		}
		return result;
	}

	@Override
	public ServiceData<E> findOneByAttribute(String attributeName, Object value) throws Exception {
		List<SearchCriteria> criterias = new ArrayList<>();
		criterias.add(new SearchCriteria(attributeName, value, ESearchOperationType.EQUAL));
		return findOne(criterias);
	}

	@Override
	public ServiceData<E> findAllByAttribute(String attributeName, Object value) throws Exception {
		List<SearchCriteria> criterias = new ArrayList<>();
		criterias.add(new SearchCriteria(attributeName, value, ESearchOperationType.EQUAL));
		return findAll(criterias);
	}

    @Override
    public ServiceData<E> findByCode( Object value) throws Exception {
        return findOneByAttribute("code",value);
    }

	@Override
	public ServiceData<E> findOneBy2Attributes(String attributeName1, Object value1, String attributeName2, Object value2)  throws Exception{
		List<SearchCriteria> criterias = new ArrayList<>();
		criterias.add(new SearchCriteria(attributeName1, value1, ESearchOperationType.EQUAL, true));
		criterias.add(new SearchCriteria(attributeName2, value2, ESearchOperationType.EQUAL, true));
		return findOne(criterias);
	}

}
