package com.example.smartcal.db.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Transaction;
import androidx.room.Delete;
import androidx.room.RewriteQueriesToDropUnusedColumns;

import com.example.smartcal.db.entities.FoodItem;
import com.example.smartcal.db.entities.MealEntry;
import com.example.smartcal.db.projections.MealDistributionRaw;
import com.example.smartcal.db.projections.MealEntryWithFoodDetails;

import java.util.List;

@Dao
public interface FoodDao {

    // FoodItem CRUD
    @Insert
    long insertFoodItem(FoodItem foodItem);

    @Query("SELECT * FROM food_item ORDER BY name ASC")
    LiveData<List<FoodItem>> getAllFoodItems();

    @Query("SELECT * FROM food_item WHERE name = :name LIMIT 1")
    FoodItem getFoodItemByName(String name);

    // MealEntry CRUD
    @Insert
    void insertMealEntry(MealEntry entry);

    @Delete
    void deleteMealEntry(MealEntry entry);

    // JOIN projection: alias meal_entry.weightInGrams -> grams and meal_entry.mealType -> meal
    // and alias food_item.name -> foodName, caloriesPerHundredGrams -> kcalPer100g
    @Transaction
    @Query("SELECT T1.id AS id, T1.foodId AS foodId, T1.weightInGrams AS grams, " +
            "T1.mealType AS meal, T1.timestamp AS timestamp, " +
            "T2.name AS foodName, T2.caloriesPerHundredGrams AS kcalPer100g " +
            "FROM meal_entry AS T1 " +
            "INNER JOIN food_item AS T2 ON T1.foodId = T2.id " +
            "ORDER BY T1.timestamp DESC")
    @RewriteQueriesToDropUnusedColumns
    LiveData<List<MealEntryWithFoodDetails>> getAllMealEntriesWithDetails();

    // Total calories: sum(weightInGrams * caloriesPerHundredGrams / 100)
    @Query("SELECT SUM(T1.weightInGrams * T2.caloriesPerHundredGrams / 100.0) " +
            "FROM meal_entry AS T1 " +
            "INNER JOIN food_item AS T2 ON T1.foodId = T2.id")
    LiveData<Double> getTotalCalories();

    // Meal distribution grouped by mealType; alias mealType -> meal, SUM(...) -> totalCalories
    @Query("SELECT T1.mealType AS meal, " +
            "SUM(T1.weightInGrams * T2.caloriesPerHundredGrams / 100.0) AS totalCalories " +
            "FROM meal_entry AS T1 " +
            "INNER JOIN food_item AS T2 ON T1.foodId = T2.id " +
            "GROUP BY T1.mealType")
    LiveData<List<MealDistributionRaw>> getMealDistributionRaw();
}
