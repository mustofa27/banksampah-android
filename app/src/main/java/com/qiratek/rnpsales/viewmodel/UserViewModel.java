package com.qiratek.rnpsales.viewmodel;

import android.content.Context;

import com.qiratek.rnpsales.model.datasource.local.AppDatabase;
import com.qiratek.rnpsales.model.datasource.network.BaseNetwork;
import com.qiratek.rnpsales.model.entity.User;
import com.qiratek.rnpsales.model.helper.SharedPreferenceHelper;
import com.qiratek.rnpsales.model.repository.UserRepository;


public class UserViewModel extends BaseViewModel {

    UserRepository userRepository;

    public UserViewModel(Context context) {
        userRepository = UserRepository.getInstance(BaseNetwork.getInstance(context), SharedPreferenceHelper.getInstance(context), this,
                AppDatabase.getInstance(context));
    }

    public void login(String username, String password, String fcmId, String imei){
        loading.setValue(true);
        userRepository.login(username, password, fcmId, imei);
    }

    public User getUser(){
        return userRepository.getUser();
    }

    public void ResetVMRepoInterface(){
        userRepository.ResetVMRepoInterface(this);
    }
}
