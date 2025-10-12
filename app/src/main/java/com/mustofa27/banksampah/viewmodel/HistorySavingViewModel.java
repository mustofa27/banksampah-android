package com.mustofa27.banksampah.viewmodel;

import android.content.Context;

import androidx.lifecycle.LiveData;

import com.mustofa27.banksampah.model.datasource.local.AppDatabase;
import com.mustofa27.banksampah.model.datasource.network.BaseNetwork;
import com.mustofa27.banksampah.model.entity.Garbage;
import com.mustofa27.banksampah.model.entity.Saving;
import com.mustofa27.banksampah.model.helper.SharedPreferenceHelper;
import com.mustofa27.banksampah.model.repository.GarbageRepository;
import com.mustofa27.banksampah.model.repository.SavingRepository;

import java.util.ArrayList;


public class HistorySavingViewModel extends BaseViewModel {

    SavingRepository savingRepository;
    GarbageRepository garbageRepository;
    LiveData<ArrayList<Saving>> savingLiveData;
    int page;
    boolean isNextPage = false;

    public HistorySavingViewModel(Context context) {
        savingRepository = SavingRepository.getInstance(BaseNetwork.getInstance(context), SharedPreferenceHelper.getInstance(context),
                this, AppDatabase.getInstance(context));
        garbageRepository = GarbageRepository.getInstance(BaseNetwork.getInstance(context), SharedPreferenceHelper.getInstance(context),
                this, AppDatabase.getInstance(context));
        page = 1;
    }
    public LiveData<ArrayList<Saving>> getSaving(){
        savingLiveData = savingRepository.getData(page++);
        isNextPage = false;
        return savingLiveData;
    }
    public boolean isNextAvailable(){
        return savingRepository.isNextPageAvailable();
    }
    public void getNextData() {
        savingLiveData = savingRepository.getData(page++);
        isNextPage = true;
    }
    public boolean isNextPage() {
        return isNextPage;
    }
    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }
}
