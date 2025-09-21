package com.mustofa27.banksampah.viewmodel;

import android.content.Context;


import androidx.lifecycle.LiveData;

import com.mustofa27.banksampah.model.datasource.local.AppDatabase;
import com.mustofa27.banksampah.model.datasource.network.BaseNetwork;
import com.mustofa27.banksampah.model.entity.NewsClass;
import com.mustofa27.banksampah.model.entity.Product;
import com.mustofa27.banksampah.model.helper.SharedPreferenceHelper;
import com.mustofa27.banksampah.model.repository.NewsRepository;
import com.mustofa27.banksampah.model.repository.ProductRepository;
import com.mustofa27.banksampah.model.repository.UserRepository;

import java.util.ArrayList;


public class HomeViewModel extends BaseViewModel {

    UserRepository userRepository;

    ProductRepository productRepository;
    LiveData<ArrayList<Product>> productLiveData;
    NewsRepository newsRepository;
    LiveData<ArrayList<NewsClass>> newsLiveData;

    public HomeViewModel(Context context) {
        userRepository = UserRepository.getInstance(BaseNetwork.getInstance(context), SharedPreferenceHelper.getInstance(context),
                this, AppDatabase.getInstance(context));

        productRepository = ProductRepository.getInstance(BaseNetwork.getInstance(context), SharedPreferenceHelper.getInstance(context), this,
                AppDatabase.getInstance(context));

        newsRepository = NewsRepository.getInstance(BaseNetwork.getInstance(context), SharedPreferenceHelper.getInstance(context), this,
                AppDatabase.getInstance(context));

    }

    public LiveData<ArrayList<NewsClass>> getAllNews(){
        newsLiveData = newsRepository.getAllNewsClass();
        if(newsLiveData.getValue() == null || newsLiveData.getValue().size() == 0) {
            loading.setValue(true);
        }
        return newsLiveData;
    }
    public LiveData<ArrayList<Product>> getAllProduct(){
        productLiveData = productRepository.getFromCloud();
        return productLiveData;
    }

    public LiveData<ArrayList<Product>> getProductLiveData() {
        return productLiveData;
    }

    public LiveData<ArrayList<NewsClass>> getNewsLiveData() {
        return newsLiveData;
    }
}
