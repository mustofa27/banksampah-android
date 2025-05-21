package com.qiratek.rnpsales.model.repository;

import androidx.lifecycle.MutableLiveData;

import com.qiratek.rnpsales.model.datasource.Result;
import com.qiratek.rnpsales.model.datasource.local.AppDatabase;
import com.qiratek.rnpsales.model.datasource.network.BaseNetwork;
import com.qiratek.rnpsales.model.datasource.network.ConnectionHandler;
import com.qiratek.rnpsales.model.datasource.network.MultipartFile;
import com.qiratek.rnpsales.model.datasource.network.NetworkCallback;
import com.qiratek.rnpsales.model.entity.BillingData;
import com.qiratek.rnpsales.model.entity.User;
import com.qiratek.rnpsales.model.helper.SharedPreferenceHelper;
import com.qiratek.rnpsales.viewmodel.VMRepoInterface;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * Class that requests authentication and user information from the remote data source and
 * maintains an in-memory cache of login status and user credentials information.
 */
public class BillingRepository extends BaseRepository {

    private static volatile BillingRepository instance;
    private MutableLiveData<ArrayList<BillingData>> billingMutableLiveData;
    private User user;

    private BillingRepository(BaseNetwork baseNetwork, SharedPreferenceHelper sharedPreferenceHelper, VMRepoInterface vmRepoInterface, AppDatabase db) {
        super(baseNetwork, sharedPreferenceHelper, vmRepoInterface, db);
        billingMutableLiveData = new MutableLiveData<>();
    }

    public static BillingRepository getInstance(BaseNetwork baseNetwork, SharedPreferenceHelper sharedPreferenceHelper, VMRepoInterface vmRepoInterface, AppDatabase db) {
        if (instance == null) {
            instance = new BillingRepository(baseNetwork, sharedPreferenceHelper, vmRepoInterface, db);
        }
        if(instance.vmRepoInterface != vmRepoInterface){
            instance.vmRepoInterface = vmRepoInterface;
        }
        instance.user = baseNetwork.getUser();
        return instance;
    }

    public void submit(Map<String, Object> param, Map<String, MultipartFile> paramFile){
        dataSource.Connect(ConnectionHandler.post_method, "submitBilling", param, paramFile, new NetworkCallback() {
            @Override
            public void onFinish() {

            }

            @Override
            public void onSuccess(Result result) {
                vmRepoInterface.setMessage(result.toString());
                vmRepoInterface.getStatus().setValue(true);
            }

            @Override
            public void onError(Result result) {
                vmRepoInterface.setMessage(result.toString());
                vmRepoInterface.getStatus().setValue(false);
            }
        });
    }

    public MutableLiveData<Boolean> submitSavedBilling(Map<String, Object> param, Map<String, MultipartFile> paramFile, BillingData billingData){
        MutableLiveData<Boolean> statusUpload = new MutableLiveData<>();
        dataSource.Connect(ConnectionHandler.post_method, "order/billing", param, paramFile, new NetworkCallback() {
            @Override
            public void onFinish() {

            }

            @Override
            public void onSuccess(Result result) {
                vmRepoInterface.setMessage(result.toString());
                vmRepoInterface.getStatus().setValue(true);
                new Thread() {
                    @Override
                    public void run() {
                        db.billingDataDAO().delete(billingData);
                        vmRepoInterface.setMessage(result.toString());
                        statusUpload.postValue(true);
                    }
                }.start();
            }

            @Override
            public void onError(Result result) {
                vmRepoInterface.setMessage(result.toString());
                statusUpload.setValue(false);
            }
        });
        return statusUpload;
    }

    public void saveBilling(BillingData billingData){
        new Thread() {
            @Override
            public void run() {
                List<Long> tmp = db.billingDataDAO().insertAll(billingData);
                vmRepoInterface.setMessage("Berhasil menyimpan data");
                vmRepoInterface.getStatus().postValue(true);
            }
        }.start();
    }

    public MutableLiveData<ArrayList<BillingData>> getSavedBilling(){
        billingMutableLiveData = new MutableLiveData<>();
        if(billingMutableLiveData.getValue() == null || billingMutableLiveData.getValue().size() == 0) {
            new Thread() {
                @Override
                public void run() {
                    ArrayList<BillingData> localData = new ArrayList<>();
                    localData.addAll(db.billingDataDAO().getAll());
                    vmRepoInterface.getStatus().postValue(true);
                    vmRepoInterface.setMessage("Data berhasil didapatkan");
                    billingMutableLiveData.postValue(localData);
                }
            }.start();
        }
        return billingMutableLiveData;
    }

    public MutableLiveData<ArrayList<BillingData>> getAllBillingData(){
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("user_id", user.getId());
            dataSource.Connect(ConnectionHandler.post_method, "getLogBilling", jsonObject, new NetworkCallback() {
                @Override
                public void onFinish() {

                }

                @Override
                public void onSuccess(Result result) {
                    BillingData[] billingData = dataSource.getGson().fromJson(((Result.Success) result).getData().toString(), BillingData[].class);
                    ArrayList<BillingData> billingDatas = new ArrayList<>();
                    billingDatas.addAll(Arrays.asList(billingData));
                    billingMutableLiveData.setValue(billingDatas);
                    vmRepoInterface.setMessage(result.toString());
                    vmRepoInterface.getStatus().setValue(true);
                }

                @Override
                public void onError(Result result) {
                    vmRepoInterface.setMessage(result.toString());
                    vmRepoInterface.getStatus().setValue(false);
                }
            });
        } catch (JSONException e) {
            vmRepoInterface.setMessage(e.getMessage());
            vmRepoInterface.getStatus().setValue(false);
        }
        return billingMutableLiveData;
    }
}