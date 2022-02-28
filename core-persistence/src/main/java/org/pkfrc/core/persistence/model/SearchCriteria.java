package org.pkfrc.core.persistence.model;

import java.util.ArrayList;
import java.util.List;

import org.pkfrc.core.utilities.exceptions.SmartTechException;
import org.pkfrc.core.utilities.helper.StringHelper;

public class SearchCriteria {
	private String key;
	private String aliasKey;
	private Object value;
	private Object value1;
	private ESearchOperationType type;
	private boolean applyAnd;

	public SearchCriteria() {
		super();
	}

	public SearchCriteria(String key, Object value, ESearchOperationType type) {
		super();
		this.key = key;
		this.value = value;
		this.type = type;
	}

	public SearchCriteria(String key, Object value, ESearchOperationType type, boolean applyAnd) {
		this(key, value, type);
		this.applyAnd = applyAnd;
	}

	public SearchCriteria(String key, String aliasKey, Object value, ESearchOperationType type) {
		this.aliasKey = aliasKey;
	}

	public String getAliasKey() {
		return aliasKey;
	}

	public void setAliasKey(String aliasKey) {
		this.aliasKey = aliasKey;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public Object getValue() {
		return value;
	}

	public void setValue(Object value) {
		this.value = value;
	}

	public Object getValue1() {
		return value1;
	}

	public void setValue1(Object value1) {
		this.value1 = value1;
	}

	public ESearchOperationType getType() {
		return type;
	}

	public void setType(ESearchOperationType type) {
		this.type = type;
	}

	public boolean isApplyAnd() {
		return applyAnd;
	}

	public void setApplyAnd(boolean applyAnd) {
		this.applyAnd = applyAnd;
	}

	@Override
	public String toString() {
		return key + " :" + type + " " + value;
	}


	/**
	 * Build the list of search criteria to find By this field
	 *
	 * @param key
	 *            the attribute separated by dot (.) if it is attribute of an
	 *            attribute (e.g user.profile.id)
	 * @param value
	 *            of the field
	 * @return the list of criteria needed to find by the field
	 *
	 * @author Ulrich LELE
	 */
	public static List<SearchCriteria> build(String key, Object value, Object value1, ESearchOperationType type,
											 boolean applyAnd) {
		List<SearchCriteria> criterias = new ArrayList<>();

		int occurences = StringHelper.countOccurrencesOf(key, ".");
		switch (occurences) {
			case 0:
				criterias.add(create(key, value, value1, type, applyAnd, null));
				break;
			case 1:
				String alias = key.substring(0, key.indexOf("."));
				criterias.add(new SearchCriteria(alias, alias, ESearchOperationType.ALIAS));
				criterias.add(create(key, value, value1, type, applyAnd, null));
				break;
			case 2:
				int firstIndex = key.indexOf(".");
				int lastIndex = key.lastIndexOf(".");
				String alias1 = key.substring(0, firstIndex);
				String alias2 = key.substring(0, lastIndex);
				String val2 = key.substring(firstIndex + 1, lastIndex);
				String aliasKey = key.substring(firstIndex + 1, key.length());

				criterias.add(new SearchCriteria(alias1, alias1, ESearchOperationType.ALIAS));
				criterias.add(new SearchCriteria(alias2, val2, ESearchOperationType.ALIAS));
				criterias.add(create(key, value, value1, type, applyAnd, aliasKey));
				break;
			case 3:
				int index1 = key.indexOf(".");
				int index2 = key.indexOf(".", index1 + 1);
				int index3 = key.lastIndexOf(".");
				String ali1 = key.substring(0, index1);
				String ali2 = key.substring(0,index2);
				String ali3 = key.substring(0, index3);
				String value2 = key.substring(index1 + 1, index2);
				String value3 = key.substring(index2 + 1, index3);
				String alias_Key = key.substring(index2 + 1, key.length());

				criterias.add(new SearchCriteria(ali1, ali1, ESearchOperationType.ALIAS));
				criterias.add(new SearchCriteria(ali2, value2, ESearchOperationType.ALIAS));
				criterias.add(new SearchCriteria(ali3, value3, ESearchOperationType.ALIAS));
				criterias.add(create(key, value, value1, type, applyAnd, alias_Key));
				break;
			default:
				throw new SmartTechException("Method.SearchCriteria.build.Too.Many.Attributes");
		}

		return criterias;
	}

	public static List<SearchCriteria> buildAliasFromSearchCriterias(List<SearchCriteria> criterias) {
		List<SearchCriteria> generatedCriterias = new ArrayList<>();
		for (SearchCriteria cirteria : criterias) {
			generatedCriterias.addAll(buildFromSearchCriteria(cirteria));
		}
		return generatedCriterias;
	}

	public static List<SearchCriteria> buildFromSearchCriteria(SearchCriteria criteria) {
		return build(criteria.getKey(), criteria.getValue(), criteria.getValue1(), criteria.getType(),
				criteria.isApplyAnd());
	}

	private static SearchCriteria create(String key, Object value, Object value1, ESearchOperationType type,
										 boolean applyAnd, String aliasKey) {
		SearchCriteria sc = new SearchCriteria();
		sc.setKey(key);
		sc.setValue(value);
		sc.setType(type);
		sc.setApplyAnd(applyAnd);
		sc.setAliasKey(aliasKey);
		return sc;
	}

	public static void main(String[] args) {
		List<SearchCriteria> sc = build("risque.assure.id", 1L, null, ESearchOperationType.EQUAL, true);
		for (SearchCriteria s : sc) {
			System.out.println("Key: "+ s.getKey() + "; Key:"+s.getType() +"; Value:"+ s.getValue()+"; AliasKey:"+s.getAliasKey());
		}
		System.out.println("Crit : ");
	}
}
