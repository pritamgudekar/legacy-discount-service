package com.example.legacy;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

/**
 * Deliberately legacy-style Java 8 business logic -- written the way a
 * real pre-modernization codebase tends to look, to give AWS Transform
 * something genuine to upgrade:
 *   - java.util.Date instead of java.time (java.time existed since Java
 *     8, but plenty of real Java 8 codebases still use the old API).
 *   - A traditional switch statement instead of a switch expression
 *     (switch expressions arrived in Java 14).
 *   - An anonymous inner class implementing Comparator instead of a
 *     lambda or method reference.
 *   - A manual for-loop instead of a stream pipeline.
 *
 * None of this is broken -- it's intentionally old-style, not buggy,
 * so the transformation has something real and visible to modernize
 * without changing behavior.
 */
public class DiscountCalculator {

    public static final String CUSTOMER_REGULAR = "REGULAR";
    public static final String CUSTOMER_PREMIUM = "PREMIUM";
    public static final String CUSTOMER_VIP = "VIP";

    /**
     * Computes the discount rate for a given customer type.
     * Legacy-style traditional switch statement.
     */
    public double discountRateFor(String customerType) {
        double rate;
        switch (customerType) {
            case CUSTOMER_REGULAR:
                rate = 0.0;
                break;
            case CUSTOMER_PREMIUM:
                rate = 0.10;
                break;
            case CUSTOMER_VIP:
                rate = 0.20;
                break;
            default:
                throw new IllegalArgumentException("Unknown customer type: " + customerType);
        }
        return rate;
    }

    /**
     * Applies the discount for the given customer type to a price.
     */
    public double applyDiscount(double amount, String customerType) {
        double rate = discountRateFor(customerType);
        return amount - (amount * rate);
    }

    /**
     * Returns the current time as a legacy java.util.Date, the way an
     * old codebase would record "calculated at" timestamps.
     */
    public Date calculatedAt() {
        return new Date();
    }

    /**
     * Sorts a list of order amounts in descending order, using an
     * anonymous inner class implementing Comparator -- the pre-lambda
     * style many real legacy codebases still carry forward even after
     * upgrading past Java 8.
     */
    public List<Double> sortDescending(List<Double> amounts) {
        List<Double> copy = new ArrayList<Double>(amounts);
        Collections.sort(copy, new Comparator<Double>() {
            @Override
            public int compare(Double a, Double b) {
                return b.compareTo(a);
            }
        });
        return copy;
    }

    /**
     * Sums a list of order amounts using a manual for-loop instead of
     * a stream pipeline -- again, deliberately old-style, not a bug.
     */
    public double sumAmounts(List<Double> amounts) {
        double total = 0.0;
        for (int i = 0; i < amounts.size(); i++) {
            total = total + amounts.get(i);
        }
        return total;
    }
}
