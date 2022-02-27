package org.pkfrc.core.entities.base;

import lombok.Data;
import org.pkfrc.core.entities.translation.Translation;
import org.pkfrc.core.utilities.enumerations.EWriteMode;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.*;

@Data
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


	@OneToMany(fetch = FetchType.LAZY, orphanRemoval = true, cascade = CascadeType.ALL)
	@JoinColumn(name = "LANG_ID", foreignKey = @ForeignKey(name = "FK_TRANSLATION_LANGUAGE"))
	public Set<Translation> translations = new HashSet<>(0);

	public Language() {
	}

	@Override
	public int compareTo(Language o) {
		return o.getCode().compareTo(o.getCode());
	}
}
