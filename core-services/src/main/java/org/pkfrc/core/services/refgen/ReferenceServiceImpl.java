package org.pkfrc.core.services.refgen;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.pkfrc.core.services.base.BaseServiceImpl;
import org.pkfrc.core.services.base.Validation;
import org.slf4j.Logger;import org.slf4j.LoggerFactory;
import org.pkfrc.core.entities.refgen.AbstractReference;
import org.pkfrc.core.entities.refgen.Reference;
import org.pkfrc.core.entities.refgen.ReferenceSegment; 
import org.pkfrc.core.utilities.enumerations.ETransactionalOperation;
import org.pkfrc.core.utilities.helper.StringHelper;
import org.springframework.stereotype.Service;

@Service
public class ReferenceServiceImpl extends BaseServiceImpl<Reference, Long> implements IReferenceService {

	Logger logger = LoggerFactory.getLogger(ReferenceServiceImpl.class);

	@Override
	protected Logger getLogger() {
		return logger;
	}

	@Override
	protected List<Validation> validateRecord(Reference record, ETransactionalOperation operation) {

		List<Validation> result = validate(record, operation, false);
		if (result != null) {
			return result;
		}
		int length = 0;
		for (ReferenceSegment segement : record.getSegments()) {
			result = validate(segement, operation);
			if (result != null && !result.isEmpty()) {
				return result;
			}
			length += segement.getLength();
		}
		if (length > record.getLength()) {

		}
		return null;
	}

	@Override
	protected List<Validation> validateRecords(Collection<Reference> record, ETransactionalOperation operation) {
		// TODO Auto-generated method stub
		return new ArrayList<>(0);
	}

	@Override
	protected Class<Reference> getClazz() {
		return Reference.class;
	}

	private List<Validation> validate(ReferenceSegment segment, ETransactionalOperation operation) {
		List<Validation> result = validate(segment, operation, true);
		if (result != null) {
			return result;
		}
		if (segment.getType() == null) {

		}
		if (StringHelper.isEmpty(segment.getValue())) {

		}
		if (segment.getSequence() == null) {

		}

		return result;
	}

	private <E extends AbstractReference> List<Validation> validate(E entity, ETransactionalOperation operation,
			boolean segment) {
		List<Validation> result = new ArrayList<>(0);
		// String message = null;
		//
		// if (entity.getLength() <= 0) {
		// message = "Segment : " + entity.getCode() + " must be greeater than > 0";
		// }
		return result;
	}

}
