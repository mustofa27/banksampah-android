package com.qiratek.rnpsales.model.datasource.local;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.qiratek.rnpsales.model.entity.Outlet;

import java.util.List;

@Dao
public interface OutletDAO {
    @Query("SELECT * FROM " + Outlet.table_name)
    List<Outlet> getAll();

    @Query("SELECT * FROM " + Outlet.table_name + " WHERE " + Outlet.column_id + " IN (:outletIds)")
    List<Outlet> loadAllByIds(String[] outletIds);

    @Query("SELECT * FROM " + Outlet.table_name + " WHERE " + Outlet.column_nm_outlet + " LIKE :name LIMIT 1")
    Outlet findByName(String name);

    @Query("SELECT * FROM " + Outlet.table_name + " WHERE " + Outlet.column_id + " = (:id)")
    Outlet getOutlet(String id);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    List<Long> insertAll(Outlet... outlets);

    @Delete
    void delete(Outlet outlet);
}
