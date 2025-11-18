package com.mustofa27.banksampah.viewmodel;

import android.content.Context;

import androidx.lifecycle.LiveData;

import com.mustofa27.banksampah.model.datasource.local.AppDatabase;
import com.mustofa27.banksampah.model.datasource.network.BaseNetwork;
import com.mustofa27.banksampah.model.entity.Saving;
import com.mustofa27.banksampah.model.entity.Transaction;
import com.mustofa27.banksampah.model.helper.SharedPreferenceHelper;
import com.mustofa27.banksampah.model.repository.SavingRepository;
import com.mustofa27.banksampah.model.repository.TransactionRepository;

import java.util.ArrayList;


public class HistoryTransactionViewModel extends BaseViewModel {

    TransactionRepository transactionRepository;
    LiveData<ArrayList<Transaction>> transactionLiveData;

    public HistoryTransactionViewModel(Context context) {
        transactionRepository = TransactionRepository.getInstance(BaseNetwork.getInstance(context), SharedPreferenceHelper.getInstance(context),
                this, AppDatabase.getInstance(context));
    }
    public LiveData<ArrayList<Transaction>> getData(){
        transactionLiveData = transactionRepository.getData();
        return transactionLiveData;
    }
}
