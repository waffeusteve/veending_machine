package org.pkfrc.core.entities.base;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.MappedSuperclass;

@Data
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

	@SuppressWarnings("unchecked")
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

}
