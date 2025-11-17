package com.mustofa27.banksampah.viewmodel;

import android.content.Context;

import androidx.lifecycle.LiveData;

import com.mustofa27.banksampah.model.datasource.local.AppDatabase;
import com.mustofa27.banksampah.model.datasource.network.BaseNetwork;
import com.mustofa27.banksampah.model.entity.Garbage;
import com.mustofa27.banksampah.model.helper.SharedPreferenceHelper;
import com.mustofa27.banksampah.model.repository.GarbageRepository;
import com.mustofa27.banksampah.model.repository.SavingRepository;
import com.mustofa27.banksampah.model.repository.WithdrawRepository;

import java.util.ArrayList;


public class WithdrawViewModel extends BaseViewModel {

    WithdrawRepository withdrawRepository;

    public WithdrawViewModel(Context context) {
        withdrawRepository = WithdrawRepository.getInstance(BaseNetwork.getInstance(context), SharedPreferenceHelper.getInstance(context),
                this, AppDatabase.getInstance(context));
    }

    public void addWithdraw(int count, int balance_used, int withdraw_option_id){
        loading.setValue(true);
        withdrawRepository.addWithdraw(count,balance_used,withdraw_option_id);
    }
}
