package org.pkfrc.core.utilities.helper;

import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Locale;

public class DoubleHelper {

	public static double round(double montant, int precision) {
		double p = Math.pow(10.0, precision);
		return Math.floor((montant * p) + 0.5) / p;
	}

	/**
	 * Permet de transformer un double en chaine de caract�re avec p chiffre comme
	 * virgule
	 * 
	 * @param d
	 * @param p
	 * @return
	 */
	public static String doubleToString(Double d) {
		return doubleToString(d, 0);
	}

	/**
	 * Permet de transformer un double en chaine de caract�re avec p chiffre comme
	 * virgule
	 * 
	 * @param d
	 * @param p
	 * @return
	 */
	public static String doubleToString(Double d, Integer p) {
		d = round(d, p);
		d = Math.abs(d);
		String strEntiere = "";
		String strDecimal = "";

		String strSoldeDispo = String.format("%.0f", d);
		int posPoint = strSoldeDispo.indexOf(".");
		if (posPoint != -1) {
			strDecimal = strSoldeDispo.substring(posPoint + 1);
			strEntiere = strSoldeDispo.substring(0, posPoint);
		} else {
			strEntiere = strSoldeDispo;
		}

		// Alignement de la partie d�cimale au nombre de caract�re pr�vu
		strDecimal = StringHelper.rpad(strDecimal, '0', p);

		return strEntiere + strDecimal;
	}

	public static double parse(String y, String locale, int precision) throws ParseException {
		Locale locFr = new Locale(locale);
		return parse(y, locFr, precision);
	}

	public static double parse(String y, Locale locale, int precision) throws ParseException {
		double reponse = 0.0;
		NumberFormat n1 = NumberFormat.getInstance(locale);
		n1.setMaximumFractionDigits(precision);
		reponse = n1.parse(y).doubleValue();
		return reponse;
	}

	public static String format(double y, String locale, int precision) {
		Locale locFr = new Locale(locale);
		return format(y, locFr, precision);
	}

	public static String format(double y, Locale locFr, int precision) {
		String reponse = "";
		NumberFormat n1 = NumberFormat.getInstance(locFr);
		n1.setMaximumFractionDigits(precision);
		reponse = n1.format(y);
		return reponse;
	}
}
