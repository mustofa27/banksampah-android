package com.qiratek.rnpsales.viewmodel;

import android.content.Context;


import com.qiratek.rnpsales.model.datasource.local.AppDatabase;
import com.qiratek.rnpsales.model.datasource.network.BaseNetwork;
import com.qiratek.rnpsales.model.datasource.network.MultipartFile;
import com.qiratek.rnpsales.model.entity.VisitReport;
import com.qiratek.rnpsales.model.helper.SharedPreferenceHelper;
import com.qiratek.rnpsales.model.repository.VisitReportRepository;


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
