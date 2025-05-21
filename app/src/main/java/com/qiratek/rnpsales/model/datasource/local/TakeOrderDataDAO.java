package com.qiratek.rnpsales.model.datasource.local;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;


import com.qiratek.rnpsales.model.entity.TakeOrderData;

import java.util.List;

@Dao
public interface TakeOrderDataDAO {
    @Query("SELECT * FROM " + TakeOrderData.table_name)
    List<TakeOrderData> getAll();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    List<Long> insertAll(TakeOrderData... takeOrderData);

    @Delete
    void delete(TakeOrderData takeOrderData);
}
