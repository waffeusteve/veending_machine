package org.pkfrc.core.dto.refgen;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.pkfrc.core.dto.base.AbstractDTO;
import org.pkfrc.core.entities.refgen.AbstractReference;
import org.pkfrc.core.utilities.enumerations.EWriteMode;

@Data
@NoArgsConstructor
public abstract class AbstractReferenceDTO<E extends AbstractReference> extends AbstractDTO<E, Long> {

	private static final long serialVersionUID = 1L;

	private Integer length;
	private EWriteMode padMode;
	private EWriteMode trimMode;
	private Character padValue;


	public AbstractReferenceDTO(E entity) {
		super(entity);
		this.length = entity.getLength();
		this.padMode = entity.getPadMode();
		this.trimMode = entity.getTrimMode();
		this.padValue = entity.getPadValue();
	}

	@Override
	public E toEntity(E entity) {
		entity = super.toEntity(entity);
		entity.setLength(length);
		entity.setPadMode(padMode);
		entity.setTrimMode(trimMode);
		entity.setPadValue(padValue);
		return entity;
	}


}
