package com.mustofa27.banksampah.viewmodel;

import android.content.Context;
import android.widget.PopupWindow;

import androidx.lifecycle.LiveData;

import com.mustofa27.banksampah.model.datasource.local.AppDatabase;
import com.mustofa27.banksampah.model.datasource.network.BaseNetwork;
import com.mustofa27.banksampah.model.entity.Cart;
import com.mustofa27.banksampah.model.helper.SharedPreferenceHelper;
import com.mustofa27.banksampah.model.repository.CartRepository;

import org.json.JSONObject;

import java.util.ArrayList;


public class HistoryViewModel extends BaseViewModel {


    LiveData<ArrayList<Cart>> cartLiveData;
    CartRepository cartRepository;
    public HistoryViewModel(Context context) {
        cartRepository = CartRepository.getInstance(BaseNetwork.getInstance(context), SharedPreferenceHelper.getInstance(context), this,
                AppDatabase.getInstance(context));
    }
    public LiveData<ArrayList<Cart>> getCart(){
        cartLiveData = cartRepository.getData();
        return cartLiveData;
    }
}
