package com.qiratek.rnpsales.viewmodel;

import android.content.Context;

import androidx.lifecycle.LiveData;

import com.qiratek.rnpsales.model.datasource.local.AppDatabase;
import com.qiratek.rnpsales.model.datasource.network.BaseNetwork;
import com.qiratek.rnpsales.model.datasource.network.MultipartFile;
import com.qiratek.rnpsales.model.entity.Configuration;
import com.qiratek.rnpsales.model.entity.Tipe;
import com.qiratek.rnpsales.model.entity.VisitPlanDb;
import com.qiratek.rnpsales.model.helper.SharedPreferenceHelper;
import com.qiratek.rnpsales.model.repository.ConfigRepository;
import com.qiratek.rnpsales.model.repository.TipeRepository;
import com.qiratek.rnpsales.model.repository.VisitPlanRepository;

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
