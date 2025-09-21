package com.mustofa27.banksampah.model.datasource.local;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.mustofa27.banksampah.model.entity.City;

import java.util.List;

@Dao
public interface CityDAO {
    @Query("SELECT * FROM " + City.table_name)
    List<City> getAll();

    @Query("SELECT * FROM " + City.table_name + " WHERE " + City.column_id + " IN (:cityIds)")
    List<City> loadAllByIds(int[] cityIds);

    @Query("SELECT * FROM " + City.table_name + " WHERE " + City.column_nama + " LIKE :name LIMIT 1")
    City findByName(String name);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    List<Long> insertAll(City... cities);

    @Delete
    void delete(City city);
}
