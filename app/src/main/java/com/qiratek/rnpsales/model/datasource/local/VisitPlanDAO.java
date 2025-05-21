package com.qiratek.rnpsales.model.datasource.local;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.qiratek.rnpsales.model.entity.VisitPlanDb;

import java.util.List;

@Dao
public interface VisitPlanDAO {
    @Query("SELECT * FROM " + VisitPlanDb.table_name + " WHERE " + VisitPlanDb.column_is_checkout + "=0")
    List<VisitPlanDb> getAll();

    @Query("SELECT * FROM " + VisitPlanDb.table_name + " WHERE " + VisitPlanDb.column_is_checkout + "=1" + " AND " + VisitPlanDb.column_created_at + " =''")
    List<VisitPlanDb> getAllForSync();

    @Query("SELECT * FROM " + VisitPlanDb.table_name + " WHERE " + VisitPlanDb.column_id + " IN (:ids)")
    List<VisitPlanDb> loadAllByIds(String[] ids);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    List<Long> insertAll(VisitPlanDb... outlets);

    @Delete
    void delete(VisitPlanDb outlet);
}
