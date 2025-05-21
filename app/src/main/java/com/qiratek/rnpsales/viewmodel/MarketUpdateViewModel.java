package com.qiratek.rnpsales.viewmodel;

import android.content.Context;

import com.qiratek.rnpsales.model.datasource.local.AppDatabase;
import com.qiratek.rnpsales.model.datasource.network.BaseNetwork;
import com.qiratek.rnpsales.model.datasource.network.MultipartFile;
import com.qiratek.rnpsales.model.helper.SharedPreferenceHelper;
import com.qiratek.rnpsales.model.repository.MarketUpdateRepository;

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
