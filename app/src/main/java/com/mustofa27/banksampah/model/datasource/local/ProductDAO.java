package com.mustofa27.banksampah.model.datasource.local;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.mustofa27.banksampah.model.entity.Product;

import java.util.List;

@Dao
public interface ProductDAO {
    @Query("SELECT * FROM " + Product.table_name)
    List<Product> getAll();

    @Query("SELECT * FROM " + Product.table_name + " WHERE " + Product.column_id + " IN (:ids)")
    List<Product> loadAllByIds(String[] ids);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    List<Long> insertAll(Product... products);

    @Delete
    void delete(Product product);
}
