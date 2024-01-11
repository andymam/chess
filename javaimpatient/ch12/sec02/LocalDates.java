package ch12.sec02;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.Month;
import java.time.Period;
import java.time.temporal.ChronoUnit;
import java.util.stream.Stream;

public class LocalDates {
    public static void main(String[] args) {
        LocalDate today = LocalDate.now(); // Today’s date
        System.out.println("today: " + today);

        LocalDate alonzosBirthday = LocalDate.of(1903, 6, 14);
        alonzosBirthday = LocalDate.of(1903, Month.JUNE, 14);
        // Uses the Month enumeration
        System.out.println("alonzosBirthday: " + alonzosBirthday);

        LocalDate programmersDay = LocalDate.of(2014, 1, 1).plusDays(255);
        // September 13, but in a leap year it would be September 12
        System.out.println("programmersDay: " + programmersDay);

        LocalDate independenceDay = LocalDate.of(2014, Month.JULY, 4);
        LocalDate christmas = LocalDate.of(2014, Month.DECEMBER, 25);

        System.out.println("Until christmas: " + independenceDay.until(christmas));
        System.out.println("Until christmas: " + independenceDay.until(christmas, ChronoUnit.DAYS));

        System.out.println(LocalDate.of(2016, 1, 31).plusMonths(1));
        System.out.println(LocalDate.of(2016, 3, 31).minusMonths(1));

        DayOfWeek startOfLastMillennium = LocalDate.of(1900, 1, 1).getDayOfWeek();
        System.out.println("startOfLastMillennium: " + startOfLastMillennium);
        System.out.println(startOfLastMillennium.getValue());
        System.out.println(DayOfWeek.SATURDAY.plus(3));
        
        Stream<LocalDate> allDaysInMay2018
        	= LocalDate.of(2018,5,1).datesUntil(LocalDate.of(2018,6,1));
        Stream<LocalDate> allMondaysIn2018
        	= LocalDate.of(2018,1,1).datesUntil(LocalDate.of(2019,1,1), Period.ofDays(7));
        System.out.println("allDaysInMay2018: " + allDaysInMay2018.toList());
        System.out.println("allMondaysIn2018: " + allMondaysIn2018.toList());
    }
}
