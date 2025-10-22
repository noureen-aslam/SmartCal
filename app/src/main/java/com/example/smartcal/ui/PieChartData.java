package com.example.smartcal.ui;

/**
 * Helper data class to represent a single segment of the calorie pie chart.
 * Used to pass meal distribution data (label, value, and color) from the
 * Fragment/ViewModel to the custom PieChartView for drawing.
 */
public class PieChartData {
    public final String label; // e.g., "Breakfast"
    public final float value; // The total calorie amount for this segment
    public final int color; // The color to draw this segment

    public PieChartData(String label, float value, int color) {
        this.label = label;
        this.value = value;
        this.color = color;
    }
}
