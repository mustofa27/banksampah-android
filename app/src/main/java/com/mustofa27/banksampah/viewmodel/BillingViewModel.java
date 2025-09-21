package com.mustofa27.banksampah.viewmodel;

import android.content.Context;

import androidx.lifecycle.LiveData;

import com.mustofa27.banksampah.model.datasource.local.AppDatabase;
import com.mustofa27.banksampah.model.datasource.network.BaseNetwork;
import com.mustofa27.banksampah.model.datasource.network.MultipartFile;
import com.mustofa27.banksampah.model.entity.BillingData;
import com.mustofa27.banksampah.model.entity.Configuration;
import com.mustofa27.banksampah.model.helper.SharedPreferenceHelper;
import com.mustofa27.banksampah.model.repository.BillingRepository;
import com.mustofa27.banksampah.model.repository.ConfigRepository;

import java.util.Map;


public class BillingViewModel extends BaseViewModel {

    BillingRepository billingRepository;
    LiveData<Configuration> configurationLiveData;
    ConfigRepository configRepository;

    public BillingViewModel(Context context) {
        billingRepository = BillingRepository.getInstance(BaseNetwork.getInstance(context), SharedPreferenceHelper.getInstance(context), this,
                AppDatabase.getInstance(context));
        configRepository = ConfigRepository.getInstance(BaseNetwork.getInstance(context), SharedPreferenceHelper.getInstance(context), this,
                AppDatabase.getInstance(context));
    }

    public void submit(Map<String, Object> param, Map<String, MultipartFile> paramFile){
        loading.setValue(true);
        billingRepository.submit(param, paramFile);
    }

    public void submit(BillingData billingData){
        loading.setValue(true);
        billingRepository.saveBilling(billingData);
    }

    public LiveData<Configuration> getConfigurationLiveData() {
        return configurationLiveData;
    }
    public LiveData<Configuration> getConfiguration(boolean isDraft){
        configurationLiveData = configRepository.getConfigurationMutableLiveData(isDraft);
        return configurationLiveData;
    }
}
