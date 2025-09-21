package com.mustofa27.banksampah.model.repository;

import androidx.lifecycle.MutableLiveData;

import com.mustofa27.banksampah.model.datasource.Result;
import com.mustofa27.banksampah.model.datasource.local.AppDatabase;
import com.mustofa27.banksampah.model.datasource.network.BaseNetwork;
import com.mustofa27.banksampah.model.datasource.network.ConnectionHandler;
import com.mustofa27.banksampah.model.datasource.network.NetworkCallback;
import com.mustofa27.banksampah.model.entity.PriceList;
import com.mustofa27.banksampah.model.entity.User;
import com.mustofa27.banksampah.model.helper.SharedPreferenceHelper;
import com.mustofa27.banksampah.viewmodel.VMRepoInterface;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;

public class PriceListRepository extends BaseRepository {

    private static volatile PriceListRepository instance;
    private MutableLiveData<ArrayList<PriceList>> priceListMutable;
    private User user;

    private PriceListRepository(BaseNetwork baseNetwork, SharedPreferenceHelper sharedPreferenceHelper, VMRepoInterface vmRepoInterface, AppDatabase db) {
        super(baseNetwork, sharedPreferenceHelper, vmRepoInterface, db);
        priceListMutable = new MutableLiveData<>();
    }

    public static PriceListRepository getInstance(BaseNetwork baseNetwork, SharedPreferenceHelper sharedPreferenceHelper, VMRepoInterface vmRepoInterface, AppDatabase db) {
        if (instance == null) {
            instance = new PriceListRepository(baseNetwork, sharedPreferenceHelper, vmRepoInterface, db);
        }
        instance.user = baseNetwork.getUser();
        return instance;
    }

    public MutableLiveData<ArrayList<PriceList>> getAllPriceList(int brand_id){
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("brand_id", brand_id);
            jsonObject.put("kota_id", user.getKota_id());
            dataSource.Connect(ConnectionHandler.post_method, "getPrice", jsonObject, new NetworkCallback() {
                @Override
                public void onFinish() {

                }

                @Override
                public void onSuccess(Result result) {
                    PriceList[] tipeArray = dataSource.getGson().fromJson(((Result.Success) result).getData().toString(), PriceList[].class);
                    ArrayList<PriceList> catalogs = new ArrayList<>();
                    catalogs.addAll(Arrays.asList(tipeArray));
                    priceListMutable.setValue(catalogs);
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
            vmRepoInterface.setMessage("Json Error");
            vmRepoInterface.getStatus().setValue(false);
            e.printStackTrace();
        }
        return priceListMutable;
    }
}