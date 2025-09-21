package com.mustofa27.banksampah.model.repository;

import androidx.lifecycle.MutableLiveData;

import com.mustofa27.banksampah.model.datasource.Result;
import com.mustofa27.banksampah.model.datasource.local.AppDatabase;
import com.mustofa27.banksampah.model.datasource.network.BaseNetwork;
import com.mustofa27.banksampah.model.datasource.network.ConnectionHandler;
import com.mustofa27.banksampah.model.datasource.network.NetworkCallback;
import com.mustofa27.banksampah.model.entity.NewsClass;
import com.mustofa27.banksampah.model.helper.SharedPreferenceHelper;
import com.mustofa27.banksampah.viewmodel.VMRepoInterface;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Class that requests authentication and user information from the remote data source and
 * maintains an in-memory cache of login status and user credentials information.
 */
public class NewsRepository extends BaseRepository {

    private static volatile NewsRepository instance;
    private MutableLiveData<ArrayList<NewsClass>> newsMutableLiveData;

    private NewsRepository(BaseNetwork baseNetwork, SharedPreferenceHelper sharedPreferenceHelper, VMRepoInterface vmRepoInterface, AppDatabase db) {
        super(baseNetwork, sharedPreferenceHelper, vmRepoInterface, db);
        newsMutableLiveData = new MutableLiveData<>();
    }

    public static NewsRepository getInstance(BaseNetwork baseNetwork, SharedPreferenceHelper sharedPreferenceHelper, VMRepoInterface vmRepoInterface, AppDatabase db) {
        if (instance == null) {
            instance = new NewsRepository(baseNetwork, sharedPreferenceHelper, vmRepoInterface, db);
        }
        return instance;
    }

    public MutableLiveData<ArrayList<NewsClass>> getAllNewsClass(){
        // handle login
        if(newsMutableLiveData.getValue() == null || newsMutableLiveData.getValue().size() == 0) {
            dataSource.Connect(ConnectionHandler.get_method, "getNews", null, new NetworkCallback() {
                @Override
                public void onFinish() {

                }

                @Override
                public void onSuccess(Result result) {
                    NewsClass[] tipeArray = dataSource.getGson().fromJson(((Result.Success) result).getData().toString(), NewsClass[].class);
                    ArrayList<NewsClass> newsClasses = new ArrayList<>();
                    newsClasses.addAll(Arrays.asList(tipeArray));
                    newsMutableLiveData.setValue(newsClasses);
                    vmRepoInterface.setMessage(result.toString());
                }

                @Override
                public void onError(Result result) {
                    vmRepoInterface.setMessage(result.toString());
                    vmRepoInterface.getStatus().setValue(false);
                }
            });
        }
        return newsMutableLiveData;
    }
}