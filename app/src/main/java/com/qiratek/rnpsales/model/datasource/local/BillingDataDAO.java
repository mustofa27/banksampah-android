package com.qiratek.rnpsales.model.datasource.local;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.qiratek.rnpsales.model.entity.BillingData;

import java.util.List;

@Dao
public interface BillingDataDAO {
    @Query("SELECT * FROM " + BillingData.table_name)
    List<BillingData> getAll();

    @Query("SELECT * FROM " + BillingData.table_name + " WHERE " + BillingData.column_id + " IN (:ids)")
    List<BillingData> loadAllByIds(int[] ids);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    List<Long> insertAll(BillingData... outlets);

    @Delete
    void delete(BillingData outlet);
}
