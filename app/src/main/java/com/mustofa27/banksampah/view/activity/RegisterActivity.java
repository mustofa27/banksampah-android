package com.mustofa27.banksampah.view.activity;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;

import com.mustofa27.banksampah.R;
import com.mustofa27.banksampah.databinding.ActivityRegisterBinding;
import com.mustofa27.banksampah.view.BaseActivity;
import com.mustofa27.banksampah.viewmodel.BaseViewModel;
import com.mustofa27.banksampah.viewmodel.CustomViewModelFactory;
import com.mustofa27.banksampah.viewmodel.UserViewModel;

public class RegisterActivity extends BaseActivity {
    ActivityRegisterBinding binding;
    UserViewModel viewModel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRegisterBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        viewModel = new ViewModelProvider(this, new CustomViewModelFactory(this)).get(UserViewModel.class);
        binding.register.setOnClickListener(v -> {
            if(isStringNotEmpty(binding.name) && isStringNotEmpty(binding.username) && isStringNotEmpty(binding.password)
                    && isStringNotEmpty(binding.passwordConfirm) && isStringNotEmpty(binding.phone)){
                if(binding.password.getText().toString().equals(binding.passwordConfirm.getText().toString())){
                    viewModel.register(binding.name.getText().toString(), binding.username.getText().toString(),
                            binding.password.getText().toString(), binding.passwordConfirm.getText().toString(), binding.phone.getText().toString());
                } else {
                    showMessageFailed("Password tidak cocok");
                }
            } else{
                showMessageFailed("Pastikan semua isian telah terisi");
            }
        });
        initObserver();
    }

    @Override
    protected BaseViewModel getViewModel() {
        return viewModel;
    }

    @Override
    protected void showLoading(boolean isLoading) {
        showDefaulLoading(isLoading);
    }

    @Override
    protected void initObserver() {
        viewModel.getStatus().observe(this, status -> {
            viewModel.getLoading().setValue(false);
            showMessage(status);
            if(status){
                startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                finish();
            }
        });
        viewModel.getLoading().observe(this, loading -> {
            showLoading(loading);
        });
    }
}