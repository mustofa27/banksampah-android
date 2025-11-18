package com.mustofa27.banksampah.model.repository;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.mustofa27.banksampah.model.datasource.Result;
import com.mustofa27.banksampah.model.datasource.local.AppDatabase;
import com.mustofa27.banksampah.model.datasource.network.BaseNetwork;
import com.mustofa27.banksampah.model.datasource.network.ConnectionHandler;
import com.mustofa27.banksampah.model.datasource.network.NetworkCallback;
import com.mustofa27.banksampah.model.entity.Transaction;
import com.mustofa27.banksampah.model.entity.Transaction;
import com.mustofa27.banksampah.model.helper.SharedPreferenceHelper;
import com.mustofa27.banksampah.viewmodel.VMRepoInterface;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Class that requests authentication and user information from the remote data source and
 * maintains an in-memory cache of login status and user credentials information.
 */
public class TransactionRepository extends BaseRepository {

    private static volatile TransactionRepository instance;
    private MutableLiveData<ArrayList<Transaction>> transactionListMutableLiveData;
    private MutableLiveData<Transaction> transactionMutableLiveData;

    private TransactionRepository(BaseNetwork baseNetwork, SharedPreferenceHelper sharedPreferenceHelper, VMRepoInterface vmRepoInterface, AppDatabase db) {
        super(baseNetwork, sharedPreferenceHelper, vmRepoInterface, db);
        transactionListMutableLiveData = new MutableLiveData<>();
        transactionMutableLiveData = new MutableLiveData<>();
    }

    public static TransactionRepository getInstance(BaseNetwork baseNetwork, SharedPreferenceHelper sharedPreferenceHelper, VMRepoInterface vmRepoInterface, AppDatabase db) {
        if (instance == null) {
            instance = new TransactionRepository(baseNetwork, sharedPreferenceHelper, vmRepoInterface, db);
        } else{
            instance.vmRepoInterface = vmRepoInterface;
        }
        return instance;
    }

    public LiveData<ArrayList<Transaction>> getData(){
        dataSource.Connect(ConnectionHandler.post_method, "transaction/my", null, new NetworkCallback() {
            @Override
            public void onFinish() {

            }

            @Override
            public void onSuccess(Result result) {
                Transaction[] transactions = dataSource.getGson().fromJson(((Result.Success) result).getData().toString(), Transaction[].class);
                ArrayList<Transaction> transactionArrayList = new ArrayList<>();
                transactionArrayList.addAll(Arrays.asList(transactions));
                transactionListMutableLiveData.setValue(transactionArrayList);
                vmRepoInterface.setMessage(result.toString());
                vmRepoInterface.getStatus().setValue(true);
            }

            @Override
            public void onError(Result result) {
                vmRepoInterface.setMessage(result.toString());
                vmRepoInterface.getStatus().setValue(false);
            }
        });
        return transactionListMutableLiveData;
    }
    public LiveData<Transaction> addTransaction(JSONObject jsonObject){
        // handle login
        dataSource.Connect(ConnectionHandler.post_method, "transaction", jsonObject, new NetworkCallback() {
            @Override
            public void onFinish() {

            }

            @Override
            public void onSuccess(Result result) {
                Transaction transaction = dataSource.getGson().fromJson(((Result.Success) result).getData().toString(), Transaction.class);
                transactionMutableLiveData.setValue(transaction);
                vmRepoInterface.setMessage(result.toString());
                vmRepoInterface.getStatus().setValue(Boolean.TRUE);
            }

            @Override
            public void onError(Result result) {
                vmRepoInterface.setMessage(result.toString());
                vmRepoInterface.getStatus().setValue(false);
            }
        });
        return transactionMutableLiveData;
    }
}