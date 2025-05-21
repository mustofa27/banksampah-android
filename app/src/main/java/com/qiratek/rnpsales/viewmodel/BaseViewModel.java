package com.qiratek.rnpsales.viewmodel;

import androidx.activity.result.ActivityResultLauncher;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;


public abstract class BaseViewModel extends ViewModel implements VMRepoInterface {
    String message;
    MutableLiveData<Boolean> status;
    MutableLiveData<Boolean> loading;
    private ActivityResultLauncher activityResultLauncher;
    private MutableLiveData<Boolean> statusCamera;

    public BaseViewModel(){
        loading = new MutableLiveData<>();
        status = new MutableLiveData<>();
        statusCamera = new MutableLiveData<>();
    }

    public String getMessage() {
        return message;
    }

    @Override
    public MutableLiveData<Boolean> getStatus() {
        return status;
    }

    @Override
    public void setMessage(String message) {
        this.message = message;
    }

    public void setStatus(MutableLiveData<Boolean> status) {
        this.status = status;
    }

    public MutableLiveData<Boolean> getLoading() {
        return loading;
    }

    public void setLoading(MutableLiveData<Boolean> loading) {
        this.loading = loading;
    }

    public ActivityResultLauncher getActivityResultLauncher() {
        return activityResultLauncher;
    }

    public void setActivityResultLauncher(ActivityResultLauncher activityResultLauncher) {
        this.activityResultLauncher = activityResultLauncher;
    }

    public MutableLiveData<Boolean> getStatusCamera() {
        return statusCamera;
    }

    public void setStatusCamera(MutableLiveData<Boolean> statusCamera) {
        this.statusCamera = statusCamera;
    }
}
