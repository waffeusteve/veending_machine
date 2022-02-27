package org.pkfrc.core.persistence.base;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.transaction.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.hibernate.Criteria;
import org.hibernate.FetchMode;
import org.hibernate.Session;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.pkfrc.core.persistence.tools.AliasesContainer;
import org.pkfrc.core.persistence.tools.LoaderModeContainer;
import org.pkfrc.core.persistence.tools.OrderContainer;
import org.pkfrc.core.persistence.tools.RestrictionsContainer;
import org.springframework.stereotype.Repository;

@Repository
@Transactional
public class CoreDAO {

	// @PersistenceContext
	private EntityManager manager;

	public void setManager(EntityManager manager) {
		this.manager = manager;
	}

	private void checkParams(String strSQL, List<String> params) throws Exception {
		for (String param : params) {
			if (!strSQL.contains(":" + param)) {
				throw new Exception("Parametre invalide " + param);
			}
		}
	}

	@SuppressWarnings("unchecked")
	public <T> T findOne(Class<T> entityClass, Object entityID) {

		Session session = (Session) manager.getDelegate();

		Criteria criteria = session.createCriteria(entityClass);

		criteria.add(Restrictions.idEq(entityID));

		T result = (T) criteria.uniqueResult();

		return result;
	}

	public <T> T save(T entity) {
		manager.persist(entity);
		return entity;
	}

	public <T> T saveAndFlush(T entity) {
		entity = save(entity);
		manager.flush();
		return entity;
	}

	public <T> T update(T entity) {
		entity = manager.merge(entity);
		return entity;
	}

	public <T> T updateAndFlush(T entity) {
		entity = update(entity);
		manager.flush();
		return entity;
	}

	public <T> T delete(T entity) {
		manager.remove(entity);
		return entity;
	}

	public <T> T deleteAndFlush(T entity) {
		entity = delete(entity);
		manager.flush();
		return entity;
	}

	public <T> T findOneByQuery(String queryName, Object... argList) throws Exception {
		List<T> result = findByQuery(queryName, argList);
		if (!result.isEmpty()) {
			return result.get(0);
		}
		return null;
	}

	public void executeNativeUpdate(String queryName, Object... object) throws Exception {

		Query query = manager.createNativeQuery(queryName);
		for (int i = 0; i < object.length; i++) {
			query.setParameter("param" + i, object[i]);
		}
		query.executeUpdate();
	}

	@SuppressWarnings("unchecked")
	public <T> List<T> findByQuery(String queryName, Object... argList) throws Exception {
		Query query = manager.createNativeQuery(queryName);
		if (argList != null && argList.length != 0) {
			String param;
			List<String> params = new ArrayList<>(0);
			for (int i = 0; i < argList.length; i++) {
				param = "param" + i;
				query.setParameter(param, argList[i]);
				params.add(param);
			}
			checkParams(queryName, params);
		}
		return query.getResultList();
	}

	@SuppressWarnings("unchecked")
	public <T extends Object> List<T> findByQuery(String queryName, Integer first, Integer max, Object... argList)
			throws Exception {
		Query query = manager.createNativeQuery(queryName);
		if (argList != null && argList.length != 0) {
			String param;
			List<String> params = new ArrayList<>(0);
			for (int i = 0; i < argList.length; i++) {
				param = "param" + i;
				query.setParameter(param, argList[i]);
				params.add(param);
			}
			checkParams(queryName, params);
		}
		if (first > 0) {
			query.setFirstResult(first);
		}
		if (max > 0) {
			query.setMaxResults(max);
		}
		return query.getResultList();
	}

