package org.pkfrc.core.utilities.helper;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.util.Calendar;
import java.util.Date;

public class CodeHelperProjurise {

    public static String getSapceFill(Long id, int numberOfChar) {
        String toFormat = "%0" + numberOfChar + "d";
        return String.format(toFormat, id);
    }

    public static String getCode(String prefix, String sufix, Long numberOfItem, int numberOfChar) {
        prefix = prefix != null ? prefix : "";
        sufix = sufix != null ? sufix : "";
        return prefix + getSapceFill(numberOfItem, numberOfChar) + sufix;
    }

    public static String getCode(String prefix, String sufix, Integer numberOfItem, int numberOfChar) {
        return getCode(prefix, sufix, numberOfItem.longValue(), numberOfChar);
    }

    public static String thisYear() {
        return String.valueOf(LocalDateTime.now().getYear());
    }

    public static String thisMonth() {
        return String.valueOf(LocalDateTime.now().getMonth().getValue());
    }

    public static String today() {
        return String.valueOf(LocalDateTime.now().getDayOfMonth());
    }

    public static void main(String[] args) {
        // Get the current date and time
        LocalDateTime currentTime = LocalDateTime.now();
        System.out.println("Date et heure courante : " + currentTime);

        LocalDate date1 = currentTime.toLocalDate();
        System.out.println("Date courante : " + date1);

        Month mois = currentTime.getMonth();
        int jour = currentTime.getDayOfMonth();
        int heure = currentTime.getHour();

        System.out.println("Mois : " + mois.getValue() + ", jour : " + jour + ", heure : " + heure);
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        System.out.println("test : " + String.valueOf(cal.get(Calendar.DAY_OF_MONTH)+ " "+ new Date()));
        // Avoir le 25 décembre 2023
        LocalDateTime date2 = currentTime.withDayOfMonth(25).withYear(2023).withMonth(12);
        System.out.println("Date modifiée : " + date2);

        // une autre façon
        LocalDate date3 = LocalDate.of(2023, Month.DECEMBER, 25);
        System.out.println("Autre façon de faire : " + date3);

        // On peut même parser une date depuis un String
        LocalTime parsed = LocalTime.parse("20:15:30");
        System.out.println("Date parsée : " + parsed);
    }
}