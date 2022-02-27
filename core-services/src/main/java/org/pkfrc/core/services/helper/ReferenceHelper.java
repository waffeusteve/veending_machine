package org.pkfrc.core.services.helper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.hibernate.criterion.Restrictions;
import org.pkfrc.core.entities.refgen.AbstractReference;
import org.pkfrc.core.entities.refgen.Reference;
import org.pkfrc.core.entities.refgen.ReferenceSegment;
import org.pkfrc.core.persistence.base.CoreDAO;
import org.pkfrc.core.persistence.sequence.SequenceHelper;
import org.pkfrc.core.persistence.tools.RestrictionsContainer;
import org.pkfrc.core.persistence.vendors.Vendor;
import org.pkfrc.core.utilities.annontations.ReferencePart;
import org.pkfrc.core.utilities.annontations.References;
import org.pkfrc.core.utilities.annontations.Segment;
import org.pkfrc.core.utilities.enumerations.EWriteMode;
import org.pkfrc.core.utilities.exceptions.SmartTechException;
import org.pkfrc.core.utilities.helper.DateHelper;
import org.pkfrc.core.utilities.helper.ScannerHelper;
import org.pkfrc.core.utilities.helper.StringHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ReferenceHelper {

	private static final Character DEFAULT_PAD = '0';
	protected static final Logger logger = LoggerFactory.getLogger(ReferenceHelper.class);

	@Autowired
	SequenceHelper seqHelper;

	private CoreDAO dao;

	public void setParams(Vendor vendor, CoreDAO dao) {
		seqHelper.setParams(vendor, dao);
		this.dao = dao;
	}

	public <E> E generate(E entity) throws Exception {
		RestrictionsContainer restriction = RestrictionsContainer.getInstance()
				.add(Restrictions.eq("clazz", entity.getClass().getName()));

		List<Reference> numeroteurs = dao.filter(Reference.class, null, restriction, null, null, 0, -1);
		List<ReferenceSegment> segments;
		StringBuffer buffer;

		for (Reference numeroteur : numeroteurs) {
			segments = new ArrayList<>(numeroteur.getSegments());
			Collections.sort(segments);
			buffer = new StringBuffer();
			// Je parcours les éléments du numéroteur
			for (ReferenceSegment segment : segments) {
				buffer.append(getSegmentValue(segment, entity));
			}
			String generated = format(buffer.toString(), numeroteur);
			logger.info("Reference generated : " + generated);

			ScannerHelper.setFieldValue(entity, numeroteur.getField(), generated);
		}

		return entity;
	}


	private String format(String value, AbstractReference ref) {
		if (value.length() > ref.getLength()) {
			value = trim(ref.getTrimMode(), ref.getLength(), value);
		} else if (value.length() < ref.getLength()) {
			value = pad(ref.getPadMode(), ref.getLength(), ref.getPadValue(), value);
		}
		return value;
	}

	private String trim(EWriteMode mode, Integer length, String value) {
		if (mode == null || mode.equals(EWriteMode.ltr)) {
			value = StringHelper.ltrim(value, length);
		} else {
			value = StringHelper.rtrim(value, length);
		}
		return value;
	}

	private String pad(EWriteMode mode, Integer length, Character pad, String value) {
		if (pad == null) {
			pad = DEFAULT_PAD;
		}
		if (mode == null || mode.equals(EWriteMode.ltr)) {
			value = StringHelper.lpad(value, pad, length);
		} else {
			value = StringHelper.rpad(value, pad, length);
		}
		return value;
	}

	private <E> String getSegmentValue(ReferenceSegment segment, E entity) throws Exception {
		String value = null;
		switch (segment.getType()) {
		case Date:
			Object result = ScannerHelper.readFieldValue(entity, segment.getValue());
			if (!(result instanceof Date)) {
				throw new SmartTechException("Field : " + segment.getValue() + " is not a date type");
			}
			Date date = (Date) result;
			value = DateHelper.format(date, segment.getPattern());
			break;
		case CurrentDate:
			value = DateHelper.format(new Date(), segment.getPattern());
			break;
		case Field:
			value = (String) ScannerHelper.readFieldValue(entity, segment.getValue());
			break;
		case Constant:
			value = segment.getValue();
			break;
		case Sequence:
			value = getSequenceValue(segment.getValue());
			break;
		case Method:
			value = (String) ScannerHelper.executeMethod(entity, segment.getValue(), null, null);
			break;
		default:
			value = null;
		}

		return format(value, segment);
	}

	private String getSequenceValue(String sequence) throws Exception {
		return String.valueOf(seqHelper.getNextFromSequence(sequence));
	}

	public static Set<Reference> readClass(String entityClassName) {
		logger.info("Lecture de la classe : " + entityClassName);
		Class<?> entityClass = ScannerHelper.readClassType(entityClassName);
		References references = entityClass.getAnnotation(References.class);
		ReferencePart[] information;
		if (references == null) {
			ReferencePart numerotable = entityClass.getAnnotation(ReferencePart.class);
			information = new ReferencePart[] { numerotable };
		} else {
			information = references.value();
		}
		Set<Reference> result = new HashSet<>();
		for (ReferencePart numerotable : information) {
			result.add(readNumeroteur(numerotable, entityClassName));
		}
		return result;
	}

	private static Reference readNumeroteur(ReferencePart numerotable, String clazz) {
		logger.debug("Création du ClassModel");
		Reference reference = new Reference();
		reference.setClazz(clazz);
		reference.setCode(numerotable.code());
		reference.setField(numerotable.field());
		reference.setLabel(numerotable.label());
		reference.setPadMode(numerotable.padMode());
		reference.setPadValue(numerotable.padValue());
		reference.setTrimMode(numerotable.trimMode());
		reference.setLength(numerotable.length());

		reference.setSegments(readElements(numerotable.segments()));

		if (reference.getLength() == 0) {
			int length = reference.getSegments().stream().mapToInt(s -> s.getLength()).sum();
			reference.setLength(length);

		}

		return reference;
	}

	private static Set<ReferenceSegment> readElements(Segment[] segments) {
		Set<ReferenceSegment> result = new HashSet<>(0);
		for (int i = 0; i < segments.length; i++) {
			Segment line = segments[i];
			ReferenceSegment segment = new ReferenceSegment();
			segment.setSequence(i);
			segment.setLabel(line.label());
			segment.setLength(line.length());
			segment.setPadMode(line.padMode());
			segment.setPadValue(line.padValue());
			segment.setPattern(line.pattern());
			segment.setType(line.type());
			segment.setTrimMode(line.trimMode());
			segment.setValue(line.value());
			segment.setSequence(i);
			segment.setType(line.type());

			result.add(segment);
		}
		return result;
	}

}
