package com.mustofa27.banksampah.model.repository;

import androidx.lifecycle.MutableLiveData;

import com.mustofa27.banksampah.model.datasource.Result;
import com.mustofa27.banksampah.model.datasource.local.AppDatabase;
import com.mustofa27.banksampah.model.datasource.network.BaseNetwork;
import com.mustofa27.banksampah.model.datasource.network.ConnectionHandler;
import com.mustofa27.banksampah.model.datasource.network.NetworkCallback;
import com.mustofa27.banksampah.model.entity.WithdrawOption;
import com.mustofa27.banksampah.model.entity.NewsPagination;
import com.mustofa27.banksampah.model.helper.SharedPreferenceHelper;
import com.mustofa27.banksampah.viewmodel.VMRepoInterface;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Class that requests authentication and user information from the remote data source and
 * maintains an in-memory cache of login status and user credentials information.
 */
public class WithdrawOptionRepository extends BaseRepository {

    private static volatile WithdrawOptionRepository instance;
    private MutableLiveData<ArrayList<WithdrawOption>> withdrawOptionMutableLiveData;

    private WithdrawOptionRepository(BaseNetwork baseNetwork, SharedPreferenceHelper sharedPreferenceHelper, VMRepoInterface vmRepoInterface, AppDatabase db) {
        super(baseNetwork, sharedPreferenceHelper, vmRepoInterface, db);
        withdrawOptionMutableLiveData = new MutableLiveData<>();
    }

    public static WithdrawOptionRepository getInstance(BaseNetwork baseNetwork, SharedPreferenceHelper sharedPreferenceHelper, VMRepoInterface vmRepoInterface, AppDatabase db) {
        if (instance == null) {
            instance = new WithdrawOptionRepository(baseNetwork, sharedPreferenceHelper, vmRepoInterface, db);
        } else{
            instance.vmRepoInterface = vmRepoInterface;
        }
        return instance;
    }

    public MutableLiveData<ArrayList<WithdrawOption>> getAllWithdrawOption(){
        if(withdrawOptionMutableLiveData.getValue() == null || withdrawOptionMutableLiveData.getValue().size() == 0) {
            dataSource.Connect(ConnectionHandler.get_method, "withdraw_option", null, new NetworkCallback() {
                @Override
                public void onFinish() {

                }

                @Override
                public void onSuccess(Result result) {
                    WithdrawOption[] withdrawOptions = dataSource.getGson().fromJson(((Result.Success) result).getData().toString(), WithdrawOption[].class);
                    ArrayList<WithdrawOption> withdrawOptions1 = new ArrayList<>();
                    withdrawOptions1.addAll(Arrays.asList(withdrawOptions));
                    withdrawOptionMutableLiveData.setValue(withdrawOptions1);
                    vmRepoInterface.setMessage(result.toString());
                }

                @Override
                public void onError(Result result) {
                    vmRepoInterface.setMessage(result.toString());
                    vmRepoInterface.getStatus().setValue(false);
                }
            });
        }
        return withdrawOptionMutableLiveData;
    }
}