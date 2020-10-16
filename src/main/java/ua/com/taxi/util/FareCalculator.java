package ua.com.taxi.util;

import ua.com.taxi.domain.Category;

import java.util.HashMap;
import java.util.Map;

public class FareCalculator {

    private static final Map<Category, Long> PRICES = new HashMap();

    static {
        PRICES.put(Category.PREMIUM, 25L);
        PRICES.put(Category.STANDARD, 15L);
    }

    private FareCalculator() {
    }

    public static long calculate(Category category, long distance) {
        Long costPerKm = PRICES.get(category);
        return costPerKm * distance / 1000;
    }
}
