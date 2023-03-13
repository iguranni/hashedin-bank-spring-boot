package com.hashedin.hashedinbank.utils;

import com.hashedin.hashedinbank.exception.DataTransformationException;
import org.apache.poi.util.RecordFormatException;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoField;

public class CommonUtils {

    private CommonUtils() {
    }

    /**
     * data formatters to validate date columns
     */
    public static final DateTimeFormatter dateTimeFormatter = new DateTimeFormatterBuilder()
            .appendOptional(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS"))
            .appendOptional(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
            .appendOptional(DateTimeFormatter.ofPattern("yyyy-MM-dd"))
            .parseDefaulting(ChronoField.HOUR_OF_DAY, 0)
            .parseDefaulting(ChronoField.MINUTE_OF_HOUR, 0)
            .parseDefaulting(ChronoField.SECOND_OF_MINUTE, 0)
            .toFormatter();

    public static final DateTimeFormatter dateTimeFormatter2 = new DateTimeFormatterBuilder()
            .appendOptional(DateTimeFormatter.ofPattern("yyyy_MM_dd_HH_mm_ss"))
            .toFormatter();

    /**
     * validating the date columns, if any past date exists
     */
    public static boolean isPastEffectiveEndDate(LocalDateTime effectiveEndDate) {
        return effectiveEndDate.isBefore(LocalDateTime.now());
    }


    /**
     * validating the date columns from string
     */
    public static LocalDateTime parseableDate(String date) throws DataTransformationException {
        try {
            return getLocalDateTimeFormat(date);
        } catch (DateTimeParseException e) {
            throw new DataTransformationException("Unable to transform time data");
        }
    }

    public static String currentDateTime() {
        return LocalDateTime.now().format(dateTimeFormatter2);
    }
    public static LocalDateTime getLocalDateTimeFormat(String dateTimeString) throws DateTimeParseException {
        return LocalDateTime.parse(dateTimeString, dateTimeFormatter);
    }

}
