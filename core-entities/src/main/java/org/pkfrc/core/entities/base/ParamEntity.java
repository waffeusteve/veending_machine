package org.pkfrc.core.entities.base;

import java.io.Serializable;

public interface ParamEntity<ID extends Serializable> extends BaseEntity<ID> {

	String getCode();

	void setCode(String code);

	String getDesignation();

	void setDesignation(String designation);


	public default int paramEntityCompareTo(ParamEntity<ID> arg0) {
		return getCode().compareToIgnoreCase(arg0.getCode());
	}

}
