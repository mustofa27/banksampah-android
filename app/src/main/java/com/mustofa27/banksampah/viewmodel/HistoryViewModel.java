package com.mustofa27.banksampah.viewmodel;

import android.content.Context;
import android.widget.PopupWindow;

import androidx.lifecycle.LiveData;

import org.json.JSONObject;

import java.util.ArrayList;


public class HistoryViewModel extends BaseViewModel {

    PopupWindow popupWindow;

    public HistoryViewModel(Context context) {
//        billingRepository = BillingRepository.getInstance(BaseNetwork.getInstance(context), SharedPreferenceHelper.getInstance(context), this,
//                AppDatabase.getInstance(context));
    }

    public PopupWindow getPopupWindow() {
        return popupWindow;
    }

    public void setPopupWindow(PopupWindow popupWindow) {
        this.popupWindow = popupWindow;
    }
}
