package com.example.currencyconvertor;

import java.util.HashMap;
import java.util.Map;

public class CurrencyUtils {
    private static final Map<String, Double> rates = new HashMap<>();

    static {
        // Base: 1 USD
        rates.put("USD", 1.0);
        rates.put("INR", 83.51);
        rates.put("JPY", 151.20);
        rates.put("EUR", 0.92);
    }

    public static double convert(double amount, String from, String to) {
        String fromCode = from.split(" - ")[0];
        String toCode = to.split(" - ")[0];

        Double fromRate = rates.get(fromCode);
        Double toRate = rates.get(toCode);

        if (fromRate == null || toRate == null) return 0.0;

        return (amount / fromRate) * toRate;
    }
}