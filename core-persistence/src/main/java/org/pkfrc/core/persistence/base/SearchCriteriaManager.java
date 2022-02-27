package org.pkfrc.core.persistence.base;

import java.util.List;

import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.pkfrc.core.persistence.model.ESearchOperationType;
import org.pkfrc.core.persistence.model.SearchCriteria;
import org.pkfrc.core.persistence.tools.AliasesContainer;
import org.pkfrc.core.persistence.tools.OrderContainer;
import org.pkfrc.core.persistence.tools.RestrictionsContainer;
import org.pkfrc.core.utilities.helper.ScannerHelper;
import org.pkfrc.core.utilities.helper.StringHelper;

public class SearchCriteriaManager {

	public static void validate(SearchCriteria criteria) throws Exception {
		if (criteria.getValue() == null) {
			throw new Exception("No value defined for key :" + criteria.getKey());
		}

		// if (criteria.getType().equals(ESearchOperationType.BEWTEEN)) {
		// if (criteria.getValue() == null || criteria.getValue1() == null) {
		// throw new Exception("Two values expected for key :" +
		// criteria.getKey());
		// }
		// }
	}

	private static Criterion getRestriction(Class<?> clazz, SearchCriteria criteria) {

		if (criteria.getType().equals(ESearchOperationType.ALIAS)
				|| criteria.getType().equals(ESearchOperationType.ORDER)) {
			return null;
		}

		boolean isList = criteria.getType().equals(ESearchOperationType.IN);
		criteria.setValue(ScannerHelper.buildFieldValue(clazz, criteria.getKey(), criteria.getValue(), isList));
		criteria.setValue1(ScannerHelper.buildFieldValue(clazz, criteria.getKey(), criteria.getValue1(), isList));

		// no need to join
		if (criteria.getKey().contains(".") && ScannerHelper.isNull(criteria.getValue())
				&& !criteria.getType().equals(ESearchOperationType.NULL)
				&& !criteria.getType().equals(ESearchOperationType.NOT_NULL)) {
			return null;
		}

		String key = StringHelper.isNotEmpty(criteria.getAliasKey()) ? criteria.getAliasKey() : criteria.getKey();
		switch (criteria.getType()) {
		case LESS_THAN:
			if (criteria.getValue() == null) {
				return null;
			}
			return Restrictions.lt(key, criteria.getValue());
		case LESS_THAN_EQUAL:
			if (criteria.getValue() == null) {
				return null;
			}
			return Restrictions.le(key, criteria.getValue());
		case EQUAL:
			if (criteria.getValue() == null) {
				return null;
			}
			return Restrictions.eq(key, criteria.getValue());
		case GREATER_THAN_EQUAL:
			if (criteria.getValue() == null) {
				return null;
			}
			return Restrictions.ge(key, criteria.getValue());
		case GREATER_THAN:
			if (criteria.getValue() == null) {
				return null;
			}
			return Restrictions.gt(key, criteria.getValue());
		case NOT_EQUAL:
			if (criteria.getValue() == null) {
				return null;
			}
			return Restrictions.ne(key, criteria.getValue());
		case LIKE:
			if (criteria.getValue() == null) {
				return null;
			}
			return Restrictions.ilike(key, (String) criteria.getValue(), MatchMode.ANYWHERE);
		case BETWEEN:
			if (criteria.getValue() == null || criteria.getValue1() == null) {
				return null;
			}
			return Restrictions.between(key, criteria.getValue(), criteria.getValue1());
		case IN:
			return Restrictions.in(key, (List<?>) criteria.getValue());
		// case NOT_IN:
		// return Restrictions.in(criteria.getKey(), (List<?>) criteria.getValue());
		case NULL:
			return Restrictions.isNull(key);
		case NOT_NULL:
			return Restrictions.isNotNull(key);
		default:
		}
		return null;
	}

	public static RestrictionsContainer getRestrictions(Class<?> clazz, List<SearchCriteria> criterias)
			throws Exception {
		return getRestrictions(clazz, criterias, false);
	}

	public static AliasesContainer getAlias(List<SearchCriteria> criterias) throws Exception {
		if (criterias == null || criterias.isEmpty()) {
			return null;
		}
		AliasesContainer alias = AliasesContainer.getInstance();

		for (SearchCriteria crit : criterias) {
			if (crit.getType().equals(ESearchOperationType.ALIAS)) {
				if (StringHelper.isEmpty((String) crit.getValue())) {
					crit.setValue(crit.getKey());
				}
				if (isValidAlias(criterias, (String) crit.getValue())) {
					alias.add(crit.getKey(), (String) crit.getValue());
				}
			}
		}
		return alias;
	}

	private static boolean isValidAlias(List<SearchCriteria> criterias, String alias) {
		for (SearchCriteria criteria : criterias) {
			if (!criteria.getType().equals(ESearchOperationType.ALIAS) && criteria.getKey().contains(alias + ".")
					&& !ScannerHelper.isNull(criteria.getValue())) {
				return true;
			}
		}
		return false;
	}

	public static OrderContainer getOrder(List<SearchCriteria> criterias) throws Exception {
		if (criterias == null || criterias.isEmpty()) {
			return null;
		}
		OrderContainer order = OrderContainer.getInstance();

		for (SearchCriteria crit : criterias) {
			if (crit.getType().equals(ESearchOperationType.ORDER)) {

				String key = StringHelper.isNotEmpty(crit.getAliasKey()) ? crit.getAliasKey() : crit.getKey();
				String ord = (String) crit.getValue();
				if (ord == null || StringHelper.isEmpty(ord) || ord.equals("asc")) {
					order.add(Order.asc(key));
				} else {
					order.add(Order.desc(key));
				}
			}
		}
		return order;
	}

	public static RestrictionsContainer getRestrictions(Class<?> clazz, List<SearchCriteria> criterias, boolean withOR)
			throws Exception {
		RestrictionsContainer restrictions = RestrictionsContainer.getInstance();
		if (criterias == null) {
			return restrictions;
		}
		Criterion criterion = null;
		for (SearchCriteria criteria : criterias) {
			if (criteria.getValue() == null) {
				if (!ESearchOperationType.NULL.equals(criteria.getType())
						&& !ESearchOperationType.NOT_NULL.equals(criteria.getType())) {
					continue;
				}
			}
			Criterion crit = getRestriction(clazz, criteria);
			if (crit == null) {
				continue;
			}

			if (withOR) {
				if (criterion == null) {
					criterion = crit;
				} else {
					if (criteria.isApplyAnd()) {
						criterion = Restrictions.and(criterion, crit);
					} else {
						criterion = Restrictions.or(criterion, crit);
					}
				}
			} else {
				restrictions.add(crit);
			}

		}
		if (withOR) {
			restrictions.add(criterion);
		}
		return restrictions;

	}

}
