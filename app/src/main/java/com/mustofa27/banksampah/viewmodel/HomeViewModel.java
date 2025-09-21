package com.mustofa27.banksampah.viewmodel;

import android.content.Context;


import androidx.lifecycle.LiveData;

import com.mustofa27.banksampah.model.datasource.local.AppDatabase;
import com.mustofa27.banksampah.model.datasource.network.BaseNetwork;
import com.mustofa27.banksampah.model.entity.Brand;
import com.mustofa27.banksampah.model.entity.City;
import com.mustofa27.banksampah.model.entity.Configuration;
import com.mustofa27.banksampah.model.entity.FeedOutlet;
import com.mustofa27.banksampah.model.entity.NewsClass;
import com.mustofa27.banksampah.model.entity.Outlet;
import com.mustofa27.banksampah.model.entity.Product;
import com.mustofa27.banksampah.model.entity.Satuan;
import com.mustofa27.banksampah.model.entity.Tipe;
import com.mustofa27.banksampah.model.entity.VisitPlanDb;
import com.mustofa27.banksampah.model.helper.SharedPreferenceHelper;
import com.mustofa27.banksampah.model.helper.TimerPreference;
import com.mustofa27.banksampah.model.repository.BrandRepository;
import com.mustofa27.banksampah.model.repository.CityRepository;
import com.mustofa27.banksampah.model.repository.ConfigRepository;
import com.mustofa27.banksampah.model.repository.MarketUpdateRepository;
import com.mustofa27.banksampah.model.repository.NewsRepository;
import com.mustofa27.banksampah.model.repository.OutletRepository;
import com.mustofa27.banksampah.model.repository.ProductRepository;
import com.mustofa27.banksampah.model.repository.SatuanRepository;
import com.mustofa27.banksampah.model.repository.TipeRepository;
import com.mustofa27.banksampah.model.repository.UserRepository;
import com.mustofa27.banksampah.model.repository.VisitPlanRepository;

import java.util.ArrayList;


public class HomeViewModel extends BaseViewModel {

    UserRepository userRepository;

    VisitPlanRepository visitPlanRepository;
    LiveData<ArrayList<VisitPlanDb>> visitPlanDbLiveData;
    TimerPreference timerPreference;
    OutletRepository outletRepository;
    TipeRepository tipeRepository;
    CityRepository cityRepository;
    BrandRepository brandRepository;
    ProductRepository productRepository;
    SatuanRepository satuanRepository;
    ConfigRepository configRepository;
    MarketUpdateRepository marketUpdateRepository;
    LiveData<ArrayList<Outlet>> outlets;
    LiveData<ArrayList<Tipe>> tipes;
    LiveData<ArrayList<City>> cities;
    LiveData<ArrayList<Brand>> brandLiveData;
    LiveData<ArrayList<Product>> productLiveData;
    LiveData<ArrayList<Satuan>> satuanLiveData;
    LiveData<Configuration> configurationLiveData;
    LiveData<ArrayList<FeedOutlet>> feedLiveData;

    NewsRepository newsRepository;
    LiveData<ArrayList<NewsClass>> newsLiveData;

    public HomeViewModel(Context context) {
        userRepository = UserRepository.getInstance(BaseNetwork.getInstance(context), SharedPreferenceHelper.getInstance(context),
                this, AppDatabase.getInstance(context));
        visitPlanRepository = VisitPlanRepository.getInstance(BaseNetwork.getInstance(context), SharedPreferenceHelper.getInstance(context),
                this, AppDatabase.getInstance(context));
        timerPreference = new TimerPreference(context);
        outletRepository = OutletRepository.getInstance(BaseNetwork.getInstance(context), SharedPreferenceHelper.getInstance(context), this,
                AppDatabase.getInstance(context));
        tipeRepository = TipeRepository.getInstance(BaseNetwork.getInstance(context), SharedPreferenceHelper.getInstance(context), this,
                AppDatabase.getInstance(context));
        cityRepository = CityRepository.getInstance(BaseNetwork.getInstance(context), SharedPreferenceHelper.getInstance(context), this,
                AppDatabase.getInstance(context));
        brandRepository = BrandRepository.getInstance(BaseNetwork.getInstance(context), SharedPreferenceHelper.getInstance(context), this,
                AppDatabase.getInstance(context));
        productRepository = ProductRepository.getInstance(BaseNetwork.getInstance(context), SharedPreferenceHelper.getInstance(context), this,
                AppDatabase.getInstance(context));
        satuanRepository = SatuanRepository.getInstance(BaseNetwork.getInstance(context), SharedPreferenceHelper.getInstance(context), this,
                AppDatabase.getInstance(context));
        configRepository = ConfigRepository.getInstance(BaseNetwork.getInstance(context), SharedPreferenceHelper.getInstance(context), this,
                AppDatabase.getInstance(context));
        newsRepository = NewsRepository.getInstance(BaseNetwork.getInstance(context), SharedPreferenceHelper.getInstance(context), this,
                AppDatabase.getInstance(context));
        marketUpdateRepository = MarketUpdateRepository.getInstance(BaseNetwork.getInstance(context), SharedPreferenceHelper.getInstance(context), this,
                AppDatabase.getInstance(context));
    }

