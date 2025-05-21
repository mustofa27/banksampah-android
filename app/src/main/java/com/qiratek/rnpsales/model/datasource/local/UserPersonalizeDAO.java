package com.qiratek.rnpsales.model.datasource.local;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.qiratek.rnpsales.model.entity.UserPersonalize;

import java.util.List;

@Dao
public interface UserPersonalizeDAO {
    @Query("SELECT * FROM " + UserPersonalize.table_name)
    List<UserPersonalize> getAll();

    @Query("SELECT * FROM " + UserPersonalize.table_name + " WHERE " + UserPersonalize.column_id + " IN (:ids)")
    List<UserPersonalize> loadAllByIds(int[] ids);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    List<Long> insertAll(UserPersonalize... userPersonalizes);

    @Delete
    void delete(UserPersonalize userPersonalize);
}
