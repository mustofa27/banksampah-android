package com.mustofa27.banksampah.viewmodel;

import android.content.Context;

import androidx.lifecycle.LiveData;

import com.mustofa27.banksampah.model.datasource.local.AppDatabase;
import com.mustofa27.banksampah.model.datasource.network.BaseNetwork;
import com.mustofa27.banksampah.model.entity.Brand;
import com.mustofa27.banksampah.model.entity.Product;
import com.mustofa27.banksampah.model.entity.Satuan;
import com.mustofa27.banksampah.model.helper.SharedPreferenceHelper;
import com.mustofa27.banksampah.model.repository.BrandRepository;
import com.mustofa27.banksampah.model.repository.ProductRepository;
import com.mustofa27.banksampah.model.repository.SatuanRepository;

import java.util.ArrayList;


public class ProductSearchViewModel extends BaseViewModel {

    BrandRepository brandRepository;
    ProductRepository productRepository;
    SatuanRepository satuanRepository;
    LiveData<ArrayList<Brand>> brandLiveData;
    LiveData<ArrayList<Product>> productLiveData;
    LiveData<ArrayList<Satuan>> satuanLiveData;

    public ProductSearchViewModel(Context context) {
        brandRepository = BrandRepository.getInstance(BaseNetwork.getInstance(context), SharedPreferenceHelper.getInstance(context), this,
                AppDatabase.getInstance(context));
        productRepository = ProductRepository.getInstance(BaseNetwork.getInstance(context), SharedPreferenceHelper.getInstance(context), this,
                AppDatabase.getInstance(context));
        satuanRepository = SatuanRepository.getInstance(BaseNetwork.getInstance(context), SharedPreferenceHelper.getInstance(context), this,
                AppDatabase.getInstance(context));
    }

    public LiveData<ArrayList<Product>> getAllProduct(boolean isRefreshed){
        if(isRefreshed){
            productLiveData = productRepository.getFromCloud();
        } else{
            productLiveData = productRepository.getAllProduk();
        }
        if(productLiveData.getValue() == null || productLiveData.getValue().size() == 0) {
            loading.setValue(true);
        }
        return productLiveData;
    }

    public LiveData<ArrayList<Brand>> getAllBrand(boolean isRefreshed){
        brandLiveData = brandRepository.getAllBrand(isRefreshed);
        if(brandLiveData.getValue() == null || brandLiveData.getValue().size() == 0) {
            loading.setValue(true);
        }
        return brandLiveData;
    }

    public LiveData<ArrayList<Satuan>> getAllSatuan(boolean isRefreshed){
        satuanLiveData = satuanRepository.getAllSatuan(isRefreshed);
        if(satuanLiveData.getValue() == null || satuanLiveData.getValue().size() == 0) {
            loading.setValue(true);
        }
        return satuanLiveData;
    }


}
