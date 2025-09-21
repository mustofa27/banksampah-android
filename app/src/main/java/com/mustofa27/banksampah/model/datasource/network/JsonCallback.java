package com.mustofa27.banksampah.model.datasource.network;

import org.json.JSONObject;

/**
 * Created by LENOVO on 2/2/2017.
 */

public interface JsonCallback {
    public void Done(JSONObject jsonObject, String message);
}
