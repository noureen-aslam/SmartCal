package com.example.smartcal.db.projections;

import androidx.room.ColumnInfo;

/**
 * Projection combining MealEntry and selected FoodItem fields.
 * DAO aliases columns so names here align with the query.
 */
public class MealEntryWithFoodDetails {

    @ColumnInfo(name = "id") // meal_entry.id
    public long id;

    @ColumnInfo(name = "foodId") // meal_entry.foodId
    public long foodId;

    @ColumnInfo(name = "grams") // alias for meal_entry.weightInGrams in DAO
    public double grams;

    @ColumnInfo(name = "meal") // alias for meal_entry.mealType in DAO
    public String meal;

    @ColumnInfo(name = "timestamp")
    public long timestamp;

    // From food_item
    @ColumnInfo(name = "foodName")
    public String foodName;

    @ColumnInfo(name = "kcalPer100g")
    public double kcalPer100g;

    /**
     * Convenience computed property. Room will set grams and kcalPer100g,
     * and you can call this from UI code to get calories for this entry.
     */
    public double getTotalCalories() {
        return (kcalPer100g * grams) / 100.0;
    }
}
