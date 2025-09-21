package com.mustofa27.banksampah.viewmodel;

import android.content.Context;

import androidx.lifecycle.LiveData;

import com.mustofa27.banksampah.model.datasource.local.AppDatabase;
import com.mustofa27.banksampah.model.datasource.network.BaseNetwork;
import com.mustofa27.banksampah.model.entity.VisitPlanDb;
import com.mustofa27.banksampah.model.helper.SharedPreferenceHelper;
import com.mustofa27.banksampah.model.helper.TimerPreference;
import com.mustofa27.banksampah.model.repository.UserRepository;
import com.mustofa27.banksampah.model.repository.VisitPlanRepository;

import java.util.ArrayList;


public class MainActivityViewModel extends BaseViewModel {

    UserRepository userRepository;

    VisitPlanRepository visitPlanRepository;
    LiveData<ArrayList<VisitPlanDb>> visitPlanDbLiveData;
    TimerPreference timerPreference;
    public MainActivityViewModel(Context context) {
        userRepository = UserRepository.getInstance(BaseNetwork.getInstance(context), SharedPreferenceHelper.getInstance(context),
                this, AppDatabase.getInstance(context));
        visitPlanRepository = VisitPlanRepository.getInstance(BaseNetwork.getInstance(context), SharedPreferenceHelper.getInstance(context),
                this, AppDatabase.getInstance(context));
        timerPreference = new TimerPreference(context);
    }

    public LiveData<ArrayList<VisitPlanDb>> getVisitPlanDbLiveData(){
        visitPlanDbLiveData = visitPlanRepository.getUnCheckoutVisit();
        if(visitPlanDbLiveData.getValue() == null || visitPlanDbLiveData.getValue().size() == 0){
            getLoading().setValue(true);
        }
        return visitPlanDbLiveData;
    }

    public LiveData<ArrayList<VisitPlanDb>> getVisitPlanLiveData(){
        return visitPlanDbLiveData;
    }

    public TimerPreference getTimerPreference() {
        return timerPreference;
    }

    public void setTimerPreference(TimerPreference timerPreference) {
        this.timerPreference = timerPreference;
    }
}
