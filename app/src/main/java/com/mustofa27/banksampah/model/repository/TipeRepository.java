package com.mustofa27.banksampah.model.repository;

import androidx.lifecycle.MutableLiveData;

import com.mustofa27.banksampah.model.datasource.Result;
import com.mustofa27.banksampah.model.datasource.local.AppDatabase;
import com.mustofa27.banksampah.model.datasource.network.BaseNetwork;
import com.mustofa27.banksampah.model.datasource.network.ConnectionHandler;
import com.mustofa27.banksampah.model.datasource.network.NetworkCallback;
import com.mustofa27.banksampah.model.entity.Tipe;
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
public class TipeRepository extends BaseRepository {

    private static volatile TipeRepository instance;
    private MutableLiveData<ArrayList<Tipe>> tipesMutableLiveData;
    private MutableLiveData<Tipe> tipeMutableLiveData;
    private User user;

    private TipeRepository(BaseNetwork baseNetwork, SharedPreferenceHelper sharedPreferenceHelper, VMRepoInterface vmRepoInterface, AppDatabase db) {
        super(baseNetwork, sharedPreferenceHelper, vmRepoInterface, db);
        tipesMutableLiveData = new MutableLiveData<>();
        tipeMutableLiveData = new MutableLiveData<>();
        user = baseNetwork.getGson().fromJson(sharedPreferenceHelper.getPreference(User.table), User.class);
    }

    public static TipeRepository getInstance(BaseNetwork baseNetwork, SharedPreferenceHelper sharedPreferenceHelper, VMRepoInterface vmRepoInterface, AppDatabase db) {
        if (instance == null) {
            instance = new TipeRepository(baseNetwork, sharedPreferenceHelper, vmRepoInterface, db);
        }
        return instance;
    }

    public MutableLiveData<ArrayList<Tipe>> getAllTipe(){
        // handle login
        if(tipesMutableLiveData.getValue() == null || tipesMutableLiveData.getValue().size() == 0) {
            new Thread() {
                @Override
                public void run() {
                    ArrayList<Tipe> localData = new ArrayList<>();
                    localData.addAll(db.tipeDAO().getAll());
                    if (localData.size() == 0) {
                        dataSource.Connect(ConnectionHandler.get_method, "getTipeOutlet", null, new NetworkCallback() {
                            @Override
                            public void onFinish() {

                            }

                            @Override
                            public void onSuccess(Result result) {
                                Tipe[] tipeArray = dataSource.getGson().fromJson(((Result.Success) result).getData().toString(), Tipe[].class);
                                ArrayList<Tipe> tipes = new ArrayList<>();
                                tipes.addAll(Arrays.asList(tipeArray));
                                tipesMutableLiveData.setValue(tipes);
                                new Thread() {
                                    @Override
                                    public void run() {
                                        List<Long> tmp = db.tipeDAO().insertAll(tipeArray);
                                    }
                                }.start();

                                vmRepoInterface.setMessage(result.toString());
                                vmRepoInterface.getStatus().setValue(true);
                            }

                            @Override
                            public void onError(Result result) {
                                vmRepoInterface.setMessage(result.toString());
                                vmRepoInterface.getStatus().setValue(false);
                                tipesMutableLiveData.postValue(localData);
                            }
                        });
                    } else {
                        vmRepoInterface.getStatus().postValue(true);
                        vmRepoInterface.setMessage("Data berhasil didapatkan");
                        tipesMutableLiveData.postValue(localData);
                    }
                }
            }.start();
        }
        return tipesMutableLiveData;
    }

    public MutableLiveData<ArrayList<Tipe>> getAllTipeFromCloud(){
        // handle login
        MutableLiveData<ArrayList<Tipe>> data = new MutableLiveData<>();
        dataSource.Connect(ConnectionHandler.get_method, "getTipeOutlet", null, new NetworkCallback() {
            @Override
            public void onFinish() {

            }

            @Override
            public void onSuccess(Result result) {
                Tipe[] tipeArray = dataSource.getGson().fromJson(((Result.Success) result).getData().toString(), Tipe[].class);
                ArrayList<Tipe> tipes = new ArrayList<>();
                tipes.addAll(Arrays.asList(tipeArray));
                new Thread() {
                    @Override
                    public void run() {
                        List<Long> tmp = db.tipeDAO().insertAll(tipeArray);
                        data.postValue(tipes);
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

    public MutableLiveData<Tipe> getTipe(int id){
        // handle login
        tipeMutableLiveData = new MutableLiveData<>();
        if(tipeMutableLiveData.getValue() == null || tipeMutableLiveData.getValue().getId() != id) {
            new Thread() {
                @Override
                public void run() {
                    Tipe tipe = db.tipeDAO().getTipe(id);
                    vmRepoInterface.getStatus().postValue(true);
                    vmRepoInterface.setMessage("Data berhasil didapatkan");
                    tipeMutableLiveData.postValue(tipe);
                }
            }.start();
        }
        return tipeMutableLiveData;
    }
}