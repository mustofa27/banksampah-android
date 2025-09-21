package com.mustofa27.banksampah.model.repository;

import androidx.lifecycle.MutableLiveData;

import com.mustofa27.banksampah.model.datasource.Result;
import com.mustofa27.banksampah.model.datasource.local.AppDatabase;
import com.mustofa27.banksampah.model.datasource.network.BaseNetwork;
import com.mustofa27.banksampah.model.datasource.network.ConnectionHandler;
import com.mustofa27.banksampah.model.datasource.network.MultipartFile;
import com.mustofa27.banksampah.model.datasource.network.NetworkCallback;
import com.mustofa27.banksampah.model.entity.Outlet;
import com.mustofa27.banksampah.model.entity.User;
import com.mustofa27.banksampah.model.entity.VisitPlanDb;
import com.mustofa27.banksampah.model.helper.SharedPreferenceHelper;
import com.mustofa27.banksampah.viewmodel.VMRepoInterface;

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
public class VisitPlanRepository extends BaseRepository {

    private static volatile VisitPlanRepository instance;
    private MutableLiveData<ArrayList<VisitPlanDb>> visitPlanListMutableLiveData;
    private MutableLiveData<ArrayList<VisitPlanDb>> lastvisitPlanListMutableLiveData;
    private MutableLiveData<VisitPlanDb> visitPlanMutableLiveData;
    private User user;

    private VisitPlanRepository(BaseNetwork baseNetwork, SharedPreferenceHelper sharedPreferenceHelper, VMRepoInterface vmRepoInterface, AppDatabase db) {
        super(baseNetwork, sharedPreferenceHelper, vmRepoInterface, db);
        visitPlanListMutableLiveData = new MutableLiveData<>();
        visitPlanMutableLiveData = new MutableLiveData<>();
    }

    public static VisitPlanRepository getInstance(BaseNetwork baseNetwork, SharedPreferenceHelper sharedPreferenceHelper, VMRepoInterface vmRepoInterface, AppDatabase db) {
        if (instance == null) {
            instance = new VisitPlanRepository(baseNetwork, sharedPreferenceHelper, vmRepoInterface, db);
        }
        if(instance.vmRepoInterface != vmRepoInterface) {
            instance.vmRepoInterface = vmRepoInterface;
            instance.visitPlanMutableLiveData = new MutableLiveData<>();
            instance.visitPlanListMutableLiveData =  new MutableLiveData<>();
        }
        instance.user = baseNetwork.getUser();
        return instance;
    }

