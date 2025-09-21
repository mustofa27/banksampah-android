package com.mustofa27.banksampah.view.activity;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.ActionBar;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import android.os.Bundle;
import android.widget.TextView;

import com.mustofa27.banksampah.R;
import com.mustofa27.banksampah.databinding.ActivityRegisterOutletBinding;
import com.mustofa27.banksampah.view.BaseActivity;
import com.mustofa27.banksampah.viewmodel.BaseViewModel;
import com.mustofa27.banksampah.viewmodel.CustomViewModelFactory;
import com.mustofa27.banksampah.viewmodel.RegisterOutletViewModel;

public class RegisterOutlet extends BaseActivity {

    ActivityRegisterOutletBinding binding;
    RegisterOutletViewModel viewModel;
    NavController navController;
    ActivityResultLauncher activityResultLauncher = registerForActivityResult(new ActivityResultContracts.TakePicture(), result -> {
        viewModel.getStatusCamera().setValue(result);
    });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRegisterOutletBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        viewModel = new ViewModelProvider(this, new CustomViewModelFactory(this)).get(RegisterOutletViewModel.class);
        navController = ((NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.ro_nav_host_fragment)).getNavController();
        viewModel.setNavController(navController);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.main_topbar);
        ((TextView) getSupportActionBar().getCustomView().findViewById(R.id.title)).setText("Register Outlet");
        getSupportActionBar().getCustomView().findViewById(R.id.back).setOnClickListener(v -> onBackPressed());
        viewModel.setActivityResultLauncher(activityResultLauncher);
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