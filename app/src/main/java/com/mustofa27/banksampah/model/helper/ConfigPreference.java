package com.mustofa27.banksampah.model.helper;
import com.mustofa27.banksampah.model.entity.Configuration;

public class ConfigPreference {
    protected static ConfigPreference configPreference = null;
    private SharedPreferenceHelper sharedPreferenceHelper;

    public static ConfigPreference getInstance(SharedPreferenceHelper helper) {
        if(configPreference == null){
            configPreference = new ConfigPreference();
        }
        configPreference.sharedPreferenceHelper = helper;
        return configPreference;
    }

    public void writeConfig(Configuration configuration) {
        sharedPreferenceHelper.setPreference(Configuration.TOLERANSI, String.valueOf(configuration.getToleransi_max()));
        sharedPreferenceHelper.setPreference(Configuration.TOLERANSI_DRAFT, String.valueOf(configuration.getToleransi_draft()));
    }

    public Configuration getConfiguration(){
        try {
            Configuration configuration = new Configuration();
            configuration.setToleransi_max(Integer.valueOf(sharedPreferenceHelper.getPreference(Configuration.TOLERANSI)));
            configuration.setToleransi_draft(Integer.valueOf(sharedPreferenceHelper.getPreference(Configuration.TOLERANSI_DRAFT)));
            return configuration;
        } catch (Exception exception){
            return null;
        }
    }
}
