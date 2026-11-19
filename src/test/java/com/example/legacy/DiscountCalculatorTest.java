package com.example.legacy;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * Deliberately JUnit 4 -- the dated testing style matching a genuinely
 * legacy codebase (not JUnit 5). These are the "legacy test cases"
 * this project's test plan / baseline capture step would discover and
 * run against the pre-transformation app.
 */
public class DiscountCalculatorTest {

    private final DiscountCalculator calculator = new DiscountCalculator();

    @Test
    public void regularCustomerGetsNoDiscount() {
        double result = calculator.applyDiscount(100.0, DiscountCalculator.CUSTOMER_REGULAR);
        assertEquals(100.0, result, 0.0001);
    }

    @Test
    public void premiumCustomerGetsTenPercentDiscount() {
        double result = calculator.applyDiscount(100.0, DiscountCalculator.CUSTOMER_PREMIUM);
        assertEquals(90.0, result, 0.0001);
    }

    @Test
    public void vipCustomerGetsTwentyPercentDiscount() {
        double result = calculator.applyDiscount(100.0, DiscountCalculator.CUSTOMER_VIP);
        assertEquals(80.0, result, 0.0001);
    }

    @Test(expected = IllegalArgumentException.class)
    public void unknownCustomerTypeThrows() {
        calculator.applyDiscount(100.0, "NOT_A_REAL_TYPE");
    }

    @Test
    public void sortDescendingOrdersAmountsHighToLow() {
        List<Double> amounts = new ArrayList<Double>(Arrays.asList(10.0, 50.0, 25.0));
        List<Double> sorted = calculator.sortDescending(amounts);
        assertEquals(Arrays.asList(50.0, 25.0, 10.0), sorted);
    }

    @Test
    public void sumAmountsAddsAllValues() {
        List<Double> amounts = new ArrayList<Double>(Arrays.asList(10.0, 20.0, 30.0));
        double total = calculator.sumAmounts(amounts);
        assertEquals(60.0, total, 0.0001);
    }

    @Test
    public void calculatedAtReturnsARecentDate() {
        Date now = calculator.calculatedAt();
        assertTrue(now.getTime() > 0);
    }
}
