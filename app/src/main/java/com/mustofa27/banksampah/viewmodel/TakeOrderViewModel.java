package com.mustofa27.banksampah.viewmodel;

import android.content.Context;

import androidx.lifecycle.LiveData;

import com.mustofa27.banksampah.model.datasource.local.AppDatabase;
import com.mustofa27.banksampah.model.datasource.network.BaseNetwork;
import com.mustofa27.banksampah.model.helper.SharedPreferenceHelper;
import com.mustofa27.banksampah.model.repository.TakeOrderRepository;

import org.json.JSONObject;


public class TakeOrderViewModel extends BaseViewModel {

    TakeOrderRepository takeOrderRepository;

    public TakeOrderViewModel(Context context) {
        takeOrderRepository = TakeOrderRepository.getInstance(BaseNetwork.getInstance(context), SharedPreferenceHelper.getInstance(context), this,
                AppDatabase.getInstance(context));
    }

    public void submitTakeOrder(JSONObject jsonObject){
        loading.setValue(true);
        //takeOrderRepository.submitTakeOrder(jsonObject);
    }
}
