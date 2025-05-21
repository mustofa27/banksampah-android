package com.qiratek.rnpsales.model.repository;

import androidx.lifecycle.MutableLiveData;

import com.qiratek.rnpsales.model.datasource.Result;
import com.qiratek.rnpsales.model.datasource.local.AppDatabase;
import com.qiratek.rnpsales.model.datasource.network.BaseNetwork;
import com.qiratek.rnpsales.model.datasource.network.ConnectionHandler;
import com.qiratek.rnpsales.model.datasource.network.NetworkCallback;
import com.qiratek.rnpsales.model.entity.Brand;
import com.qiratek.rnpsales.model.entity.User;
import com.qiratek.rnpsales.model.helper.SharedPreferenceHelper;
import com.qiratek.rnpsales.viewmodel.VMRepoInterface;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Class that requests authentication and user information from the remote data source and
 * maintains an in-memory cache of login status and user credentials information.
 */
public class BrandRepository extends BaseRepository {

    private static volatile BrandRepository instance;
    private MutableLiveData<ArrayList<Brand>> brandsCatalogMutableLiveData, brandsPriceListMutableLiveData;
    private User user;

    private BrandRepository(BaseNetwork baseNetwork, SharedPreferenceHelper sharedPreferenceHelper, VMRepoInterface vmRepoInterface, AppDatabase db) {
        super(baseNetwork, sharedPreferenceHelper, vmRepoInterface, db);
        brandsCatalogMutableLiveData = new MutableLiveData<>();
        brandsPriceListMutableLiveData = new MutableLiveData<>();
    }

    public static BrandRepository getInstance(BaseNetwork baseNetwork, SharedPreferenceHelper sharedPreferenceHelper, VMRepoInterface vmRepoInterface, AppDatabase db) {
        if (instance == null) {
            instance = new BrandRepository(baseNetwork, sharedPreferenceHelper, vmRepoInterface, db);
        }
        instance.vmRepoInterface = vmRepoInterface;
        instance.user = baseNetwork.getUser();
        instance.brandsCatalogMutableLiveData = new MutableLiveData<>();
        instance.brandsPriceListMutableLiveData = new MutableLiveData<>();
        return instance;
    }

    public MutableLiveData<ArrayList<Brand>> getAllBrandCatalog(){
        // handle login
        if(brandsCatalogMutableLiveData.getValue() == null || brandsCatalogMutableLiveData.getValue().size() == 0) {
            dataSource.Connect(ConnectionHandler.post_method, "countCatalog", null, new NetworkCallback() {
                @Override
                public void onFinish() {

                }

                @Override
                public void onSuccess(Result result) {
                    Brand[] brandArray = dataSource.getGson().fromJson(((Result.Success) result).getData().toString(), Brand[].class);
                    ArrayList<Brand> brands = new ArrayList<>();
                    brands.addAll(Arrays.asList(brandArray));
                    brandsCatalogMutableLiveData.setValue(brands);
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
        return brandsCatalogMutableLiveData;
    }

    public MutableLiveData<ArrayList<Brand>> getAllBrand(boolean isRefreshed){
        // handle login
        if(isRefreshed){
            brandsCatalogMutableLiveData = new MutableLiveData<>();
        }
        if(brandsCatalogMutableLiveData.getValue() == null || brandsCatalogMutableLiveData.getValue().size() == 0) {
            new Thread() {
                @Override
                public void run() {
                    ArrayList<Brand> localData = new ArrayList<>();
                    localData.addAll(db.brandDAO().getAll());
                    if (localData.size() == 0 || isRefreshed) {
                        dataSource.Connect(ConnectionHandler.post_method, "getBrand", null, new NetworkCallback() {
                            @Override
                            public void onFinish() {

                            }

                            @Override
                            public void onSuccess(Result result) {
                                Brand[] brandArray = dataSource.getGson().fromJson(((Result.Success) result).getData().toString(), Brand[].class);
                                ArrayList<Brand> brands = new ArrayList<>();
                                brands.addAll(Arrays.asList(brandArray));
                                new Thread() {
                                    @Override
                                    public void run() {
                                        List<Long> tmp = db.brandDAO().insertAll(brandArray);
                                        brandsCatalogMutableLiveData.postValue(brands);
                                        vmRepoInterface.setMessage(result.toString());
                                    }
                                }.start();
                            }

                            @Override
                            public void onError(Result result) {
                                vmRepoInterface.setMessage(result.toString());
                                vmRepoInterface.getStatus().setValue(false);
                            }
                        });
                    } else{
                        vmRepoInterface.getStatus().postValue(true);
                        vmRepoInterface.setMessage("Data berhasil didapatkan");
                        brandsCatalogMutableLiveData.postValue(localData);
                    }
                }
            }.start();
        }
        return brandsCatalogMutableLiveData;
    }

    public MutableLiveData<ArrayList<Brand>> getAllBrandPrice(){
        // handle login
        if(brandsPriceListMutableLiveData.getValue() == null || brandsPriceListMutableLiveData.getValue().size() == 0) {
            try {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("kota_id", user.getKota_id());
                dataSource.Connect(ConnectionHandler.post_method, "countPrice", jsonObject, new NetworkCallback() {
                    @Override
                    public void onFinish() {

                    }

                    @Override
                    public void onSuccess(Result result) {
                        Brand[] brandArray = dataSource.getGson().fromJson(((Result.Success) result).getData().toString(), Brand[].class);
                        ArrayList<Brand> brands = new ArrayList<>();
                        brands.addAll(Arrays.asList(brandArray));
                        brandsPriceListMutableLiveData.setValue(brands);
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
        }
        return brandsPriceListMutableLiveData;
    }
}