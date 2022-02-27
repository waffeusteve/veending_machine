package org.pkfrc.core.utilities.helper;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Locale;

public class BigDecimalHelper {

	public static BigDecimal round(BigDecimal montant, int precision) {
		double p = Math.pow(10.0, precision);
		double result = Math.floor((montant.doubleValue() * p) + 0.5) / p;
		return new BigDecimal(result);
	}

	public static String format(BigDecimal y, String locale, int precision) {
		Locale locFr = new Locale(locale);
		return format(y, locFr, precision);
	}

	public static String format(BigDecimal y, Locale locFr, int precision) {
		String reponse = "";
		NumberFormat n1 = NumberFormat.getInstance(locFr);
		n1.setMaximumFractionDigits(precision);
		reponse = n1.format(y);
		return reponse;
	}

	public static BigDecimal parse(String y, String locale, int precision) throws ParseException {
		Locale locFr = new Locale(locale);
		return parse(y, locFr, precision);
	}

	public static BigDecimal parse(String y, Locale locale, int precision) throws ParseException {
		BigDecimal reponse = new BigDecimal(DoubleHelper.parse(y, locale, precision));
		return reponse;
	}

	public static BigDecimal minValue(BigDecimal obj1, BigDecimal obj2) {
		if (obj1.compareTo(obj2) > 0)
			return obj2;
		return obj1;
	}
	
	public static BigDecimal maxValue(BigDecimal obj1, BigDecimal obj2) {
		if(obj1.compareTo(obj2)>0)
			return obj1;
		return obj2;
	}
}