	public <T> List<T> listObject(Class<T> clazz, Map<String, String> paramAlias, Map<String, String> paramLike,
			Map<String, Object> paramEq, LoaderModeContainer loaderModes, int first, int max) throws Exception {
		RestrictionsContainer restrictions = RestrictionsContainer.getInstance();
		AliasesContainer alias = AliasesContainer.getInstance();
		// Initialiser les allias
		if (paramAlias != null) {
			for (String aliasK : paramAlias.keySet()) {
				alias.add(aliasK, paramAlias.get(aliasK));
			}
		}
		// Initialisation des restriction like
		if (paramLike != null) {
			for (String paramK : paramLike.keySet()) {
				restrictions.add(Restrictions.ilike(paramK, paramLike.get(paramK), MatchMode.ANYWHERE));
			}
		}
		// Initialisation des restriction eq
		if (paramEq != null) {
			for (String paramK : paramEq.keySet()) {
				Object val = paramEq.get(paramK);
				if (val instanceof Object[]) {
					restrictions.add(Restrictions.between(paramK, ((Object[]) val)[0], ((Object[]) val)[1]));
				} else if (val instanceof List) {
					restrictions.add(Restrictions.in(paramK, ((List<?>) val).toArray()));
				} else {
					if (paramK.startsWith("!")) {
						restrictions.add(Restrictions.ne(paramK.substring(1), paramEq.get(paramK)));
					} else {
						restrictions.add(Restrictions.eq(paramK, paramEq.get(paramK)));
					}
				}
			}
		}
		return filter(clazz, alias, restrictions, null, loaderModes, first, max);
	}

	public <T> List<T> listObject(Class<T> clazz, Map<String, String> paramAlias, Map<String, String> paramLike,
			Map<String, Object> paramEq, LoaderModeContainer loaderModes) throws Exception {
		return listObject(clazz, paramAlias, paramLike, paramEq, loaderModes, 0, -1);
	}

	public <T> Integer countObject(Class<T> clazz, Map<String, String> paramAlias, Map<String, String> paramLike,
			Map<String, Object> paramEq, LoaderModeContainer loaderModes) throws Exception {
		return countObject(clazz, paramAlias, paramLike, paramEq, loaderModes, 0, -1);
	}

	public <T> Integer countObject(Class<T> clazz) throws Exception {
		return countObject(clazz, 0, -1);
	}

	public <T> Integer countObject(Class<T> clazz, Integer first, Integer max) throws Exception {
		return countObject(clazz, new HashMap<String, String>(), null, null, null, first, max);
	}

	public <T> Integer countObject(Class<T> clazz, AliasesContainer paramAlias, RestrictionsContainer paramRestriction,
			LoaderModeContainer loaderModes) throws Exception {
		return countObject(clazz, paramAlias, paramRestriction, loaderModes, 0, 0);
	}

	public <T> Integer countObject(Class<T> clazz, AliasesContainer paramAlias, RestrictionsContainer paramRestriction,
			LoaderModeContainer loaderModes, Integer pos, Integer tail) throws Exception {
		return countObject(clazz, paramAlias, paramRestriction, null, loaderModes, pos, tail);

	}

	public <T> Integer countObject(Class<T> clazz, AliasesContainer arg1, RestrictionsContainer arg2,
			OrderContainer arg3, LoaderModeContainer arg4, Integer arg5, Integer arg6) throws Exception {
		return count(clazz, arg1, arg2, arg3, arg4, arg5, arg6);

	}

	public <T> Integer countObject(Class<T> clazz, Map<String, String> paramAlias, Map<String, String> paramLike,
			Map<String, Object> paramEq, LoaderModeContainer loaderModes, Integer first, Integer max) throws Exception {
		RestrictionsContainer restrictions = RestrictionsContainer.getInstance();
		AliasesContainer alias = AliasesContainer.getInstance();
		// Initialiser les allias
		if (paramAlias != null) {
			for (String aliasK : paramAlias.keySet()) {
				alias.add(aliasK, paramAlias.get(aliasK));
			}
		}
		// Initialisation des restriction like
		if (paramLike != null) {
			for (String paramK : paramLike.keySet()) {
				restrictions.add(Restrictions.ilike(paramK, paramLike.get(paramK), MatchMode.ANYWHERE));
			}
		}
		// Initialisation des restriction eq
		if (paramEq != null) {
			for (String paramK : paramEq.keySet()) {
				Object val = paramEq.get(paramK);
				if (val instanceof Object[]) {
					restrictions.add(Restrictions.between(paramK, ((Object[]) val)[0], ((Object[]) val)[1]));
				} else if (val instanceof List) {
					restrictions.add(Restrictions.in(paramK, ((List<?>) val).toArray()));
				} else {
					restrictions.add(Restrictions.eq(paramK, paramEq.get(paramK)));
				}
			}
		}
		return count(clazz, alias, restrictions, null, loaderModes, first, max);
	}

