package com.mustofa27.banksampah.viewmodel;

import androidx.lifecycle.MutableLiveData;

public interface VMRepoInterface {
    public MutableLiveData<Boolean> getStatus();
    public void setMessage(String message);
}
