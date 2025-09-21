package com.mustofa27.banksampah.model.datasource.local;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.mustofa27.banksampah.model.entity.Satuan;

import java.util.List;

@Dao
public interface SatuanDAO {
    @Query("SELECT * FROM " + Satuan.table_name)
    List<Satuan> getAll();

    @Query("SELECT * FROM " + Satuan.table_name + " WHERE " + Satuan.column_id + " IN (:ids)")
    List<Satuan> loadAllByIds(int[] ids);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    List<Long> insertAll(Satuan... products);

    @Delete
    void delete(Satuan product);
}
