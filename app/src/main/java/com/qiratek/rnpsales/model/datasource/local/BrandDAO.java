package com.qiratek.rnpsales.model.datasource.local;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.qiratek.rnpsales.model.entity.Brand;

import java.util.List;

@Dao
public interface BrandDAO {
    @Query("SELECT * FROM " + Brand.table_name)
    List<Brand> getAll();

    @Query("SELECT * FROM " + Brand.table_name + " WHERE " + Brand.column_id + " IN (:ids)")
    List<Brand> loadAllByIds(int[] ids);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    List<Long> insertAll(Brand... products);

    @Delete
    void delete(Brand product);
}
