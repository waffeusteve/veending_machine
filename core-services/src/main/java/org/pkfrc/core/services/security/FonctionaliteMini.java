package org.pkfrc.core.services.security;


import org.pkfrc.core.entities.security.EFonctionModule;
import org.pkfrc.core.entities.security.EFonctionalite;
import org.pkfrc.core.entities.security.Fonctionalite;

import java.io.Serializable;

/**
 * <p>
 * <p>
 * Projurise (c) 2018 PKFokam Research Center
 * 
 * @author Ulrich LELE
 */
public class FonctionaliteMini implements Serializable{

	private static final long serialVersionUID = 1L;


	private EFonctionalite fonctionalite;
	private EFonctionModule module;

	public FonctionaliteMini() {
		super();

	}

	public FonctionaliteMini(Fonctionalite entity) {
		this.fonctionalite = entity.getFonctionalite();
		this.module = entity.getModule();
	}


	public Fonctionalite toEntity(Fonctionalite entity) {
		entity.setFonctionalite(fonctionalite);
		entity.setModule(module);
		return entity;

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
		FonctionaliteMini other = (FonctionaliteMini) obj;
		if (fonctionalite != other.fonctionalite)
			return false;
		if (module != other.module)
			return false;
		return true;
	}
	
	

}
