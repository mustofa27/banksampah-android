package com.qiratek.rnpsales.viewmodel;

import android.content.Context;

import androidx.navigation.NavController;

import com.qiratek.rnpsales.model.datasource.local.AppDatabase;
import com.qiratek.rnpsales.model.datasource.network.BaseNetwork;
import com.qiratek.rnpsales.model.entity.User;
import com.qiratek.rnpsales.model.helper.SharedPreferenceHelper;
import com.qiratek.rnpsales.model.repository.UserRepository;


public class ResetPassViewModel extends BaseViewModel {

    UserRepository userRepository;
    NavController navController;

    public ResetPassViewModel(Context context) {
        userRepository = UserRepository.getInstance(BaseNetwork.getInstance(context), SharedPreferenceHelper.getInstance(context), this,
                AppDatabase.getInstance(context));
    }

    public void resetPass(String otp, String password, String cpassword){
        loading.setValue(true);
        userRepository.resetPass(otp, password, cpassword);
    }

    public void sendOTP(String email){
        loading.setValue(true);
        userRepository.sendOTP(email);
    }

    public NavController getNavController() {
        return navController;
    }

    public void setNavController(NavController navController) {
        this.navController = navController;
    }

    public User getUser(){
        return userRepository.getUser();
    }

    public void ResetVMRepoInterface(){
        userRepository.ResetVMRepoInterface(this);
    }
}
