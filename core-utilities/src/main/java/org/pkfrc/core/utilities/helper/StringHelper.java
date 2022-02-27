/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.pkfrc.core.utilities.helper;

import java.sql.Time;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
/*
import com.ibm.icu.text.Normalizer;*/

/**
 * 
 * @author Gaetan
 */
public class StringHelper {

	private static final String EMAIL_REGEX = "^[\\w-\\+]+(\\.[\\w]+)*@[\\w-]+(\\.[\\w]+)*(\\.[a-z]{2,})$";

	/**
	 * Permet savoir si oui ou non une chaine est vide ou non
	 * 
	 * @param str
	 *            chaine dont on veut vérifier le contenu
	 * @return True si la chaine est vide et False sinon
	 */
	public static boolean isEmpty(String str) {
		if (str == null) {
			return true;
		}
		return str.trim().isEmpty();
	}

	public static boolean isNotEmpty(String string) {
		return !isEmpty(string);
	}

	/**
	 * @deprecated Use {@link DateHelper#parse(String)} instead
	 * @param date
	 * @return
	 */
	@Deprecated
	public static Date stringToDate(String date) {
		return DateHelper.parse(date);
		// SimpleDateFormat dateFormattee = new
		// SimpleDateFormat(MultiLangue.getFormatLongDate());
		// dateFormattee.setLenient(false);
		// Date maDate = null;
		// try {
		// maDate = dateFormattee.parse(date);
		// return maDate;
		// } catch (Exception e) {
		// return null;
		// }
	}

	/**
	 * @deprecated Use {@link DateHelper#parse(String, String)} instead
	 * @param date
	 * @return
	 */
	@Deprecated
	public static Date stringToDate(String date, String format) {
		return DateHelper.parse(date, format);
	}

	/**
	 * @deprecated Use {@link DateHelper#format(Date)} instead
	 * @param date
	 * @return
	 */
	public static String dateToString(Date date) {
		return DateHelper.format(date);
	}

	/**
	 * @deprecated Use {@link DateHelper#formatDate(Date,String)} instead
	 */
	public static String dateToString(Date date, String format) {
		return DateHelper.formatDate(date, format);
	}

	public static Time stringToTime(String time) {
		SimpleDateFormat dateFormattee = new SimpleDateFormat("HH:mm");
		dateFormattee.setLenient(false);
		Date myTime = null;
		try {
			myTime = dateFormattee.parse(time);
			return new Time(myTime.getTime());
		} catch (Exception e) {
			return null;
		}
	}

