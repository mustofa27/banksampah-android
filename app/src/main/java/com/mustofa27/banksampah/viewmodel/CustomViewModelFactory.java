package com.mustofa27.banksampah.viewmodel;

import android.content.Context;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

public class CustomViewModelFactory implements ViewModelProvider.Factory {

    private Context context;

    public CustomViewModelFactory(Context context) {
        this.context = context;
    }


    @Override
    public <T extends ViewModel> T create(Class<T> modelClass) {
        if (UserViewModel.class.equals(modelClass)) {
            return (T) new UserViewModel(context);
        } else if (HomeViewModel.class.equals(modelClass)) {
            return (T) new HomeViewModel(context);
        } else if (OutletViewModel.class.equals(modelClass)) {
            return (T) new OutletViewModel(context);
        } else if (RegisterOutletViewModel.class.equals(modelClass)) {
            return (T) new RegisterOutletViewModel(context);
        } else if (NewsViewModel.class.equals(modelClass)) {
            return (T) new NewsViewModel(context);
        } else if (CatalogViewModel.class.equals(modelClass)) {
            return (T) new CatalogViewModel(context);
        } else if (PriceListViewModel.class.equals(modelClass)) {
            return (T) new PriceListViewModel(context);
        } else if (SubmitVisitViewModel.class.equals(modelClass)) {
            return (T) new SubmitVisitViewModel(context);
        } else if (OutletSearchViewModel.class.equals(modelClass)) {
            return (T) new OutletSearchViewModel(context);
        } else if (VisitReportViewModel.class.equals(modelClass)) {
            return (T) new VisitReportViewModel(context);
        } else if (CheckoutViewModel.class.equals(modelClass)) {
            return (T) new CheckoutViewModel(context);
        } else if (TakeOrderViewModel.class.equals(modelClass)) {
            return (T) new TakeOrderViewModel(context);
        } else if (CountDownTimerViewModel.class.equals(modelClass)) {
            return (T) new CountDownTimerViewModel(context);
        } else if (ProductSearchViewModel.class.equals(modelClass)) {
            return (T) new ProductSearchViewModel(context);
        } else if (MarketUpdateViewModel.class.equals(modelClass)) {
            return (T) new MarketUpdateViewModel(context);
        } else if (BillingViewModel.class.equals(modelClass)) {
            return (T) new BillingViewModel(context);
        } else if (HistoryViewModel.class.equals(modelClass)) {
            return (T) new HistoryViewModel(context);
        } else if (SyncViewModel.class.equals(modelClass)) {
            return (T) new SyncViewModel(context);
        } else if (MainActivityViewModel.class.equals(modelClass)) {
            return (T) new MainActivityViewModel(context);
        } else if (ResetPassViewModel.class.equals(modelClass)) {
            return (T) new ResetPassViewModel(context);
        } else{
            return (T) new UserViewModel(context);
        }
    }
}
