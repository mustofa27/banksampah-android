package com.mustofa27.banksampah.viewmodel;

import android.content.Context;

import androidx.lifecycle.LiveData;

import com.mustofa27.banksampah.model.datasource.local.AppDatabase;
import com.mustofa27.banksampah.model.datasource.network.BaseNetwork;
import com.mustofa27.banksampah.model.entity.Withdraw;
import com.mustofa27.banksampah.model.helper.SharedPreferenceHelper;
import com.mustofa27.banksampah.model.repository.GarbageRepository;
import com.mustofa27.banksampah.model.repository.WithdrawRepository;

import java.util.ArrayList;


public class HistoryWithdrawViewModel extends BaseViewModel {

    WithdrawRepository withdrawRepository;
    GarbageRepository garbageRepository;
    LiveData<ArrayList<Withdraw>> withdrawLiveData;
    int page;
    boolean isNextPage = false;

    public HistoryWithdrawViewModel(Context context) {
        withdrawRepository = WithdrawRepository.getInstance(BaseNetwork.getInstance(context), SharedPreferenceHelper.getInstance(context),
                this, AppDatabase.getInstance(context));
        page = 1;
    }
    public LiveData<ArrayList<Withdraw>> getWithdraw(){
        withdrawLiveData = withdrawRepository.getData(page++);
        isNextPage = false;
        return withdrawLiveData;
    }
    public boolean isNextAvailable(){
        return withdrawRepository.isNextPageAvailable();
    }
    public void getNextData() {
        withdrawLiveData = withdrawRepository.getData(page++);
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
