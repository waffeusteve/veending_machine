/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.pkfrc.core.utilities.helper;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;

import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;

public class DateHelper {

	public static Integer getMonth(Date date) {
		GregorianCalendar gc = new GregorianCalendar();
		gc.setTime(date);
		return gc.get(Calendar.MONTH) + 1;
	}

	public static Integer numberMonthBetween(Date begin, Date end) {
		Integer mBegin = getMonth(begin);
		Integer mEnd = getMonth(end);
		return Math.abs(mBegin - mEnd) + 1;
	}

	public static Date firstDayOfYear(Date date) {
		GregorianCalendar gc = new GregorianCalendar();
		gc.setTime(date);
		int field = gc.getActualMinimum(Calendar.DAY_OF_YEAR);
		gc.set(Calendar.DAY_OF_YEAR, field);
		return gc.getTime();
	}

	public static Date firstDayOfMonth(Date date) {
		GregorianCalendar gc = new GregorianCalendar();
		gc.setTime(date);
		int field = gc.getActualMinimum(Calendar.DAY_OF_MONTH);
		gc.set(Calendar.DAY_OF_MONTH, field);
		return gc.getTime();
	}

	public static Date lastDayOfMonth(Date date) {
		GregorianCalendar gc = new GregorianCalendar();
		gc.setTime(date);
		int field = gc.getActualMaximum(Calendar.DAY_OF_MONTH);
		gc.set(Calendar.DAY_OF_MONTH, field);
		return gc.getTime();
	}

	public static Date lastDayOfYear(Date date) {
		GregorianCalendar gc = new GregorianCalendar();
		gc.setTime(date);
		int field = gc.getActualMaximum(Calendar.DAY_OF_YEAR);
		gc.set(Calendar.DAY_OF_YEAR, field);
		return gc.getTime();
	}

	public static int numberDayInMonth(Date date) {
		GregorianCalendar gc = new GregorianCalendar();
		gc.setTime(date);
		return gc.getActualMaximum(Calendar.DAY_OF_MONTH);
	}

	public static int numberDayInYear(Date date) {
		GregorianCalendar gc = new GregorianCalendar();
		gc.setTime(date);
		return gc.getActualMaximum(Calendar.DAY_OF_YEAR);
	}

	public static int getElement(Date date, int field) {
		GregorianCalendar gc = new GregorianCalendar();
		gc.setTime(date);
		return gc.get(field);
	}

	public static int nombreMoisIntercale(Date date1, Date date2) throws Exception {
		GregorianCalendar gc1 = new GregorianCalendar();
		GregorianCalendar gc2 = new GregorianCalendar();
		if (date1.compareTo(date2) == 1) {
			gc1.setTime(date2);
			gc2.setTime(date1);
		} else {
			gc1.setTime(date1);
			gc2.setTime(date2);
		}
		int gap = 0;
		gc1.add(GregorianCalendar.MONTH, 1);
		while (gc1.compareTo(gc2) <= 0) {
			gap++;
			gc1.add(GregorianCalendar.MONTH, 1);
		}
		return gap;
	}

	public static boolean between(Date start, Date end, Date dte) {
		return before(start, dte) && before(dte, end);
	}

	public static boolean betweenOrEqual(Date start, Date end, Date dte) {
		return beforeOrEqual(start, dte) && beforeOrEqual(dte, end);
	}

	private static SimpleDateFormat getSimpleDateFormat() {
		return getSimpleDateFormat(formatDateSimple);
	}

	private static SimpleDateFormat getSimpleDateFormat(String format) {
		SimpleDateFormat simple = new SimpleDateFormat(format);
		simple.setTimeZone(TimeZone.getTimeZone("GMT"));
		simple.setLenient(false);
		return simple;
	}

	public static boolean before(Date start, Date end) {
		SimpleDateFormat simple = getSimpleDateFormat();
		try {
			start = simple.parse(simple.format(start));
			end = simple.parse(simple.format(end));
		} catch (Exception e) {
		}
		return start.compareTo(end) < 0 ? true : false;
	}

	public static boolean beforeOrEqual(Date start, Date end) {
		SimpleDateFormat simple = getSimpleDateFormat();
		try {
			start = simple.parse(simple.format(start));
			end = simple.parse(simple.format(end));
		} catch (Exception e) {
		}
		return start.compareTo(end) <= 0 ? true : false;
	}

