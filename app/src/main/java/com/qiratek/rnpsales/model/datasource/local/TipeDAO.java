package com.qiratek.rnpsales.model.datasource.local;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.qiratek.rnpsales.model.entity.Tipe;

import java.util.List;

@Dao
public interface TipeDAO {
    @Query("SELECT * FROM " + Tipe.TABLE_TIPE)
    List<Tipe> getAll();

    @Query("SELECT * FROM " + Tipe.TABLE_TIPE + " WHERE " + Tipe.KEY_KODE_TIPE + " IN (:tipeIds)")
    List<Tipe> loadAllByIds(int... tipeIds);

    @Query("SELECT * FROM " + Tipe.TABLE_TIPE + " WHERE " + Tipe.KEY_KODE_TIPE + " = (:id)")
    Tipe getTipe(int id);

    @Query("SELECT * FROM " + Tipe.TABLE_TIPE + " WHERE " + Tipe.KEY_NAMA_TIPE + " LIKE :name LIMIT 1")
    Tipe findByName(String name);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    List<Long> insertAll(Tipe... tipes);

    @Delete
    void delete(Tipe tipe);
}
