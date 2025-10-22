package com.example.smartcal.db.entities;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import static androidx.room.ForeignKey.CASCADE;

@Entity(
        tableName = "meal_entry",
        foreignKeys = @ForeignKey(entity = FoodItem.class,
                parentColumns = "id",
                childColumns = "foodId",
                onDelete = CASCADE),
        indices = {@Index(value = {"foodId"})}
)
public class MealEntry {

    @PrimaryKey(autoGenerate = true)
    public long id;

    @ColumnInfo(name = "foodId")
    public long foodId;

    @ColumnInfo(name = "weightInGrams")
    public double weightInGrams;

    @ColumnInfo(name = "mealType")
    public String mealType;

    @ColumnInfo(name = "timestamp")
    public long timestamp;

    @ColumnInfo(name = "calories")
    public double calories; // added calories field

    public MealEntry() { }

    public MealEntry(long foodId, double weightInGrams, String mealType, long timestamp, double calories) {
        this.foodId = foodId;
        this.weightInGrams = weightInGrams;
        this.mealType = mealType;
        this.timestamp = timestamp;
        this.calories = calories;
    }

    public MealEntry(long id, long foodId, double grams, String meal, long timestamp) {
    }
}