	public static boolean after(Date start, Date end) {
		SimpleDateFormat simple = getSimpleDateFormat();
		try {
			start = simple.parse(simple.format(start));
			end = simple.parse(simple.format(end));
		} catch (Exception e) {
		}
		return start.compareTo(end) > 0 ? true : false;
	}

	public static boolean afterOrEqual(Date start, Date end) {
		SimpleDateFormat simple = getSimpleDateFormat();
		try {
			start = simple.parse(simple.format(start));
			end = simple.parse(simple.format(end));
		} catch (Exception e) {
		}
		return start.compareTo(end) >= 0 ? true : false;
	}

	public static boolean equal(Date start, Date end) {
		SimpleDateFormat simple = getSimpleDateFormat();
		try {
			start = simple.parse(simple.format(start));
			end = simple.parse(simple.format(end));
		} catch (Exception e) {
		}
		return start.compareTo(end) == 0 ? true : false;
	}

	public static long getNbreJourEntre2Date(Date date1, Date date2) {
		Calendar c1 = Calendar.getInstance();
		Calendar c2 = Calendar.getInstance();

		c1.setTime(date1);

		c2.setTime(date2);

		long duree1 = c1.getTimeInMillis();
		long duree2 = c2.getTimeInMillis();

		long nbjour = ((duree1 - duree2) / 1000);
		nbjour = nbjour / (3600 * 24);

		return nbjour + 1;
	}

	public static int getNbEcheance(long duree, double periode) {
		double reste = (duree % periode);

		if (reste == 0)
			return (int) (duree / periode);
		else
			return (int) Math.floor(duree / periode) + 1;
	}

	public static int getNbJourRestant(long duree, double periode) {
		double reste = (duree % periode);
		return (int) reste;
	}

	public static int getDureePeriodicite(Character periode) {
		switch (periode) {
		case 'M':
			return 30;
		case 'A':
			return 360;
		case 'T':
			return 90;
		case 'S':
			return 180;
		case 'H':
			return 7;
		case 'J':
			return 1;
		case 'B':
			return 60;
		case 'Q':
			return 15;
		default:
			return 0;
		}

	}

	public static String ajoutJour(Date date1, int nbJour) {
		Calendar c1 = Calendar.getInstance();

		c1.setTime(date1);
		c1.add(Calendar.DAY_OF_MONTH, nbJour);
		return format(c1.getTime());

	}

	public static String ajoutMois(Date date1, int nbMois) {
		Calendar c1 = Calendar.getInstance();

		c1.setTime(date1);
		c1.add(Calendar.MONTH, nbMois);
		return format(c1.getTime());

	}

	public static String getDateFinMois(Date date1) {
		Calendar c1 = Calendar.getInstance();

		c1.setTime(date1);

		int lastDay = c1.getActualMaximum(Calendar.DAY_OF_MONTH);

		if (c1.get(Calendar.DAY_OF_MONTH) != lastDay) { // si ce n'est pas deja
			// la date de fin de
			// mois
			c1.set(c1.get(Calendar.YEAR), c1.get(Calendar.MONTH), lastDay);
		}
		return format(c1.getTime());

	}

	public static final int DATE_REF = 57;

	public static final String formatDateSimple = "dd/MM/yyyy";

	public static final String formatDateHeure = "dd/MM/yyyy HH:mm";

	public static int differenceDate(Date dte1, Date dte2, int unite, boolean net) {

		int ret = 1;
		if (dte1.getTime() < dte2.getTime())
			ret = -1;

		GregorianCalendar date1 = new GregorianCalendar();
		GregorianCalendar date2 = new GregorianCalendar();
		if (ret < 0) {
			date1.setTimeInMillis(dte1.getTime());
			date2.setTimeInMillis(dte2.getTime());
		} else {
			date1.setTimeInMillis(dte2.getTime());
			date2.setTimeInMillis(dte1.getTime());
		}
		ret = 1;
		switch (unite) {
		case Calendar.YEAR:
			ret = date2.get(Calendar.YEAR) - date1.get(Calendar.YEAR);
			break;
		case Calendar.MONTH:
			ret = date2.get(Calendar.YEAR) - date1.get(Calendar.YEAR);
			ret = 12 * ret - date1.get(Calendar.MONTH);
			ret += date2.get(Calendar.MONTH);

			if (date2.get(Calendar.DAY_OF_MONTH) >= date1.get(Calendar.DAY_OF_MONTH))
				ret++;

			break;
		case Calendar.DATE:
			date2.set(Calendar.HOUR, 0);
			date2.set(Calendar.MINUTE, 0);
			date2.set(Calendar.SECOND, 0);
			date2.set(Calendar.MILLISECOND, 0);
			date1.set(Calendar.HOUR, 0);
			date1.set(Calendar.MINUTE, 0);
			date1.set(Calendar.SECOND, 0);
			date1.set(Calendar.MILLISECOND, 0);
			long nbrJr = (long) (date2.getTimeInMillis() - date1.getTimeInMillis()) / (24 * 60 * 60 * 1000);
			ret = (int) nbrJr + (net ? 0 : 1);
			break;
		default:
			System.out.println("unite non pris en compre dans la difference des date");
			ret = 0;
		}
		return ret;
	}

