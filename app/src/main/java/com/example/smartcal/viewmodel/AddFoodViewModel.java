package com.example.smartcal.viewmodel;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.smartcal.db.AppDatabase;
import com.example.smartcal.db.FoodRepository;
import com.example.smartcal.db.dao.FoodDao;
import com.example.smartcal.db.entities.FoodItem;
import com.example.smartcal.db.entities.MealEntry;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class AddFoodViewModel extends AndroidViewModel {

    private final FoodRepository repo;
    private final FoodDao dao;
    private final ExecutorService io = Executors.newSingleThreadExecutor();

    public AddFoodViewModel(@NonNull Application application) {
        super(application);
        repo = new FoodRepository(application);
        dao = AppDatabase.getDatabase(application).foodDao();
    }

    public LiveData<List<FoodItem>> getAllFoodItems() {
        return repo.getAllFoodItems();
    }

    public void addFoodEntry(final String foodName, final double grams, final String mealType, final double caloriesPer100g) {
        io.execute(() -> {
            // Step 1: Check if food exists
            FoodItem found = dao.getFoodItemByName(foodName);
            long foodId;
            double foodCalories;

            if (found == null) {
                foodCalories = caloriesPer100g; // fallback calories
                FoodItem newItem = new FoodItem(foodName, foodCalories, 0.0, 0.0, 0.0);
                foodId = dao.insertFoodItem(newItem);
            } else {
                foodId = found.id;
                foodCalories = found.caloriesPerHundredGrams > 0 ? found.caloriesPerHundredGrams : caloriesPer100g;
            }

            // Step 2: Calculate total calories
            double totalCalories = (grams / 100.0) * foodCalories;

            // Step 3: Create MealEntry and insert
            MealEntry entry = new MealEntry(foodId, grams, mealType, System.currentTimeMillis(), totalCalories);
            repo.insertMealEntry(entry);
        });
    }
}
