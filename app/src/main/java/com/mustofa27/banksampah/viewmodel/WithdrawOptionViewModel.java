package com.mustofa27.banksampah.viewmodel;

import android.content.Context;

import androidx.lifecycle.LiveData;

import com.mustofa27.banksampah.model.datasource.local.AppDatabase;
import com.mustofa27.banksampah.model.datasource.network.BaseNetwork;
import com.mustofa27.banksampah.model.entity.WithdrawOption;
import com.mustofa27.banksampah.model.helper.SharedPreferenceHelper;
import com.mustofa27.banksampah.model.repository.WithdrawOptionRepository;

import java.util.ArrayList;
import java.util.List;


public class WithdrawOptionViewModel extends BaseViewModel {

    WithdrawOptionRepository withdrawOptionRepository;
    LiveData<ArrayList<WithdrawOption>> withdrawOptionLiveData;

    public WithdrawOptionViewModel(Context context) {
        withdrawOptionRepository = WithdrawOptionRepository.getInstance(BaseNetwork.getInstance(context), SharedPreferenceHelper.getInstance(context),
                this, AppDatabase.getInstance(context));
    }

    public LiveData<ArrayList<WithdrawOption>> getData(){
        loading.setValue(true);
        withdrawOptionLiveData = withdrawOptionRepository.getAllWithdrawOption();
        return withdrawOptionLiveData;
    }
}
