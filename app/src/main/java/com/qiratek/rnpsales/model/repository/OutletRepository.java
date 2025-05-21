package com.qiratek.rnpsales.model.repository;


import androidx.lifecycle.MutableLiveData;

import com.qiratek.rnpsales.model.datasource.Result;
import com.qiratek.rnpsales.model.datasource.local.AppDatabase;
import com.qiratek.rnpsales.model.datasource.network.BaseNetwork;
import com.qiratek.rnpsales.model.datasource.network.ConnectionHandler;
import com.qiratek.rnpsales.model.datasource.network.MultipartFile;
import com.qiratek.rnpsales.model.datasource.network.NetworkCallback;
import com.qiratek.rnpsales.model.entity.Outlet;
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
public class OutletRepository extends BaseRepository {

    private static volatile OutletRepository instance;
    private MutableLiveData<ArrayList<Outlet>> outletMutableLiveData;
    private MutableLiveData<Outlet> createdOutletMutableLiveData;
    private User user;

    private OutletRepository(BaseNetwork baseNetwork, SharedPreferenceHelper sharedPreferenceHelper, VMRepoInterface vmRepoInterface, AppDatabase db) {
        super(baseNetwork, sharedPreferenceHelper, vmRepoInterface, db);
        outletMutableLiveData = new MutableLiveData<>();
        createdOutletMutableLiveData = new MutableLiveData<>();
        user = baseNetwork.getGson().fromJson(sharedPreferenceHelper.getPreference(User.table), User.class);
    }

    public static OutletRepository getInstance(BaseNetwork baseNetwork, SharedPreferenceHelper sharedPreferenceHelper, VMRepoInterface vmRepoInterface, AppDatabase db) {
        if (instance == null) {
            instance = new OutletRepository(baseNetwork, sharedPreferenceHelper, vmRepoInterface, db);
        }
        instance.vmRepoInterface = vmRepoInterface;
        return instance;
    }

    public MutableLiveData<ArrayList<Outlet>> getAllOutlet(){
        // handle login
        new Thread() {
            @Override
            public void run() {
                ArrayList<Outlet> localData = new ArrayList<>();
                localData.addAll(db.outletDAO().getAll());
                if (localData.size() == 0) {
                    try {
                        JSONObject jsonObject = new JSONObject();
                        jsonObject.put("user_id", user.getId());
                        dataSource.Connect(ConnectionHandler.post_method, "getOutlet", jsonObject, new NetworkCallback() {
                            @Override
                            public void onFinish() {

                            }

                            @Override
                            public void onSuccess(Result result) {
                                Outlet[] outletArray = dataSource.getGson().fromJson(((Result.Success) result).getData().toString(), Outlet[].class);
                                ArrayList<Outlet> outlets = new ArrayList<>();
                                outlets.addAll(Arrays.asList(outletArray));
                                outletMutableLiveData.setValue(outlets);
                                new Thread() {
                                    @Override
                                    public void run() {
                                        List<Long> tmp = db.outletDAO().insertAll(outletArray);
                                    }
                                }.start();

                                vmRepoInterface.setMessage(result.toString());
                                vmRepoInterface.getStatus().setValue(true);
                            }

                            @Override
                            public void onError(Result result) {
                                vmRepoInterface.setMessage(result.toString());
                                vmRepoInterface.getStatus().setValue(false);
                                outletMutableLiveData.postValue(localData);
                            }
                        });
                    } catch (JSONException e) {
                        vmRepoInterface.setMessage(e.getMessage());
                        vmRepoInterface.getStatus().setValue(false);
                        outletMutableLiveData.postValue(localData);
                    }
                } else {
                    vmRepoInterface.getStatus().postValue(true);
                    vmRepoInterface.setMessage("Data berhasil didapatkan");
                    outletMutableLiveData.postValue(localData);
                }
            }
        }.start();
        return outletMutableLiveData;
    }

    public MutableLiveData<ArrayList<Outlet>> getAllOutletFromCloud(){
        // handle login
        MutableLiveData<ArrayList<Outlet>> data= new MutableLiveData<>();
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("user_id", user.getId());
            dataSource.Connect(ConnectionHandler.post_method, "getOutlet", jsonObject, new NetworkCallback() {
                @Override
                public void onFinish() {

                }

                @Override
                public void onSuccess(Result result) {
                    Outlet[] outletArray = dataSource.getGson().fromJson(((Result.Success) result).getData().toString(), Outlet[].class);
                    ArrayList<Outlet> outlets = new ArrayList<>();
                    outlets.addAll(Arrays.asList(outletArray));
                    new Thread() {
                        @Override
                        public void run() {
                            List<Long> tmp = db.outletDAO().insertAll(outletArray);
                            data.postValue(outlets);
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
        } catch (JSONException e) {
            vmRepoInterface.setMessage(e.getMessage());
            vmRepoInterface.getStatus().setValue(false);
        }
        return data;
    }

    public MutableLiveData<Outlet> createOutlet(Map<String , Object> param, Map<String , MultipartFile> paramFile){
        // handle login
        createdOutletMutableLiveData = new MutableLiveData<>();
        if(createdOutletMutableLiveData.getValue() == null) {
            param.put("user_id", String.valueOf(user.getId()));
            dataSource.Connect(ConnectionHandler.post_method, "setOutlet", param, paramFile, new NetworkCallback() {
                @Override
                public void onFinish() {

                }

                @Override
                public void onSuccess(Result result) {
//                                try {
                    Outlet outletArray = dataSource.getGson().fromJson(((Result.Success) result).getData().toString(), Outlet.class);
                    createdOutletMutableLiveData.setValue(outletArray);
                    new Thread() {
                        @Override
                        public void run() {
                            List<Long> tmp = db.outletDAO().insertAll(outletArray);
                        }
                    }.start();
                    vmRepoInterface.setMessage(result.toString());
                    vmRepoInterface.getStatus().setValue(true);
//                                } catch (JSONException e) {
//                                    vmRepoInterface.setMessage(e.getMessage());
//                                    vmRepoInterface.getStatus().setValue(false);
//                                }
                }

                @Override
                public void onError(Result result) {
                    vmRepoInterface.setMessage(result.toString());
                    vmRepoInterface.getStatus().setValue(false);
                }
            });
        }
        return createdOutletMutableLiveData;
    }

    public MutableLiveData<Outlet> getOutlet(String id){
        createdOutletMutableLiveData = new MutableLiveData<>();
        if(createdOutletMutableLiveData.getValue() == null) {
            new Thread() {
                @Override
                public void run() {
                    Outlet outlet = db.outletDAO().getOutlet(id);
                    vmRepoInterface.getStatus().postValue(true);
                    vmRepoInterface.setMessage("Data berhasil didapatkan");
                    createdOutletMutableLiveData.postValue(outlet);
                }
            }.start();
        }
        return createdOutletMutableLiveData;
    }
}