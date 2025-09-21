package com.mustofa27.banksampah.viewmodel;

import android.content.Context;

import androidx.lifecycle.LiveData;

import com.mustofa27.banksampah.model.datasource.local.AppDatabase;
import com.mustofa27.banksampah.model.datasource.network.BaseNetwork;
import com.mustofa27.banksampah.model.helper.SharedPreferenceHelper;
import com.mustofa27.banksampah.model.repository.CheckInTimerRepository;

import org.json.JSONObject;


public class CountDownTimerViewModel extends BaseViewModel {

    CheckInTimerRepository checkInTimerRepository;
    LiveData<JSONObject> checkInTimerLiveData;

    public CountDownTimerViewModel(Context context) {
        checkInTimerRepository = CheckInTimerRepository.getInstance(BaseNetwork.getInstance(context), SharedPreferenceHelper.getInstance(context), this,
                AppDatabase.getInstance(context));
    }

    public LiveData<JSONObject> getCheckInTimerLiveData(){
        checkInTimerLiveData = checkInTimerRepository.getCheckInTimer();
        if(checkInTimerLiveData.getValue() == null) {
            loading.setValue(true);
        }
        return checkInTimerLiveData;
    }
}
