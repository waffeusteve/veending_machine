package org.pkfrc.core.entities.base;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.MappedSuperclass;

@SuppressWarnings("unchecked")
@MappedSuperclass
public abstract class AbstractParamEntity<ID extends Serializable> extends AbstractEntity<ID>
		implements ParamEntity<ID>, Serializable {

	private static final long serialVersionUID = 1L;

	protected String code;

	protected String designation;
	
	protected String userCreate;
	protected String userUpdate;
	protected Date dateLastUpdate;
	protected Date dateCreate;

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((code == null) ? 0 : code.hashCode());
		return result;
	}

	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		AbstractParamEntity<ID> other = (AbstractParamEntity<ID>) obj;
		if (code == null) {
			if (other.code != null)
				return false;
		} else if (!code.equals(other.code))
			return false;
		return true;
	}

	public <E extends AbstractParamEntity<ID>>  int compareTo(E arg0) {
		return code.compareToIgnoreCase(arg0.getCode());
	}


	public String getCode() {
		return code;
	}


	public void setCode(String code) {
		this.code = code;
	}


	public String getDesignation() {
		return designation;
	}


	public void setDesignation(String designation) {
		this.designation = designation;
	}


	public String getUserCreate() {
		return userCreate;
	}


	public void setUserCreate(String userCreate) {
		this.userCreate = userCreate;
	}


	public String getUserUpdate() {
		return userUpdate;
	}


	public void setUserUpdate(String userUpdate) {
		this.userUpdate = userUpdate;
	}


	public Date getDateLastUpdate() {
		return dateLastUpdate;
	}


	public void setDateLastUpdate(Date dateLastUpdate) {
		this.dateLastUpdate = dateLastUpdate;
	}


	public Date getDateCreate() {
		return dateCreate;
	}


	public void setDateCreate(Date dateCreate) {
		this.dateCreate = dateCreate;
	}

	
}
