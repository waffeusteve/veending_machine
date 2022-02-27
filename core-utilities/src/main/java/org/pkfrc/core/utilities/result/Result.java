package org.pkfrc.core.utilities.result;

import java.io.Serializable;
import java.util.LinkedHashSet;
import java.util.Set;

import org.pkfrc.core.utilities.model.EReturnType;

public class Result<T> implements Serializable {

	private static final long serialVersionUID = 1L;

	private boolean valid;
	private EReturnType type;
	private String message;
	private Object[] parameters;
	private Set<ValidatorResult> validators = new LinkedHashSet<>(0);
	private T entity;
	private Set<T> entities = new LinkedHashSet<T>(0);
	private int totalPages;
	private long totalElements;
	private boolean show;

	public boolean isValid() {
		return valid;
	}

	public void setValid(boolean isValid) {
		this.valid = isValid;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Object[] getParameters() {
		return parameters;
	}

	public void setParameters(Object[] parameters) {
		this.parameters = parameters;
	}

	public Set<ValidatorResult> getValidators() {
		return validators;
	}

	public void setValidators(Set<ValidatorResult> validatorResults) {
		this.validators = validatorResults;
	}

	public T getEntity() {
		return entity;
	}

	public void setEntity(T entity) {
		this.entity = entity;
	}

	public Set<T> getEntities() {
		return entities;
	}

	public void setEntities(Set<T> entities) {
		this.entities = entities;
	}

	public EReturnType getType() {
		return type;
	}

	public void setType(EReturnType type) {
		this.type = type;
	}

	public int getTotalPages() {
		return totalPages;
	}

	public void setTotalPages(int totalPages) {
		this.totalPages = totalPages;
	}

	public long getTotalElements() {
		return totalElements;
	}

	public void setTotalElements(long totalElements) {
		this.totalElements = totalElements;
	}

	public boolean isShow() {
		return show;
	}

	public void setShow(boolean show) {
		this.show = show;
	}

	public <E> Result<T> convert(Result<E> buffer) {
		Result<T> result = new Result<T>();
		result.setValid(buffer.isValid());
		result.setType(buffer.getType());
		result.setMessage(buffer.getMessage());
		result.setValidators(buffer.getValidators());
		result.setShow(buffer.isShow());
		return result;
	}

	public boolean isNotEmpty() {
		return entities != null && !entities.isEmpty();
	}
	public boolean isEmpty() {
		return !isNotEmpty();
	}

	@Override
	public String toString() {
		return "valid = " + valid + ", message=" + message + ", validations [" + validators + "]";
	}

}
