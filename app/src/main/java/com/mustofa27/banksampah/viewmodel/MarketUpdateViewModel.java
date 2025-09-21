package com.mustofa27.banksampah.viewmodel;

import android.content.Context;

import com.mustofa27.banksampah.model.datasource.local.AppDatabase;
import com.mustofa27.banksampah.model.datasource.network.BaseNetwork;
import com.mustofa27.banksampah.model.datasource.network.MultipartFile;
import com.mustofa27.banksampah.model.helper.SharedPreferenceHelper;
import com.mustofa27.banksampah.model.repository.MarketUpdateRepository;

import java.util.Map;


public class MarketUpdateViewModel extends BaseViewModel {

    MarketUpdateRepository marketUpdateRepository;

    public MarketUpdateViewModel(Context context) {
        marketUpdateRepository = MarketUpdateRepository.getInstance(BaseNetwork.getInstance(context), SharedPreferenceHelper.getInstance(context), this,
                AppDatabase.getInstance(context));
    }

    public void submit(Map<String, Object> param, Map<String, MultipartFile> paramFile){
        loading.setValue(true);
        marketUpdateRepository.submit(param, paramFile);
    }
}