    public LiveData<ArrayList<NewsClass>> getAllNews(){
        newsLiveData = newsRepository.getAllNewsClass();
        if(newsLiveData.getValue() == null || newsLiveData.getValue().size() == 0) {
            loading.setValue(true);
        }
        return newsLiveData;
    }

    public LiveData<ArrayList<FeedOutlet>> getAllFeed(){
        feedLiveData = marketUpdateRepository.getFromCloud();
        if(feedLiveData.getValue() == null || feedLiveData.getValue().size() == 0) {
            loading.setValue(true);
        }
        return feedLiveData;
    }

    public LiveData<ArrayList<VisitPlanDb>> getVisitPlanDbLiveData(){
        visitPlanDbLiveData = visitPlanRepository.getLastVisitPlanDb();
        if(visitPlanDbLiveData.getValue() == null || visitPlanDbLiveData.getValue().size() == 0){
            getLoading().setValue(true);
        }
        return visitPlanDbLiveData;
    }

    public LiveData<ArrayList<VisitPlanDb>> getVisitPlanLiveData(){
        return visitPlanDbLiveData;
    }

    public TimerPreference getTimerPreference() {
        return timerPreference;
    }

    public void setTimerPreference(TimerPreference timerPreference) {
        this.timerPreference = timerPreference;
    }

    public LiveData<ArrayList<Outlet>> getAllOutlet(){
        outlets = outletRepository.getAllOutletFromCloud();
        return outlets;
    }

    public LiveData<ArrayList<Tipe>> getAllTipe(){
        tipes = tipeRepository.getAllTipeFromCloud();
        return tipes;
    }

    public LiveData<ArrayList<City>> getAllCity(){
        cities = cityRepository.getAllCityFromCloud();
        return cities;
    }

    public LiveData<ArrayList<Product>> getAllProduct(){
        productLiveData = productRepository.getFromCloud();
        return productLiveData;
    }

    public LiveData<ArrayList<Brand>> getAllBrand(){
        brandLiveData = brandRepository.getAllBrand(true);
        return brandLiveData;
    }

    public LiveData<ArrayList<Satuan>> getAllSatuan(){
        satuanLiveData = satuanRepository.getAllSatuan(true);
        return satuanLiveData;
    }

    public LiveData<Configuration> getConfiguration(){
        configurationLiveData = configRepository.getConfigurationMutableLiveData(false);
        return configurationLiveData;
    }

    public LiveData<Configuration> getConfigurationLiveData() {
        return configurationLiveData;
    }

    public LiveData<ArrayList<Outlet>> getOutlets() {
        return outlets;
    }

    public LiveData<ArrayList<Tipe>> getTipes() {
        return tipes;
    }

    public LiveData<ArrayList<City>> getCities() {
        return cities;
    }

    public LiveData<ArrayList<Brand>> getBrandLiveData() {
        return brandLiveData;
    }

    public LiveData<ArrayList<Product>> getProductLiveData() {
        return productLiveData;
    }

    public LiveData<ArrayList<Satuan>> getSatuanLiveData() {
        return satuanLiveData;
    }

    public LiveData<ArrayList<FeedOutlet>> getFeedLiveData() {
        return feedLiveData;
    }

    public LiveData<ArrayList<NewsClass>> getNewsLiveData() {
        return newsLiveData;
    }
}
