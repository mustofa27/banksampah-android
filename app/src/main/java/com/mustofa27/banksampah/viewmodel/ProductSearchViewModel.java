package com.mustofa27.banksampah.viewmodel;

import android.content.Context;

import androidx.lifecycle.LiveData;

import com.mustofa27.banksampah.model.datasource.local.AppDatabase;
import com.mustofa27.banksampah.model.datasource.network.BaseNetwork;
import com.mustofa27.banksampah.model.entity.Product;
import com.mustofa27.banksampah.model.helper.SharedPreferenceHelper;
import com.mustofa27.banksampah.model.repository.ProductRepository;

import java.util.ArrayList;


public class ProductSearchViewModel extends BaseViewModel {

    ProductRepository productRepository;
    LiveData<ArrayList<Product>> productLiveData;
    public ProductSearchViewModel(Context context) {
        productRepository = ProductRepository.getInstance(BaseNetwork.getInstance(context), SharedPreferenceHelper.getInstance(context), this,
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

}
