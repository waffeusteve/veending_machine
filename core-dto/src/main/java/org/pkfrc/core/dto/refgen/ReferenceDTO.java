package org.pkfrc.core.dto.refgen;

import java.util.HashSet;
import java.util.Set;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.pkfrc.core.entities.refgen.Reference;
import org.pkfrc.core.entities.refgen.ReferenceSegment;

@Data
@NoArgsConstructor
public class ReferenceDTO extends AbstractReferenceDTO<Reference> {

	private static final long serialVersionUID = 1L;

	private Set<ReferenceSegmentDTO> segments = new HashSet<>(0);


	public ReferenceDTO(Reference entity) {
		super(entity);
		for (ReferenceSegment item : entity.getSegments()) {
			segments.add(new ReferenceSegmentDTO(item));
		}
	}

	@Override
	public Reference toEntity(Reference entity) {
		entity = super.toEntity(entity);
		for (ReferenceSegmentDTO item : segments) {
			ReferenceSegment element = new ReferenceSegment();
			element = item.toEntity(element);
			entity.getSegments().add(element);
		}
		return entity;
	}


}
