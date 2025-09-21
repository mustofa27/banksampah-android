package com.mustofa27.banksampah.viewmodel;

import android.content.Context;

import androidx.lifecycle.LiveData;

import com.mustofa27.banksampah.model.datasource.local.AppDatabase;
import com.mustofa27.banksampah.model.datasource.network.BaseNetwork;
import com.mustofa27.banksampah.model.entity.City;
import com.mustofa27.banksampah.model.entity.Outlet;
import com.mustofa27.banksampah.model.entity.Tipe;
import com.mustofa27.banksampah.model.helper.SharedPreferenceHelper;
import com.mustofa27.banksampah.model.repository.CityRepository;
import com.mustofa27.banksampah.model.repository.OutletRepository;
import com.mustofa27.banksampah.model.repository.TipeRepository;

import java.util.ArrayList;


public class OutletViewModel extends BaseViewModel {

    OutletRepository outletRepository;
    TipeRepository tipeRepository;
    CityRepository cityRepository;
    LiveData<ArrayList<Outlet>> outlets;
    LiveData<ArrayList<Tipe>> tipes;
    LiveData<ArrayList<City>> cities;
    boolean isOpenCreateOutlet = false;

    public OutletViewModel(Context context) {
        outletRepository = OutletRepository.getInstance(BaseNetwork.getInstance(context), SharedPreferenceHelper.getInstance(context), this,
                AppDatabase.getInstance(context));
        tipeRepository = TipeRepository.getInstance(BaseNetwork.getInstance(context), SharedPreferenceHelper.getInstance(context), this,
                AppDatabase.getInstance(context));
        cityRepository = CityRepository.getInstance(BaseNetwork.getInstance(context), SharedPreferenceHelper.getInstance(context), this,
                AppDatabase.getInstance(context));
    }

    public LiveData<ArrayList<Outlet>> getAllOutlet(){
        outlets = outletRepository.getAllOutlet();
        if(outlets.getValue() == null || outlets.getValue().size() == 0) {
            loading.setValue(true);
        }
        return outlets;
    }

    public LiveData<ArrayList<Tipe>> getAllTipe(){
        tipes = tipeRepository.getAllTipe();
        if(tipes.getValue() == null || tipes.getValue().size() == 0) {
            loading.setValue(true);
        }
        return tipes;
    }

    public LiveData<ArrayList<City>> getAllCity(){
        cities = cityRepository.getAllCity();
        if(cities.getValue() == null || cities.getValue().size() == 0) {
            loading.setValue(true);
        }
        return cities;
    }

    public boolean isOpenCreateOutlet() {
        return isOpenCreateOutlet;
    }

    public void setOpenCreateOutlet(boolean openCreateOutlet) {
        isOpenCreateOutlet = openCreateOutlet;
    }
}
