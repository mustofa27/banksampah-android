package com.mustofa27.banksampah.viewmodel;

import android.content.Context;

import androidx.lifecycle.LiveData;

import com.mustofa27.banksampah.model.datasource.local.AppDatabase;
import com.mustofa27.banksampah.model.datasource.network.BaseNetwork;
import com.mustofa27.banksampah.model.entity.Product;
import com.mustofa27.banksampah.model.entity.Saving;
import com.mustofa27.banksampah.model.helper.SharedPreferenceHelper;
import com.mustofa27.banksampah.model.repository.ProductRepository;

import java.util.ArrayList;


public class ProductViewModel extends BaseViewModel {

    ProductRepository productRepository;
    LiveData<ArrayList<Product>> productLiveData;
    int page;
    boolean isNextPage = false;

    public ProductViewModel(Context context) {
        productRepository = ProductRepository.getInstance(BaseNetwork.getInstance(context), SharedPreferenceHelper.getInstance(context),
                this, AppDatabase.getInstance(context));
        page = 1;
    }
    public LiveData<ArrayList<Product>> getProduct(){
        productLiveData = productRepository.getData(page++);
        isNextPage = false;
        return productLiveData;
    }
    public boolean isNextAvailable(){
        return productRepository.isNextPageAvailable();
    }
    public void getNextData() {
        productLiveData = productRepository.getData(page++);
        isNextPage = true;
    }
    public boolean isNextPage() {
        return isNextPage;
    }
    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }
}
