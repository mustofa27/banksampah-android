package com.mustofa27.banksampah.model.repository;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.mustofa27.banksampah.model.datasource.Result;
import com.mustofa27.banksampah.model.datasource.local.AppDatabase;
import com.mustofa27.banksampah.model.datasource.network.BaseNetwork;
import com.mustofa27.banksampah.model.datasource.network.ConnectionHandler;
import com.mustofa27.banksampah.model.datasource.network.NetworkCallback;
import com.mustofa27.banksampah.model.entity.Saving;
import com.mustofa27.banksampah.model.entity.SavingPagination;
import com.mustofa27.banksampah.model.entity.UserToken;
import com.mustofa27.banksampah.model.helper.SharedPreferenceHelper;
import com.mustofa27.banksampah.viewmodel.VMRepoInterface;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Class that requests authentication and user information from the remote data source and
 * maintains an in-memory cache of login status and user credentials information.
 */
public class SavingRepository extends BaseRepository {

    private static volatile SavingRepository instance;
    private MutableLiveData<ArrayList<Saving>> savingListMutableLiveData;
    int last_page, current_page;

    private SavingRepository(BaseNetwork baseNetwork, SharedPreferenceHelper sharedPreferenceHelper, VMRepoInterface vmRepoInterface, AppDatabase db) {
        super(baseNetwork, sharedPreferenceHelper, vmRepoInterface, db);
    }

    public static SavingRepository getInstance(BaseNetwork baseNetwork, SharedPreferenceHelper sharedPreferenceHelper, VMRepoInterface vmRepoInterface, AppDatabase db) {
        if (instance == null) {
            instance = new SavingRepository(baseNetwork, sharedPreferenceHelper, vmRepoInterface, db);
        } else{
            instance.vmRepoInterface = vmRepoInterface;
        }
        instance.last_page = 0;
        return instance;
    }

    public LiveData<ArrayList<Saving>> getData(int page){
        if(page == 1){
            savingListMutableLiveData = new MutableLiveData<>();
        }
        if(page <= last_page || last_page == 0) {
            dataSource.Connect(ConnectionHandler.post_method, "saving/my?page=" + page, null, new NetworkCallback() {
                @Override
                public void onFinish() {

                }

                @Override
                public void onSuccess(Result result) {
                    SavingPagination savingPagination = dataSource.getGson().fromJson(((Result.Success) result).getData().toString(), SavingPagination.class);
                    last_page = savingPagination.getLast_page();
                    current_page = savingPagination.getCurrent_page();
                    savingListMutableLiveData.setValue(savingPagination.getData());
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
        return savingListMutableLiveData;
    }
    public void addSaving(float weight, int garbage_id){
        // handle login
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("weight", weight);
            jsonObject.put("garbage_id", garbage_id);
            dataSource.Connect(ConnectionHandler.post_method, "saving", jsonObject, new NetworkCallback() {
                @Override
                public void onFinish() {

                }

                @Override
                public void onSuccess(Result result) {
                    vmRepoInterface.setMessage(result.toString());
                    vmRepoInterface.getStatus().setValue(true);
                }

                @Override
                public void onError(Result result) {
                    vmRepoInterface.setMessage(result.toString());
                    vmRepoInterface.getStatus().setValue(false);
                }
            });
        } catch (JSONException e){
            vmRepoInterface.setMessage(e.getMessage());
            vmRepoInterface.getStatus().setValue(false);
        }
    }
    public boolean isNextPageAvailable(){
        return current_page < last_page;
    }
}