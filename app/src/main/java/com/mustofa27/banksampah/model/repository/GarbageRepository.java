package com.mustofa27.banksampah.model.repository;

import androidx.lifecycle.MutableLiveData;

import com.mustofa27.banksampah.model.datasource.Result;
import com.mustofa27.banksampah.model.datasource.local.AppDatabase;
import com.mustofa27.banksampah.model.datasource.network.BaseNetwork;
import com.mustofa27.banksampah.model.datasource.network.ConnectionHandler;
import com.mustofa27.banksampah.model.datasource.network.NetworkCallback;
import com.mustofa27.banksampah.model.entity.Garbage;
import com.mustofa27.banksampah.model.entity.Product;
import com.mustofa27.banksampah.model.helper.SharedPreferenceHelper;
import com.mustofa27.banksampah.viewmodel.VMRepoInterface;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Class that requests authentication and user information from the remote data source and
 * maintains an in-memory cache of login status and user credentials information.
 */
public class GarbageRepository extends BaseRepository {

    private static volatile GarbageRepository instance;
    private MutableLiveData<ArrayList<Garbage>> garbageListMutableLiveData;

    private GarbageRepository(BaseNetwork baseNetwork, SharedPreferenceHelper sharedPreferenceHelper, VMRepoInterface vmRepoInterface, AppDatabase db) {
        super(baseNetwork, sharedPreferenceHelper, vmRepoInterface, db);
    }

    public static GarbageRepository getInstance(BaseNetwork baseNetwork, SharedPreferenceHelper sharedPreferenceHelper, VMRepoInterface vmRepoInterface, AppDatabase db) {
        if (instance == null) {
            instance = new GarbageRepository(baseNetwork, sharedPreferenceHelper, vmRepoInterface, db);
        } else{
            instance.vmRepoInterface = vmRepoInterface;
        }
        return instance;
    }

    public MutableLiveData<ArrayList<Garbage>> getAllGarbage(){
        garbageListMutableLiveData = new MutableLiveData<>();
        new Thread() {
            @Override
            public void run() {
                ArrayList<Garbage> localData = new ArrayList<>();
                localData.addAll(db.garbageDAO().getAll());
                if (localData.size() == 0) {
                    dataSource.Connect(ConnectionHandler.get_method, "garbage", null, new NetworkCallback() {
                        @Override
                        public void onFinish() {

                        }

                        @Override
                        public void onSuccess(Result result) {
                            Garbage[] garbage = dataSource.getGson().fromJson(((Result.Success) result).getData().toString(), Garbage[].class);
                            ArrayList<Garbage> garbageArrayList = new ArrayList<>();
                            garbageArrayList.addAll(Arrays.asList(garbage));
                            garbageListMutableLiveData.setValue(garbageArrayList);
                            new Thread() {
                                @Override
                                public void run() {
                                    List<Long> tmp = db.garbageDAO().insertAll(garbage);
                                }
                            }.start();

                            vmRepoInterface.setMessage(result.toString());
                            vmRepoInterface.getStatus().setValue(true);
                        }

                        @Override
                        public void onError(Result result) {
                            vmRepoInterface.setMessage(result.toString());
                            vmRepoInterface.getStatus().setValue(false);
                        }
                    });
                } else {
                    vmRepoInterface.getStatus().postValue(true);
                    vmRepoInterface.setMessage("Data berhasil didapatkan");
                    garbageListMutableLiveData.postValue(localData);
                }
            }
        }.start();
        return garbageListMutableLiveData;
    }
}