package org.pkfrc.core.dto.security;

import java.io.Serializable;

import org.pkfrc.core.dto.base.AbstractDTO;
import org.pkfrc.core.entities.security.EFonctionModule;
import org.pkfrc.core.entities.security.EFonctionalite;
import org.pkfrc.core.entities.security.Fonctionalite;
/**
 * <p>
 * <p>
 * Projurise (c) 2018 PKFokam Research Center
 * 
 * @author Ulrich LELE
 */
public class FonctionaliteDTO extends AbstractDTO<Fonctionalite,Long> implements Serializable{

	private static final long serialVersionUID = 1L;

	private String intitule;
	private EFonctionalite fonctionalite;
	private EFonctionModule module;

	public FonctionaliteDTO() {
		super();

	}

	public FonctionaliteDTO(Fonctionalite entity) {
		super(entity);
		this.intitule = entity.getIntitule();
		this.fonctionalite = entity.getFonctionalite();
		this.module = entity.getModule();
	}

	@Override
	public Fonctionalite toEntity(Fonctionalite entity) {
		entity = super.toEntity(entity);
		entity.setIntitule(intitule);
		entity.setFonctionalite(fonctionalite);
		entity.setModule(module);
		return entity;

	}

	public String getIntitule() {
		return intitule;
	}

	public void setIntitule(String intitule) {
		this.intitule = intitule;
	}

	public EFonctionalite getFonctionalite() {
		return fonctionalite;
	}

	public void setFonctionalite(EFonctionalite fonctionalite) {
		this.fonctionalite = fonctionalite;
	}

	public EFonctionModule getModule() {
		return module;
	}

	public void setModule(EFonctionModule module) {
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
		FonctionaliteDTO other = (FonctionaliteDTO) obj;
		if (fonctionalite != other.fonctionalite)
			return false;
		if (module != other.module)
			return false;
		return true;
	}
	
	

}