	public static int differenceDate(Date dte1, Date dte2, int unite) {
		return differenceDate(dte1, dte2, unite, false);
	}

	/**
	 * *
	 * <p>
	 * Auteur : <b>KDEC</b>
	 * </p>
	 * 
	 * Indique si deux dates sont �gales au jour pr�s
	 * 
	 * @param date1
	 *            Date
	 * @param date2
	 *            Date
	 * @return boolean
	 */

	public static boolean datesEgales(Date date1, Date date2) {
		GregorianCalendar dateReg = new GregorianCalendar();
		GregorianCalendar today = new GregorianCalendar();

		dateReg.setTime(date1);
		today.setTime(date2);

		return (dateReg.get(GregorianCalendar.YEAR) == today.get(GregorianCalendar.YEAR) && // �galit�
																							// sur
																							// l'ann�e
				dateReg.get(GregorianCalendar.MONTH) == today.get(GregorianCalendar.MONTH) && // �galit�
																								// sur
																								// le
																								// mois
				dateReg.get(GregorianCalendar.DAY_OF_MONTH) == today.get(GregorianCalendar.DAY_OF_MONTH)); // �galit�
																											// sur
																											// le
																											// jour

	}

	/**
	 * <p>
	 * Auteur : <b>Nad�ge</b>
	 * </p>
	 * 
	 * Compare deux date date1 et date2 au jour pr�s selon le format d�finie
	 * 
	 * @param date1
	 *            Date
	 * @param date2
	 *            Date
	 * @param dateformat
	 *            DateFormat : format � appliquer aux dates
	 * @return 0 si les deux dates sont �gales
	 * @return
	 *         <p>
	 *         sup�rieur 0 si date1(02/01) est sup�rieure � date2(01/01)
	 *         </p>
	 * @return
	 *         <p>
	 *         inf�rieure 0 si date1 (01/01) est inf�rieure � date2(02/01)
	 *         </p>
	 */
	public static int compareDates(Date date1, Date date2, DateFormat dateformat) {

		Date dateReg = new Date();
		Date today = new Date();
		try {
			dateReg = dateformat.parse(dateformat.format(date1));
			today = dateformat.parse(dateformat.format(date2));
		} catch (ParseException e) {

			e.printStackTrace();
		}
		return dateReg.compareTo(today);

	}

	public static int compareDates(Date date1, Date date2, String strFormat) {
		SimpleDateFormat simple = getSimpleDateFormat(strFormat);
		return compareDates(date1, date2, simple);

	}

	/**
	 * <p>
	 * Auteur : <b>Nad�ge</b>
	 * </p>
	 * 
	 * Compare deux date date1 et date2 au jour pr�s selon le format dd/MM/YYYY
	 * 
	 * @param date1
	 *            Date
	 * @param date2
	 *            Date
	 * @return 0 si les deux dates sont �gales
	 * @return
	 *         <p>
	 *         sup�rieur 0 si date1(02/01) est sup�rieure � date2(01/01)
	 *         </p>
	 * @return
	 *         <p>
	 *         inf�rieure 0 si date1 (01/01) est inf�rieure � date2(02/01)
	 *         </p>
	 * @see compareDates(Date date1, Date date2, DateFormat
	 *      dateformat)
	 * @since 3.0
	 */
	public static int compareDates(Date date1, Date date2) {
		SimpleDateFormat simple = getSimpleDateFormat();
		return compareDates(date1, date2, simple);

	}

