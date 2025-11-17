package com.mustofa27.banksampah.model.repository;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.mustofa27.banksampah.model.datasource.Result;
import com.mustofa27.banksampah.model.datasource.local.AppDatabase;
import com.mustofa27.banksampah.model.datasource.network.BaseNetwork;
import com.mustofa27.banksampah.model.datasource.network.ConnectionHandler;
import com.mustofa27.banksampah.model.datasource.network.NetworkCallback;
import com.mustofa27.banksampah.model.entity.Withdraw;
import com.mustofa27.banksampah.model.entity.WithdrawPagination;
import com.mustofa27.banksampah.model.helper.SharedPreferenceHelper;
import com.mustofa27.banksampah.viewmodel.VMRepoInterface;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Class that requests authentication and user information from the remote data source and
 * maintains an in-memory cache of login status and user credentials information.
 */
public class WithdrawRepository extends BaseRepository {

    private static volatile WithdrawRepository instance;
    private MutableLiveData<ArrayList<Withdraw>> WithdrawListMutableLiveData;
    int last_page, current_page;

    private WithdrawRepository(BaseNetwork baseNetwork, SharedPreferenceHelper sharedPreferenceHelper, VMRepoInterface vmRepoInterface, AppDatabase db) {
        super(baseNetwork, sharedPreferenceHelper, vmRepoInterface, db);
    }

    public static WithdrawRepository getInstance(BaseNetwork baseNetwork, SharedPreferenceHelper sharedPreferenceHelper, VMRepoInterface vmRepoInterface, AppDatabase db) {
        if (instance == null) {
            instance = new WithdrawRepository(baseNetwork, sharedPreferenceHelper, vmRepoInterface, db);
        } else{
            instance.vmRepoInterface = vmRepoInterface;
        }
        instance.last_page = 0;
        return instance;
    }

    public LiveData<ArrayList<Withdraw>> getData(int page){
        if(page == 1){
            WithdrawListMutableLiveData = new MutableLiveData<>();
        }
        if(page <= last_page || last_page == 0) {
            dataSource.Connect(ConnectionHandler.post_method, "withdraw/my?page=" + page, null, new NetworkCallback() {
                @Override
                public void onFinish() {

                }

                @Override
                public void onSuccess(Result result) {
                    WithdrawPagination WithdrawPagination = dataSource.getGson().fromJson(((Result.Success) result).getData().toString(), WithdrawPagination.class);
                    last_page = WithdrawPagination.getLast_page();
                    current_page = WithdrawPagination.getCurrent_page();
                    WithdrawListMutableLiveData.setValue(WithdrawPagination.getData());
                    vmRepoInterface.setMessage(result.toString());
                    vmRepoInterface.getStatus().setValue(true);
                }

                @Override
                public void onError(Result result) {
                    vmRepoInterface.setMessage(result.toString());
                    vmRepoInterface.getStatus().setValue(false);
                }
            });
        } else{
            vmRepoInterface.setMessage("Data tidak tersedia");
            vmRepoInterface.getStatus().setValue(false);
        }
        return WithdrawListMutableLiveData;
    }
    public void addWithdraw(int count, int balance_used,int withdraw_option_id){
        // handle login
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("count", count);
            jsonObject.put("balance_used", balance_used);
            jsonObject.put("withdraw_option_id", withdraw_option_id);
            dataSource.Connect(ConnectionHandler.post_method, "withdraw", jsonObject, new NetworkCallback() {
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
        } catch (JSONException e){
            vmRepoInterface.setMessage(e.getMessage());
            vmRepoInterface.getStatus().setValue(false);
        }
    }
    public boolean isNextPageAvailable(){
        return current_page < last_page;
    }
}