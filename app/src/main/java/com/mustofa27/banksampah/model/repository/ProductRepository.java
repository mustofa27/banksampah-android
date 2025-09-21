package com.mustofa27.banksampah.model.repository;

import androidx.lifecycle.MutableLiveData;

import com.mustofa27.banksampah.model.datasource.Result;
import com.mustofa27.banksampah.model.datasource.local.AppDatabase;
import com.mustofa27.banksampah.model.datasource.network.BaseNetwork;
import com.mustofa27.banksampah.model.datasource.network.ConnectionHandler;
import com.mustofa27.banksampah.model.datasource.network.NetworkCallback;
import com.mustofa27.banksampah.model.entity.Product;
import com.mustofa27.banksampah.model.entity.User;
import com.mustofa27.banksampah.model.helper.SharedPreferenceHelper;
import com.mustofa27.banksampah.viewmodel.VMRepoInterface;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Class that requests authentication and user information from the remote data source and
 * maintains an in-memory cache of login status and user credentials information.
 */
public class ProductRepository extends BaseRepository {

    private static volatile ProductRepository instance;
    private MutableLiveData<ArrayList<Product>> productListMutableLiveData;

    private ProductRepository(BaseNetwork baseNetwork, SharedPreferenceHelper sharedPreferenceHelper, VMRepoInterface vmRepoInterface, AppDatabase db) {
        super(baseNetwork, sharedPreferenceHelper, vmRepoInterface, db);
    }

    public static ProductRepository getInstance(BaseNetwork baseNetwork, SharedPreferenceHelper sharedPreferenceHelper, VMRepoInterface vmRepoInterface, AppDatabase db) {
        if (instance == null) {
            instance = new ProductRepository(baseNetwork, sharedPreferenceHelper, vmRepoInterface, db);
        }
        return instance;
    }

    public MutableLiveData<ArrayList<Product>> getAllProduk(){
        productListMutableLiveData = new MutableLiveData<>();
        new Thread() {
            @Override
            public void run() {
                ArrayList<Product> localData = new ArrayList<>();
                localData.addAll(db.productDAO().getAll());
                if (localData.size() == 0) {
                    dataSource.Connect(ConnectionHandler.post_method, "product", null, new NetworkCallback() {
                        @Override
                        public void onFinish() {

                        }

                        @Override
                        public void onSuccess(Result result) {
                            Product[] products = dataSource.getGson().fromJson(((Result.Success) result).getData().toString(), Product[].class);
                            ArrayList<Product> visitPlanDbs = new ArrayList<>();
                            visitPlanDbs.addAll(Arrays.asList(products));
                            productListMutableLiveData.setValue(visitPlanDbs);
                            new Thread() {
                                @Override
                                public void run() {
                                    List<Long> tmp = db.productDAO().insertAll(products);
                                }
                            }.start();

                            vmRepoInterface.setMessage(result.toString());
                            vmRepoInterface.getStatus().setValue(true);
                        }

                        @Override
                        public void onError(Result result) {
                            vmRepoInterface.setMessage(result.toString());
                            vmRepoInterface.getStatus().setValue(false);
                        }
                    });
                } else {
                    vmRepoInterface.getStatus().postValue(true);
                    vmRepoInterface.setMessage("Data berhasil didapatkan");
                    productListMutableLiveData.postValue(localData);
                }
            }
        }.start();
        return productListMutableLiveData;
    }

    public MutableLiveData<ArrayList<Product>> getFromCloud(){
        productListMutableLiveData = new MutableLiveData<>();
        dataSource.Connect(ConnectionHandler.get_method, "product", null, new NetworkCallback() {
            @Override
            public void onFinish() {

            }

            @Override
            public void onSuccess(Result result) {
                Product[] products = dataSource.getGson().fromJson(((Result.Success) result).getData().toString(), Product[].class);
                ArrayList<Product> visitPlanDbs = new ArrayList<>();
                visitPlanDbs.addAll(Arrays.asList(products));
                new Thread() {
                    @Override
                    public void run() {
                        List<Long> tmp = db.productDAO().insertAll(products);
                        productListMutableLiveData.postValue(visitPlanDbs);
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
        return productListMutableLiveData;
    }
}