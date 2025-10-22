package com.example.smartcal.db.projections;

import androidx.room.ColumnInfo;

/**
 * POJO for meal distribution results.
 * Field names (column name in @ColumnInfo) must match SELECT aliases in DAO.
 */
public class MealDistributionRaw {

    @ColumnInfo(name = "meal")
    public String meal;

    @ColumnInfo(name = "totalCalories")
    public double totalCalories;
}
