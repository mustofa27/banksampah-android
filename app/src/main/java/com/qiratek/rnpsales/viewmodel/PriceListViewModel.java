package com.qiratek.rnpsales.viewmodel;

import android.content.Context;

import androidx.lifecycle.LiveData;

import com.qiratek.rnpsales.model.datasource.local.AppDatabase;
import com.qiratek.rnpsales.model.datasource.network.BaseNetwork;
import com.qiratek.rnpsales.model.entity.Brand;
import com.qiratek.rnpsales.model.entity.PriceList;
import com.qiratek.rnpsales.model.helper.SharedPreferenceHelper;
import com.qiratek.rnpsales.model.repository.BrandRepository;
import com.qiratek.rnpsales.model.repository.PriceListRepository;

import java.util.ArrayList;


public class PriceListViewModel extends BaseViewModel {

    BrandRepository brandRepository;
    PriceListRepository priceListRepository;
    LiveData<ArrayList<Brand>> brandLiveData;
    LiveData<ArrayList<PriceList>> priceLiveData;

    public PriceListViewModel(Context context) {
        brandRepository = BrandRepository.getInstance(BaseNetwork.getInstance(context), SharedPreferenceHelper.getInstance(context), this,
                AppDatabase.getInstance(context));
        priceListRepository = PriceListRepository.getInstance(BaseNetwork.getInstance(context), SharedPreferenceHelper.getInstance(context), this,
                AppDatabase.getInstance(context));
    }

    public LiveData<ArrayList<Brand>> getAllBrands(){
        brandLiveData = brandRepository.getAllBrandPrice();
        if(brandLiveData.getValue() == null || brandLiveData.getValue().size() == 0) {
            loading.setValue(true);
        }
        return brandLiveData;
    }

    public LiveData<ArrayList<PriceList>> getAllPrice(int brand_id){
        priceLiveData = priceListRepository.getAllPriceList(brand_id);
        if(priceLiveData.getValue() == null || priceLiveData.getValue().size() == 0) {
            loading.setValue(true);
        }
        return priceLiveData;
    }
}
