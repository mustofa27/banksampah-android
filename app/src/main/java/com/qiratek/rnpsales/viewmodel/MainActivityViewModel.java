package com.qiratek.rnpsales.viewmodel;

import android.content.Context;

import androidx.lifecycle.LiveData;

import com.qiratek.rnpsales.model.datasource.local.AppDatabase;
import com.qiratek.rnpsales.model.datasource.network.BaseNetwork;
import com.qiratek.rnpsales.model.entity.Brand;
import com.qiratek.rnpsales.model.entity.City;
import com.qiratek.rnpsales.model.entity.Configuration;
import com.qiratek.rnpsales.model.entity.NewsClass;
import com.qiratek.rnpsales.model.entity.Outlet;
import com.qiratek.rnpsales.model.entity.Product;
import com.qiratek.rnpsales.model.entity.Satuan;
import com.qiratek.rnpsales.model.entity.Tipe;
import com.qiratek.rnpsales.model.entity.VisitPlanDb;
import com.qiratek.rnpsales.model.helper.SharedPreferenceHelper;
import com.qiratek.rnpsales.model.helper.TimerPreference;
import com.qiratek.rnpsales.model.repository.BrandRepository;
import com.qiratek.rnpsales.model.repository.CityRepository;
import com.qiratek.rnpsales.model.repository.ConfigRepository;
import com.qiratek.rnpsales.model.repository.NewsRepository;
import com.qiratek.rnpsales.model.repository.OutletRepository;
import com.qiratek.rnpsales.model.repository.ProductRepository;
import com.qiratek.rnpsales.model.repository.SatuanRepository;
import com.qiratek.rnpsales.model.repository.TipeRepository;
import com.qiratek.rnpsales.model.repository.UserRepository;
import com.qiratek.rnpsales.model.repository.VisitPlanRepository;

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
