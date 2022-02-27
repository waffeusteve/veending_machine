package org.pkfrc.core.utilities.model;

public enum EReturnType {

	Entity(0), Set(1), Page(2);

	private Integer returnType;

	private EReturnType(Integer returnType) {
		this.returnType = returnType;
	}

	public Integer getReturnType() {
		return returnType;
	}

}
