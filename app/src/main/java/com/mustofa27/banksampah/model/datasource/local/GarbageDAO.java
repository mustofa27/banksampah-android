package com.mustofa27.banksampah.model.datasource.local;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.mustofa27.banksampah.model.entity.Garbage;

import java.util.List;

@Dao
public interface GarbageDAO {
    @Query("SELECT * FROM " + Garbage.table_name)
    List<Garbage> getAll();

    @Query("SELECT * FROM " + Garbage.table_name + " WHERE " + Garbage.column_id + " IN (:ids)")
    List<Garbage> loadAllByIds(String[] ids);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    List<Long> insertAll(Garbage... garbages);

    @Delete
    void delete(Garbage garbage);
}
