package org.pkfrc.core.entities.refgen;

import lombok.Data;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/**
 * Entity representing a reference. Used for generating references (or codes) using a sequence of segments
 */
@Data
@Entity
@Table(name = "PKF_CR_REFERENCE")
@SequenceGenerator(name = "PKF_CR_REFERENCE_SEQ", sequenceName = "PKF_CR_REFERENCE_SEQ", allocationSize = 1, initialValue = 1)
public class Reference extends AbstractReference {

	private static final long serialVersionUID = 1L;


	@Id
	@Column(name = "id", unique = true, updatable = false)
	@GeneratedValue(generator = "PKF_CR_REFERENCE_SEQ", strategy = GenerationType.SEQUENCE)
	private Long id;

	@Column(name = "REF_CODE")
	private String code;

	@Column(name = "REF_CLAZZ")
	private String clazz;

	@Column(name = "REF_FIELD")
	private String field;

	@OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
	@JoinColumn(name = "REF_ID", foreignKey = @ForeignKey(name = "FK_SEGMENT_REFERENCE"))
	private Set<ReferenceSegment> segments = new HashSet<>(0);


	public Reference() {
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((getClazz() == null) ? 0 : getClazz().hashCode());
		result = prime * result + ((getCode() == null) ? 0 : getCode().hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (!getClass().isAssignableFrom(obj.getClass()))
			return false;
		Reference other = (Reference) obj;
		if (getClazz() == null) {
			if (other.getClazz() != null)
				return false;
		} else if (!getClazz().equals(other.getClazz()))
			return false;
		if (getCode() == null) {
			if (other.getCode() != null)
				return false;
		} else if (!getCode().equals(other.getCode()))
			return false;
		return true;
	}

}
