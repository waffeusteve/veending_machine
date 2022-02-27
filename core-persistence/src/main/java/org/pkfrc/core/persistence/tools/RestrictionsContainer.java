/**
 * 
 */
package org.pkfrc.core.persistence.tools;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.hibernate.criterion.Criterion;

/**
 * Classe representant un conteneur de restrictions
 * @author Jean-Jacques
 * @version 1.0
 */
public class RestrictionsContainer implements Serializable {

	/**
	 * ID Genere par eclipse
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * Liste des restrictions
	 */
	private List<Criterion> restrictions = new ArrayList<Criterion>();

	/**
	 * Obtention de l'instance
	 * @return	Instance de travail
	 */
	public static synchronized final RestrictionsContainer getInstance() {
		
		// On retourne l'instance
		return new RestrictionsContainer();
	}
	
	/**
	 * Methode d'ajout d'une restriction
	 * @param restriction	Restriction a ajouter
	 * @return	Conteneur de restrictions
	 */
	public RestrictionsContainer add(Criterion restriction) {
		
		// Si la restriction est nulle
		if(restriction != null) restrictions.add(restriction);
		
		// On retourne le container
		return this;
	}

	/**
	 * Methode d'obtention de la Liste des restrictions
	 * @return Liste des restrictions
	 */
	public List<Criterion> getRestrictions() {
		return Collections.unmodifiableList(restrictions);
	}
	
	/**
	 * Methode d'obtention de la taille du conteneur
	 * @return	Taille du conteneur
	 */
	public int size() {
		
		// On retourne la taille de la collection
		return this.restrictions.size();
	}
	
	/**
	 * Methode de vidage du conteneur
	 */
	public void clear() {
		
		// Si la collection est non nulle
		if(restrictions != null) restrictions.clear();
	}
}
