package com.example.smartcal.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.*;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import com.example.smartcal.R;
import com.example.smartcal.viewmodel.AddFoodViewModel;

public class AddFoodActivity extends AppCompatActivity {

    private AddFoodViewModel vm;
    private EditText etName, etAmount;
    private Spinner spinnerMeal;
    private Button btnSave, btnPhoto, btnBarcode;

    private static final int REQ_BARCODE = 100;
    private static final int REQ_PHOTO = 200;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_food);

        vm = new ViewModelProvider(this).get(AddFoodViewModel.class);

        etName = findViewById(R.id.edit_food_name);
        etAmount = findViewById(R.id.edit_grams);
        spinnerMeal = findViewById(R.id.spinner_meal_type);
        btnSave = findViewById(R.id.btn_save_meal);
        btnPhoto = findViewById(R.id.btn_add_photo);
        btnBarcode = findViewById(R.id.btn_scan_barcode);

        if (spinnerMeal.getAdapter() == null) {
            ArrayAdapter<String> adapter = new ArrayAdapter<>(
                    this,
                    android.R.layout.simple_spinner_item,
                    new String[]{"Breakfast", "Lunch", "Dinner", "Snack"}
            );
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinnerMeal.setAdapter(adapter);
        }

        btnSave.setOnClickListener(v -> {
            String name = etName.getText() != null ? etName.getText().toString().trim() : "";
            String amountStr = etAmount.getText() != null ? etAmount.getText().toString().trim() : "";
            double amount = 0.0;
            try {
                if (!amountStr.isEmpty()) amount = Double.parseDouble(amountStr);
            } catch (NumberFormatException e) {
                amount = 0.0;
            }
            String meal = spinnerMeal.getSelectedItem() != null ? spinnerMeal.getSelectedItem().toString() : "Unknown";

            if (name.isEmpty() || amount <= 0) {
                Toast.makeText(this, "Please enter a name and amount greater than 0.", Toast.LENGTH_SHORT).show();
                return;
            }

            // --- HARD CODE CALORIES PER 100G ---
            double caloriesPer100g = 100.0;

            // Call ViewModel to insert (with calories)
            vm.addFoodEntry(name, amount, meal, caloriesPer100g);

            Toast.makeText(this, "Saved: " + name, Toast.LENGTH_SHORT).show();
            setResult(Activity.RESULT_OK);
            finish();
        });


        btnPhoto.setOnClickListener(v -> {
            Intent i = new Intent(this, PhotoConfirmActivity.class);
            startActivityForResult(i, REQ_PHOTO);
        });

        btnBarcode.setOnClickListener(v -> {
            Intent i = new Intent(this, BarcodeScannerActivity.class);
            startActivityForResult(i, REQ_BARCODE);
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQ_BARCODE) {
            if (resultCode == RESULT_OK && data != null) {
                String barcode = data.getStringExtra("barcode_value");
                if (barcode != null) {
                    etName.setText(barcode);
                    Toast.makeText(this, "Scanned: " + barcode + ". Name needs lookup.", Toast.LENGTH_LONG).show();
                }
            } else {
                Toast.makeText(this, "Barcode scan cancelled or failed.", Toast.LENGTH_SHORT).show();
            }
        } else if (requestCode == REQ_PHOTO) {
            if (resultCode == RESULT_OK && data != null) {
                String photoPath = data.getStringExtra("photo_path");
                if (photoPath != null) {
                    Toast.makeText(this, "Photo captured. Image processing logic needed.", Toast.LENGTH_LONG).show();
                }
            } else {
                Toast.makeText(this, "No photo captured.", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
