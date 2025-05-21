package com.qiratek.rnpsales.viewmodel;

import android.content.Context;
import android.net.Uri;

import androidx.lifecycle.LiveData;
import androidx.navigation.NavController;

import com.qiratek.rnpsales.model.datasource.local.AppDatabase;
import com.qiratek.rnpsales.model.datasource.network.BaseNetwork;
import com.qiratek.rnpsales.model.datasource.network.MultipartFile;
import com.qiratek.rnpsales.model.entity.City;
import com.qiratek.rnpsales.model.entity.Distributor;
import com.qiratek.rnpsales.model.entity.Outlet;
import com.qiratek.rnpsales.model.entity.Tipe;
import com.qiratek.rnpsales.model.helper.SharedPreferenceHelper;
import com.qiratek.rnpsales.model.repository.CityRepository;
import com.qiratek.rnpsales.model.repository.DistributorRepository;
import com.qiratek.rnpsales.model.repository.OutletRepository;
import com.qiratek.rnpsales.model.repository.TipeRepository;

import java.util.ArrayList;
import java.util.Map;


public class RegisterOutletViewModel extends BaseViewModel {

    private OutletRepository outletRepository;
    private TipeRepository tipeRepository;
    private CityRepository cityRepository;
    private DistributorRepository distributorRepository;
    private LiveData<Outlet> outlet;
    private LiveData<ArrayList<Tipe>> tipes;
    private LiveData<ArrayList<City>> cities;
    private LiveData<ArrayList<Distributor>> distributors;
    private Map<String, Object> param;
    private Map<String, MultipartFile> fileParam;
    private NavController navController;
    private Uri selectedImage;


    public RegisterOutletViewModel(Context context) {
        outletRepository = OutletRepository.getInstance(BaseNetwork.getInstance(context), SharedPreferenceHelper.getInstance(context), this,
                AppDatabase.getInstance(context));
        tipeRepository = TipeRepository.getInstance(BaseNetwork.getInstance(context), SharedPreferenceHelper.getInstance(context), this,
                AppDatabase.getInstance(context));
        cityRepository = CityRepository.getInstance(BaseNetwork.getInstance(context), SharedPreferenceHelper.getInstance(context), this,
                AppDatabase.getInstance(context));
        distributorRepository = DistributorRepository.getInstance(BaseNetwork.getInstance(context), SharedPreferenceHelper.getInstance(context), this,
                AppDatabase.getInstance(context));
    }

    public LiveData<Outlet> createOutlet(){
        loading.setValue(true);
        return outletRepository.createOutlet(param, fileParam);
    }

    public LiveData<ArrayList<Tipe>> getAllTipe(){
        tipes = tipeRepository.getAllTipe();
        if(tipes.getValue() == null || tipes.getValue().size() == 0) {
            loading.setValue(true);
        }
        return tipes;
    }

    public LiveData<ArrayList<City>> getAllCity(){
        cities = cityRepository.getAllCity();
        if(cities.getValue() == null || cities.getValue().size() == 0) {
            loading.setValue(true);
        }
        return cities;
    }

    public LiveData<ArrayList<Distributor>> getAllDist(){
        distributors = distributorRepository.getAllDist();
        if(distributors.getValue() == null || distributors.getValue().size() == 0) {
            loading.setValue(true);
        }
        return distributors;
    }

    public NavController getNavController() {
        return navController;
    }

    public void setNavController(NavController navController) {
        this.navController = navController;
    }

    public Map<String, Object > getParam() {
        return param;
    }

    public void setParam(Map<String, Object > param) {
        this.param = param;
    }

    public Map<String, MultipartFile> getFileParam() {
        return fileParam;
    }

    public void setFileParam(Map<String, MultipartFile> fileParam) {
        this.fileParam = fileParam;
    }

    public Uri getSelectedImage() {
        return selectedImage;
    }

    public void setSelectedImage(Uri selectedImage) {
        this.selectedImage = selectedImage;
    }
}
