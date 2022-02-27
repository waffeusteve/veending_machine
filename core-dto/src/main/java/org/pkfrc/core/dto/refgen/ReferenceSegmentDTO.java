package org.pkfrc.core.dto.refgen;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.pkfrc.core.entities.refgen.ReferenceSegment;
import org.pkfrc.core.utilities.enumerations.ESegmentType;

@Data
@NoArgsConstructor
public class ReferenceSegmentDTO extends AbstractReferenceDTO<ReferenceSegment> {

	private static final long serialVersionUID = 1L;

	private ESegmentType type;
	private String value;
	// uniquement pour date
	private String pattern;
	private Integer sequence;


	public ReferenceSegmentDTO(ReferenceSegment entity) {
		super(entity);
		this.type = entity.getType();
		this.value = entity.getValue();
		this.pattern = entity.getPattern();
		this.sequence = entity.getSequence();
	}

	@Override
	public ReferenceSegment toEntity(ReferenceSegment entity) {
		entity = super.toEntity(entity);
		entity.setType(type);
		entity.setValue(value);
		entity.setPattern(pattern);
		entity.setSequence(sequence);
		return entity;
	}


}
