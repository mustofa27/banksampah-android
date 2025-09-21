package com.mustofa27.banksampah.model.repository;

import androidx.lifecycle.MutableLiveData;

import com.mustofa27.banksampah.model.datasource.Result;
import com.mustofa27.banksampah.model.datasource.local.AppDatabase;
import com.mustofa27.banksampah.model.datasource.network.BaseNetwork;
import com.mustofa27.banksampah.model.datasource.network.ConnectionHandler;
import com.mustofa27.banksampah.model.datasource.network.MultipartFile;
import com.mustofa27.banksampah.model.datasource.network.NetworkCallback;
import com.mustofa27.banksampah.model.entity.User;
import com.mustofa27.banksampah.model.entity.VisitReport;
import com.mustofa27.banksampah.model.helper.SharedPreferenceHelper;
import com.mustofa27.banksampah.viewmodel.VMRepoInterface;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Class that requests authentication and user information from the remote data source and
 * maintains an in-memory cache of login status and user credentials information.
 */
public class VisitReportRepository extends BaseRepository {

    private static volatile VisitReportRepository instance;
    private User user;

    private VisitReportRepository(BaseNetwork baseNetwork, SharedPreferenceHelper sharedPreferenceHelper, VMRepoInterface vmRepoInterface, AppDatabase db) {
        super(baseNetwork, sharedPreferenceHelper, vmRepoInterface, db);
    }

    public static VisitReportRepository getInstance(BaseNetwork baseNetwork, SharedPreferenceHelper sharedPreferenceHelper, VMRepoInterface vmRepoInterface, AppDatabase db) {
        if (instance == null) {
            instance = new VisitReportRepository(baseNetwork, sharedPreferenceHelper, vmRepoInterface, db);
        }
        if(instance.vmRepoInterface != vmRepoInterface) {
            instance.vmRepoInterface = vmRepoInterface;
        }
        instance.user = baseNetwork.getUser();
        return instance;
    }

    public void setVisitBukti(Map<String, Object> param, Map<String, MultipartFile> paramFile){
        param.put("user_id", user.getId());
        dataSource.Connect(ConnectionHandler.post_method, "setVisitBukti", param, paramFile, new NetworkCallback() {
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
    }

    public MutableLiveData<Boolean> submitSavedBukti(Map<String, Object> param, Map<String, MultipartFile> paramFile, VisitReport visitReport){
        MutableLiveData<Boolean> statusUpload = new MutableLiveData<>();
        param.put("user_id", user.getId());
        dataSource.Connect(ConnectionHandler.post_method, "setVisitBukti", param, paramFile, new NetworkCallback() {
            @Override
            public void onFinish() {

            }

            @Override
            public void onSuccess(Result result) {
                new Thread() {
                    @Override
                    public void run() {
                        db.visitReportDAO().delete(visitReport);
                        vmRepoInterface.setMessage(result.toString());
                        statusUpload.postValue(true);
                    }
                }.start();
            }

            @Override
            public void onError(Result result) {
                vmRepoInterface.setMessage(result.toString());
                statusUpload.setValue(false);
            }
        });
        return statusUpload;
    }

    public void saveVisitReport(VisitReport visitReport){
        new Thread() {
            @Override
            public void run() {
                List<Long> tmp = db.visitReportDAO().insertAll(visitReport);
                vmRepoInterface.setMessage("Berhasil menyimpan data");
                vmRepoInterface.getStatus().postValue(true);
            }
        }.start();
    }

    public MutableLiveData<ArrayList<VisitReport>> getSavedData(){
        MutableLiveData<ArrayList<VisitReport>> data = new MutableLiveData<>();
        new Thread() {
            @Override
            public void run() {
                ArrayList<VisitReport> localData = new ArrayList<>();
                localData.addAll(db.visitReportDAO().getAll());
                vmRepoInterface.getStatus().postValue(true);
                vmRepoInterface.setMessage("Data berhasil didapatkan");
                data.postValue(localData);
            }
        }.start();
        return data;
    }
}