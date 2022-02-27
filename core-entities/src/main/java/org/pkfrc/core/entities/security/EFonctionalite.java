package org.pkfrc.core.entities.security;

public enum EFonctionalite {


	CreerContentieux, ConsulterContentieux, EditerConentieux, ParametrerAlerteContentieux, supprimerContentieux,voirTousContencieux,voirTousContentieuxAgence,voirTousGarantie,
	CloturerContentieux,ajouterFraisHonnoraireContentieux,modifierFraisHonnoraireContentieux,ajouterDocumentContentieux, CreerGarantie, ConsulterGarantie, EditerGarantie, 
	ConsulterLesChampsNumeroCoffreGarantie, DeplacerGarantie, RenouvellerGarantie,
	AffecterGarantie, ConsulterGarantieInscrites, ExporterDonneesGaranties, supprimerGarantie,
	ParametrerAlerteGarantie, CreerUtilisateur, CreerModifierService, CreerModierProfile,
	CreerModifierNiveau, ModifierUtitlisateur, ConsulterUtilisateur,supprimerProfile,supprimerUtilisateur, ConsulterContacts,
	ConsulterSMS, AttribuerMenu, CreerModifierLangue, SyncroniserGaranties,
	SyncroniserContentieuxOrdinaires, ModifierLesParemetresGenereaux, CreerModifierHuissier,
	ConsulterHuissier, ConsulterPersonne, CreerModiferPersonne, CreerModierAvocat, ConsulterAvocat,
	CreerModierNotaire, ConsulterNotaire, ConsulterAgences, CreerModifierGarant, ConsulterGarant,
	CloturerContentieuxOrdinaire, ExporterLesEtatsContentieux, ActiverSuspendreUtilisateur,
	ReinitialiserMotDePasseUtilisateur, ConfigurerDeviseParDefaut, InnitierPaiement,Approuver_paiement,consulterGarantieDashboard,
	Valider_Paiement,payer,Annuler_paiement,consulter_paiement,supprimer_paiement,refuser_paiement,voirTousFrais,voirTourHonnoraire,voirTousPaiment, consulterContentieuxDashboard;

	@Override
	public String toString() {
		return "EFonctionalite." +  this.name() ;
	}
}