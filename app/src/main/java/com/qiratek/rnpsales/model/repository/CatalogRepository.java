package com.qiratek.rnpsales.model.repository;

import androidx.lifecycle.MutableLiveData;

import com.qiratek.rnpsales.model.datasource.Result;
import com.qiratek.rnpsales.model.datasource.local.AppDatabase;
import com.qiratek.rnpsales.model.datasource.network.BaseNetwork;
import com.qiratek.rnpsales.model.datasource.network.ConnectionHandler;
import com.qiratek.rnpsales.model.datasource.network.NetworkCallback;
import com.qiratek.rnpsales.model.entity.Catalog;
import com.qiratek.rnpsales.model.helper.SharedPreferenceHelper;
import com.qiratek.rnpsales.viewmodel.VMRepoInterface;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Class that requests authentication and user information from the remote data source and
 * maintains an in-memory cache of login status and user credentials information.
 */
public class CatalogRepository extends BaseRepository {

    private static volatile CatalogRepository instance;
    private MutableLiveData<ArrayList<Catalog>> catalogsMutableLiveData;

    private CatalogRepository(BaseNetwork baseNetwork, SharedPreferenceHelper sharedPreferenceHelper, VMRepoInterface vmRepoInterface, AppDatabase db) {
        super(baseNetwork, sharedPreferenceHelper, vmRepoInterface, db);
        catalogsMutableLiveData = new MutableLiveData<>();
    }

    public static CatalogRepository getInstance(BaseNetwork baseNetwork, SharedPreferenceHelper sharedPreferenceHelper, VMRepoInterface vmRepoInterface, AppDatabase db) {
        if (instance == null) {
            instance = new CatalogRepository(baseNetwork, sharedPreferenceHelper, vmRepoInterface, db);
        }
        return instance;
    }

    public MutableLiveData<ArrayList<Catalog>> getAllCatalog(int brand_id){
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("brand_id", brand_id);
            dataSource.Connect(ConnectionHandler.post_method, "getCatalog", jsonObject, new NetworkCallback() {
                @Override
                public void onFinish() {

                }

                @Override
                public void onSuccess(Result result) {
                    Catalog[] tipeArray = dataSource.getGson().fromJson(((Result.Success) result).getData().toString(), Catalog[].class);
                    ArrayList<Catalog> catalogs = new ArrayList<>();
                    catalogs.addAll(Arrays.asList(tipeArray));
                    catalogsMutableLiveData.setValue(catalogs);
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
        return catalogsMutableLiveData;
    }
}