package com.example.smartcal.ui;

import android.app.Application;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import com.example.smartcal.db.FoodRepository;
import com.example.smartcal.db.entities.FoodItem;
import com.example.smartcal.db.entities.MealEntry;
import com.example.smartcal.db.projections.MealDistributionRaw;
import com.example.smartcal.db.projections.MealEntryWithFoodDetails;
import java.util.List;

/**
 * ViewModel for the main screen, responsible for:
 * 1. Holding LiveData streams for all UI components.
 * 2. Providing methods for data operations (insert/delete).
 * 3. Hiding the repository implementation from the UI (Fragment).
 */
public class MainViewModel extends AndroidViewModel {

    private final FoodRepository repository;
    private final LiveData<List<FoodItem>> allFoodItems;
    private final LiveData<List<MealEntryWithFoodDetails>> allMealEntriesWithDetails;
    private final LiveData<Double> totalCalories;
    private final LiveData<List<MealDistributionRaw>> mealDistributionRaw;

    public MainViewModel(Application application) {
        super(application);
        repository = new FoodRepository(application);

        // Initialize LiveData streams from the repository
        allFoodItems = repository.getAllFoodItems();
        allMealEntriesWithDetails = repository.getAllMealEntriesWithDetails();
        totalCalories = repository.getTotalCalories();
        mealDistributionRaw = repository.getMealDistributionRaw();
    }

    // --- Data Access for UI ---

    public LiveData<List<FoodItem>> getAllFoodItems() {
        return allFoodItems;
    }

    /**
     * Returns the LiveData stream of all meal entries with joined food details.
     * This fixes the 'cannot find symbol' error in MainFragment.
     */
    public LiveData<List<MealEntryWithFoodDetails>> getAllFoodEntries() {
        return allMealEntriesWithDetails;
    }

    /**
     * Returns the LiveData stream of the calculated total calories.
     * This fixes the 'cannot find symbol' error in MainFragment.
     */
    public LiveData<Double> getTotalCalories() {
        return totalCalories;
    }

    /**
     * Returns the LiveData stream for the raw meal distribution data.
     */
    public LiveData<List<MealDistributionRaw>> getMealDistributionRaw() {
        return mealDistributionRaw;
    }

    // --- Data Modification Methods (calls repository) ---

    public void insertFoodItem(FoodItem foodItem) {
        repository.insertFoodItem(foodItem);
    }

    public void insertMealEntry(MealEntry mealEntry) {
        repository.insertMealEntry(mealEntry);
    }

    /**
     * Deletes a meal entry. Called by the MainFragment when the user swipes an item.
     * This fixes the 'cannot find symbol' error in MainFragment's delete logic.
     */
    public void deleteMealEntry(MealEntryWithFoodDetails entry) {
        // Must convert the POJO back to the base MealEntry entity for deletion
        MealEntry mealEntryToDelete = new MealEntry(
                entry.id,
                entry.foodId,
                entry.grams,
                entry.meal,
                entry.timestamp
        );
        repository.deleteMealEntry(mealEntryToDelete);
    }

    // This method can be used as a simple stub if we ever need to manually force data refresh
    public void refreshData() {
        // Since all data is handled by LiveData from Room, this currently does nothing
        // but is included to satisfy the MainFragment method call.
    }
}
