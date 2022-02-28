package org.pkfrc.core.entities.base;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.pkfrc.core.utilities.enumerations.EWriteMode;


@Entity
@Table(name = "PKF_CR_LANGUAGE")
@SequenceGenerator(name = "PKF_CR_LANGUAGE_SEQ", sequenceName = "PKF_CR_LANGUAGE_SEQ", allocationSize = 1, initialValue = 1)
public class Language extends AbstractParamEntity<Long> implements  Comparable<Language> {

	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "id", unique = true, updatable = false)
	@GeneratedValue(generator = "PKF_CR_LANGUAGE_SEQ", strategy = GenerationType.SEQUENCE)
	Long id;

	@Column(name = "WRITE_MODE")
	@Enumerated(EnumType.ORDINAL)
	private EWriteMode mode = EWriteMode.ltr;

	// en_US, en_UK, en_CA etc separated by |
	@Column(name = "LOCALE")
	private String locale;

	@Column(name = "FLAG")
	private String flag;

	@Column(name = "DEFAULT_LANG")
	private boolean prefered;

	@Column(name = "LANG_ACTIVE")
	private boolean active;

	public Language() {
	}

	@Override
	public int compareTo(Language o) {
		return o.getCode().compareTo(o.getCode());
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public EWriteMode getMode() {
		return mode;
	}

	public void setMode(EWriteMode mode) {
		this.mode = mode;
	}

	public String getLocale() {
		return locale;
	}

	public void setLocale(String locale) {
		this.locale = locale;
	}

	public String getFlag() {
		return flag;
	}

	public void setFlag(String flag) {
		this.flag = flag;
	}

	public boolean isPrefered() {
		return prefered;
	}

	public void setPrefered(boolean prefered) {
		this.prefered = prefered;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}
	
	
	
}
