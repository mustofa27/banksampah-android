package com.mustofa27.banksampah.viewmodel;

import android.content.Context;


import com.mustofa27.banksampah.model.datasource.local.AppDatabase;
import com.mustofa27.banksampah.model.datasource.network.BaseNetwork;
import com.mustofa27.banksampah.model.datasource.network.MultipartFile;
import com.mustofa27.banksampah.model.entity.VisitReport;
import com.mustofa27.banksampah.model.helper.SharedPreferenceHelper;
import com.mustofa27.banksampah.model.repository.VisitReportRepository;


import java.util.Map;


public class VisitReportViewModel extends BaseViewModel {

    VisitReportRepository visitReportRepository;

    public VisitReportViewModel(Context context) {
        visitReportRepository = VisitReportRepository.getInstance(BaseNetwork.getInstance(context), SharedPreferenceHelper.getInstance(context), this,
                AppDatabase.getInstance(context));
    }

    public void setVisitBukti(Map<String, Object> param, Map<String, MultipartFile> paramFile){
        loading.setValue(true);
        visitReportRepository.setVisitBukti(param, paramFile);
    }

    public void saveVisitBukti(VisitReport visitReport){
        loading.setValue(true);
        visitReportRepository.saveVisitReport(visitReport);
    }

}
