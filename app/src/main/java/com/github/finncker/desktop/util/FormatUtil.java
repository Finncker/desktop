package com.github.finncker.desktop.util;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class FormatUtil {

    private static final NumberFormat CURRENCY_FORMATTER = NumberFormat.getCurrencyInstance(new Locale("pt", "BR"));
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    public static String formatCurrency(BigDecimal value) {
        if (value == null) {
            return "R$ 0,00";
        }
        return CURRENCY_FORMATTER.format(value.setScale(2, RoundingMode.HALF_UP));
    }

    public static String formatCurrency(double value) {
        return formatCurrency(BigDecimal.valueOf(value));
    }

    public static String formatDate(LocalDate date) {
        if (date == null) {
            return "";
        }
        return date.format(DATE_FORMATTER);
    }

    public static BigDecimal parseCurrency(String value) {
        if (value == null || value.trim().isEmpty()) {
            return BigDecimal.ZERO;
        }
        try {
            String cleaned = value.replace("R$", "").replace(".", "").replace(",", ".").trim();
            return new BigDecimal(cleaned);
        } catch (NumberFormatException e) {
            return BigDecimal.ZERO;
        }
    }
}
