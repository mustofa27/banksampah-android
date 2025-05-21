package com.qiratek.rnpsales.view.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;

import android.os.Bundle;

import com.qiratek.rnpsales.R;
import com.qiratek.rnpsales.databinding.ActivityResetPasswordBinding;
import com.qiratek.rnpsales.view.BaseActivity;
import com.qiratek.rnpsales.viewmodel.BaseViewModel;
import com.qiratek.rnpsales.viewmodel.CustomViewModelFactory;
import com.qiratek.rnpsales.viewmodel.ResetPassViewModel;

public class ResetPassword extends BaseActivity {

    ActivityResetPasswordBinding binding;
    ResetPassViewModel viewModel;
    NavHostFragment navHostFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityResetPasswordBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        viewModel = new ViewModelProvider(this, new CustomViewModelFactory(this)).get(ResetPassViewModel.class);
        navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.reset_nav_host_fragment);
        navHostFragment.getNavController().addOnDestinationChangedListener((controller, destination, arguments) -> {
            binding.title.setText(destination.getLabel());
        });
        viewModel.setNavController(navHostFragment.getNavController());
        binding.tahun.setText(getTahun());
    }

    @Override
    protected BaseViewModel getViewModel() {
        return viewModel;
    }

    @Override
    protected void showLoading(boolean isLoading) {

    }

    @Override
    protected void initObserver() {

    }
}