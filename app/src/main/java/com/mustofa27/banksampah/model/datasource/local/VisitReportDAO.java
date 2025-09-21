package com.mustofa27.banksampah.model.datasource.local;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.mustofa27.banksampah.model.entity.VisitReport;

import java.util.List;

@Dao
public interface VisitReportDAO {
    @Query("SELECT * FROM " + VisitReport.table_name)
    List<VisitReport> getAll();

    @Query("SELECT * FROM " + VisitReport.table_name + " WHERE " + VisitReport.column_id + " IN (:ids)")
    List<VisitReport> loadAllByIds(int[] ids);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    List<Long> insertAll(VisitReport... visitReports);

    @Delete
    void delete(VisitReport visitReport);
}
