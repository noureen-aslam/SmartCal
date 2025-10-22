package com.example.smartcal.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.smartcal.R;
import com.example.smartcal.db.projections.MealEntryWithFoodDetails;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MainFragment extends Fragment {

    private static final int REQ_ADD_FOOD = 101; // Request code for AddFoodActivity
    private MainViewModel viewModel;
    private MealEntryAdapter adapter;
    private TextView totalCaloriesText;
    private ImageView mealChartImage;

    public static MainFragment newInstance() {
        return new MainFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_main, container, false);

        totalCaloriesText = root.findViewById(R.id.tv_total_calories);
        mealChartImage = root.findViewById(R.id.iv_meal_chart);
        RecyclerView recyclerView = root.findViewById(R.id.rv_food_list);
        FloatingActionButton fab = root.findViewById(R.id.fab_add_food);

        // RecyclerView setup
        adapter = new MealEntryAdapter(new ArrayList<>());
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        recyclerView.setAdapter(adapter);
        recyclerView.addItemDecoration(new DividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.VERTICAL));
        setupSwipeToDelete(recyclerView);

        // FAB click: open AddFoodActivity
        fab.setOnClickListener(v -> {
            Log.d("MainFragment", "FAB clicked - opening AddFoodActivity");
            Intent intent = new Intent(requireContext(), AddFoodActivity.class);
            startActivityForResult(intent, REQ_ADD_FOOD);
        });

        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        viewModel = new ViewModelProvider(this).get(MainViewModel.class);

        // Observe food entries
        viewModel.getAllFoodEntries().observe(getViewLifecycleOwner(), entries -> {
            adapter.setEntries(entries);
        });

        // Observe total calories
        viewModel.getTotalCalories().observe(getViewLifecycleOwner(), total -> {
            String text = String.format(Locale.getDefault(), "%,.0f", total != null ? total : 0.0);
            totalCaloriesText.setText(text);
        });

        // Observe meal distribution (for chart placeholder)
        viewModel.getMealDistributionRaw().observe(getViewLifecycleOwner(), distribution -> {
            if (distribution != null && !distribution.isEmpty()) {
                mealChartImage.clearColorFilter();
            } else {
                mealChartImage.setColorFilter(0x55FFFFFF); // Dim placeholder if no data
            }
        });

        viewModel.refreshData();
    }

    // Handle AddFoodActivity result
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQ_ADD_FOOD && resultCode == Activity.RESULT_OK) {
            if (viewModel != null) {
                viewModel.refreshData(); // Refresh RecyclerView after adding food
            }
        }
    }

    private void setupSwipeToDelete(RecyclerView recyclerView) {
        ItemTouchHelper.SimpleCallback simpleItemTouchCallback =
                new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {

                    @Override
                    public boolean onMove(@NonNull RecyclerView r, @NonNull RecyclerView.ViewHolder vh,
                                          @NonNull RecyclerView.ViewHolder target) {
                        return false;
                    }

                    @Override
                    public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                        int pos = viewHolder.getAdapterPosition();
                        if (pos != RecyclerView.NO_POSITION) {
                            MealEntryWithFoodDetails entryToDelete = adapter.getEntryAt(pos);
                            viewModel.deleteMealEntry(entryToDelete);
                        }
                    }
                };

        ItemTouchHelper ith = new ItemTouchHelper(simpleItemTouchCallback);
        ith.attachToRecyclerView(recyclerView);
    }

    // RecyclerView Adapter
    private static class MealEntryAdapter extends RecyclerView.Adapter<MealEntryAdapter.EntryViewHolder> {
        private List<MealEntryWithFoodDetails> entries;

        MealEntryAdapter(List<MealEntryWithFoodDetails> entries) {
            this.entries = entries == null ? new ArrayList<>() : entries;
        }

        void setEntries(List<MealEntryWithFoodDetails> newEntries) {
            this.entries = newEntries == null ? new ArrayList<>() : newEntries;
            notifyDataSetChanged();
        }

        MealEntryWithFoodDetails getEntryAt(int position) {
            return entries.get(position);
        }

        @NonNull
        @Override
        public EntryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_food, parent, false);
            return new EntryViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull EntryViewHolder holder, int position) {
            MealEntryWithFoodDetails currentEntry = entries.get(position);
            String name = currentEntry.foodName == null ? "Food" : currentEntry.foodName;
            String meal = currentEntry.meal == null ? "Other" : currentEntry.meal;
            String kcal = String.format(Locale.getDefault(), "%.0f kcal", currentEntry.getTotalCalories());

            holder.tvName.setText(name);
            holder.tvMeal.setText(meal);
            holder.tvKcal.setText(kcal);
        }

        @Override
        public int getItemCount() {
            return entries.size();
        }

        static class EntryViewHolder extends RecyclerView.ViewHolder {
            final TextView tvName;
            final TextView tvKcal;
            final TextView tvMeal;

            EntryViewHolder(View itemView) {
                super(itemView);
                tvName = itemView.findViewById(R.id.tvFoodName);
                tvKcal = itemView.findViewById(R.id.tvCalories);
                tvMeal = itemView.findViewById(R.id.tvMeal);
            }
        }
    }
}
