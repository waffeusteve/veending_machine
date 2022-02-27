package org.pkfrc.core.entities.translation;

import java.io.Serializable;

import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import lombok.Data;
import org.pkfrc.core.entities.base.AbstractEntity;

@Data
@Entity
@Table(name = "PKF_CR_DATA_TRANSLATIONS")
@Inheritance(strategy = InheritanceType.JOINED)
@SequenceGenerator(name = "PKF_CR_DATA_TRANS_SEQ", sequenceName = "PKF_CR_DATA_TRANS_SEQ", allocationSize = 1, initialValue = 1)
public class Translation extends AbstractEntity<Long> implements Serializable, Comparable<Translation> {

	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(generator = "PKF_CR_DATA_TRANS_SEQ", strategy = GenerationType.SEQUENCE)
	private Long id;

	@Column(name = "LANG_CODE", length = 32)
	private String lang;

	@Column(name = "TRANSLATION")
	private String translation;

	@Column(name = "LANGUAGE_CODE")
	public String getLang() {
		return lang;
	}
	public Translation() {
	}

	public Translation(String lang, String translation, Long id, Long version) {
		super();
		this.lang = lang;
		this.translation = translation;
		this.id = id;
		this.version = version;
	}

	public Translation(String lang, String translation) {
		super();
		this.lang = lang;
		this.translation = translation;
	}


	@Override
	public int compareTo(Translation arg0) {
		return getLang().compareTo(arg0.getLang());
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((getLang() == null) ? 0 : getLang().hashCode());
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
		Translation other = (Translation) obj;
		if (getLang() == null) {
			if (other.getLang() != null)
				return false;
		} else if (!getLang().equals(other.getLang()))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return lang + " : " + translation;
	}

}