	/**
	 * <p>
	 * Auteur : <b>KDEC</b>
	 * </p>
	 * V�rifie si la chaine saisie est une date correcte
	 * 
	 * @return true : si la chane est une date valide
	 * @return false : si la chane n'est pas une date valide
	 * @see isValidDate(String sDateIn)
	 */
	public static boolean isValidateDate(String sDateIn) {
		Date d1 = isValidDate(sDateIn);
		if (d1 != null) {
			return true;
			// boolean vale = d1.compareTo(new Date()) < 1 ? true : false;
			// return vale;
		} else {
			return false;
		}
	}

	/**
	 * <p>
	 * Auteur : <b>KDEC</b>
	 * </p>
	 * V�rifie si la chaine saisie est une date correcte
	 * 
	 * @return true : si la chane est une date valide
	 * @return false : si la chane n'est pas une date valide
	 * @see isValidDate(String sDateIn, Date dteLim)
	 */
	public static Date isValidDate(String sDateIn) {
		SimpleDateFormat simple;
		boolean shortDate = false;
		int slen = sDateIn.length();
		if (slen == 8) {
			simple = getSimpleDateFormat("dd/MM/yy");
			shortDate = true;
		} else {
			simple = getSimpleDateFormat(formatDateSimple);
		}
		ParsePosition pos = new ParsePosition(0);
		Date dDate = simple.parse(sDateIn, pos);

		if (shortDate) {
			if (dDate != null) {
				String newDroite = new String();
				String gauche = sDateIn.substring(0, 6);
				String droite = sDateIn.substring(6);
				int annee = Integer.parseInt(droite);
				if (annee >= DATE_REF) {
					newDroite = "19" + droite;
				} else {
					newDroite = "20" + droite;
				}
				String newDate = gauche.concat(newDroite);
				simple = getSimpleDateFormat(formatDateSimple);
				pos = new ParsePosition(0);
				dDate = simple.parse(newDate, pos);
			}
		}
		return dDate;
	}

	/**
	 * <p>
	 * Auteur : <b>KDEC</b>
	 * </p>
	 * V�rifie si la chaine saisie est une date correcte, puis V�rifie si cette date
	 * est inf�rieure � une autre
	 * 
	 * @param sDateIn
	 *            String : date initiale
	 * @param dteLim
	 *            Date : date limite
	 * @return Date : correspondant � la date initiale si date initiale < date
	 *         limites
	 * @return null : si date limite < date initiale
	 * @see isValidDate(String sDateIn)
	 */
	public static Date isValidDate(String sDateIn, Date dteLim) {
		Date dVal = isValidDate(sDateIn);
		if (dVal != null) {
			if (dteLim.before(dVal)) {
				dVal = null;
			}
		}
		return dVal;
	}

	/**
	 * <p>
	 * Auteur : <b>KDEC</b>
	 * </p>
	 * 
	 * Permet d'appliquer un format � une date
	 * 
	 * @param dte
	 *            Date : date � laquelle le format doit �tre appliqu�
	 * @param format
	 *            String : format � appliquer ex: dd/MM/YYYY
	 * @see formatDate(long dte, String format)
	 */
	public static String formatDate(Date dte, String format) {
		if (dte == null) {
			return StringHelper.lpad("", ' ', format.length());
		}
		try {
			SimpleDateFormat sdfYMD = getSimpleDateFormat(format);
			return sdfYMD.format(dte);
		} catch (Exception e) {
			e.printStackTrace();
			return "-";
		}
	}

	/**
	 * <p>
	 * Auteur : <b>KDEC</b>
	 * </p>
	 * 
	 * Permet d'appliquer un format � une date
	 * 
	 * @param dte
	 *            long : date � laquelle le format doit �tre appliqu�
	 * @param format
	 *            String : format � appliquer ex: dd/MM/YYYY
	 * @see formatDate(Date dte, String format)
	 * @see FormateDouble(double value)
	 */
	public static String formatDate(long dte, String format) {
		return formatDate(new Date(dte), format);
	}

	/**
	 * <p>
	 * Auteur : <b>KDEC</b>
	 * </p>
	 * Classe utilitaire servant � limiter le nombre de caractere dans un jTextfield
	 * , elle n'accepte que les entiers exemple this.txtCleHis.setDocument(new
	 * Utilitaires.NumberDocument(2));
	 */
	public static class TimeDocument extends PlainDocument {

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		private boolean time;
		private int limite = 2;

		public TimeDocument(boolean isTime) {
			super();
			this.time = isTime;
		}

