package com.mustofa27.banksampah.viewmodel;

import android.content.Context;

import androidx.navigation.NavController;

import com.mustofa27.banksampah.model.datasource.local.AppDatabase;
import com.mustofa27.banksampah.model.datasource.network.BaseNetwork;
import com.mustofa27.banksampah.model.entity.User;
import com.mustofa27.banksampah.model.helper.SharedPreferenceHelper;
import com.mustofa27.banksampah.model.repository.UserRepository;


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
