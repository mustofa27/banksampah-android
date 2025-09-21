package com.mustofa27.banksampah.viewmodel;

import android.content.Context;

import androidx.lifecycle.LiveData;

import com.mustofa27.banksampah.model.datasource.local.AppDatabase;
import com.mustofa27.banksampah.model.datasource.network.BaseNetwork;
import com.mustofa27.banksampah.model.entity.NewsClass;
import com.mustofa27.banksampah.model.helper.SharedPreferenceHelper;
import com.mustofa27.banksampah.model.repository.NewsRepository;

import java.util.ArrayList;


public class NewsViewModel extends BaseViewModel {

    NewsRepository newsRepository;
    LiveData<ArrayList<NewsClass>> newsLiveData;

    public NewsViewModel(Context context) {
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
}
