package com.qiratek.rnpsales.model.helper.fcm;

import android.content.Context;
import android.content.SharedPreferences;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by LENOVO on 1/31/2017.
 */

public class TempIDFCM {
    public static String prefName = "pmpsales";
    private Context context;
    public TempIDFCM(Context context){
        this.context = context;
    }
    public void SaveToken(String token){
        SharedPreferences.Editor editor = context.getSharedPreferences(prefName, MODE_PRIVATE).edit();
        editor.putString("token", token);
        editor.apply();
    }
    public String getToken(){
        SharedPreferences prefs = context.getSharedPreferences(prefName, MODE_PRIVATE);
        String token = prefs.getString("token", "-");
        return token;
    }
}
