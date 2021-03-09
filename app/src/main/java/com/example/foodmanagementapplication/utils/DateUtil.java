package com.example.foodmanagementapplication.utils;

import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Locale;

public class DateUtil {
    public static final String SEPARATOR = "/";
    public static final  String FORMAT_DMMMMMYYYY = "d MMMM yyyy";
    public static final String FORMAT_DDMMYYYY = "dd/MM/yyyy";
    public static final DateTimeFormatter DATE_TIME_FORMATTER_DDMMYYYY = DateTimeFormatter.ofPattern(FORMAT_DDMMYYYY);

    private DateUtil() {
        throw new IllegalStateException("Never instantiate utility classes");
    }

    public static LocalDate toStringDateNumberToLocalDate(final String stringDate) throws DateTimeParseException {
        final StringBuilder newDate = new StringBuilder(stringDate.replace(" ", ""));
        if (newDate.toString().split(SEPARATOR).length == 1) {
            newDate.insert(2, SEPARATOR).insert(5, SEPARATOR);
        }
        if (newDate.length() == 8) {
            newDate.insert(6, "20");
        }
        final String res = newDate.toString();
        if (!res.matches("\\d{2}/\\d{2}/\\d{4}")) {
            throw new DateTimeParseException("Erreur, la date doit être avec des nombres", res, res.length());
        }
        final String[] splitRes = res.split("/");
        return toLocalDate(splitRes[2], splitRes[1], splitRes[0]);
    }

    public static LocalDate stringDateToLocalDate(final String stringDate, final String pattern)
            throws DateTimeParseException {
        return LocalDate.parse(stringDate.trim().toLowerCase(), DateTimeFormatter.ofPattern(pattern, Locale.FRANCE));
    }

    public static LocalDate toLocalDate(final String year, final String month, final String dayOfMonth) throws DateTimeParseException {
        return LocalDate.of(Integer.parseInt(year), Integer.parseInt(month), Integer.parseInt(dayOfMonth));
    }

    public static boolean checkPeriodicity(final LocalDate actualDate, final LocalDate dateToCompare) {
        return dateToCompare.isBefore(actualDate) || ((dateToCompare.isEqual(actualDate) || dateToCompare.isAfter(actualDate)) && Period.between(actualDate, dateToCompare).getDays() < 3);
    }

    public static LocalDate chooseAppropriatedDate(final String stringDate) {
        return stringDate.replace(" ", "").length() > 8 && stringDate.split(SEPARATOR).length == 1
                ? stringDateToLocalDate(stringDate, FORMAT_DMMMMMYYYY) : toStringDateNumberToLocalDate(stringDate);
    }
}
