package org.pkfrc.projurise.services.administration.model;

import java.io.Serializable;
import java.util.Set;
import java.util.TreeSet;

import org.pkfrc.core.entities.security.EFonctionModule;
import org.pkfrc.core.entities.security.EFonctionalite;
import org.pkfrc.core.entities.security.Fonctionalite;

public class FonctionaliteModel implements Serializable, Comparable<FonctionaliteModel> {

	private static final long serialVersionUID = 1;

	private Long id;
	private EFonctionalite fonctionalite;
	private EFonctionModule module;

	public FonctionaliteModel() {
		super();
	}

	public FonctionaliteModel(Fonctionalite entity) {
		this.id = entity.getId();
		this.fonctionalite = entity.getFonctionalite();
		;
		this.module = entity.getModule();

	}

	static public TreeSet<FonctionaliteModel> toModels(Set<Fonctionalite> entities) {
		TreeSet<FonctionaliteModel> models = new TreeSet<>();
		for (Fonctionalite entity : entities) {
			models.add(new FonctionaliteModel(entity));
		}
		return models;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
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
	public int compareTo(FonctionaliteModel o) {
		return this.id.compareTo(o.id);
	}

}