	public <T> Integer count(Class<T> clazz, AliasesContainer alias, RestrictionsContainer restrictions,
			OrderContainer orders, LoaderModeContainer loaderModes, Integer firstResult, Integer maxResult)
			throws Exception {

		// Une information
		// logger.trace("JPAGenericDAORulesBased#count");

		// Si la Classe a interroger est nulle
		if (clazz == null) {

			// Un Log
			// logger.trace("JPAGenericDAORulesBased#count - Classe cible du filtre nulle");

			// On leve une exception
			throw new Exception("JPAGenericDAORulesBased.filter.class.null");
		}

		// Un Log
		// logger.trace("JPAGenericDAORulesBased#count - Obtention de la session
		// deleguee");

		// Obtention de la session Hibernate en cours
		Session session = (Session) manager.getDelegate();

		// Un Log
		// logger.trace("JPAGenericDAORulesBased#count - Creation de la requête
		// Criteria");

		// Creation de la requête de recherche
		Criteria criteria = session.createCriteria(clazz);
		// Ajout de la restriction du retour au nombre de ligne
		criteria.setProjection(Projections.rowCount());
		// Liste resultat
		Integer result = null;

		// Traitement de l'index du premier resultat

		// Un Log
		// logger.trace("JPAGenericDAORulesBased#count - Traitement de l'index du
		// premier resultat");

		// Si l'index du premier element est < 0
		if (firstResult < 0)
			criteria.setFirstResult(0);
		else
			criteria.setFirstResult(firstResult);

		// Traitement du nombre max de resultat

		// Un Log
		// logger.trace("JPAGenericDAORulesBased#count - Traitement du nombre max de
		// resultat");

		// Si le nombre max d'element est <= 0
		if (maxResult > 0)
			criteria.setMaxResults(maxResult);

		// Un Log
		// logger.trace("JPAGenericDAORulesBased#count - Traitement des Alias");

		// Traitement des Alias
		addAliases(criteria, alias);

		// Un Log
		// logger.trace("JPAGenericDAORulesBased#count - Traitement des Restrictions");

		// Traitement des Restrictions
		addRestrictions(criteria, restrictions);

		// Un Log
		// logger.trace("JPAGenericDAORulesBased#count - Traitements des Ordres de
		// tri");

		// Traitements des Ordres de tri
		addOrders(criteria, orders);

		// Un Log
		// logger.trace("JPAGenericDAORulesBased#count - Traitement des modes de
		// chargements");

		// Traitement des modes de chargements
		addLoaderMode(criteria, loaderModes);

		// Un Log
		// logger.trace("JPAGenericDAORulesBased#count - Exceution de la requête");

		// Ajout de la contrainte de retourner les element unique
		criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);

		// Exceution de la requête
		result = ((Long) criteria.list().get(0)).intValue();

		// Un Log
		// logger.trace("JPAGenericDAORulesBased#count - On retourne le nombre
		// d'element");