    public MutableLiveData<ArrayList<VisitPlanDb>> getAllVisitPlanDb(){
        if(visitPlanListMutableLiveData.getValue() == null || visitPlanListMutableLiveData.getValue().size() == 0) {
            new Thread() {
                @Override
                public void run() {
                    ArrayList<VisitPlanDb> localData = new ArrayList<>();
                    localData.addAll(db.visitPlanDAO().getAll());
                    if (localData.size() == 0) {
                        try {
                            JSONObject jsonObject = new JSONObject();
                            jsonObject.put("user_id", user.getId());
                            dataSource.Connect(ConnectionHandler.post_method, "getCheckoutUserVisit", jsonObject, new NetworkCallback() {
                                @Override
                                public void onFinish() {

                                }

                                @Override
                                public void onSuccess(Result result) {
                                    VisitPlanDb[] visitplanArray = dataSource.getGson().fromJson(((Result.Success) result).getData().toString(), VisitPlanDb[].class);
                                    ArrayList<VisitPlanDb> visitPlanDbs = new ArrayList<>();
                                    visitPlanDbs.addAll(Arrays.asList(visitplanArray));
                                    visitPlanListMutableLiveData.setValue(visitPlanDbs);
                                    new Thread() {
                                        @Override
                                        public void run() {
                                            List<Long> tmp = db.visitPlanDAO().insertAll(visitplanArray);
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
                        } catch (JSONException e) {
                            vmRepoInterface.setMessage(e.getMessage());
                            vmRepoInterface.getStatus().setValue(false);
                        }
                    } else {
                        vmRepoInterface.getStatus().postValue(true);
                        vmRepoInterface.setMessage("Data berhasil didapatkan");
                        visitPlanListMutableLiveData.postValue(localData);
                    }
                }
            }.start();
        }
        return visitPlanListMutableLiveData;
    }

    public MutableLiveData<ArrayList<VisitPlanDb>> getUnCheckoutVisit(){
        visitPlanListMutableLiveData = new MutableLiveData<>();
        if(visitPlanListMutableLiveData.getValue() == null || visitPlanListMutableLiveData.getValue().size() == 0) {
            new Thread() {
                @Override
                public void run() {
                    ArrayList<VisitPlanDb> localData = new ArrayList<>();
                    List<VisitPlanDb> data = db.visitPlanDAO().getAll();
                    for (VisitPlanDb visitPlanDb : data) {
                        visitPlanDb.setOutlets(dataSource.getGson().fromJson(visitPlanDb.getOutletJson(), Outlet.class));
                        localData.add(visitPlanDb);
                    }
                    vmRepoInterface.getStatus().postValue(true);
                    vmRepoInterface.setMessage("Data berhasil didapatkan");
                    visitPlanListMutableLiveData.postValue(localData);
                }
            }.start();
        }
        return visitPlanListMutableLiveData;
    }

    public MutableLiveData<ArrayList<VisitPlanDb>> getSavedVisit(){
        visitPlanListMutableLiveData = new MutableLiveData<>();
        if(visitPlanListMutableLiveData.getValue() == null || visitPlanListMutableLiveData.getValue().size() == 0) {
            new Thread() {
                @Override
                public void run() {
                    ArrayList<VisitPlanDb> localData = new ArrayList<>();
                    localData.addAll(db.visitPlanDAO().getAllForSync());
                    vmRepoInterface.getStatus().postValue(true);
                    vmRepoInterface.setMessage("Data berhasil didapatkan");
                    visitPlanListMutableLiveData.postValue(localData);
                }
            }.start();
        }
        return visitPlanListMutableLiveData;
    }

    public MutableLiveData<VisitPlanDb> submitVisit(Map<String, Object> param, Map<String, MultipartFile> paramFile){
        param.put("user_id", user.getId());
        dataSource.Connect(ConnectionHandler.post_method, "submitCheckIn", param, paramFile, new NetworkCallback() {
            @Override
            public void onFinish() {

            }

            @Override
            public void onSuccess(Result result) {
                VisitPlanDb visitPlan = dataSource.getGson().fromJson(((Result.Success) result).getData().toString(), VisitPlanDb.class);
                visitPlan.setOutletJson(dataSource.getGson().toJson(visitPlan.getOutlets()));
                new Thread() {
                    @Override
                    public void run() {
                        if(visitPlan.getDate_checkout() == null || visitPlan.getDate_checkout().length() == 0) {
                            List<Long> tmp = db.visitPlanDAO().insertAll(visitPlan);
                        }
                        visitPlanMutableLiveData.postValue(visitPlan);
                        vmRepoInterface.setMessage(result.toString());
                        vmRepoInterface.getStatus().postValue(true);
                    }
                }.start();
            }

            @Override
            public void onError(Result result) {
                vmRepoInterface.setMessage(result.toString());
                vmRepoInterface.getStatus().setValue(false);
            }
        });
        return visitPlanMutableLiveData;
    }

    public MutableLiveData<VisitPlanDb> submitDraftVisit(Map<String, Object> param, Map<String, MultipartFile> paramFile){
        param.put("user_id", user.getId());
        visitPlanMutableLiveData = new MutableLiveData<>();
        dataSource.Connect(ConnectionHandler.post_method, "submitCheckInDraft", param, paramFile, new NetworkCallback() {
            @Override
            public void onFinish() {

            }

            @Override
            public void onSuccess(Result result) {
                VisitPlanDb visitPlan = dataSource.getGson().fromJson(((Result.Success) result).getData().toString(), VisitPlanDb.class);
                visitPlanMutableLiveData.setValue(visitPlan);
                vmRepoInterface.setMessage(result.toString());
            }

            @Override
            public void onError(Result result) {
                vmRepoInterface.setMessage(result.toString());
            }
        });
        return visitPlanMutableLiveData;
    }

    public MutableLiveData<VisitPlanDb> saveVisit(VisitPlanDb visitPlanDb){
        new Thread() {
            @Override
            public void run() {
                List<Long> tmp = db.visitPlanDAO().insertAll(visitPlanDb);
                visitPlanMutableLiveData.postValue(visitPlanDb);
                vmRepoInterface.setMessage("Berhasil menyimpan data");
                vmRepoInterface.getStatus().postValue(true);
            }
        }.start();
        return visitPlanMutableLiveData;
    }

    public MutableLiveData<VisitPlanDb> submitCheckout(JSONObject jsonObject, VisitPlanDb visitPlanDb){
        try {
            jsonObject.put("user_id", user.getId());
            dataSource.Connect(ConnectionHandler.post_method, "submitCheckOut", jsonObject, new NetworkCallback() {
                @Override
                public void onFinish() {

                }

                @Override
                public void onSuccess(Result result) {
                    VisitPlanDb visitPlan = dataSource.getGson().fromJson(((Result.Success) result).getData().toString(), VisitPlanDb.class);
                    new Thread() {
                        @Override
                        public void run() {
                            db.visitPlanDAO().delete(visitPlanDb);
                            vmRepoInterface.setMessage(result.toString());
                            vmRepoInterface.getStatus().postValue(true);
                            visitPlanMutableLiveData.postValue(visitPlan);
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
        return visitPlanMutableLiveData;
    }

    public MutableLiveData<VisitPlanDb> submitSavedCheckout(JSONObject jsonObject, VisitPlanDb visitPlanDb){
        visitPlanMutableLiveData = new MutableLiveData<>();
        try {
            jsonObject.put("user_id", user.getId());
            dataSource.Connect(ConnectionHandler.post_method, "submitCheckOutDraft", jsonObject, new NetworkCallback() {
                @Override
                public void onFinish() {

                }

                @Override
                public void onSuccess(Result result) {
                    VisitPlanDb visitPlan = dataSource.getGson().fromJson(((Result.Success) result).getData().toString(), VisitPlanDb.class);
                    new Thread() {
                        @Override
                        public void run() {
                            db.visitPlanDAO().delete(visitPlanDb);
                            vmRepoInterface.setMessage(result.toString());
                            visitPlanMutableLiveData.postValue(visitPlan);
                        }
                    }.start();
                }

                @Override
                public void onError(Result result) {
                    vmRepoInterface.setMessage(result.toString());
                }
            });
        } catch (JSONException e) {
            vmRepoInterface.setMessage(e.getMessage());
        }
        return visitPlanMutableLiveData;
    }

    public MutableLiveData<ArrayList<VisitPlanDb>> getLastVisitPlanDb(){
        if(lastvisitPlanListMutableLiveData == null){
            lastvisitPlanListMutableLiveData = new MutableLiveData<>();
        }
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("user_id", user.getId());
            dataSource.Connect(ConnectionHandler.post_method, "getVisitToday", jsonObject, new NetworkCallback() {
                @Override
                public void onFinish() {

                }

                @Override
                public void onSuccess(Result result) {
                    VisitPlanDb[] visitplanArray = dataSource.getGson().fromJson(((Result.Success) result).getData().toString(), VisitPlanDb[].class);
                    ArrayList<VisitPlanDb> visitPlanDbs = new ArrayList<>();
                    visitPlanDbs.addAll(Arrays.asList(visitplanArray));
                    lastvisitPlanListMutableLiveData.setValue(visitPlanDbs);
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
        return lastvisitPlanListMutableLiveData;
    }
}