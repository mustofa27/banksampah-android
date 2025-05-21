package com.qiratek.rnpsales.model.repository;

import androidx.lifecycle.MutableLiveData;

import com.qiratek.rnpsales.model.datasource.Result;
import com.qiratek.rnpsales.model.datasource.local.AppDatabase;
import com.qiratek.rnpsales.model.datasource.network.BaseNetwork;
import com.qiratek.rnpsales.model.datasource.network.ConnectionHandler;
import com.qiratek.rnpsales.model.datasource.network.MultipartFile;
import com.qiratek.rnpsales.model.datasource.network.NetworkCallback;
import com.qiratek.rnpsales.model.entity.FeedOutlet;
import com.qiratek.rnpsales.model.entity.User;
import com.qiratek.rnpsales.model.helper.SharedPreferenceHelper;
import com.qiratek.rnpsales.viewmodel.VMRepoInterface;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;

/**
 * Class that requests authentication and user information from the remote data source and
 * maintains an in-memory cache of login status and user credentials information.
 */
public class MarketUpdateRepository extends BaseRepository {

    private static volatile MarketUpdateRepository instance;
    private User user;
    private MutableLiveData<ArrayList<FeedOutlet>> feedOutletListMutableLiveData;

    private MarketUpdateRepository(BaseNetwork baseNetwork, SharedPreferenceHelper sharedPreferenceHelper, VMRepoInterface vmRepoInterface, AppDatabase db) {
        super(baseNetwork, sharedPreferenceHelper, vmRepoInterface, db);
    }

    public static MarketUpdateRepository getInstance(BaseNetwork baseNetwork, SharedPreferenceHelper sharedPreferenceHelper, VMRepoInterface vmRepoInterface, AppDatabase db) {
        if (instance == null) {
            instance = new MarketUpdateRepository(baseNetwork, sharedPreferenceHelper, vmRepoInterface, db);
        }
        instance.vmRepoInterface = vmRepoInterface;
        instance.user = baseNetwork.getUser();
        return instance;
    }

    public void submit(Map<String, Object> param, Map<String, MultipartFile> paramFile){
        param.put("user_id", user.getId());
        dataSource.Connect(ConnectionHandler.post_method, "setPhotoActivity", param, paramFile, new NetworkCallback() {
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
    }

    public MutableLiveData<ArrayList<FeedOutlet>> getFromCloud(){
        feedOutletListMutableLiveData = new MutableLiveData<>();
        dataSource.Connect(ConnectionHandler.post_method, "getPhotoList", null, new NetworkCallback() {
            @Override
            public void onFinish() {

            }

            @Override
            public void onSuccess(Result result) {
                FeedOutlet[] feedOutlets = dataSource.getGson().fromJson(((Result.Success) result).getData().toString(), FeedOutlet[].class);
                ArrayList<FeedOutlet> feedOutlets1 = new ArrayList<>();
                feedOutlets1.addAll(Arrays.asList(feedOutlets));
                feedOutletListMutableLiveData.postValue(feedOutlets1);
                vmRepoInterface.setMessage(result.toString());
            }

            @Override
            public void onError(Result result) {
                vmRepoInterface.setMessage(result.toString());
                vmRepoInterface.getStatus().setValue(false);
            }
        });
        return feedOutletListMutableLiveData;
    }
}