		// On retourne la liste
		return result;
	}

	@SuppressWarnings("unchecked")
	public <T> List<T> filter(Class<T> clazz, AliasesContainer alias, RestrictionsContainer restrictions,
			OrderContainer orders, LoaderModeContainer loaderModes, int firstResult, int maxResult) throws Exception {

		// Une information
		// logger.info("JPAGenericDAO#filter");

		// Si la Classe a interroger est nulle
		if (clazz == null) {

			// Un Log
			// logger.info("JPAGenericDAO#filter - Classe cible du filtre nulle");

			// On leve une exception
			throw new Exception("JPAGenericDAO.filter.class.null");
		}

		// Un Log
		// logger.info("JPAGenericDAO#filter - Obtention de la session deleguee");

		// Obtention de la session Hibernate en cours
		Session session = (Session) manager.getDelegate();

		// Un Log
		// logger.info("JPAGenericDAO#filter - Creation de la requête Criteria");

		// Creation de la requête de recherche
		Criteria criteria = session.createCriteria(clazz);

		// Liste resultat
		List<T> result = new ArrayList<T>();

		// Traitement de l'index du premier resultat

		// Un Log
		// logger.info("JPAGenericDAO#filter - Traitement de l'index du premier
		// resultat");

		// Si l'index du premier element est < 0
		if (firstResult < 0)
			criteria.setFirstResult(0);
		else
			criteria.setFirstResult(firstResult);

		// Traitement du nombre max de resultat

		// Un Log
		// logger.info("JPAGenericDAO#filter - Traitement du nombre max de resultat");

		// Si le nombre max d'element est <= 0
		if (maxResult > 0)
			criteria.setMaxResults(maxResult);

		// Un Log
		// logger.info("JPAGenericDAO#filter - Traitement des Alias");

		// Traitement des Alias
		addAliases(criteria, alias);

		// Un Log
		// logger.info("JPAGenericDAO#filter - Traitement des Restrictions");

		// Traitement des Restrictions
		addRestrictions(criteria, restrictions);

		// Un Log
		// logger.info("JPAGenericDAO#filter - Traitements des Ordres de tri");

		// Traitements des Ordres de tri
		addOrders(criteria, orders);

		// Un Log
		// logger.info("JPAGenericDAO#filter - Traitement des modes de chargements");

		// Traitement des modes de chargements
		addLoaderMode(criteria, loaderModes);

		// Un Log
		// logger.info("JPAGenericDAO#filter - Exceution de la requête");

		// Exceution de la requête
		result = criteria.list();

		// Un Log
		// logger.info("JPAGenericDAO#filter - On retourne la liste");

		// On retourne la liste
		return result;
	}

	/**
	 * Methode d'ajout des Alias a la requete de recherche
	 * 
	 * @param criteria
	 *            Requete de recherche
	 * @param container
	 *            Conteneur d'alias
	 */
	private void addAliases(Criteria criteria, AliasesContainer container) {

		// Si le conteneur est vide
		if (container == null || container.size() == 0)
			return;

		// Obtention du conteneur
		Map<String, String> lContainer = container.getAliases();

		// Obtention de l'esemble des cles
		Set<String> keys = lContainer.keySet();

		// Parcours du conteneur
		for (String key : keys) {

			// Ajout de l'alias
			criteria.createAlias(key, lContainer.get(key));
		}
	}

	/**
	 * Methode d'ajout des Restrictions a la requete de recherche
	 * 
	 * @param criteria
	 *            Requete de recherche
	 * @param container
	 *            Conteneur de restrictions
	 */
	private void addRestrictions(Criteria criteria, RestrictionsContainer container) {

		// Si le conteneur est vide
		if (container == null || container.size() == 0)
			return;

		// Obtention du conteneur
		List<Criterion> lContainer = container.getRestrictions();

		// Parcours du conteneur
		for (Criterion restriction : lContainer) {

			// Ajout de la restriction
			criteria.add(restriction);
		}
	}

	/**
	 * Methode d'ajout des Ordres de tri a la requete de recherche
	 * 
	 * @param criteria
	 *            Requete de recherche
	 * @param container
	 *            Conteneur d'ordre de tri
	 */
	private void addOrders(Criteria criteria, OrderContainer container) {

		// Si le conteneur est vide
		if (container == null || container.size() == 0)
			return;

		// Obtention du conteneur
		List<Order> lContainer = container.getOrders();

		// Parcours du conteneur
		for (Order order : lContainer) {

			// Ajout de l'ordre de tri
			criteria.addOrder(order);
		}
	}

	/**
	 * Methode d'ajout des Mode de chargement a la requete de recherche
	 * 
	 * @param criteria
	 *            Requete de recherche
	 * @param container
	 *            Conteneur de mode de chargement
	 */
	private void addLoaderMode(Criteria criteria, LoaderModeContainer container) {

		// Si le conteneur est vide
		if (container == null || container.size() == 0)
			return;

		// Obtention du conteneur
		Map<String, FetchMode> lContainer = container.getLoaderMode();

		// Obtention de l'esemble des cles
		Set<String> keys = lContainer.keySet();

		// Parcours du conteneur
		for (String key : keys) {

			// Ajout de l'alias
			criteria.setFetchMode(key, lContainer.get(key));
		}
	}

}
