package com.mustofa27.banksampah.view.activity;

import androidx.appcompat.app.AlertDialog;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

import android.content.Intent;
import android.content.IntentSender;
import android.os.Bundle;

import com.google.android.gms.tasks.Task;
import com.google.android.play.core.appupdate.AppUpdateInfo;
import com.google.android.play.core.appupdate.AppUpdateManager;
import com.google.android.play.core.appupdate.AppUpdateManagerFactory;
import com.google.android.play.core.install.model.AppUpdateType;
import com.google.android.play.core.install.model.UpdateAvailability;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.mustofa27.banksampah.R;
import com.mustofa27.banksampah.databinding.ActivityMainBinding;
import com.mustofa27.banksampah.view.BaseActivity;
import com.mustofa27.banksampah.viewmodel.BaseViewModel;
import com.mustofa27.banksampah.viewmodel.CustomViewModelFactory;
import com.mustofa27.banksampah.viewmodel.MainActivityViewModel;

import java.util.ArrayList;

public class MainActivity extends BaseActivity {

    FirebaseAnalytics mFirebaseAnalytics;
    NavHostFragment navHostFragment;
    ActivityMainBinding binding;
    AlertDialog.Builder builder;
    MainActivityViewModel viewModel;

    //updater
    AppUpdateManager appUpdateManager;
    private static final int REQ_CODE_VERSION_UPDATE = 530;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        viewModel = new ViewModelProvider(this, new CustomViewModelFactory(this)).get(MainActivityViewModel.class);
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.main_nav_host_fragment);
        NavigationUI.setupWithNavController(binding.bottomNav,
                navHostFragment.getNavController());
        navHostFragment.getNavController().addOnDestinationChangedListener((controller, destination, arguments) -> {
            //binding.title.setText(destination.getLabel());
        });
        checkUpdate();
    }

    @Override
    public void onResume() {
        super.onResume();
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

    //updater
    private void checkUpdate(){
        appUpdateManager = AppUpdateManagerFactory.create(this);
        // Returns an intent object that you use to check for an update.
        Task<AppUpdateInfo> appUpdateInfoTask = appUpdateManager.getAppUpdateInfo();

        // Checks that the platform will allow the specified type of update.
        appUpdateInfoTask.addOnSuccessListener(appUpdateInfo -> {
            if (appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE
                    && appUpdateInfo.isUpdateTypeAllowed(AppUpdateType.IMMEDIATE)) {
                startAppUpdateImmediate(appUpdateInfo);
            }
        });
    }

    private void startAppUpdateImmediate(AppUpdateInfo appUpdateInfo) {
        try {
            appUpdateManager.startUpdateFlowForResult(
                    appUpdateInfo,
                    AppUpdateType.IMMEDIATE,
                    this,
                    REQ_CODE_VERSION_UPDATE);
        } catch (IntentSender.SendIntentException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQ_CODE_VERSION_UPDATE) {
            if (resultCode != RESULT_OK) {
                showMessageFailed("Update flow failed, Try again later! Result code: " + resultCode);
                finish();
            }
        }
    }
}