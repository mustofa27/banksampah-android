package com.mustofa27.banksampah.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mustofa27.banksampah.databinding.ActivitySplashBinding;
import com.mustofa27.banksampah.model.entity.User;
import com.mustofa27.banksampah.model.entity.UserToken;
import com.mustofa27.banksampah.model.helper.SharedPreferenceHelper;
import com.mustofa27.banksampah.view.BaseActivity;
import com.mustofa27.banksampah.viewmodel.BaseViewModel;

public class SplashActivity extends BaseActivity {

    ActivitySplashBinding binding;
    private final int SPLASH_DISPLAY_LENGTH = 1000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySplashBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                FirebaseMessaging.getInstance().subscribeToTopic("allDevices");
                Intent mainIntent;
                if (SharedPreferenceHelper.getInstance(SplashActivity.this).isEmptyPreference(UserToken.table)) {
                    mainIntent = new Intent(SplashActivity.this, LoginActivity.class);
                } else {
                    Gson gson = new GsonBuilder().create();
                    User user = gson.fromJson(SharedPreferenceHelper.getInstance(SplashActivity.this).getPreference(User.table), User.class);
                    mainIntent = new Intent(SplashActivity.this, MainActivity.class);
                    mainIntent.putExtra("type",getIntent().getStringExtra("type"));
                }
                startActivity(mainIntent);
                finish();
            }
        }, SPLASH_DISPLAY_LENGTH);
        binding.tahun.setText(getTahun());
    }

    @Override
    protected BaseViewModel getViewModel() {
        return null;
    }

    @Override
    protected void showLoading(boolean isLoading) {

    }

    @Override
    protected void initObserver() {

    }
}