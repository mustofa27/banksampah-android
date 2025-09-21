package com.mustofa27.banksampah.viewmodel;

import android.content.Context;

import androidx.lifecycle.LiveData;

import com.mustofa27.banksampah.model.datasource.local.AppDatabase;
import com.mustofa27.banksampah.model.datasource.network.BaseNetwork;
import com.mustofa27.banksampah.model.entity.Outlet;
import com.mustofa27.banksampah.model.entity.Tipe;
import com.mustofa27.banksampah.model.entity.VisitPlanDb;
import com.mustofa27.banksampah.model.helper.SharedPreferenceHelper;
import com.mustofa27.banksampah.model.repository.OutletRepository;
import com.mustofa27.banksampah.model.repository.TipeRepository;
import com.mustofa27.banksampah.model.repository.VisitPlanRepository;

import org.json.JSONObject;


public class CheckoutViewModel extends BaseViewModel {

    VisitPlanRepository visitPlanRepository;
    LiveData<Tipe> tipeLiveData;
    TipeRepository tipeRepository;
    OutletRepository outletRepository;
    LiveData<Outlet> outletLiveData;

    public CheckoutViewModel(Context context) {
        visitPlanRepository = VisitPlanRepository.getInstance(BaseNetwork.getInstance(context), SharedPreferenceHelper.getInstance(context), this,
                AppDatabase.getInstance(context));
        tipeRepository = TipeRepository.getInstance(BaseNetwork.getInstance(context), SharedPreferenceHelper.getInstance(context), this,
                AppDatabase.getInstance(context));
        outletRepository = OutletRepository.getInstance(BaseNetwork.getInstance(context), SharedPreferenceHelper.getInstance(context), this,
                AppDatabase.getInstance(context));
    }

    public LiveData<VisitPlanDb> submitCheckout(JSONObject jsonObject, VisitPlanDb visitPlanDb){
        loading.setValue(true);
        return visitPlanRepository.submitCheckout(jsonObject, visitPlanDb);
    }

    public LiveData<VisitPlanDb> saveVisit(VisitPlanDb visitPlanDb){
        loading.setValue(true);
        return visitPlanRepository.saveVisit(visitPlanDb);
    }

    public LiveData<Outlet> getOutlet(String id){
        outletLiveData = outletRepository.getOutlet(id);
        return outletLiveData;
    }

    public LiveData<Tipe> getTipe(int id){
        tipeLiveData = tipeRepository.getTipe(id);
        return tipeLiveData;
    }

    public LiveData<Tipe> getTipeLiveData() {
        return tipeLiveData;
    }
}
