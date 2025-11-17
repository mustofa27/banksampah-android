package com.mustofa27.banksampah.model.repository;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.mustofa27.banksampah.model.datasource.Result;
import com.mustofa27.banksampah.model.datasource.local.AppDatabase;
import com.mustofa27.banksampah.model.datasource.network.BaseNetwork;
import com.mustofa27.banksampah.model.datasource.network.ConnectionHandler;
import com.mustofa27.banksampah.model.datasource.network.NetworkCallback;
import com.mustofa27.banksampah.model.entity.Product;
import com.mustofa27.banksampah.model.entity.ProductPagination;
import com.mustofa27.banksampah.model.entity.Saving;
import com.mustofa27.banksampah.model.entity.SavingPagination;
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
    int last_page, current_page;

    private ProductRepository(BaseNetwork baseNetwork, SharedPreferenceHelper sharedPreferenceHelper, VMRepoInterface vmRepoInterface, AppDatabase db) {
        super(baseNetwork, sharedPreferenceHelper, vmRepoInterface, db);
    }

    public static ProductRepository getInstance(BaseNetwork baseNetwork, SharedPreferenceHelper sharedPreferenceHelper, VMRepoInterface vmRepoInterface, AppDatabase db) {
        if (instance == null) {
            instance = new ProductRepository(baseNetwork, sharedPreferenceHelper, vmRepoInterface, db);
        } else{
            instance.vmRepoInterface = vmRepoInterface;
        }
        instance.last_page = 0;
        return instance;
    }

    public LiveData<ArrayList<Product>> getData(int page){
        if(page == 1){
            productListMutableLiveData = new MutableLiveData<>();
        }
        if(page <= last_page || last_page == 0) {
            dataSource.Connect(ConnectionHandler.get_method, "product?page=" + page, null, new NetworkCallback() {
                @Override
                public void onFinish() {

                }

                @Override
                public void onSuccess(Result result) {
                    ProductPagination productPagination = dataSource.getGson().fromJson(((Result.Success) result).getData().toString(), ProductPagination.class);
                    last_page = productPagination.getLast_page();
                    current_page = productPagination.getCurrent_page();
                    productListMutableLiveData.setValue(productPagination.getData());
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
        return productListMutableLiveData;
    }

    public MutableLiveData<ArrayList<Product>> getAllProduk(){
        productListMutableLiveData = new MutableLiveData<>();
        new Thread() {
            @Override
            public void run() {
                ArrayList<Product> localData = new ArrayList<>();
                localData.addAll(db.productDAO().getAll());
                if (localData.size() == 0) {
                    dataSource.Connect(ConnectionHandler.get_method, "product", null, new NetworkCallback() {
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
                ProductPagination productPagination = dataSource.getGson().fromJson(((Result.Success) result).getData().toString(), ProductPagination.class);
                new Thread() {
                    @Override
                    public void run() {
                        List<Long> tmp = db.productDAO().insertAll(productPagination.getData().toArray(new Product[0]));
                        productListMutableLiveData.postValue(productPagination.getData());
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
    public boolean isNextPageAvailable(){
        return current_page < last_page;
    }
}