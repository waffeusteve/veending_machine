package org.pkfrc.core.entities.security;

public enum EFonctionModule {

	Administration, ContentieuxOrdinaire, Garantie, Paramettrage, Commun, Syncroniser, workflowPaiement;

	@Override
	public String toString() {
		return "EFonctionModule." +  this.name() ;
	}
}