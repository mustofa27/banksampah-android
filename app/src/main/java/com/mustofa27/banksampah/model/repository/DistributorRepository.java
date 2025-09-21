package com.mustofa27.banksampah.model.repository;

import androidx.lifecycle.MutableLiveData;

import com.mustofa27.banksampah.model.datasource.Result;
import com.mustofa27.banksampah.model.datasource.local.AppDatabase;
import com.mustofa27.banksampah.model.datasource.network.BaseNetwork;
import com.mustofa27.banksampah.model.datasource.network.ConnectionHandler;
import com.mustofa27.banksampah.model.datasource.network.NetworkCallback;
import com.mustofa27.banksampah.model.entity.Distributor;
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
public class DistributorRepository extends BaseRepository {

    private static volatile DistributorRepository instance;
    private MutableLiveData<ArrayList<Distributor>> distMutableLiveData;
    private User user;

    private DistributorRepository(BaseNetwork baseNetwork, SharedPreferenceHelper sharedPreferenceHelper, VMRepoInterface vmRepoInterface, AppDatabase db) {
        super(baseNetwork, sharedPreferenceHelper, vmRepoInterface, db);
        distMutableLiveData = new MutableLiveData<>();
        user = baseNetwork.getGson().fromJson(sharedPreferenceHelper.getPreference(User.table), User.class);
    }

    public static DistributorRepository getInstance(BaseNetwork baseNetwork, SharedPreferenceHelper sharedPreferenceHelper, VMRepoInterface vmRepoInterface, AppDatabase db) {
        if (instance == null) {
            instance = new DistributorRepository(baseNetwork, sharedPreferenceHelper, vmRepoInterface, db);
        }
        return instance;
    }

    public MutableLiveData<ArrayList<Distributor>> getAllDist(){
        // handle login
        if(distMutableLiveData.getValue() == null || distMutableLiveData.getValue().size() == 0) {
            dataSource.Connect(ConnectionHandler.post_method, "getAllDistributor", null, new NetworkCallback() {
                @Override
                public void onFinish() {

                }

                @Override
                public void onSuccess(Result result) {
                    Distributor[] distArray = dataSource.getGson().fromJson(((Result.Success) result).getData().toString(), Distributor[].class);
                    ArrayList<Distributor> distributors = new ArrayList<>();
                    distributors.addAll(Arrays.asList(distArray));
                    distMutableLiveData.setValue(distributors);
                    new Thread() {
                        @Override
                        public void run() {
                            List<Long> tmp = db.distDAO().insertAll(distArray);
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
        }
        return distMutableLiveData;
    }
}