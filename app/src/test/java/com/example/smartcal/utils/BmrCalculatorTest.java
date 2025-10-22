package com.example.smartcal.utils;

import org.junit.Test;
import static org.junit.Assert.*;

public class BmrCalculatorTest {

    @Test
    public void testBmrMale() {
        double bmr = BmrCalculator.mifflinStJeor(true, 70, 175, 25);
        assertEquals(1673.75, bmr, 1.0);
    }

    @Test
    public void testBmrFemale() {
        double bmr = BmrCalculator.mifflinStJeor(false, 60, 165, 30);
        assertEquals(1320.25, bmr, 1.0);
    }
}
