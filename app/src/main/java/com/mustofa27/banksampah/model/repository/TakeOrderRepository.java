package com.mustofa27.banksampah.model.repository;

import androidx.lifecycle.MutableLiveData;


import com.mustofa27.banksampah.model.datasource.Result;
import com.mustofa27.banksampah.model.datasource.local.AppDatabase;
import com.mustofa27.banksampah.model.datasource.network.BaseNetwork;
import com.mustofa27.banksampah.model.datasource.network.ConnectionHandler;
import com.mustofa27.banksampah.model.datasource.network.NetworkCallback;
import com.mustofa27.banksampah.model.entity.User;
import com.mustofa27.banksampah.model.helper.SharedPreferenceHelper;
import com.mustofa27.banksampah.viewmodel.VMRepoInterface;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Class that requests authentication and user information from the remote data source and
 * maintains an in-memory cache of login status and user credentials information.
 */
public class TakeOrderRepository extends BaseRepository {

    private static volatile TakeOrderRepository instance;
    //private MutableLiveData<ArrayList<TakeOrder>> takeOrderMutableLiveData;

    private TakeOrderRepository(BaseNetwork baseNetwork, SharedPreferenceHelper sharedPreferenceHelper, VMRepoInterface vmRepoInterface, AppDatabase db) {
        super(baseNetwork, sharedPreferenceHelper, vmRepoInterface, db);
//        takeOrderMutableLiveData = new MutableLiveData<>();
    }

    public static TakeOrderRepository getInstance(BaseNetwork baseNetwork, SharedPreferenceHelper sharedPreferenceHelper, VMRepoInterface vmRepoInterface, AppDatabase db) {
        if (instance == null) {
            instance = new TakeOrderRepository(baseNetwork, sharedPreferenceHelper, vmRepoInterface, db);
        }
        if(instance.vmRepoInterface != vmRepoInterface){
            instance.vmRepoInterface = vmRepoInterface;
        }
//        instance.takeOrderMutableLiveData = new MutableLiveData<>();
        return instance;
    }

//    public void submitTakeOrder(JSONObject jsonObject){
//        try {
//            jsonObject.put("user_id", user.getId());
//            dataSource.Connect(ConnectionHandler.post_method, "submitTakeOrder", jsonObject, new NetworkCallback() {
//                @Override
//                public void onFinish() {
//
//                }
//
//                @Override
//                public void onSuccess(Result result) {
//                    vmRepoInterface.setMessage(result.toString());
//                    vmRepoInterface.getStatus().setValue(true);
//                }
//
//                @Override
//                public void onError(Result result) {
//                    vmRepoInterface.setMessage(result.toString());
//                    vmRepoInterface.getStatus().setValue(false);
//                }
//            });
//        } catch (JSONException e) {
//            vmRepoInterface.setMessage(e.getMessage());
//            vmRepoInterface.getStatus().setValue(false);
//        }
//    }

//    public MutableLiveData<ArrayList<TakeOrder>> getAllTakeOrder(){
//        try {
//            JSONObject jsonObject = new JSONObject();
//            jsonObject.put("user_id", user.getId());
//            dataSource.Connect(ConnectionHandler.post_method, "getTakeOrderUser", jsonObject, new NetworkCallback() {
//                @Override
//                public void onFinish() {
//
//                }
//
//                @Override
//                public void onSuccess(Result result) {
//                    TakeOrder[] takeOrder = dataSource.getGson().fromJson(((Result.Success) result).getData().toString(), TakeOrder[].class);
//                    ArrayList<TakeOrder> takeOrders = new ArrayList<>();
//                    takeOrders.addAll(Arrays.asList(takeOrder));
//                    takeOrderMutableLiveData.setValue(takeOrders);
//                    vmRepoInterface.setMessage(result.toString());
//                    vmRepoInterface.getStatus().setValue(true);
//                }
//
//                @Override
//                public void onError(Result result) {
//                    vmRepoInterface.setMessage(result.toString());
//                    vmRepoInterface.getStatus().setValue(false);
//                }
//            });
//        } catch (JSONException e) {
//            vmRepoInterface.setMessage(e.getMessage());
//            vmRepoInterface.getStatus().setValue(false);
//        }
//        return takeOrderMutableLiveData;
//    }
//
//    public void saveTakeOrder(TakeOrderData takeOrderData){
//        new Thread() {
//            @Override
//            public void run() {
//                List<Long> tmp = db.takeOrderDataDAO().insertAll(takeOrderData);
//                vmRepoInterface.setMessage("Berhasil menyimpan data");
//                vmRepoInterface.getStatus().postValue(true);
//            }
//        }.start();
//    }
//
//    public MutableLiveData<ArrayList<TakeOrderData>> getSavedData(){
//        MutableLiveData<ArrayList<TakeOrderData>> data = new MutableLiveData<>();
//        new Thread() {
//            @Override
//            public void run() {
//                ArrayList<TakeOrderData> localData = new ArrayList<>();
//                localData.addAll(db.takeOrderDataDAO().getAll());
//                vmRepoInterface.getStatus().postValue(true);
//                vmRepoInterface.setMessage("Data berhasil didapatkan");
//                data.postValue(localData);
//            }
//        }.start();
//        return data;
//    }
//
//    public MutableLiveData<Boolean> submitSavedTakeOrder(JSONObject jsonObject, TakeOrderData takeOrderData){
//        MutableLiveData<Boolean> statusUpload = new MutableLiveData<>();
//        try {
//            jsonObject.put("user_id", user.getId());
//            dataSource.Connect(ConnectionHandler.post_method, "order/add", jsonObject, new NetworkCallback() {
//                @Override
//                public void onFinish() {
//
//                }
//
//                @Override
//                public void onSuccess(Result result) {
//                    new Thread() {
//                        @Override
//                        public void run() {
//                            db.takeOrderDataDAO().delete(takeOrderData);
//                            vmRepoInterface.setMessage(result.toString());
//                            statusUpload.postValue(true);
//                        }
//                    }.start();
//                }
//
//                @Override
//                public void onError(Result result) {
//                    vmRepoInterface.setMessage(result.toString());
//                    statusUpload.setValue(false);
//                }
//            });
//        } catch (JSONException e) {
//            vmRepoInterface.setMessage(e.getMessage());
//            statusUpload.setValue(false);
//        }
//        return statusUpload;
//    }
}