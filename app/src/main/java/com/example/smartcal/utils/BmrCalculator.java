package com.example.smartcal.utils;

public class BmrCalculator {

    public static double mifflinStJeor(boolean male, double weightKg, double heightCm, int ageYears) {
        double bmr = 10 * weightKg + 6.25 * heightCm - 5 * ageYears;
        bmr += male ? 5 : -161;
        return bmr;
    }

    public static double tdee(double bmr, double activityMultiplier) {
        return bmr * activityMultiplier;
    }
}
