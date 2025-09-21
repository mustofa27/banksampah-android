package com.mustofa27.banksampah.view.activity;


import android.content.Intent;
import android.os.Bundle;

import androidx.lifecycle.ViewModelProvider;

import com.mustofa27.banksampah.model.helper.fcm.TempIDFCM;
import com.mustofa27.banksampah.view.BaseActivity;
import com.mustofa27.banksampah.viewmodel.BaseViewModel;
import com.mustofa27.banksampah.viewmodel.CustomViewModelFactory;
import com.mustofa27.banksampah.viewmodel.UserViewModel;
import com.mustofa27.banksampah.databinding.ActivityLoginBinding;

public class LoginActivity extends BaseActivity {

    ActivityLoginBinding binding;
    UserViewModel viewModel;
    TempIDFCM tempIDFCM;
    String imei;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        tempIDFCM = new TempIDFCM(this);
        viewModel = new ViewModelProvider(this, new CustomViewModelFactory(this)).get(UserViewModel.class);
        binding.signin.setOnClickListener(v -> {
            viewModel.login(binding.username.getText().toString(), binding.password.getText().toString(), tempIDFCM.getToken());
        });
        initObserver();
    }

    @Override
    protected BaseViewModel getViewModel() {
        return viewModel;
    }

    @Override
    protected void showLoading(boolean isLoading) {
        if (isLoading) {
            showLoadingDialog();
        } else {
            dismissLoadingDialog();
        }
    }

    @Override
    protected void initObserver() {
        viewModel.getStatus().observe(this, status -> {
            viewModel.getLoading().setValue(false);
            showMessage(status);
            if(status){
                startActivity(new Intent(LoginActivity.this, MainActivity.class));
                finish();
            }
        });
        viewModel.getLoading().observe(this, loading -> {
            showLoading(loading);
        });
    }
}