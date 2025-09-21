package com.mustofa27.banksampah.model.datasource.local;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.mustofa27.banksampah.model.entity.BillingData;
import com.mustofa27.banksampah.model.entity.Brand;
import com.mustofa27.banksampah.model.entity.City;
import com.mustofa27.banksampah.model.entity.Distributor;
import com.mustofa27.banksampah.model.entity.Outlet;
import com.mustofa27.banksampah.model.entity.Product;
import com.mustofa27.banksampah.model.entity.Satuan;
import com.mustofa27.banksampah.model.entity.TakeOrderData;
import com.mustofa27.banksampah.model.entity.Tipe;
import com.mustofa27.banksampah.model.entity.UserPersonalize;
import com.mustofa27.banksampah.model.entity.VisitPlanDb;
import com.mustofa27.banksampah.model.entity.VisitReport;

@Database(entities = {Outlet.class, Tipe.class, City.class, Distributor.class,
        VisitPlanDb.class, Product.class, VisitReport.class, TakeOrderData.class,
        UserPersonalize.class, Brand.class, Satuan.class, BillingData.class},
        version = 15, exportSchema = false)
public abstract class AppDatabase  extends RoomDatabase {
    public static final String DB_NAME = "pmpsales";
    public abstract OutletDAO outletDAO();
    public abstract VisitPlanDAO visitPlanDAO();
    public abstract ProductDAO productDAO();
    public abstract TipeDAO tipeDAO();
    public abstract CityDAO cityDAO();
    public abstract DistributorDAO distDAO();
    public abstract VisitReportDAO visitReportDAO();
    public abstract TakeOrderDataDAO takeOrderDataDAO();
    public abstract UserPersonalizeDAO userPersonalizeDAO();
    public abstract BrandDAO brandDAO();
    public abstract SatuanDAO satuanDAO();
    public abstract BillingDataDAO billingDataDAO();

    private static volatile AppDatabase instance;

    public static AppDatabase getInstance(Context context){
        if(instance == null){
            instance = Room.databaseBuilder(context.getApplicationContext(), AppDatabase.class, AppDatabase.DB_NAME).
                    fallbackToDestructiveMigration().build();
        }
        return instance;
    }
}
