package org.pkfrc.core.ws.base;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.pkfrc.core.dto.base.EnumDTO;
import org.pkfrc.core.utilities.helper.ScannerHelper;

public class BaseEnumWS {

	public static <E> Set<EnumDTO> buildEnum(E[] array) {
		List<E> values = Arrays.asList(array);
		return buildEnum(values);
	}

	public static <E> Set<EnumDTO> buildEnum(Set<E> entities) {
		List<E> values = new ArrayList<E>(entities);
		return buildEnum(values);
	}

	private static <E> List<EnumDTO> getEnum(List<E> values) {
		String name;
		Integer order;
		String label;
		List<EnumDTO> liste = new ArrayList<>(0);
		for (E value : values) {
			name = (String) ScannerHelper.executeMethod(value, "name", null, null);
			order = (Integer) ScannerHelper.executeMethod(value, "ordinal", null, null);
			label = (String) ScannerHelper.executeMethod(value, "toString", null, null);
			liste.add(new EnumDTO(name, order, label));
		}
		Collections.sort(liste);
		return liste;
	}

	public static <E> Set<EnumDTO> buildEnum(List<E> values) {
		return new LinkedHashSet<>(getEnum(values));

	}
}
