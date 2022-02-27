package org.pkfrc.core.utilities.helper;

import java.io.Serializable;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Random;

public class RandomHelper implements Serializable {

	private static final long serialVersionUID = 1L;

	public Integer getIntRandom() {
		Random rand = new Random();
		return new Integer(rand.nextInt());
	}

	public Integer getIntRandom(int debut, int fin) {
		Random rand = new Random();
		int result = rand.nextInt((fin + 1) - debut) + debut;
		return result;
	}

	public Double getDoubleRandom() {
		Random rand = new Random();
		return rand.nextDouble();
	}

	public Double getDoubleRandom(Double debut, Double fin) {
		Random rand = new Random();
		double range = fin - debut;
		double scaled = rand.nextDouble() * range;
		double shifted = scaled + debut;
		return shifted;
	}

	public String getStringRandom() {
		return getStringRandom(20);
	}

	public String getStringRandom(int length) {
		if (length == 0) {
			return "";
		}
		String numbers = "0123456789";
		String upperChars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
		String lowerChars = upperChars.toLowerCase();
		String chars = numbers + upperChars + lowerChars + " ";

		// Début des caractères ascci
		int debut = 0;
		// Fin des caractères ascci
		int fin = chars.length() - 1;
		StringBuilder sb = new StringBuilder(length);
		for (int i = 0; i < length; i++) {
			sb.append(chars.charAt(getIntRandom(debut, fin)));
		}
		return sb.toString();
	}

	public String getStringNumRandom() {
		return getStringNumRandom(20);
	}

	public String getStringNumRandom(int length) {
		if (length == 0) {
			return "";
		}
		String numbers = "0123456789";
		String chars = numbers;

		// Début des caractères ascci
		int debut = 0;
		// Fin des caractères ascci
		int fin = chars.length() - 1;
		StringBuilder sb = new StringBuilder(length);
		for (int i = 0; i < length; i++) {
			sb.append(chars.charAt(getIntRandom(debut, fin)));
		}
		return sb.toString();
	}

	public Date getPastDateRandom() {
		return getPastDateRandom(new Date());
	}

	public Date getPastDateRandom(Date current) {
		GregorianCalendar currentGC = new GregorianCalendar();
		currentGC.setTime(current);
		int debut = 1950;
		int fin = currentGC.get(GregorianCalendar.YEAR);
		int year = getIntRandom(debut, fin);

		// Premier mois, janvier
		debut = 0;
		// Derbier mois, Décembre
		if (year == currentGC.get(GregorianCalendar.YEAR)) {
			fin = currentGC.get(GregorianCalendar.MONTH);
		} else {
			fin = 11;
		}
		int month = getIntRandom(debut, fin);

		GregorianCalendar gc = new GregorianCalendar(year, month, 1);

		// Premier jour du mois
		debut = 1;
		// Nombre de jour du mois
		// S'il faut faire avec un equal, c'est sur le jour qu'il faut agir
		if (month == currentGC.get(GregorianCalendar.MONTH)) {
			fin = currentGC.get(GregorianCalendar.DAY_OF_MONTH) - 1;
		} else {
			fin = gc.getActualMaximum(GregorianCalendar.DAY_OF_MONTH);
		}
		int day = getIntRandom(debut, fin);
		gc.set(year, month, day);
		return gc.getTime();
	}

	public Date getDateRandom() {
		int debut = 1950;
		int fin = 2050;
		int year = getIntRandom(debut, fin);

		// Premier mois, janvier
		debut = 0;
		// Derbier mois, Décembre
		fin = 11;
		int month = getIntRandom(debut, fin);

		GregorianCalendar gc = new GregorianCalendar(year, month, 1);

		// Premier jour du mois
		debut = 1;
		// Nombre de jour du mois
		fin = gc.getActualMaximum(GregorianCalendar.DAY_OF_MONTH);
		int day = getIntRandom(debut, fin);
		gc.set(year, month, day);
		return gc.getTime();
	}

	public Date getFutureDateRandom() {
		return getFutureDateRandom(new Date());
	}

	public Date getFutureDateRandom(Date current) {
		GregorianCalendar currentGC = new GregorianCalendar();
		currentGC.setTime(current);
		int debut = currentGC.get(GregorianCalendar.YEAR);
		int fin = 2050;
		int year = getIntRandom(debut, fin);

		// Premier mois, janvier
		if (year == currentGC.get(GregorianCalendar.YEAR)) {
			debut = currentGC.get(GregorianCalendar.MONTH);
		} else {
			debut = 0;
		}
		// Derbier mois, Décembre
		fin = 11;
		int month = getIntRandom(debut, fin);

		GregorianCalendar gc = new GregorianCalendar(year, month, 1);

		// Premier jour du mois
		// S'il faut faire avec un equal, c'est sur le jour qu'il faut agir
		if (month == currentGC.get(GregorianCalendar.MONTH)) {
			debut = currentGC.get(GregorianCalendar.DAY_OF_MONTH) - 1;
		} else {
			debut = 1;
		}
		// Nombre de jour du mois
		fin = gc.getActualMaximum(GregorianCalendar.DAY_OF_MONTH);

		int day = getIntRandom(debut, fin);
		gc.set(year, month, day);
		return gc.getTime();
	}

	// public <T extends Enum> T getEnumRand(Class<T> enumClass, int length) {
	// return getEnumRand(enumClass);
	// }

	@SuppressWarnings("rawtypes")
	public <T extends Enum> T getEnumRand(Class<T> enumClass) {
		if (!enumClass.isEnum()) {
			return null;
		}
		int code = getIntRandom(0, enumClass.getEnumConstants().length - 1);
		T result = null;
		try {
			result = enumClass.getEnumConstants()[code];
		} catch (Exception e) {
		}
		return result;
	}

	// public <T extends Enum> T getEnumRand(Class<T> enumClass, int length, int
	// start) {
	// if (!enumClass.isEnum()) {
	// return null;
	// }
	// int code = getIntRandom(0, length - 1);
	// if (start == 0) {
	// code = code + start;
	// }
	// T result = null;
	// try {
	// result = enumClass.getEnumConstants()[code];
	// } catch (Exception e) {
	// }
	// return result;
	// }

	public <T extends Object> T getRandomValue(T[] liste) {
		if (liste == null || liste.length == 0) {
			return null;
		}
		return liste[getIntRandom(0, liste.length - 1)];
	}

	public String getRandomValue() {
		// 0 = Int;
		// 1 = Double
		// 2 = String
		// 3 = Char
		// 4 = Date
		// 5 = String Numérique
		// 6 = Vide

		int data = getIntRandom(0, 5);
		String result;
		if (data == 0) {
			result = String.valueOf(getIntRandom());
		} else if (data == 1) {
			result = String.valueOf(getDoubleRandom());
		} else if (data == 2) {
			result = String.valueOf(getStringRandom());
		} else if (data == 3) {
			result = String.valueOf(getStringRandom(1));
		} else if (data == 4) {
			result = String.valueOf(getDateRandom());
		} else if (data == 4) {
			result = String.valueOf(getStringNumRandom(1));
		} else {
			result = "";
		}
		return result;
	}
}
