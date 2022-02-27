package org.pkfrc.core.entities.refgen;

import lombok.Data;
import org.pkfrc.core.utilities.enumerations.ESegmentType;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/**
 * Entity representing a segment in a reference.
 */
@Data
@Entity
@Table(name = "PKF_CR_REF_SEGMENT")
@SequenceGenerator(name = "PKF_CR_REF_SEGMENT_SEQ", sequenceName = "PKF_CR_REF_SEGMENT_SEQ", allocationSize = 1, initialValue = 1)
public class ReferenceSegment extends AbstractReference implements Comparable<ReferenceSegment> {

	private static final long serialVersionUID = 1L;

	public ReferenceSegment() {
	}

	@Id
	@Column(name = "id", unique = true, updatable = false)
	@GeneratedValue(generator = "PKF_CR_REF_SEGMENT_SEQ", strategy = GenerationType.SEQUENCE)
	private Long id;

	@Column(name = "SEG_TYPE")
	@Enumerated(EnumType.ORDINAL)
	private ESegmentType type;

	@Column(name = "SEG_VALUE", length = 64)
	private String value;

	/**
	 * Segment pattern defined only for date
	 */
	@Column(name = "SEG_PATTERN", length = 64)
	private String pattern;

	@Column(name = "SEG_SEQUENCE")
	private Integer sequence;


	public int compareTo(ReferenceSegment arg0) {
		return sequence.compareTo(arg0.getSequence());
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((getSequence() == null) ? 0 : getSequence().hashCode());
		result = prime * result + ((getType() == null) ? 0 : getType().hashCode());
		result = prime * result + ((getValue() == null) ? 0 : getValue().hashCode());
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
		ReferenceSegment other = (ReferenceSegment) obj;
		if (getSequence() == null) {
			if (other.getSequence() != null)
				return false;
		} else if (!getSequence().equals(other.getSequence()))
			return false;
		if (getType() != other.getType())
			return false;
		if (getValue() == null) {
			if (other.getValue() != null)
				return false;
		} else if (!getValue().equals(other.getValue()))
			return false;
		return true;
	}

}
