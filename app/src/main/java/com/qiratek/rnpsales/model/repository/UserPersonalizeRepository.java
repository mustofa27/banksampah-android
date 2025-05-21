package com.qiratek.rnpsales.model.repository;

import androidx.lifecycle.MutableLiveData;

import com.qiratek.rnpsales.model.datasource.Result;
import com.qiratek.rnpsales.model.datasource.local.AppDatabase;
import com.qiratek.rnpsales.model.datasource.network.BaseNetwork;
import com.qiratek.rnpsales.model.datasource.network.ConnectionHandler;
import com.qiratek.rnpsales.model.datasource.network.NetworkCallback;
import com.qiratek.rnpsales.model.entity.User;
import com.qiratek.rnpsales.model.entity.UserPersonalize;
import com.qiratek.rnpsales.model.helper.SharedPreferenceHelper;
import com.qiratek.rnpsales.viewmodel.VMRepoInterface;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Class that requests authentication and user information from the remote data source and
 * maintains an in-memory cache of login status and user credentials information.
 */
public class UserPersonalizeRepository extends BaseRepository {

    private static volatile UserPersonalizeRepository instance;
    private User user;
    private MutableLiveData<UserPersonalize> userPersonalizeMutableLiveData;

    private UserPersonalizeRepository(BaseNetwork baseNetwork, SharedPreferenceHelper sharedPreferenceHelper, VMRepoInterface vmRepoInterface, AppDatabase db) {
        super(baseNetwork, sharedPreferenceHelper, vmRepoInterface, db);
    }

    public static UserPersonalizeRepository getInstance(BaseNetwork baseNetwork, SharedPreferenceHelper sharedPreferenceHelper, VMRepoInterface vmRepoInterface, AppDatabase db) {
        if (instance == null) {
            instance = new UserPersonalizeRepository(baseNetwork, sharedPreferenceHelper, vmRepoInterface, db);
        }
        if(instance.vmRepoInterface != vmRepoInterface) {
            instance.vmRepoInterface = vmRepoInterface;
        }
        instance.userPersonalizeMutableLiveData = new MutableLiveData<>();
        instance.user = baseNetwork.getUser();
        return instance;
    }

    public void setUserPersonalize(JSONObject jsonObject){
        try {
            jsonObject.put("user_id", user.getId());
            sharedPreferenceHelper.setPreference("userPersonalize", jsonObject.toString());
            UserPersonalize userPersonalize = dataSource.getGson().fromJson(sharedPreferenceHelper.getPreference("userPersonalize"), UserPersonalize.class);
            userPersonalizeMutableLiveData.setValue(userPersonalize);
            vmRepoInterface.setMessage("Berhasil disimpan");
            vmRepoInterface.getStatus().postValue(true);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public MutableLiveData<UserPersonalize> getUserPersonalize(){
        if(userPersonalizeMutableLiveData == null){
            userPersonalizeMutableLiveData = new MutableLiveData<>();
        }
        UserPersonalize userPersonalize;
        if(sharedPreferenceHelper.isEmptyPreference("userPersonalize")){
            userPersonalize = new UserPersonalize();
            userPersonalize.setUser_id(user.getId());
            userPersonalize.setPilihan_tampil(0);
        } else{
            userPersonalize = dataSource.getGson().fromJson(sharedPreferenceHelper.getPreference("userPersonalize"), UserPersonalize.class);
        }
        userPersonalizeMutableLiveData.setValue(userPersonalize);
        vmRepoInterface.setMessage("Sukses");
        vmRepoInterface.getStatus().postValue(true);
        return userPersonalizeMutableLiveData;
    }
}