	public static String timeToString(Time time) {
		SimpleDateFormat dateFormattee = new SimpleDateFormat("HH:mm");
		dateFormattee.setLenient(false);

		try {
			return dateFormattee.format(time);

		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * Permet de completer par la droite la chaine en entr�e avec le caract�re donn�
	 * sur la longueur sp�cifi�e
	 * 
	 * @param input
	 *            chaine en entr�e
	 * @param padding
	 *            le caract�re de remplacement
	 * @param length
	 *            la longueur de la chaine de retour
	 * @return La chaine compl�t�
	 * @author Gaetan
	 */
	public static String rpad(String input, char padding, int length) {
		if (input == null) {
			input = "";
		}
		if (input.length() >= length) {
			return input.substring(0, length);
		}
		String result = input;
		while (result.length() < length) {
			result = result + String.valueOf(padding);
		}
		return result;
	}

	/**
	 * Permet de completer par la gauche la chaine en entr�e avec le caract�re donn�
	 * sur la longueur sp�cifi�e
	 * 
	 * @param input
	 *            chaine en entr�e
	 * @param padding
	 *            le caract�re de remplacement
	 * @param length
	 *            la longueur de la chaine de retour
	 * @return La chaine compl�t�
	 * @author Gaetan
	 */
	public static String lpad(String input, char padding, int length) {
		if (input == null) {
			input = "";
		}
		if (input.length() >= length) {
			return input.substring(input.length() - length, input.length());
		}
		String result = input;
		while (result.length() < length) {
			result = String.valueOf(padding) + result;
		}
		return result;
	}

	public static String centerText(String input, char padding, int length) {
		if (input.length() > length) {
			return lpad(input, padding, length);
		}
		if (input.length() % 2 != length % 2) {
			input += String.valueOf(padding);
		}
		int addLength = (length - input.length()) / 2;
		String toComplete = lpad("", padding, addLength);
		return toComplete + input + toComplete;
	}

	private static int getTrimLength(String input, int length) {
		if (isEmpty(input)) {
			return 0;
		}
		if (input.length() <= length) {
			return 0;
		}
		return input.length() - length;
	}

	public static String ltrim(String input, int length) {

		int nbChars = getTrimLength(input, length);

		if (nbChars == 0) {
			return input;
		}
		String result = input;
		result = result.substring(nbChars, input.length());
		return result;

	}

	public static String rtrim(String input, int length) {

		int nbChars = getTrimLength(input, length);

		if (nbChars == 0) {
			return input;
		}
		String result = input;
		result = result.substring(0, nbChars);
		return result;

	}

	/**
	 * 
	 * @param texte
	 * @param tokken
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static <T> List<T> toList(String texte, String tokken) {
		List<T> liste = new ArrayList<T>(0);
		if (StringHelper.isEmpty(texte)) {
			return liste;
		}
		StringTokenizer test = new StringTokenizer(texte, tokken);
		while (test.hasMoreElements()) {
			liste.add((T) test.nextElement());
		}
		return liste;

	}

	/**
	 *
	 * @param liste
	 * @param tokken
	 * @param <T>
	 * @return
	 */
	public static <T> String listToString(List<T> liste, String tokken) {
		String texte = "";
		if (liste == null || liste.isEmpty()) {
			return texte;
		}
		texte = String.valueOf(liste.get(0));
		liste.remove(0);
		Iterator<T> iter = liste.iterator();
		while (iter.hasNext()) {
			String str = String.valueOf(iter.next());
			texte = texte + tokken + str;
		}
		return texte;

	}

 /**
	 * remplace les accent d'une chaine de caractere
	 * 
	 * @param
	 * @return
	 *//*
	public static String stripAccent(String s) {
		String temp = Normalizer.normalize(s, Normalizer.NFD);
		return temp.replaceAll("[^\\p{ASCII}]", "");
	}*/

	public static boolean containsSpecialChar(String word) {
		int length = word.length();
		boolean result = false;
		int i = 0;
		while (!result && i < length) {
			if (!Character.isAlphabetic(word.charAt(i))) {
				result = true;
			}
			i++;
		}
		return result;
	}

	public static Integer countSpecialChar(String word) {
		int length = word.length();
		Integer result = 0;
		int i = 0;
		while (i < length) {
			if (!Character.isAlphabetic(word.charAt(i))) {
				result++;
			}
			i++;
		}
		return result;
	}

	public static boolean containsUpperCasse(String word) {
		int length = word.length();
		boolean result = false;
		int i = 0;
		while (!result && i < length) {
			if (Character.isUpperCase(word.charAt(i))) {
				result = true;
			}
			i++;
		}
		return result;
	}

	public static Integer countUpperCasse(String word) {
		int length = word.length();
		Integer result = 0;
		int i = 0;
		while (i < length) {
			if (Character.isUpperCase(word.charAt(i))) {
				result++;
			}
			i++;
		}
		return result;
	}

	public static boolean containsDigit(String word) {
		int length = word.length();
		boolean result = false;
		int i = 0;
		while (!result && i < length) {
			if (Character.isDigit(word.charAt(i))) {
				result = true;
			}
			i++;
		}
		return result;
	}

	public static Integer countDigit(String word) {
		int length = word.length();
		Integer result = 0;
		int i = 0;
		while (i < length) {
			if (Character.isDigit(word.charAt(i))) {
				result++;
			}
			i++;
		}
		return result;
	}

	/**
	 * Permet de mettre la premiere lettre en majuscule
	 * 
	 * @param value
	 * @return
	 */
	public static String firstLetterToUpperCase(String value) {
		String result = "";
		String firstCharacter = "";
		if (value.length() > 0) {
			firstCharacter = value.substring(0, 1).toUpperCase();
			result = firstCharacter + value.substring(1);
		}
		return result;
	}

	/**
	 * Permet de mettre la premiere lettre en minuscule
	 * 
	 * @param value
	 * @return
	 */
	public static String firstLetterToLowerCase(String value) {
		String result = "";
		String firstCharacter = "";
		if (value.length() > 0) {
			firstCharacter = value.substring(0, 1).toLowerCase();
			result = firstCharacter + value.substring(1);
		}
		return result;
	}

	public String replaceMessageParameters(String message, Object[] params) {
		return MessageFormat.format(message, params);
	}

	/**
	 * 
	 * @param message
	 * @param params
	 * @return
	 */
	public static String format(String message, Object[] params) {
		return MessageFormat.format(message, params);
	}

	public static boolean isValidEmail(String email) {
		if (isEmpty(email)) {
			return false;
		}
		Pattern pattern = Pattern.compile(EMAIL_REGEX, Pattern.CASE_INSENSITIVE);
		Matcher matcher = pattern.matcher(email);
		return matcher.matches();
	}

	private static final String ALPHA_NUMERIC_STRING = "abcdefghijklmnopqrstuvwxyz0123456789";

	public static String randomAlphaNumeric(int count) {
		StringBuilder builder = new StringBuilder();
		while (count-- != 0) {
			int character = (int) (Math.random() * ALPHA_NUMERIC_STRING.length());
			builder.append(ALPHA_NUMERIC_STRING.charAt(character));
		}
		return builder.toString();
	}

	/**
	 * Count the occurrences of the substring {@code sub} in string {@code str}.
	 * @param text string to search in
	 * @param sub string to search for
	 */
	public static int countOccurrencesOf(String text, String sub) {
		if (isEmpty(text) || isEmpty(sub)) {
			return 0;
		}

		int count = 0;
		int pos = 0;
		int idx;
		while ((idx = text.indexOf(sub, pos)) != -1) {
			++count;
			pos = idx + sub.length();
		}
		return count;
	}
}
