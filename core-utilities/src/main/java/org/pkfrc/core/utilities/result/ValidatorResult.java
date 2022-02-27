package org.pkfrc.core.utilities.result;

import java.io.Serializable;

public class ValidatorResult implements Serializable {

	private static final long serialVersionUID = 1L;

	private String clazz;
	private String field;
	private String message;
	private Object[] parameters = new Object[] {};

	public ValidatorResult() {
	}

	public ValidatorResult(Class<?> clazz, String field, String message) {
		this(clazz.getSimpleName(), field, message);
	}

	public ValidatorResult(String clazz, String field, String message) {
		super();
		this.clazz = clazz;
		this.field = field;
		this.message = message;

	}

	public ValidatorResult(Class<?> clazz, String field, String message, Object[] parameters) {
		this(clazz.getSimpleName(), field, message, parameters);
	}

	public ValidatorResult(String clazz, String field, String message, Object[] parameters) {
		this(clazz, field, message);
		this.parameters = parameters;
	}

	public String getClazz() {
		return clazz;
	}

	public void setClazz(String clazz) {
		this.clazz = clazz;
	}

	public String getField() {
		return field;
	}

	public void setField(String field) {
		this.field = field;
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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((clazz == null) ? 0 : clazz.hashCode());
		result = prime * result + ((field == null) ? 0 : field.hashCode());
		result = prime * result + ((message == null) ? 0 : message.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ValidatorResult other = (ValidatorResult) obj;
		if (clazz == null) {
			if (other.clazz != null)
				return false;
		} else if (!clazz.equals(other.clazz))
			return false;
		if (field == null) {
			if (other.field != null)
				return false;
		} else if (!field.equals(other.field))
			return false;
		if (message == null) {
			if (other.message != null)
				return false;
		} else if (!message.equals(other.message))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return message + " : " + parameters;
	}
}
