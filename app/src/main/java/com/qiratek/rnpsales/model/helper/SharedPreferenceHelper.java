package com.qiratek.rnpsales.model.helper;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.Map;

public class SharedPreferenceHelper {
    private static final String PREFERENCE_USER = "pmpsales";
    private static SharedPreferenceHelper instance = null;
    private Context context;

    public static SharedPreferenceHelper getInstance(Context context){
        if(instance == null){
            instance =  new SharedPreferenceHelper(context);
        }
        return instance;
    }

    private SharedPreferenceHelper(Context context){
        this.context = context;
    }

    public void setPreference(String key, String value) {
        SharedPreferences.Editor editor = context.getSharedPreferences(PREFERENCE_USER, 0).edit();
        editor.putString(key, value).apply();
    }

    public void setPreference(Map<String, String> preferences) {
        SharedPreferences.Editor editor = context.getSharedPreferences(PREFERENCE_USER, 0).edit();
        for (Map.Entry<String, String> kvPair : preferences.entrySet()) {
            editor.putString(kvPair.getKey(), kvPair.getValue());
        }
        editor.apply();
    }

    public void removePreference(String... keys) {
        SharedPreferences.Editor editor = context.getSharedPreferences(PREFERENCE_USER, 0).edit();
        for (String key : keys) {
            editor.remove(key);
        }
        editor.apply();
    }

    public String getPreference(String key) {
        SharedPreferences preferences = context.getSharedPreferences(PREFERENCE_USER, Context.MODE_PRIVATE);
        return preferences.getString(key, "");
    }

    public boolean isEmptyPreference(String key) {
        if(getPreference(key) == null || getPreference(key).isEmpty()){
            return true;
        }
        return false;
    }
}
