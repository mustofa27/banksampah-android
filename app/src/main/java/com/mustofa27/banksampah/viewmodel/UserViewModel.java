package com.mustofa27.banksampah.viewmodel;

import android.content.Context;

import androidx.lifecycle.LiveData;

import com.mustofa27.banksampah.model.datasource.local.AppDatabase;
import com.mustofa27.banksampah.model.datasource.network.BaseNetwork;
import com.mustofa27.banksampah.model.entity.User;
import com.mustofa27.banksampah.model.helper.SharedPreferenceHelper;
import com.mustofa27.banksampah.model.repository.UserRepository;


public class UserViewModel extends BaseViewModel {

    UserRepository userRepository;
    LiveData<User> userLiveData;

    public UserViewModel(Context context) {
        userRepository = UserRepository.getInstance(BaseNetwork.getInstance(context), SharedPreferenceHelper.getInstance(context), this,
                AppDatabase.getInstance(context));
    }

    public void login(String username, String password, String fcmId){
        loading.setValue(true);
        userRepository.login(username, password, fcmId);
    }

    public void ResetVMRepoInterface(){
        userRepository.ResetVMRepoInterface(this);
    }

    public LiveData<User> getUserLiveData(){
        userLiveData = userRepository.me();
        if(userLiveData.getValue() == null) {
            loading.setValue(true);
        }
        return userLiveData;
    }
}
