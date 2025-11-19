package com.mustofa27.banksampah.viewmodel;

import android.content.Context;

import androidx.lifecycle.LiveData;

import com.mustofa27.banksampah.model.datasource.local.AppDatabase;
import com.mustofa27.banksampah.model.datasource.network.BaseNetwork;
import com.mustofa27.banksampah.model.datasource.network.MultipartFile;
import com.mustofa27.banksampah.model.entity.Transaction;
import com.mustofa27.banksampah.model.helper.SharedPreferenceHelper;
import com.mustofa27.banksampah.model.repository.TransactionRepository;

import java.util.Map;


public class PaymentViewModel extends BaseViewModel{
    TransactionRepository transactionRepository;

    public PaymentViewModel(Context context) {
        transactionRepository = TransactionRepository.getInstance(BaseNetwork.getInstance(context), SharedPreferenceHelper.getInstance(context), this,
                AppDatabase.getInstance(context));
    }
    public LiveData<Transaction> pay(Map<String, Object> param, Map<String, MultipartFile> paramFile){
        return transactionRepository.addPaymentProof(param, paramFile);
    }
}
