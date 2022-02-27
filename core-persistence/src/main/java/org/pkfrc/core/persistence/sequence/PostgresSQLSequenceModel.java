package org.pkfrc.core.persistence.sequence;

import javax.persistence.SequenceGenerator;

public class PostgresSQLSequenceModel extends SequenceModel {

	private String createSeq(String sequence, Long start, Long finish, Long increment) {
		return "CREATE SEQUENCE " + sequence + " START WITH " + start + " MINVALUE " + start + " INCREMENT BY "
				+ increment + " MAXVALUE " + finish + " NOCYCLE NOCACHE NOORDER";
	}

	@Override
	public String create(String sequence) {
		return createSeq(sequence, START, FINISH, INCREMENT);
	}

	@Override
	public String delete(String sequence) {
		return "DROP SEQUENCE " + sequence.toLowerCase();
	}

	@Override
	public String nextValue(String sequence) {
		return "SELECT NEXTVAL('" + sequence.toLowerCase() + "')";
	}

	@Override
	public String exists(String sequence) {
		return "SELECT COUNT(*) FROM pg_class where relname = '" + sequence.toLowerCase() + "'";
	}

	@Override
	public String create(SequenceGenerator sequence) {
		return createSeq(sequence.name(), Long.valueOf(sequence.initialValue()), FINISH,
				Long.valueOf(sequence.allocationSize()));
	}

}
