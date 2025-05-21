package com.qiratek.rnpsales.model.repository;

import androidx.lifecycle.MutableLiveData;

import com.qiratek.rnpsales.model.datasource.Result;
import com.qiratek.rnpsales.model.datasource.local.AppDatabase;
import com.qiratek.rnpsales.model.datasource.network.BaseNetwork;
import com.qiratek.rnpsales.model.datasource.network.ConnectionHandler;
import com.qiratek.rnpsales.model.datasource.network.NetworkCallback;
import com.qiratek.rnpsales.model.entity.User;
import com.qiratek.rnpsales.model.helper.SharedPreferenceHelper;
import com.qiratek.rnpsales.viewmodel.VMRepoInterface;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Class that requests authentication and user information from the remote data source and
 * maintains an in-memory cache of login status and user credentials information.
 */
public class CheckInTimerRepository extends BaseRepository {

    private static volatile CheckInTimerRepository instance;
    private MutableLiveData<JSONObject> checkInTimerMutableLiveData;
    private User user;

    private CheckInTimerRepository(BaseNetwork baseNetwork, SharedPreferenceHelper sharedPreferenceHelper, VMRepoInterface vmRepoInterface, AppDatabase db) {
        super(baseNetwork, sharedPreferenceHelper, vmRepoInterface, db);
        user = baseNetwork.getGson().fromJson(sharedPreferenceHelper.getPreference(User.table), User.class);
    }

    public static CheckInTimerRepository getInstance(BaseNetwork baseNetwork, SharedPreferenceHelper sharedPreferenceHelper, VMRepoInterface vmRepoInterface, AppDatabase db) {
        if (instance == null) {
            instance = new CheckInTimerRepository(baseNetwork, sharedPreferenceHelper, vmRepoInterface, db);
        }
        instance.checkInTimerMutableLiveData = new MutableLiveData<>();
        return instance;
    }

    public MutableLiveData<JSONObject> getCheckInTimer(){
        // handle login
        dataSource.Connect(ConnectionHandler.get_method, "countDownTimer", null, new NetworkCallback() {
            @Override
            public void onFinish() {

            }

            @Override
            public void onSuccess(Result result) {
                try {
                    checkInTimerMutableLiveData.setValue(new JSONObject(((Result.Success) result).getData().toString()));
                    vmRepoInterface.setMessage(result.toString());
                    vmRepoInterface.getStatus().setValue(true);
                } catch (JSONException e) {
                    vmRepoInterface.setMessage("Error json");
                    vmRepoInterface.getStatus().setValue(false);
                    checkInTimerMutableLiveData.setValue(null);
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(Result result) {
                checkInTimerMutableLiveData.setValue(null);
                vmRepoInterface.setMessage(result.toString());
                vmRepoInterface.getStatus().setValue(false);
            }
        });
        return checkInTimerMutableLiveData;
    }
}