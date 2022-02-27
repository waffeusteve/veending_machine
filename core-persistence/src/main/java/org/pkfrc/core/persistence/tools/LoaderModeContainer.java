/**
 * 
 */
package org.pkfrc.core.persistence.tools;

import java.io.Serializable;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.hibernate.FetchMode;


/**
 * Conteneur de mode de chargement de proprietes (JOIN or SELECT)
 * Si une propriete a deja un mode de chargement, il est remplace
 * @author Jean-Jacques
 * @version 1.0
 */
public class LoaderModeContainer implements Serializable {

	/**
	 * ID genere par Eclipse
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * Carte des modes de chargements des proprietes
	 */
	private Map<String, FetchMode> loaderMode = new HashMap<String, FetchMode>();
	
	/**
	 * Obtention de l'instance
	 * @return	Instance de travail
	 */
	public static synchronized final LoaderModeContainer getInstance() {
		
		// On retourne l'instance
		return new LoaderModeContainer();
	}
	
	/**
	 * Methode d'ajout d'un mode de chargement pour une propriete
	 * @param property	Propriete a charger
	 * @return	Conteneur de mode de chargement
	 */
	public LoaderModeContainer add(String property, FetchMode mode) {
		
		// Si la propriete n'est pas vide
		if(property != null && property.trim().length() > 0) {
			
			// Si le mode n'est pas null
			if(mode != null) loaderMode.put(property.trim(), mode);
		}
		
		// On retourne le conteneur
		return this;
	}


	/**
	 * Methode d'obtention de la Carte des modes de chargements des proprietes
	 * @return Carte des modes de chargements des proprietes
	 */
	public Map<String, FetchMode> getLoaderMode() {
		return Collections.unmodifiableMap(loaderMode);
	}
	
	/**
	 * Methode d'obtention de la taille du conteneur
	 * @return	Taille du conteneur
	 */
	public int size() {
		
		// On retourne la taille de la collection
		return this.loaderMode.size();
	}

	/**
	 * Methode de vidage du conteneur
	 */
	public void clear() {
		
		// Si la collection est non nulle
		if(loaderMode != null) loaderMode.clear();
	}
}
