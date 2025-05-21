package com.qiratek.rnpsales.model.datasource.network;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import com.google.gson.Gson;
import com.qiratek.rnpsales.model.datasource.Result;
import com.qiratek.rnpsales.model.entity.User;
import com.qiratek.rnpsales.model.helper.SharedPreferenceHelper;
import com.qiratek.rnpsales.view.activity.SplashActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


public class BaseNetwork {
    private static BaseNetwork instance = null;
    private ConnectionHandler connectionHandler;
    protected Context context;
    private Map<String, String> header;
    private User user;
    protected Gson gson;
    private String apiToken = "";
    private NetworkCallback networkCallback;

    public static BaseNetwork getInstance(Context context) {
        if (instance == null) {
            instance = new BaseNetwork(context);
        }
        instance.populateHeader();
        return instance;
    }

    private BaseNetwork(Context context) {
        connectionHandler = ConnectionHandler.getInstance(context);
        gson = connectionHandler.getGson();
        this.context = context;
    }

    private void populateHeader(){
        if(!SharedPreferenceHelper.getInstance(context).getPreference(User.table).isEmpty()) {
            user = gson.fromJson(SharedPreferenceHelper.getInstance(context).getPreference(User.table), User.class);
        }
        header = new HashMap<>();
        if (user != null) {
            header.put("Authorization", "Bearer " + user.getApi_token());
            apiToken = user.getApi_token();
        }
    }

    public void Connect(int method, String url, JSONObject params, NetworkCallback networkCallback){
        this.networkCallback = networkCallback;
        connectionHandler.MakeConnection(method, url, params, header, (jsonObject, message) -> {
            try {
                networkCallback.onFinish();
                if (message.equalsIgnoreCase(connectionHandler.response_message_success)) {
                    if (jsonObject.has("status") && jsonObject.getString("status").contains(connectionHandler.response_message_success)) {
                        networkCallback.onSuccess(new Result.Success(jsonObject.getString("data"), jsonObject.getString("message")));;
                    } else {
                        networkCallback.onError(new Result.Error(jsonObject.getString("message")));
                    }
                } else {
                    networkCallback.onError(new Result.Error(jsonObject.getString("message")));
                    if(message.equalsIgnoreCase(connectionHandler.auth_error) && !SharedPreferenceHelper.getInstance(context).isEmptyPreference(User.table)){
                        SharedPreferenceHelper.getInstance(context).removePreference(User.table);
                        context.startActivity(new Intent(context, SplashActivity.class));
                        if(context instanceof Activity) {
                            ((Activity) context).finish();
                        }
                    }
                }
            } catch (JSONException e) {
                networkCallback.onError(new Result.Error("Json Error"));
            }
        });
    }

    public void Connect(int method, String url, Map<String, Object> param, Map<String, MultipartFile> paramFile, NetworkCallback networkCallback){
        this.networkCallback = networkCallback;
        connectionHandler.MakeConnection(method, url, param, paramFile, header, (jsonObject, message) -> {
            try {
                networkCallback.onFinish();
                if (message.equalsIgnoreCase(connectionHandler.response_message_success)) {
                    if (jsonObject.has("status") && jsonObject.getString("status").contains(connectionHandler.response_message_success)) {
                        networkCallback.onSuccess(new Result.Success(jsonObject.getString("data"), jsonObject.getString("message")));;
                    } else {
                        networkCallback.onError(new Result.Error(jsonObject.getString("message")));
                    }
                } else {
                    networkCallback.onError(new Result.Error(jsonObject.getString("message")));
                    if(message.equalsIgnoreCase(connectionHandler.auth_error) && !SharedPreferenceHelper.getInstance(context).isEmptyPreference(User.table)){
                        SharedPreferenceHelper.getInstance(context).removePreference(User.table);
                        context.startActivity(new Intent(context, SplashActivity.class));
                        if(context instanceof Activity) {
                            ((Activity) context).finish();
                        }
                    }
                }
            } catch (JSONException e) {
                networkCallback.onError(new Result.Error("Json Error"));
            }
        });
    }

    public User getUser() {
        return user;
    }

    public Gson getGson() {
        return gson;
    }
}
