package com.qiratek.rnpsales.model.datasource.network;


import com.qiratek.rnpsales.model.datasource.Result;

/**
 * Created by LENOVO on 2/2/2017.
 */

public interface NetworkCallback {
    public void onFinish();
    public void onSuccess(Result result);
    public void onError(Result result);
}
