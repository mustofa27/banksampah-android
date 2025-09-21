package com.mustofa27.banksampah.model.repository;


import androidx.lifecycle.MutableLiveData;

import com.mustofa27.banksampah.model.datasource.Result;
import com.mustofa27.banksampah.model.datasource.local.AppDatabase;
import com.mustofa27.banksampah.model.datasource.network.BaseNetwork;
import com.mustofa27.banksampah.model.datasource.network.ConnectionHandler;
import com.mustofa27.banksampah.model.datasource.network.NetworkCallback;
import com.mustofa27.banksampah.model.entity.NewsClass;
import com.mustofa27.banksampah.model.entity.User;
import com.mustofa27.banksampah.model.entity.UserToken;
import com.mustofa27.banksampah.model.helper.SharedPreferenceHelper;
import com.mustofa27.banksampah.viewmodel.VMRepoInterface;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Class that requests authentication and user information from the remote data source and
 * maintains an in-memory cache of login status and user credentials information.
 */
public class UserRepository extends BaseRepository {

    private static volatile UserRepository instance;
    private MutableLiveData<User> userMutableLiveData;

    private UserRepository(BaseNetwork baseNetwork, SharedPreferenceHelper sharedPreferenceHelper, VMRepoInterface vmRepoInterface, AppDatabase db) {
        super(baseNetwork, sharedPreferenceHelper, vmRepoInterface, db);
        userMutableLiveData = new MutableLiveData<>();
    }

    public static UserRepository getInstance(BaseNetwork baseNetwork, SharedPreferenceHelper sharedPreferenceHelper, VMRepoInterface vmRepoInterface, AppDatabase db) {
        if (instance == null) {
            instance = new UserRepository(baseNetwork, sharedPreferenceHelper, vmRepoInterface, db);
        } else{
            instance.ResetVMRepoInterface(vmRepoInterface);
        }
        return instance;
    }

    public void login(String username, String password, String fcmId){
        // handle login
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("email", username);
            jsonObject.put("password", password);
            jsonObject.put("fcm_id", fcmId);
            dataSource.Connect(ConnectionHandler.post_method, "auth/login", jsonObject, new NetworkCallback() {
                @Override
                public void onFinish() {

                }

                @Override
                public void onSuccess(Result result) {
                    sharedPreferenceHelper.setPreference(UserToken.table, ((Result.Success)result).getData().toString());
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

    public MutableLiveData<User> me(){
        if(userMutableLiveData.getValue() == null) {
            dataSource.Connect(ConnectionHandler.post_method, "auth/me", null, new NetworkCallback() {
                @Override
                public void onFinish() {

                }

                @Override
                public void onSuccess(Result result) {
                    User user = dataSource.getGson().fromJson(((Result.Success) result).getData().toString(), User.class);
                    userMutableLiveData.setValue(user);
                    vmRepoInterface.setMessage(result.toString());
                }

                @Override
                public void onError(Result result) {
                    vmRepoInterface.setMessage(result.toString());
                    vmRepoInterface.getStatus().setValue(false);
                }
            });
        }
        return userMutableLiveData;
    }

    public void resetPass(String otp, String password, String cpassword){
        // handle login
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("otp", otp);
            jsonObject.put("password", password);
            jsonObject.put("cpassword", cpassword);
            dataSource.Connect(ConnectionHandler.post_method, "sales/newspassword", jsonObject, new NetworkCallback() {
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

    public void sendOTP(String email){
        // handle login
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("email", email);
            dataSource.Connect(ConnectionHandler.post_method, "sales/reset", jsonObject, new NetworkCallback() {
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


    public void ResetVMRepoInterface(VMRepoInterface vmRepoInterface){
        instance.vmRepoInterface = vmRepoInterface;
    }
}