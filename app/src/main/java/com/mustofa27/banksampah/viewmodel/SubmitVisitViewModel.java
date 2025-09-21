package com.mustofa27.banksampah.viewmodel;

import android.content.Context;

import androidx.lifecycle.LiveData;

import com.mustofa27.banksampah.model.datasource.local.AppDatabase;
import com.mustofa27.banksampah.model.datasource.network.BaseNetwork;
import com.mustofa27.banksampah.model.datasource.network.MultipartFile;
import com.mustofa27.banksampah.model.entity.Configuration;
import com.mustofa27.banksampah.model.entity.Tipe;
import com.mustofa27.banksampah.model.entity.VisitPlanDb;
import com.mustofa27.banksampah.model.helper.SharedPreferenceHelper;
import com.mustofa27.banksampah.model.repository.ConfigRepository;
import com.mustofa27.banksampah.model.repository.TipeRepository;
import com.mustofa27.banksampah.model.repository.VisitPlanRepository;

import java.util.Map;


public class SubmitVisitViewModel extends BaseViewModel {

    VisitPlanRepository visitPlanRepository;
    LiveData<VisitPlanDb> visitPlanDbLiveData;
    LiveData<Tipe> tipeLiveData;
    TipeRepository tipeRepository;
    LiveData<Configuration> configurationLiveData;
    ConfigRepository configRepository;

    public SubmitVisitViewModel(Context context) {
        visitPlanRepository = VisitPlanRepository.getInstance(BaseNetwork.getInstance(context), SharedPreferenceHelper.getInstance(context), this,
                AppDatabase.getInstance(context));
        tipeRepository = TipeRepository.getInstance(BaseNetwork.getInstance(context), SharedPreferenceHelper.getInstance(context), this,
                AppDatabase.getInstance(context));
        configRepository = ConfigRepository.getInstance(BaseNetwork.getInstance(context), SharedPreferenceHelper.getInstance(context), this,
                AppDatabase.getInstance(context));
    }

    public LiveData<VisitPlanDb> submitVisit(Map<String, Object> param, Map<String, MultipartFile> paramFile){
        visitPlanDbLiveData = visitPlanRepository.submitVisit(param, paramFile);
        if(visitPlanDbLiveData == null || visitPlanDbLiveData.getValue() == null) {
            loading.setValue(true);
        }
        return visitPlanDbLiveData;
    }

    public LiveData<VisitPlanDb> saveVisit(VisitPlanDb visitPlanDb){
        visitPlanDbLiveData = visitPlanRepository.saveVisit(visitPlanDb);
        if(visitPlanDbLiveData == null || visitPlanDbLiveData.getValue() == null) {
            loading.setValue(true);
        }
        return visitPlanDbLiveData;
    }

    public LiveData<Tipe> getTipe(int id){
        tipeLiveData = tipeRepository.getTipe(id);
        return tipeLiveData;
    }

    public LiveData<Tipe> getTipeLiveData() {
        return tipeLiveData;
    }

    public LiveData<Configuration> getConfigurationLiveData() {
        return configurationLiveData;
    }
    public LiveData<Configuration> getConfiguration(boolean isDraft){
        configurationLiveData = configRepository.getConfigurationMutableLiveData(isDraft);
        return configurationLiveData;
    }
}
