package com.mustofa27.banksampah.model.datasource.local;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.mustofa27.banksampah.model.entity.Product;
import com.mustofa27.banksampah.model.entity.Garbage;

@Database(entities = {Product.class, Garbage.class},
        version = 1, exportSchema = false)
public abstract class AppDatabase  extends RoomDatabase {
    public static final String DB_NAME = "gogreen";
    public abstract ProductDAO productDAO();
    public abstract GarbageDAO garbageDAO();

    private static volatile AppDatabase instance;

    public static AppDatabase getInstance(Context context){
        if(instance == null){
            instance = Room.databaseBuilder(context.getApplicationContext(), AppDatabase.class, AppDatabase.DB_NAME).
                    fallbackToDestructiveMigration().build();
        }
        return instance;
    }
}