package com.example.smartcal.db;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.example.smartcal.db.dao.FoodDao;
import com.example.smartcal.db.entities.FoodItem;
import com.example.smartcal.db.entities.MealEntry;
import com.example.smartcal.db.projections.MealDistributionRaw;
import com.example.smartcal.db.projections.MealEntryWithFoodDetails;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class FoodRepository {

    private final FoodDao foodDao;
    private final LiveData<List<FoodItem>> allFoodItems;
    private final LiveData<List<MealEntryWithFoodDetails>> allMealEntriesWithDetails;
    private final LiveData<Double> totalCalories;
    private final LiveData<List<MealDistributionRaw>> mealDistributionRaw;

    private static final ExecutorService databaseWriteExecutor = Executors.newFixedThreadPool(4);

    public FoodRepository(Application application) {
        AppDatabase db = AppDatabase.getDatabase(application);
        foodDao = db.foodDao();

        allFoodItems = foodDao.getAllFoodItems();
        allMealEntriesWithDetails = foodDao.getAllMealEntriesWithDetails();
        totalCalories = foodDao.getTotalCalories();
        mealDistributionRaw = foodDao.getMealDistributionRaw();
    }

    // FoodItem methods
    public LiveData<List<FoodItem>> getAllFoodItems() {
        return allFoodItems;
    }

    public void insertFoodItem(final FoodItem foodItem) {
        databaseWriteExecutor.execute(() -> foodDao.insertFoodItem(foodItem));
    }

    public FoodItem getFoodItemByName(String name) {
        return foodDao.getFoodItemByName(name);
    }

    // MealEntry methods
    public void insertMealEntry(final MealEntry mealEntry) {
        databaseWriteExecutor.execute(() -> foodDao.insertMealEntry(mealEntry));
    }

    public void deleteMealEntry(final MealEntry mealEntry) {
        databaseWriteExecutor.execute(() -> foodDao.deleteMealEntry(mealEntry));
    }

    // LiveData getters
    public LiveData<List<MealEntryWithFoodDetails>> getAllMealEntriesWithDetails() {
        return allMealEntriesWithDetails;
    }

    public LiveData<Double> getTotalCalories() {
        return totalCalories;
    }

    public LiveData<List<MealDistributionRaw>> getMealDistributionRaw() {
        return mealDistributionRaw;
    }
}
