package com.qiratek.rnpsales.model.repository;

import androidx.lifecycle.MutableLiveData;

import com.qiratek.rnpsales.model.datasource.Result;
import com.qiratek.rnpsales.model.datasource.local.AppDatabase;
import com.qiratek.rnpsales.model.datasource.network.BaseNetwork;
import com.qiratek.rnpsales.model.datasource.network.ConnectionHandler;
import com.qiratek.rnpsales.model.datasource.network.NetworkCallback;
import com.qiratek.rnpsales.model.entity.Configuration;
import com.qiratek.rnpsales.model.entity.User;
import com.qiratek.rnpsales.model.helper.ConfigPreference;
import com.qiratek.rnpsales.model.helper.SharedPreferenceHelper;
import com.qiratek.rnpsales.viewmodel.VMRepoInterface;

/**
 * Class that requests authentication and user information from the remote data source and
 * maintains an in-memory cache of login status and user credentials information.
 */
public class ConfigRepository extends BaseRepository {

    private static volatile ConfigRepository instance;
    private MutableLiveData<Configuration> configurationMutableLiveData;
    private ConfigPreference configPreference;
    private User user;

    private ConfigRepository(BaseNetwork baseNetwork, SharedPreferenceHelper sharedPreferenceHelper, VMRepoInterface vmRepoInterface, AppDatabase db) {
        super(baseNetwork, sharedPreferenceHelper, vmRepoInterface, db);
        configurationMutableLiveData = new MutableLiveData<>();
        configPreference = ConfigPreference.getInstance(sharedPreferenceHelper);
    }

    public static ConfigRepository getInstance(BaseNetwork baseNetwork, SharedPreferenceHelper sharedPreferenceHelper, VMRepoInterface vmRepoInterface, AppDatabase db) {
        if (instance == null) {
            instance = new ConfigRepository(baseNetwork, sharedPreferenceHelper, vmRepoInterface, db);
        }
        instance.vmRepoInterface = vmRepoInterface;
        instance.user = baseNetwork.getUser();
        instance.configurationMutableLiveData = new MutableLiveData<>();
        return instance;
    }

    public MutableLiveData<Configuration> getConfigurationMutableLiveData(boolean isDraft){
        // handle login
        if(!isDraft || configPreference.getConfiguration() == null) {
            dataSource.Connect(ConnectionHandler.post_method, "getConfiguration", null, new NetworkCallback() {
                @Override
                public void onFinish() {

                }

                @Override
                public void onSuccess(Result result) {
                    Configuration configuration = dataSource.getGson().fromJson(((Result.Success) result).getData().toString(), Configuration.class);
                    configurationMutableLiveData.setValue(configuration);
                    configPreference.writeConfig(configuration);
                }

                @Override
                public void onError(Result result) {
                    vmRepoInterface.setMessage(result.toString());
                    vmRepoInterface.getStatus().setValue(false);
                }
            });
        } else{
            configurationMutableLiveData.setValue(configPreference.getConfiguration());
        }
        return configurationMutableLiveData;
    }
}