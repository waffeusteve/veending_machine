package org.pkfrc.projurise.services.administration.model;

import java.io.Serializable;
import java.util.Set;
import java.util.TreeSet;

import org.pkfrc.core.entities.security.EFonctionModule;
import org.pkfrc.core.entities.security.Fonctionalite;

public class ModuleFonctionaliteModel implements Serializable, Comparable<ModuleFonctionaliteModel> {

	private static final long serialVersionUID = 1L;
	EFonctionModule module;
	TreeSet<FonctionaliteModel> fonctionalites = new TreeSet<>();

	public ModuleFonctionaliteModel() {
		super();
	}

	public ModuleFonctionaliteModel(Set<Fonctionalite> fonctionalites, EFonctionModule module) {
		this.module = module;
		this.fonctionalites = FonctionaliteModel.toModels(fonctionalites);
	}

	public EFonctionModule getModule() {
		return module;
	}

	public void setModule(EFonctionModule module) {
		this.module = module;
	}

	public TreeSet<FonctionaliteModel> getFonctionalites() {
		return fonctionalites;
	}

	public void setFonctionalites(TreeSet<FonctionaliteModel> fonctionalites) {
		this.fonctionalites = fonctionalites;
	}

	@Override
	public int compareTo(ModuleFonctionaliteModel o) {
		return this.module.compareTo(o.module);
	}

}
