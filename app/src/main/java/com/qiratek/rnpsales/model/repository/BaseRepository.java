package com.qiratek.rnpsales.model.repository;

import com.qiratek.rnpsales.model.datasource.local.AppDatabase;
import com.qiratek.rnpsales.model.datasource.network.BaseNetwork;
import com.qiratek.rnpsales.model.helper.SharedPreferenceHelper;
import com.qiratek.rnpsales.viewmodel.VMRepoInterface;

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
