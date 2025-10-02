package com.mustofa27.banksampah.viewmodel;

import android.content.Context;

import androidx.lifecycle.LiveData;

import com.mustofa27.banksampah.model.datasource.local.AppDatabase;
import com.mustofa27.banksampah.model.datasource.network.BaseNetwork;
import com.mustofa27.banksampah.model.entity.Garbage;
import com.mustofa27.banksampah.model.entity.NewsClass;
import com.mustofa27.banksampah.model.entity.Product;
import com.mustofa27.banksampah.model.helper.SharedPreferenceHelper;
import com.mustofa27.banksampah.model.repository.GarbageRepository;
import com.mustofa27.banksampah.model.repository.NewsRepository;
import com.mustofa27.banksampah.model.repository.ProductRepository;
import com.mustofa27.banksampah.model.repository.SavingRepository;
import com.mustofa27.banksampah.model.repository.UserRepository;

import java.util.ArrayList;


public class SavingViewModel extends BaseViewModel {

    SavingRepository savingRepository;

    public SavingViewModel(Context context) {
        savingRepository = SavingRepository.getInstance(BaseNetwork.getInstance(context), SharedPreferenceHelper.getInstance(context),
                this, AppDatabase.getInstance(context));
    }

    public void addSaving(float weight, int garbage_id){
        loading.setValue(true);
        savingRepository.addSaving(weight,garbage_id);
    }
}
