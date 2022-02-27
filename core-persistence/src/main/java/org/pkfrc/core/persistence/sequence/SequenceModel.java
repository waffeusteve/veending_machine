package org.pkfrc.core.persistence.sequence;

import javax.persistence.SequenceGenerator;

public abstract class SequenceModel {

	static final Long START = 1L;
	static final Long INCREMENT = 1L;
	static final Long FINISH = 999999999999999999L;

	public abstract String create(String sequence);

	public abstract String delete(String sequence);

	public abstract String nextValue(String sequence);

	public abstract String exists(String sequence);

	public abstract String create(SequenceGenerator sequence);

	public String delete(SequenceGenerator sequence) {
		return delete(sequence.name());
	}

	public String nextValue(SequenceGenerator sequence) {
		return nextValue(sequence.name());
	}

	public String exists(SequenceGenerator sequence) {
		return nextValue(sequence.name());
	}

}
