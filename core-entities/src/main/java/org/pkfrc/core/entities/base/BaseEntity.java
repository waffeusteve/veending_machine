package org.pkfrc.core.entities.base;

import java.io.Serializable;

public interface BaseEntity<ID extends Serializable> extends Serializable {

	ID getId();

	void setId(ID id);

	Long getVersion();

	void setVersion(Long version);

	public default int entityHashCode() {
		final int prime = 31;
		int result = this.hashCode();
		result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
		return result;
	}

	@SuppressWarnings("unchecked")
	public default boolean entityEequals(Object obj) {
		if (this == obj)
			return true;
		if (getClass() != obj.getClass())
			return false;
		BaseEntity<ID> other = (BaseEntity<ID>) obj;
		if (getId() == null) {
			if (other.getId() != null)
				return false;
		} else if (!getId().equals(other.getId()))
			return false;
		return true;
	}
}
