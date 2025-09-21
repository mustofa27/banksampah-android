package com.mustofa27.banksampah.model.datasource.local;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.mustofa27.banksampah.model.entity.Distributor;

import java.util.List;

@Dao
public interface DistributorDAO {
    @Query("SELECT * FROM " + Distributor.table_name)
    List<Distributor> getAll();

    @Query("SELECT * FROM " + Distributor.table_name + " WHERE " + Distributor.column_id + " IN (:distIds)")
    List<Distributor> loadAllByIds(String[] distIds);

    @Query("SELECT * FROM " + Distributor.table_name + " WHERE " + Distributor.column_nm_dist + " LIKE :name LIMIT 1")
    Distributor findByName(String name);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    List<Long> insertAll(Distributor... distributors);

    @Delete
    void delete(Distributor distributor);
}
