package com.mustofa27.banksampah.viewmodel;

import android.content.Context;

import androidx.lifecycle.LiveData;

import com.mustofa27.banksampah.model.datasource.local.AppDatabase;
import com.mustofa27.banksampah.model.datasource.network.BaseNetwork;
import com.mustofa27.banksampah.model.entity.Brand;
import com.mustofa27.banksampah.model.entity.PriceList;
import com.mustofa27.banksampah.model.helper.SharedPreferenceHelper;
import com.mustofa27.banksampah.model.repository.BrandRepository;
import com.mustofa27.banksampah.model.repository.PriceListRepository;

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
