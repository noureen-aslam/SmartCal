package com.example.smartcal.db.entities;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "food_item")
public class FoodItem {

    @PrimaryKey(autoGenerate = true)
    public long id;

    @ColumnInfo(name = "name")
    public String name;

    // calories per 100 grams
    @ColumnInfo(name = "caloriesPerHundredGrams")
    public double caloriesPerHundredGrams;

    @ColumnInfo(name = "protein")
    public double protein;

    @ColumnInfo(name = "fat")
    public double fat;

    @ColumnInfo(name = "carbs")
    public double carbs;

    public FoodItem() { }

    public FoodItem(String name, double caloriesPerHundredGrams, double protein, double fat, double carbs) {
        this.name = name;
        this.caloriesPerHundredGrams = caloriesPerHundredGrams;
        this.protein = protein;
        this.fat = fat;
        this.carbs = carbs;
    }
}
