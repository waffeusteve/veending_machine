package org.pkfrc.core.utilities.exceptions;

public class SmartTechException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	private static final String DEFAULT_CODE_MESSAGE = "Unknow Error";
	private Object[] params = {};

	public SmartTechException() {
		super(DEFAULT_CODE_MESSAGE);
	}

	public SmartTechException(String message) {
		super(message);
	}

	public SmartTechException(String message, Object[] params) {
		super(message);
		this.params = params;
	}

	public SmartTechException(Throwable ex) {
		super(ex);
	}

	public SmartTechException(String message, Throwable ex) {
		super(message, ex);
	}

	public SmartTechException(String message, Throwable ex, Object[] params) {
		super(message, ex);
		this.params = params;
	}

	public Object[] getParams() {
		return params;
	}

	public void setParams(Object[] params) {
		this.params = params;
	}

}
