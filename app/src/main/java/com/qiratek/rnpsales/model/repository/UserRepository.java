package com.qiratek.rnpsales.model.repository;

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
public class UserRepository extends BaseRepository {

    private static volatile UserRepository instance;
    private User user;

    private UserRepository(BaseNetwork baseNetwork, SharedPreferenceHelper sharedPreferenceHelper, VMRepoInterface vmRepoInterface, AppDatabase db) {
        super(baseNetwork, sharedPreferenceHelper, vmRepoInterface, db);
    }

    public static UserRepository getInstance(BaseNetwork baseNetwork, SharedPreferenceHelper sharedPreferenceHelper, VMRepoInterface vmRepoInterface, AppDatabase db) {
        if (instance == null) {
            instance = new UserRepository(baseNetwork, sharedPreferenceHelper, vmRepoInterface, db);
        } else{
            instance.ResetVMRepoInterface(vmRepoInterface);
        }
        instance.user = baseNetwork.getUser();
        return instance;
    }

    public void login(String username, String password, String fcmId, String imei){
        // handle login
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("username", username);
            jsonObject.put("password", password);
            jsonObject.put("id_gcm", fcmId);
            jsonObject.put("imei", imei);
            dataSource.Connect(ConnectionHandler.post_method, "login", jsonObject, new NetworkCallback() {
                @Override
                public void onFinish() {

                }

                @Override
                public void onSuccess(Result result) {
                    sharedPreferenceHelper.setPreference(User.table, ((Result.Success)result).getData().toString());
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

    public User getUser() {
        return user;
    }

    public void ResetVMRepoInterface(VMRepoInterface vmRepoInterface){
        instance.vmRepoInterface = vmRepoInterface;
    }
}