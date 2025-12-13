package com.example.serviceapp.util;


import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DateFormatter {

    private static final DateTimeFormatter ISO_FORMATTER =
            DateTimeFormatter.ISO_LOCAL_DATE_TIME;

    private static final DateTimeFormatter DISPLAY_FORMATTER =
            DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    public String format(String isoDateString) {
        if (isoDateString == null || isoDateString.isEmpty()) {
            return "";
        }

        try {
            LocalDateTime dateTime = LocalDateTime.parse(isoDateString, ISO_FORMATTER);
            return dateTime.format(DISPLAY_FORMATTER);
        } catch (Exception e) {
            return isoDateString; // Fallback trả về chuỗi gốc
        }
    }
}
