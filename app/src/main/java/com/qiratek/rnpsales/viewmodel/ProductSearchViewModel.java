package com.qiratek.rnpsales.viewmodel;

import android.content.Context;

import androidx.lifecycle.LiveData;

import com.qiratek.rnpsales.model.datasource.local.AppDatabase;
import com.qiratek.rnpsales.model.datasource.network.BaseNetwork;
import com.qiratek.rnpsales.model.entity.Brand;
import com.qiratek.rnpsales.model.entity.Product;
import com.qiratek.rnpsales.model.entity.Satuan;
import com.qiratek.rnpsales.model.helper.SharedPreferenceHelper;
import com.qiratek.rnpsales.model.repository.BrandRepository;
import com.qiratek.rnpsales.model.repository.ProductRepository;
import com.qiratek.rnpsales.model.repository.SatuanRepository;

import java.util.ArrayList;


public class ProductSearchViewModel extends BaseViewModel {

    BrandRepository brandRepository;
    ProductRepository productRepository;
    SatuanRepository satuanRepository;
    LiveData<ArrayList<Brand>> brandLiveData;
    LiveData<ArrayList<Product>> productLiveData;
    LiveData<ArrayList<Satuan>> satuanLiveData;

    public ProductSearchViewModel(Context context) {
        brandRepository = BrandRepository.getInstance(BaseNetwork.getInstance(context), SharedPreferenceHelper.getInstance(context), this,
                AppDatabase.getInstance(context));
        productRepository = ProductRepository.getInstance(BaseNetwork.getInstance(context), SharedPreferenceHelper.getInstance(context), this,
                AppDatabase.getInstance(context));
        satuanRepository = SatuanRepository.getInstance(BaseNetwork.getInstance(context), SharedPreferenceHelper.getInstance(context), this,
                AppDatabase.getInstance(context));
    }

    public LiveData<ArrayList<Product>> getAllProduct(boolean isRefreshed){
        if(isRefreshed){
            productLiveData = productRepository.getFromCloud();
        } else{
            productLiveData = productRepository.getAllProduk();
        }
        if(productLiveData.getValue() == null || productLiveData.getValue().size() == 0) {
            loading.setValue(true);
        }
        return productLiveData;
    }

    public LiveData<ArrayList<Brand>> getAllBrand(boolean isRefreshed){
        brandLiveData = brandRepository.getAllBrand(isRefreshed);
        if(brandLiveData.getValue() == null || brandLiveData.getValue().size() == 0) {
            loading.setValue(true);
        }
        return brandLiveData;
    }

    public LiveData<ArrayList<Satuan>> getAllSatuan(boolean isRefreshed){
        satuanLiveData = satuanRepository.getAllSatuan(isRefreshed);
        if(satuanLiveData.getValue() == null || satuanLiveData.getValue().size() == 0) {
            loading.setValue(true);
        }
        return satuanLiveData;
    }


}
