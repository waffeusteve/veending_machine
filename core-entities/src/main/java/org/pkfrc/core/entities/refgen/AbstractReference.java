package org.pkfrc.core.entities.refgen;

import lombok.Data;
import org.pkfrc.core.entities.base.AbstractEntity;
import org.pkfrc.core.utilities.enumerations.EWriteMode;

import javax.persistence.Column;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.MappedSuperclass;

@Data
@MappedSuperclass
public abstract class AbstractReference extends AbstractEntity<Long> {

	private static final long serialVersionUID = 1L;

	@Column(name = "REF_LENGTH")
	private Integer length;

	@Column(name = "PAD_MODE")
	@Enumerated(EnumType.ORDINAL)
	private EWriteMode padMode;

	@Column(name = "TRIM_MODE")
	private EWriteMode trimMode;

	@Column(name = "PAD_VALUE")
	private Character padValue;

	@Column(name = "REF_LABEL")
	private String label;


	public AbstractReference() {
	}
}