		public void insertString(int offs, String str, AttributeSet a) throws BadLocationException {
			if ((str.length() + getLength() > limite) || (str == null)) {
				return;
			}
			char[] source = str.toCharArray();
			char[] result = new char[source.length];
			int j = 0;

			for (int i = 0; i < result.length; i++) {
				if (Character.isDigit(source[i])) {
					result[j++] = source[i];
				} else {
					// toolkit.beep();
				}
			}
			if (j > 0) {
				StringBuffer st = new StringBuffer(super.getText(0, getLength()));
				st.insert(offs, String.valueOf(result));
				int val = Integer.parseInt(st.toString());// this.toString()+String.valueOf(result));
				if (time) {
					if ((val < 0) || (val > 23)) {
						return;
					}
				} else {
					if ((val < 0) || (val > 59)) {
						return;
					}
				}
			}
			super.insertString(offs, new String(result, 0, j), a);
		}
	}

	public static String format(Date date) {
		return format(date, formatDateSimple);
	}

	public static String format(Date date, String format) {
		if (date == null) {
			return null;
		}
		SimpleDateFormat simple = getSimpleDateFormat(format);
		return simple.format(date);
	}

	public static Date parse(String y, String format) {
		SimpleDateFormat simple = getSimpleDateFormat(format);

		try {
			return simple.parse(y);
		} catch (ParseException e) {
			return null;
		}
	}

	public static Date parse(String y) {
		return parse(y, formatDateSimple);
	}

