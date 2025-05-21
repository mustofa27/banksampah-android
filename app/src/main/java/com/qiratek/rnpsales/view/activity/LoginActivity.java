package com.qiratek.rnpsales.view.activity;


import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.lifecycle.ViewModelProvider;

import com.qiratek.rnpsales.databinding.ActivityLoginBinding;
import com.qiratek.rnpsales.model.helper.fcm.TempIDFCM;
import com.qiratek.rnpsales.view.BaseActivity;
import com.qiratek.rnpsales.viewmodel.BaseViewModel;
import com.qiratek.rnpsales.viewmodel.CustomViewModelFactory;
import com.qiratek.rnpsales.viewmodel.UserViewModel;

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
            if(imei == null || !isStringNotEmpty(imei)){
                getIMEI();
            } else {
                viewModel.login(binding.username.getText().toString(), binding.password.getText().toString(), tempIDFCM.getToken(), imei);
            }
        });
        binding.forgot.setOnClickListener(v -> {
            startActivity(new Intent(LoginActivity.this, ResetPassword.class));
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

    private void getIMEI() {
        TelephonyManager mTelephony = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            imei = Settings.Secure.getString(this.getContentResolver(),
                    Settings.Secure.ANDROID_ID);
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) !=
                        PackageManager.PERMISSION_GRANTED) {
                    if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                            Manifest.permission.READ_PHONE_STATE)) {
                        Toast.makeText(this, "This application need permission to read your phone state",
                                Toast.LENGTH_LONG).show();
                    } else {
                        ActivityCompat.requestPermissions(this,
                                new String[]{Manifest.permission.READ_PHONE_STATE},
                                1);
                    }
                } else{
                    assert mTelephony != null;
                    if (mTelephony.getDeviceId() != null) {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                            imei = mTelephony.getImei();
                        }else {
                            imei = mTelephony.getDeviceId();
                        }
                    } else {
                        imei = Settings.Secure.getString(this.getContentResolver(),
                                Settings.Secure.ANDROID_ID);
                    }
                }
            }
        }
        if(imei != null){
            viewModel.login(binding.username.getText().toString(), binding.password.getText().toString(), tempIDFCM.getToken(), imei);
        } else{
            getIMEI();
        }
    }
}