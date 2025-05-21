package com.qiratek.rnpsales.viewmodel;

import android.content.Context;
import android.widget.PopupWindow;

import androidx.lifecycle.LiveData;

import com.qiratek.rnpsales.model.datasource.local.AppDatabase;
import com.qiratek.rnpsales.model.datasource.network.BaseNetwork;
import com.qiratek.rnpsales.model.entity.BillingData;
import com.qiratek.rnpsales.model.entity.Outlet;
import com.qiratek.rnpsales.model.entity.Satuan;
import com.qiratek.rnpsales.model.entity.TakeOrder;
import com.qiratek.rnpsales.model.entity.Tipe;
import com.qiratek.rnpsales.model.entity.UserPersonalize;
import com.qiratek.rnpsales.model.helper.SharedPreferenceHelper;
import com.qiratek.rnpsales.model.repository.BillingRepository;
import com.qiratek.rnpsales.model.repository.OutletRepository;
import com.qiratek.rnpsales.model.repository.SatuanRepository;
import com.qiratek.rnpsales.model.repository.TakeOrderRepository;
import com.qiratek.rnpsales.model.repository.TipeRepository;
import com.qiratek.rnpsales.model.repository.UserPersonalizeRepository;

import org.json.JSONObject;

import java.util.ArrayList;


public class HistoryViewModel extends BaseViewModel {

    BillingRepository billingRepository;
    OutletRepository outletRepository;
    TakeOrderRepository takeOrderRepository;
    SatuanRepository satuanRepository;
    UserPersonalizeRepository userPersonalizeRepository;
    TipeRepository tipeRepository;
    LiveData<ArrayList<BillingData>> billingLiveData;
    LiveData<ArrayList<TakeOrder>> takeOrderLiveData;
    LiveData<ArrayList<Satuan>> satuanLiveData;
    LiveData<Tipe> tipeLiveData;
    LiveData<ArrayList<Outlet>> outlets;
    LiveData<UserPersonalize> userPersonalizeLiveData;
    UserPersonalize userPersonalize;
    PopupWindow popupWindow;

    public HistoryViewModel(Context context) {
        billingRepository = BillingRepository.getInstance(BaseNetwork.getInstance(context), SharedPreferenceHelper.getInstance(context), this,
                AppDatabase.getInstance(context));
        takeOrderRepository = TakeOrderRepository.getInstance(BaseNetwork.getInstance(context), SharedPreferenceHelper.getInstance(context), this,
                AppDatabase.getInstance(context));
        outletRepository = OutletRepository.getInstance(BaseNetwork.getInstance(context), SharedPreferenceHelper.getInstance(context), this,
                AppDatabase.getInstance(context));
        satuanRepository = SatuanRepository.getInstance(BaseNetwork.getInstance(context), SharedPreferenceHelper.getInstance(context), this,
                AppDatabase.getInstance(context));
        tipeRepository = TipeRepository.getInstance(BaseNetwork.getInstance(context), SharedPreferenceHelper.getInstance(context), this,
                AppDatabase.getInstance(context));
        userPersonalizeRepository = UserPersonalizeRepository.getInstance(BaseNetwork.getInstance(context), SharedPreferenceHelper.getInstance(context), this,
                AppDatabase.getInstance(context));
    }

    public LiveData<ArrayList<BillingData>> getBillingData(){
        billingLiveData = billingRepository.getAllBillingData();
        if(billingLiveData.getValue() == null || billingLiveData.getValue().size() == 0) {
            loading.setValue(true);
        }
        return billingLiveData;
    }

    public LiveData<ArrayList<Outlet>> getAllOutlet(){
        outlets = outletRepository.getAllOutlet();
        if(outlets.getValue() == null || outlets.getValue().size() == 0) {
            loading.setValue(true);
        }
        return outlets;
    }

    public LiveData<ArrayList<TakeOrder>> getTakeOrderData(){
        takeOrderLiveData = takeOrderRepository.getAllTakeOrder();
        if(takeOrderLiveData.getValue() == null || takeOrderLiveData.getValue().size() == 0) {
            loading.setValue(true);
        }
        return takeOrderLiveData;
    }

    public LiveData<ArrayList<Satuan>> getAllSatuan(boolean isRefreshed){
        satuanLiveData = satuanRepository.getAllSatuan(isRefreshed);
        if(satuanLiveData.getValue() == null || satuanLiveData.getValue().size() == 0) {
            loading.setValue(true);
        }
        return satuanLiveData;
    }

    public LiveData<Tipe> getTipe(int id){
        tipeLiveData = tipeRepository.getTipe(id);
        return tipeLiveData;
    }

    public LiveData<UserPersonalize> getUserPersonalizeLive(){
        userPersonalizeLiveData = userPersonalizeRepository.getUserPersonalize();
        return userPersonalizeLiveData;
    }

    public LiveData<UserPersonalize> getUserPersonalizeLiveData() {
        return userPersonalizeLiveData;
    }

    public void setUserPersonalizeLiveData(JSONObject jsonObject){
        userPersonalizeRepository.setUserPersonalize(jsonObject);
    }

    public LiveData<Outlet> getOutlet(String id){
        return outletRepository.getOutlet(id);
    }

    public UserPersonalize getUserPersonalize() {
        return userPersonalize;
    }

    public void setUserPersonalize(UserPersonalize userPersonalize) {
        this.userPersonalize = userPersonalize;
    }

    public PopupWindow getPopupWindow() {
        return popupWindow;
    }

    public void setPopupWindow(PopupWindow popupWindow) {
        this.popupWindow = popupWindow;
    }

    public LiveData<ArrayList<TakeOrder>> getTakeOrderLiveData() {
        return takeOrderLiveData;
    }

    public LiveData<ArrayList<Satuan>> getSatuanLiveData() {
        return satuanLiveData;
    }
}
