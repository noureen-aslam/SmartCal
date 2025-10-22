package com.example.smartcal.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.example.smartcal.R;
import com.example.smartcal.db.projections.MealEntryWithFoodDetails;

import java.util.Locale;

/**
 * ListAdapter for MealEntryWithFoodDetails (projection from JOIN).
 * Expects layout: res/layout/item_food.xml with TextViews:
 *  - @id/tvFoodName
 *  - @id/tvCalories
 *  - @id/tvMeal
 */
public class FoodEntryAdapter extends ListAdapter<MealEntryWithFoodDetails, FoodEntryAdapter.FoodEntryViewHolder> {

    public FoodEntryAdapter() {
        super(DIFF_CALLBACK);
    }

    private static final DiffUtil.ItemCallback<MealEntryWithFoodDetails> DIFF_CALLBACK =
            new DiffUtil.ItemCallback<MealEntryWithFoodDetails>() {
                @Override
                public boolean areItemsTheSame(@NonNull MealEntryWithFoodDetails oldItem, @NonNull MealEntryWithFoodDetails newItem) {
                    return oldItem.id == newItem.id;
                }

                @Override
                public boolean areContentsTheSame(@NonNull MealEntryWithFoodDetails oldItem, @NonNull MealEntryWithFoodDetails newItem) {
                    // compare fields used for display
                    return oldItem.getTotalCalories() == newItem.getTotalCalories()
                            && oldItem.grams == newItem.grams
                            && ((oldItem.foodName == null && newItem.foodName == null) ||
                            (oldItem.foodName != null && oldItem.foodName.equals(newItem.foodName)))
                            && ((oldItem.meal == null && newItem.meal == null) ||
                            (oldItem.meal != null && oldItem.meal.equals(newItem.meal)));
                }
            };

    @NonNull
    @Override
    public FoodEntryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_food, parent, false);
        return new FoodEntryViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull FoodEntryViewHolder holder, int position) {
        MealEntryWithFoodDetails entry = getItem(position);
        holder.bind(entry);
    }

    public static class FoodEntryViewHolder extends RecyclerView.ViewHolder {
        private final TextView foodNameTextView;
        private final TextView caloriesTextView;
        private final TextView mealTypeTextView;

        public FoodEntryViewHolder(@NonNull View itemView) {
            super(itemView);
            foodNameTextView = itemView.findViewById(R.id.tvFoodName);
            caloriesTextView = itemView.findViewById(R.id.tvCalories);
            mealTypeTextView = itemView.findViewById(R.id.tvMeal);
        }

        public void bind(@NonNull MealEntryWithFoodDetails entry) {
            // Name
            String name = entry.foodName != null ? entry.foodName : "Unknown Food";
            foodNameTextView.setText(name);

            // Meal Type
            String meal = entry.meal != null ? entry.meal : "Other";
            mealTypeTextView.setText(meal);

            // Calorie calculation (projection helper)
            double calories = entry.getTotalCalories();
            caloriesTextView.setText(String.format(Locale.getDefault(), "%.0f kcal", calories));
        }
    }
}
