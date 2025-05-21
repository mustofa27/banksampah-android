package com.qiratek.rnpsales.viewmodel;

import android.content.Context;

import androidx.lifecycle.LiveData;

import com.qiratek.rnpsales.model.datasource.local.AppDatabase;
import com.qiratek.rnpsales.model.datasource.network.BaseNetwork;
import com.qiratek.rnpsales.model.entity.Configuration;
import com.qiratek.rnpsales.model.entity.TakeOrderData;
import com.qiratek.rnpsales.model.helper.SharedPreferenceHelper;
import com.qiratek.rnpsales.model.repository.ConfigRepository;
import com.qiratek.rnpsales.model.repository.TakeOrderRepository;

import org.json.JSONObject;


public class TakeOrderViewModel extends BaseViewModel {

    TakeOrderRepository takeOrderRepository;
    LiveData<Configuration> configurationLiveData;
    ConfigRepository configRepository;

    public TakeOrderViewModel(Context context) {
        takeOrderRepository = TakeOrderRepository.getInstance(BaseNetwork.getInstance(context), SharedPreferenceHelper.getInstance(context), this,
                AppDatabase.getInstance(context));
        configRepository = ConfigRepository.getInstance(BaseNetwork.getInstance(context), SharedPreferenceHelper.getInstance(context), this,
                AppDatabase.getInstance(context));
    }

    public void submitTakeOrder(JSONObject jsonObject){
        loading.setValue(true);
        takeOrderRepository.submitTakeOrder(jsonObject);
    }

    public void saveTakeOrder(TakeOrderData takeOrderData){
        loading.setValue(true);
        takeOrderRepository.saveTakeOrder(takeOrderData);
    }

    public LiveData<Configuration> getConfigurationLiveData() {
        return configurationLiveData;
    }
    public LiveData<Configuration> getConfiguration(boolean isDraft){
        configurationLiveData = configRepository.getConfigurationMutableLiveData(isDraft);
        return configurationLiveData;
    }
}
