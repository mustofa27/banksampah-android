package com.mustofa27.banksampah.model.repository;

import androidx.lifecycle.MutableLiveData;

import com.mustofa27.banksampah.model.datasource.Result;
import com.mustofa27.banksampah.model.datasource.local.AppDatabase;
import com.mustofa27.banksampah.model.datasource.network.BaseNetwork;
import com.mustofa27.banksampah.model.datasource.network.ConnectionHandler;
import com.mustofa27.banksampah.model.datasource.network.NetworkCallback;
import com.mustofa27.banksampah.model.entity.Satuan;
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
public class SatuanRepository extends BaseRepository {

    private static volatile SatuanRepository instance;
    private MutableLiveData<ArrayList<Satuan>> satuanListMutableLiveData;
    private User user;

    private SatuanRepository(BaseNetwork baseNetwork, SharedPreferenceHelper sharedPreferenceHelper, VMRepoInterface vmRepoInterface, AppDatabase db) {
        super(baseNetwork, sharedPreferenceHelper, vmRepoInterface, db);
        satuanListMutableLiveData = new MutableLiveData<>();
    }

    public static SatuanRepository getInstance(BaseNetwork baseNetwork, SharedPreferenceHelper sharedPreferenceHelper, VMRepoInterface vmRepoInterface, AppDatabase db) {
        if (instance == null) {
            instance = new SatuanRepository(baseNetwork, sharedPreferenceHelper, vmRepoInterface, db);
        }
        instance.user = baseNetwork.getUser();
        return instance;
    }

    public MutableLiveData<ArrayList<Satuan>> getAllSatuan(boolean isRefreshed){
        if(isRefreshed){
            satuanListMutableLiveData = new MutableLiveData<>();
        }
        if(satuanListMutableLiveData.getValue() == null || satuanListMutableLiveData.getValue().size() == 0) {
            new Thread() {
                @Override
                public void run() {
                    ArrayList<Satuan> localData = new ArrayList<>();
                    localData.addAll(db.satuanDAO().getAll());
                    if (localData.size() == 0 || isRefreshed) {
                        dataSource.Connect(ConnectionHandler.post_method, "getSatuan", null, new NetworkCallback() {
                            @Override
                            public void onFinish() {

                            }

                            @Override
                            public void onSuccess(Result result) {
                                Satuan[] satuans = dataSource.getGson().fromJson(((Result.Success) result).getData().toString(), Satuan[].class);
                                ArrayList<Satuan> satuanArrayList = new ArrayList<>();
                                satuanArrayList.addAll(Arrays.asList(satuans));
                                new Thread() {
                                    @Override
                                    public void run() {
                                        List<Long> tmp = db.satuanDAO().insertAll(satuans);
                                        satuanListMutableLiveData.postValue(satuanArrayList);
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
                    } else{
                        vmRepoInterface.getStatus().postValue(true);
                        vmRepoInterface.setMessage("Data berhasil didapatkan");
                        satuanListMutableLiveData.postValue(localData);
                    }
                }
            }.start();
        }
        return satuanListMutableLiveData;
    }
}