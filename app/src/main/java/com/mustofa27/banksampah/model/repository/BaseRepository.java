package com.mustofa27.banksampah.model.repository;

import com.mustofa27.banksampah.model.datasource.local.AppDatabase;
import com.mustofa27.banksampah.model.datasource.network.BaseNetwork;
import com.mustofa27.banksampah.model.helper.SharedPreferenceHelper;
import com.mustofa27.banksampah.viewmodel.VMRepoInterface;

public class BaseRepository {

    protected BaseNetwork dataSource;
    protected SharedPreferenceHelper sharedPreferenceHelper;
    protected VMRepoInterface vmRepoInterface;
    protected AppDatabase db;

    // private constructor : singleton access
    protected BaseRepository(BaseNetwork baseNetwork, SharedPreferenceHelper sharedPreferenceHelper, VMRepoInterface vmRepoInterface, AppDatabase db) {
        this.dataSource = baseNetwork;
        this.sharedPreferenceHelper = sharedPreferenceHelper;
        this.vmRepoInterface = vmRepoInterface;
        this.db = db;
    }

}