	public static Date getEcheance(Date date, int field, int count) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(field, count);
		calendar.add(Calendar.DAY_OF_MONTH, -1);
		return calendar.getTime();
	}

	public static Date add(Date date, int field, int count) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(field, count);
		return calendar.getTime();
	}

	public static Date firstHourOfTheDay(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);

		calendar.set(Calendar.HOUR_OF_DAY, calendar.getActualMinimum(Calendar.HOUR_OF_DAY));
		calendar.set(Calendar.MINUTE, calendar.getActualMinimum(Calendar.MINUTE));
		calendar.set(Calendar.SECOND, calendar.getActualMinimum(Calendar.SECOND));
		calendar.set(Calendar.MILLISECOND, calendar.getActualMinimum(Calendar.MILLISECOND));

		return calendar.getTime();
	}

	public static Date lastHourOfTheDay(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);

		calendar.set(Calendar.HOUR_OF_DAY, calendar.getActualMaximum(Calendar.HOUR_OF_DAY));
		calendar.set(Calendar.MINUTE, calendar.getActualMaximum(Calendar.MINUTE));
		calendar.set(Calendar.SECOND, calendar.getActualMaximum(Calendar.SECOND));
		calendar.set(Calendar.MILLISECOND, calendar.getActualMaximum(Calendar.MILLISECOND));

		return calendar.getTime();
	}

	public static Date firstDayOfTheMonth(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);

		calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMinimum(Calendar.DAY_OF_MONTH));
		calendar.set(Calendar.HOUR_OF_DAY, calendar.getActualMinimum(Calendar.HOUR_OF_DAY));
		calendar.set(Calendar.MINUTE, calendar.getActualMinimum(Calendar.MINUTE));
		calendar.set(Calendar.SECOND, calendar.getActualMinimum(Calendar.SECOND));
		calendar.set(Calendar.MILLISECOND, calendar.getActualMinimum(Calendar.MILLISECOND));

		return calendar.getTime();
	}

	public static Date lastDayOfTheMonth(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);

		calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
		calendar.set(Calendar.HOUR_OF_DAY, calendar.getActualMaximum(Calendar.HOUR_OF_DAY));
		calendar.set(Calendar.MINUTE, calendar.getActualMaximum(Calendar.MINUTE));
		calendar.set(Calendar.SECOND, calendar.getActualMaximum(Calendar.SECOND));
		calendar.set(Calendar.MILLISECOND, calendar.getActualMaximum(Calendar.MILLISECOND));

		return calendar.getTime();
	}

	public static Date firstMonthOfTheYear(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);

		calendar.set(Calendar.MONTH, calendar.getActualMinimum(Calendar.MONTH));
		calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMinimum(Calendar.DAY_OF_MONTH));
		calendar.set(Calendar.HOUR_OF_DAY, calendar.getActualMinimum(Calendar.HOUR_OF_DAY));
		calendar.set(Calendar.MINUTE, calendar.getActualMinimum(Calendar.MINUTE));
		calendar.set(Calendar.SECOND, calendar.getActualMinimum(Calendar.SECOND));
		calendar.set(Calendar.MILLISECOND, calendar.getActualMinimum(Calendar.MILLISECOND));

		return calendar.getTime();
	}

	public static Date lastMonthOfTheYear(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);

		calendar.set(Calendar.MONTH, calendar.getActualMaximum(Calendar.MONTH));
		calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
		calendar.set(Calendar.HOUR_OF_DAY, calendar.getActualMaximum(Calendar.HOUR_OF_DAY));
		calendar.set(Calendar.MINUTE, calendar.getActualMaximum(Calendar.MINUTE));
		calendar.set(Calendar.SECOND, calendar.getActualMaximum(Calendar.SECOND));
		calendar.set(Calendar.MILLISECOND, calendar.getActualMaximum(Calendar.MILLISECOND));

		return calendar.getTime();
	}

	public static Date set(Date date, int field, int nb) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.set(field, nb);
		return calendar.getTime();
	}

	public static int get(Date date, int field) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		return calendar.get(field);
	}

	public static Boolean isDateInFuture(Object object) {

		if (object == null) {
			return false;
		}

		try {
			Date date = (Date) object;
			Calendar c1 = Calendar.getInstance();
			c1.setTime(date);
			if (c1.after(Calendar.getInstance())) {
				return true;
			} else {
				return false;
			}

		} catch (Exception e) {

			return false;
		}
	}

	public static Boolean isDateInPast(Object object) {

		if (object == null) {
			return false;
		}

		try {
			Date date = (Date) object;
			Calendar c1 = Calendar.getInstance();
			c1.setTime(date);
			if (c1.before(Calendar.getInstance())) {
				return true;
			} else {
				return false;
			}

		} catch (Exception e) {

			return false;
		}
	}

	public static boolean isSameDay(Date date1, Date date2) {
		if (date1 == null || date2 == null) {
			throw new IllegalArgumentException("The dates must not be null");
		}
		Calendar cal1 = Calendar.getInstance();
		cal1.setTime(date1);
		Calendar cal2 = Calendar.getInstance();
		cal2.setTime(date2);
		return isSameDay(cal1, cal2);
	}

	public static boolean isSameDay(Calendar cal1, Calendar cal2) {
		if (cal1 == null || cal2 == null) {
			throw new IllegalArgumentException("The dates must not be null");
		}
		return (cal1.get(Calendar.ERA) == cal2.get(Calendar.ERA) && cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR)
				&& cal1.get(Calendar.DAY_OF_YEAR) == cal2.get(Calendar.DAY_OF_YEAR));
	}

	public static boolean isToday(Date date) {
		return isSameDay(date, Calendar.getInstance().getTime());
	}

	public static boolean isDateNotExpired(Date date) {
		return isDateInFuture(date) || isToday(date);
	}

	public static boolean isDateExpired(Date date) {
		return !isDateNotExpired(date);
	}
	
	/**
	 * 
	 * @param dateDebut de la periode
	 * @param periode en mois
	 * @param periodiciteReelle
	 * @param percevoirAvant
	 * @return
	 */
	public static Date getFinPeriode(Date dateDebut, int periode, Boolean periodiciteReelle, Boolean percevoirAvant) {
		Date dateFin = null;
		if (periodiciteReelle) {
			// Périodicité réelle
			// On set le jour d'effet
			int joursdateDebut = get(dateDebut, Calendar.DAY_OF_MONTH);
			if (percevoirAvant) {
				dateFin = dateDebut;
			} else {
				dateFin = add(dateDebut, Calendar.MONTH, periode);
				int joursMois = numberDayInMonth(dateFin);
				if(joursdateDebut>joursMois){
					joursdateDebut=joursMois;
				}
				dateFin = set(dateFin, Calendar.DAY_OF_MONTH, joursdateDebut - 1);
			}
		} else {
			dateFin = dateDebut;
			//numéro du mois du début de la période
			int moisDeBase = getMonth(dateFin);
			//nombre de fois que la periode s'est écouléé depuis le début de l'année
			int facteur = moisDeBase / periode;
			//on positionne le mois de base
			moisDeBase = 1 + (facteur-1) * periode;
			
			if (percevoirAvant) {
				dateFin = set(dateFin, Calendar.MONTH, moisDeBase - 1);
				dateFin = firstDayOfMonth(dateFin);
			} else {
				dateFin = set(dateFin, Calendar.MONTH, moisDeBase + periode - 1);
				dateFin = lastDayOfMonth(dateFin);
			}
		}
		return dateFin;
	}

}
