package com.example.smartcal.ui.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.smartcal.R;
import com.example.smartcal.db.projections.MealEntryWithFoodDetails;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Simple RecyclerView.Adapter showing MealEntryWithFoodDetails entries.
 * Uses layout res/layout/item_food.xml and ids tvFoodName, tvCalories, tvMeal.
 */
public class FoodListAdapter extends RecyclerView.Adapter<FoodListAdapter.VH> {

    private List<MealEntryWithFoodDetails> items = new ArrayList<>();

    public void submitList(List<MealEntryWithFoodDetails> list) {
        items = list == null ? new ArrayList<>() : new ArrayList<>(list);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_food, parent, false);
        return new VH(v);
    }

    @Override
    public void onBindViewHolder(@NonNull VH holder, int position) {
        MealEntryWithFoodDetails m = items.get(position);

        holder.tvName.setText(m.foodName == null ? "Food" : m.foodName);
        holder.tvKcal.setText(String.format(Locale.getDefault(), "%.0f kcal", m.getTotalCalories()));
        holder.tvMeal.setText(m.meal == null ? "Other" : m.meal);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    static class VH extends RecyclerView.ViewHolder {
        TextView tvName, tvKcal, tvMeal;

        VH(@NonNull View v) {
            super(v);
            tvName = v.findViewById(R.id.tvFoodName);
            tvKcal = v.findViewById(R.id.tvCalories);
            tvMeal = v.findViewById(R.id.tvMeal);
        }
    }
}
