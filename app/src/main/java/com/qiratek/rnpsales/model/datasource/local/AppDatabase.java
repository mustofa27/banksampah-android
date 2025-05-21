package com.qiratek.rnpsales.model.datasource.local;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.qiratek.rnpsales.model.entity.BillingData;
import com.qiratek.rnpsales.model.entity.Brand;
import com.qiratek.rnpsales.model.entity.City;
import com.qiratek.rnpsales.model.entity.Distributor;
import com.qiratek.rnpsales.model.entity.Outlet;
import com.qiratek.rnpsales.model.entity.Product;
import com.qiratek.rnpsales.model.entity.Satuan;
import com.qiratek.rnpsales.model.entity.TakeOrderData;
import com.qiratek.rnpsales.model.entity.Tipe;
import com.qiratek.rnpsales.model.entity.UserPersonalize;
import com.qiratek.rnpsales.model.entity.VisitPlanDb;
import com.qiratek.rnpsales.model.entity.VisitReport;

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
