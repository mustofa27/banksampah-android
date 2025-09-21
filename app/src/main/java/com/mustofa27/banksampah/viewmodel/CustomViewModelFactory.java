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
        } else if (NewsViewModel.class.equals(modelClass)) {
            return (T) new NewsViewModel(context);
        } else if (CheckoutViewModel.class.equals(modelClass)) {
            return (T) new CheckoutViewModel(context);
        } else if (TakeOrderViewModel.class.equals(modelClass)) {
            return (T) new TakeOrderViewModel(context);
        } else if (ProductSearchViewModel.class.equals(modelClass)) {
            return (T) new ProductSearchViewModel(context);
        } else if (HistoryViewModel.class.equals(modelClass)) {
            return (T) new HistoryViewModel(context);
        } else if (MainActivityViewModel.class.equals(modelClass)) {
            return (T) new MainActivityViewModel(context);
        } else if (ResetPassViewModel.class.equals(modelClass)) {
            return (T) new ResetPassViewModel(context);
        } else{
            return (T) new UserViewModel(context);
        }
    }
}
