package com.mustofa27.banksampah.model.repository;

import androidx.lifecycle.MutableLiveData;

import com.mustofa27.banksampah.model.datasource.Result;
import com.mustofa27.banksampah.model.datasource.local.AppDatabase;
import com.mustofa27.banksampah.model.datasource.network.BaseNetwork;
import com.mustofa27.banksampah.model.datasource.network.ConnectionHandler;
import com.mustofa27.banksampah.model.datasource.network.NetworkCallback;
import com.mustofa27.banksampah.model.entity.City;
import com.mustofa27.banksampah.model.entity.User;
import com.mustofa27.banksampah.model.helper.SharedPreferenceHelper;
import com.mustofa27.banksampah.viewmodel.VMRepoInterface;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Class that requests authentication and user information from the remote data source and
 * maintains an in-memory cache of login status and user credentials information.
 */
public class CityRepository extends BaseRepository {

    private static volatile CityRepository instance;
    private MutableLiveData<ArrayList<City>> cityMutableLiveData;
    private User user;

    private CityRepository(BaseNetwork baseNetwork, SharedPreferenceHelper sharedPreferenceHelper, VMRepoInterface vmRepoInterface, AppDatabase db) {
        super(baseNetwork, sharedPreferenceHelper, vmRepoInterface, db);
        cityMutableLiveData = new MutableLiveData<>();
        user = baseNetwork.getGson().fromJson(sharedPreferenceHelper.getPreference(User.table), User.class);
    }

    public static CityRepository getInstance(BaseNetwork baseNetwork, SharedPreferenceHelper sharedPreferenceHelper, VMRepoInterface vmRepoInterface, AppDatabase db) {
        if (instance == null) {
            instance = new CityRepository(baseNetwork, sharedPreferenceHelper, vmRepoInterface, db);
        }
        return instance;
    }

    public MutableLiveData<ArrayList<City>> getAllCity(){
        // handle login
        if(cityMutableLiveData.getValue() == null || cityMutableLiveData.getValue().size() == 0) {
            new Thread() {
                @Override
                public void run() {
                    ArrayList<City> localData = new ArrayList<>();
                    localData.addAll(db.cityDAO().getAll());
                    if (localData.size() == 0) {
                        dataSource.Connect(ConnectionHandler.post_method, "getAllCity", null, new NetworkCallback() {
                            @Override
                            public void onFinish() {

                            }

                            @Override
                            public void onSuccess(Result result) {
                                City[] cityArray = dataSource.getGson().fromJson(((Result.Success) result).getData().toString(), City[].class);
                                ArrayList<City> cities = new ArrayList<>();
                                cities.addAll(Arrays.asList(cityArray));
                                cityMutableLiveData.setValue(cities);
                                new Thread() {
                                    @Override
                                    public void run() {
                                        List<Long> tmp = db.cityDAO().insertAll(cityArray);
                                    }
                                }.start();

                                vmRepoInterface.setMessage(result.toString());
                                vmRepoInterface.getStatus().setValue(true);
                            }

                            @Override
                            public void onError(Result result) {
                                vmRepoInterface.setMessage(result.toString());
                                vmRepoInterface.getStatus().setValue(false);
                                cityMutableLiveData.postValue(localData);
                            }
                        });
                    } else {
                        vmRepoInterface.getStatus().postValue(true);
                        vmRepoInterface.setMessage("Data berhasil didapatkan");
                        cityMutableLiveData.postValue(localData);
                    }
                }
            }.start();
        }
        return cityMutableLiveData;
    }

    public MutableLiveData<ArrayList<City>> getAllCityFromCloud(){
        // handle login
        MutableLiveData<ArrayList<City>> data = new MutableLiveData<>();
        dataSource.Connect(ConnectionHandler.post_method, "getAllCity", null, new NetworkCallback() {
            @Override
            public void onFinish() {

            }

            @Override
            public void onSuccess(Result result) {
                City[] cityArray = dataSource.getGson().fromJson(((Result.Success) result).getData().toString(), City[].class);
                ArrayList<City> cities = new ArrayList<>();
                cities.addAll(Arrays.asList(cityArray));
                new Thread() {
                    @Override
                    public void run() {
                        List<Long> tmp = db.cityDAO().insertAll(cityArray);
                        data.postValue(cities);
                        vmRepoInterface.setMessage(result.toString());
                    }
                }.start();
            }

            @Override
            public void onError(Result result) {
                vmRepoInterface.setMessage(result.toString());
                vmRepoInterface.getStatus().setValue(false);
            }
        });
        return data;
    }
}