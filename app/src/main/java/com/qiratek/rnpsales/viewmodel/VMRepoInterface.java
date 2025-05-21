package com.qiratek.rnpsales.viewmodel;

import androidx.lifecycle.MutableLiveData;

public interface VMRepoInterface {
    public MutableLiveData<Boolean> getStatus();
    public void setMessage(String message);
}
