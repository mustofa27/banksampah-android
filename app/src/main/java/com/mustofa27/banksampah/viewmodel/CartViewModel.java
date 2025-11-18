package com.mustofa27.banksampah.viewmodel;

import android.content.Context;

import androidx.lifecycle.LiveData;

import com.mustofa27.banksampah.model.datasource.local.AppDatabase;
import com.mustofa27.banksampah.model.datasource.network.BaseNetwork;
import com.mustofa27.banksampah.model.entity.Balance;
import com.mustofa27.banksampah.model.entity.Cart;
import com.mustofa27.banksampah.model.entity.Transaction;
import com.mustofa27.banksampah.model.helper.SharedPreferenceHelper;
import com.mustofa27.banksampah.model.repository.BalanceRepository;
import com.mustofa27.banksampah.model.repository.CartRepository;
import com.mustofa27.banksampah.model.repository.TransactionRepository;

import org.json.JSONObject;

import java.util.ArrayList;


public class CartViewModel extends BaseViewModel {


    LiveData<ArrayList<Cart>> cartLiveData;
    CartRepository cartRepository;
    TransactionRepository transactionRepository;
    BalanceRepository balanceRepository;

    public CartViewModel(Context context) {
        cartRepository = CartRepository.getInstance(BaseNetwork.getInstance(context), SharedPreferenceHelper.getInstance(context), this,
                AppDatabase.getInstance(context));
        transactionRepository = TransactionRepository.getInstance(BaseNetwork.getInstance(context), SharedPreferenceHelper.getInstance(context), this,
                AppDatabase.getInstance(context));
        balanceRepository = BalanceRepository.getInstance(BaseNetwork.getInstance(context), SharedPreferenceHelper.getInstance(context), this,
                AppDatabase.getInstance(context));
    }
    public LiveData<ArrayList<Cart>> getCart(){
        cartLiveData = cartRepository.getData();
        return cartLiveData;
    }

    public LiveData<Transaction> checkout(JSONObject jsonObject){
        return transactionRepository.addTransaction(jsonObject);
    }
    public LiveData<Balance> getBalance(){
        return balanceRepository.getMyBalance();
    }
}
