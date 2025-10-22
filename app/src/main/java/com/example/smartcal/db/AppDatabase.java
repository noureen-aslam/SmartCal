package com.example.smartcal.db;

import android.content.Context;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.example.smartcal.db.dao.FoodDao;
import com.example.smartcal.db.entities.FoodItem;
import com.example.smartcal.db.entities.MealEntry;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * The Room Database for the SmartCal application.
 * Defines the database configuration, lists all entities, and provides access to DAOs.
 */
@Database(entities = {FoodItem.class, MealEntry.class}, version = 1, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {

    // Expose the DAO to the rest of the app
    public abstract FoodDao foodDao();

    private static volatile AppDatabase INSTANCE;
    private static final int NUMBER_OF_THREADS = 4;
    static final ExecutorService databaseWriteExecutor =
            Executors.newFixedThreadPool(NUMBER_OF_THREADS);
    public static AppDatabase getInstance(final Context context) {
        // delegate to getDatabase so both names work
        return getDatabase(context);
    }

    public static AppDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (AppDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                                    AppDatabase.class, "smart_cal_database")
                            .addCallback(sRoomDatabaseCallback)
                            .build();
                }
            }
        }
        return INSTANCE;
    }

    /**
     * Pre-populates the database with some sample data on initial creation.
     */
    private static RoomDatabase.Callback sRoomDatabaseCallback = new RoomDatabase.Callback() {
        @Override
        public void onCreate(SupportSQLiteDatabase db) {
            super.onCreate(db);

            databaseWriteExecutor.execute(() -> {
                FoodDao dao = INSTANCE.foodDao();

                // 1. Insert Sample Food Items
                FoodItem apple = new FoodItem("Apple", 52.0, 0.3, 0.2, 13.8);
                FoodItem oatmeal = new FoodItem("Oatmeal (cooked)", 68.0, 2.4, 1.4, 12.0);
                FoodItem chickenBreast = new FoodItem("Chicken Breast (cooked)", 165.0, 31.0, 3.6, 0.0);
                dao.insertFoodItem(apple);
                dao.insertFoodItem(oatmeal);
                dao.insertFoodItem(chickenBreast);

                // Meal entries are left for runtime/user input
            });
        }
    };
}
