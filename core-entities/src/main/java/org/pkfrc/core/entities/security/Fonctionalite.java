package org.pkfrc.core.entities.security;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.pkfrc.core.entities.base.AbstractEntity;

/**
 * @author Ulrich LELE
 *
 */
@Data
@NoArgsConstructor
@Entity
@Table(name = "PROJ_FONCTE")
@SequenceGenerator(name = "PROJ_FONCTE_SEQ", sequenceName = "PROJ_FONCTE_SEQ", allocationSize = 1, initialValue = 1)
@AttributeOverride(name = "id", column = @Column(name = "FONCTE_ID", insertable = false, updatable = false))
public class Fonctionalite extends AbstractEntity<Long> implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "id", unique = true, updatable = false)
	@GeneratedValue(generator = "PROJ_FONCTE_SEQ", strategy = GenerationType.SEQUENCE)
	private Long id;

	private String intitule;

	@Enumerated(EnumType.STRING)
	private EFonctionalite fonctionalite;

	@Enumerated(EnumType.STRING)
	private EFonctionModule module;

	public Fonctionalite(String userCreate, Date dateCreate) {
		super();
	}

	public Fonctionalite(EFonctionalite fonctionalite, EFonctionModule module, String userCreate, Date dateCreate) {
		super();
		this.fonctionalite = fonctionalite;
		this.module = module;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((fonctionalite == null) ? 0 : fonctionalite.hashCode());
		result = prime * result + ((module == null) ? 0 : module.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		Fonctionalite other = (Fonctionalite) obj;
		if (fonctionalite != other.fonctionalite)
			return false;
		if (module != other.module)
			return false;
		return true;
	}
}