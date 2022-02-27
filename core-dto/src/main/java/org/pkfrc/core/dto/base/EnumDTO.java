package org.pkfrc.core.dto.base;

import lombok.Data;

import java.io.Serializable;

@Data
public class EnumDTO implements Serializable, Comparable<EnumDTO> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String code;
	private Integer order;
	private String label;

	public EnumDTO() {
		super();
	}

	public EnumDTO(String code, Integer order, String label) {
		super();
		this.code = code;
		this.order = order;
		this.label = label;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((code == null) ? 0 : code.hashCode());
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
		EnumDTO other = (EnumDTO) obj;
		if (code == null) {
			if (other.code != null)
				return false;
		} else if (!code.equals(other.code))
			return false;
		return true;
	}

	@Override
	public int compareTo(EnumDTO o) {
		return order.compareTo(o.order);
	}

}
