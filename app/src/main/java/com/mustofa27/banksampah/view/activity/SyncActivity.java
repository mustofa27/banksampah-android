package com.mustofa27.banksampah.view.activity;

import androidx.appcompat.app.ActionBar;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mustofa27.banksampah.R;
import com.mustofa27.banksampah.databinding.ActivitySyncBinding;
import com.mustofa27.banksampah.model.datasource.network.MultipartFile;
import com.mustofa27.banksampah.model.entity.BillingData;
import com.mustofa27.banksampah.model.entity.Outlet;
import com.mustofa27.banksampah.model.entity.TakeOrderData;
import com.mustofa27.banksampah.model.entity.VisitPlanDb;
import com.mustofa27.banksampah.view.BaseActivity;
import com.mustofa27.banksampah.view.adapter.AdapterCallback;
import com.mustofa27.banksampah.view.adapter.GenericRecyclerAdapter;
import com.mustofa27.banksampah.viewmodel.BaseViewModel;
import com.mustofa27.banksampah.viewmodel.CustomViewModelFactory;
import com.mustofa27.banksampah.viewmodel.SyncViewModel;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class SyncActivity extends BaseActivity {

    ActivitySyncBinding binding;
    SyncViewModel viewModel;
    ArrayList<Outlet> outlets;
    ArrayList<VisitPlanDb> visitPlanDbs;
    ArrayList<BillingData> billingDatas;
    ArrayList<TakeOrderData> takeOrderData;
    GenericRecyclerAdapter checkInAdapter, checkOutAdapter, billingAdapter, takeOrderAdapter;
    int failed = 0, total = 0, iteratorVisit, iteratorBilling, iteratorOrder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySyncBinding.inflate(getLayoutInflater());
        viewModel = new ViewModelProvider(this, new CustomViewModelFactory(this)).get(SyncViewModel.class);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.custom_bar_no_icon);
        ((TextView) getSupportActionBar().getCustomView().findViewById(R.id.title)).setText("Sync Data");
        ((LinearLayout) getSupportActionBar().getCustomView().findViewById(R.id.back)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        setContentView(binding.getRoot());
        outlets = new ArrayList<>();
        visitPlanDbs = new ArrayList<>();
        billingDatas = new ArrayList<>();
        takeOrderData = new ArrayList<>();
        checkInAdapter = new GenericRecyclerAdapter(visitPlanDbs, R.layout.check_in_out_item, new AdapterCallback() {
            @Override
            public void bindView(View view, Object object) {
                VisitPlanDb current = (VisitPlanDb) object;
                TextView name = view.findViewById(R.id.outlet_name);
                TextView titleTanggal = view.findViewById(R.id.text_date);
                TextView tanggal = view.findViewById(R.id.date);
                Outlet currentOutlet = searchOutlet(current.getOutlet_id());
                name.setText(currentOutlet == null ? "Outlet telah dihapus" : currentOutlet.getNm_outlet());
                titleTanggal.setText("Tanggal Checkin :");
                tanggal.setText(getDateTime(current.getDate_visit()));
                view.findViewById(R.id.delete).setVisibility(View.GONE);
            }

            @Override
            public View.OnClickListener onClickItem(Object object) {
                return null;
            }
        });
        checkOutAdapter = new GenericRecyclerAdapter(visitPlanDbs, R.layout.check_in_out_item, new AdapterCallback() {
            @Override
            public void bindView(View view, Object object) {
                VisitPlanDb current = (VisitPlanDb) object;
                TextView name = view.findViewById(R.id.outlet_name);
                TextView titleTanggal = view.findViewById(R.id.text_date);
                TextView tanggal = view.findViewById(R.id.date);
                Outlet currentOutlet = searchOutlet(current.getOutlet_id());
                name.setText(currentOutlet == null ? "Outlet telah dihapus" : currentOutlet.getNm_outlet());
                titleTanggal.setText("Tanggal Checkout :");
                tanggal.setText(getDateTime(current.getDate_checkout()));
                view.findViewById(R.id.delete).setVisibility(View.GONE);
            }

            @Override
            public View.OnClickListener onClickItem(Object object) {
                return null;
            }
        });
        billingAdapter = new GenericRecyclerAdapter(billingDatas, R.layout.history_billing_item, new AdapterCallback() {
            @Override
            public void bindView(View view, Object object) {
                BillingData current = (BillingData) object;
                TextView outletName = view.findViewById(R.id.tv_outlet);
                TextView note = view.findViewById(R.id.tv_note);
                TextView input_date = view.findViewById(R.id.tv_input_date);
                TextView cash = view.findViewById(R.id.tv_cash);
                TextView transfer = view.findViewById(R.id.tv_transfer);
                TextView nominal_giro = view.findViewById(R.id.tv_nominal_giro);
                TextView no_giro = view.findViewById(R.id.tv_no_giro);
                TextView due_date = view.findViewById(R.id.tv_due_date);
                TextView bank = view.findViewById(R.id.tv_bank);
                Outlet currentOutlet = searchOutlet(current.getOutlet_id());
                outletName.setText(currentOutlet == null ? "Outlet telah dihapus" : currentOutlet.getNm_outlet());
                note.setText(current.getNote());
                input_date.setText(isStringNotEmpty(current.getCreated_at()) ? getDate(current.getCreated_at()) : "-");

                if (current.getCash_value() > 0) {
                    cash.setText(" Rp. " + NumberFormat.getNumberInstance(Locale.US).format(current.getCash_value()));
                } else {
                    cash.setVisibility(View.GONE);
                }

                if (current.getTransfer_value() > 0) {
                    transfer.setText(" Rp. " + NumberFormat.getNumberInstance(Locale.US).format(current.getTransfer_value()));
                } else {
                    transfer.setVisibility(View.GONE);
                }

                if (current.getNominal_giro() > 0) {
                    view.findViewById(R.id.ll_container_giro).setVisibility(View.VISIBLE);
                    nominal_giro.setText(" Rp. " + NumberFormat.getNumberInstance(Locale.US).format(current.getNominal_giro()));
                    no_giro.setText(current.getNomor_giro());
                    due_date.setText(getDate(current.getDue_date()));
                } else {
                    view.findViewById(R.id.ll_container_giro).setVisibility(View.GONE);
                }

                if (current.getNominal_giro() > 0 || current.getTransfer_value() > 0) {
                    bank.setText(current.getBank_nm());
                } else {
                    bank.setVisibility(View.GONE);
                }
            }

            @Override
            public View.OnClickListener onClickItem(Object object) {
                return null;
            }
        });
        takeOrderAdapter = new GenericRecyclerAdapter(takeOrderData, R.layout.check_in_out_item, new AdapterCallback() {
            @Override
            public void bindView(View view, Object object) {
                try {
                    TakeOrderData current = (TakeOrderData) object;
                    JSONObject jsonObject = new JSONObject(current.getJsonContent());
                    TextView name = view.findViewById(R.id.outlet_name);
                    TextView titleTanggal = view.findViewById(R.id.text_date);
                    TextView tanggal = view.findViewById(R.id.date);
                    Outlet currentOutlet = searchOutlet(jsonObject.getString("outlet_id"));
                    name.setText(currentOutlet == null ? "Outlet telah dihapus" : currentOutlet.getNm_outlet());
                    titleTanggal.setText("Jumlah Order :");
                    tanggal.setText("" + jsonObject.getJSONArray("orders").length() + " jenis produk");
                    view.findViewById(R.id.delete).setVisibility(View.GONE);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public View.OnClickListener onClickItem(Object object) {
                return null;
            }
        });
        binding.checkInList.setLayoutManager(new LinearLayoutManager(this));
        binding.checkInList.setAdapter(checkInAdapter);
        binding.checkOutList.setLayoutManager(new LinearLayoutManager(this));
        binding.checkOutList.setAdapter(checkOutAdapter);
        binding.billingList.setLayoutManager(new LinearLayoutManager(this));
        binding.billingList.setAdapter(billingAdapter);
        binding.orderList.setLayoutManager(new LinearLayoutManager(this));
        binding.orderList.setAdapter(takeOrderAdapter);
        binding.sync.setOnClickListener(view -> {
            failed = 0;
            iteratorVisit = 0;
            iteratorBilling = 0;
            iteratorOrder = 0;
            uploadData();
        });
        initObserver();
    }

    @Override
    protected BaseViewModel getViewModel() {
        return viewModel;
    }

    @Override
    protected void showLoading(boolean isLoading) {
        binding.loading.setVisibility(isLoading ? View.VISIBLE : View.GONE);
    }

    @Override
    protected void initObserver() {
        total = 0;
        viewModel.getStatus().observe(this, status -> {
            viewModel.getLoading().setValue(false);
            if(!status) {
                showMessage(status);
            }
        });
        viewModel.getLoading().observe(this, loading -> {
            showLoading(loading);
        });
        viewModel.getAllOutlet().observe(this, outlets1 -> {
            outlets.removeAll(outlets);
            outlets.addAll(outlets1);
            viewModel.getAllVisit().observe(this, visitPlanDbs1 -> {
                total+=visitPlanDbs1.size();
                visitPlanDbs.removeAll(visitPlanDbs);
                visitPlanDbs.addAll(visitPlanDbs1);
                checkInAdapter.notifyDataSetChanged();
                checkOutAdapter.notifyDataSetChanged();
                binding.checkinContainer.setVisibility(visitPlanDbs1.size() <= 0 ? View.GONE : View.VISIBLE);
                binding.checkoutContainer.setVisibility(visitPlanDbs1.size() <= 0 ? View.GONE : View.VISIBLE);
                viewModel.getAllBilling().observe(this, billingData -> {
                    total+=billingData.size();
                    billingDatas.removeAll(billingDatas);
                    billingDatas.addAll(billingData);
                    billingAdapter.notifyDataSetChanged();
                    binding.billingContainer.setVisibility(billingData.size() <= 0 ? View.GONE : View.VISIBLE);
                    viewModel.getAllTakeOrder().observe(this, takeOrderData1 -> {
                        total+=takeOrderData1.size();
                        takeOrderData.removeAll(takeOrderData);
                        takeOrderData.addAll(takeOrderData1);
                        takeOrderAdapter.notifyDataSetChanged();
                        binding.orderContainer.setVisibility(takeOrderData1.size() <= 0 ? View.GONE : View.VISIBLE);
                        if(total == 0){
                            binding.message.setText("Anda tidak memiliki data untuk diunggah");
                        }
                    });
                });
            });
        });
    }

    private Outlet searchOutlet(String outlet_id){
        for(Outlet current : outlets){
            if(current.getKd_outlet() == outlet_id){
                return current;
            }
        }
        return null;
    }

    private void uploadVisit(){
        VisitPlanDb visit = visitPlanDbs.get(iteratorVisit++);
        try {
            Map<String, Object> param = new HashMap<>();
            param.put("outlet_id", visit.getOutlet_id());
            param.put("date_visit", visit.getDate_visit());
            param.put("skip_order_reason", visit.getSkip_order_reason());
            param.put("if_close", visit.getIf_close());
            param.put("keterangan_if_close", visit.getKeterangan_if_close());
            param.put("draft", 1);
            Map<String, MultipartFile> paramFile = new HashMap<>();
            String[] path = visit.getGambar().split("/");
            paramFile.put("path_image", new MultipartFile(path[path.length-1], Uri.parse(visit.getGambar()), this));
            viewModel.submitVisit(param, paramFile).observe(this, visitPlanDb -> {
                if(visitPlanDb != null) {
                    try {
                        JSONObject jsonObject = new JSONObject();
                        jsonObject.put("visit_plan_id", visitPlanDb.getId());
                        jsonObject.put("date_checkout", visit.getDate_checkout());
                        viewModel.submitCheckout(jsonObject, visit).observe(this, visitPlanDb1 -> {
                            if (visitPlanDb1 != null) {
                                if (visitPlanDb1.getDate_checkout() != null && isStringNotEmpty(visitPlanDb1.getDate_checkout())) {
                                    visitPlanDbs.remove(visit);
                                    checkInAdapter.notifyDataSetChanged();
                                    checkOutAdapter.notifyDataSetChanged();
                                    iteratorVisit = 0;
                                } else{
                                    failed++;
                                }
                            } else{
                                failed++;
                            }
                            uploadData();
                        });
                    } catch (JSONException e) {
                        failed++;
                        uploadData();
                        e.printStackTrace();
                    }
                } else{
                    failed++;
                    uploadData();
                }
            });
        } catch (IOException e) {
            failed++;
            uploadData();
            e.printStackTrace();
        }
    }

    private void uploadBilling(){
        BillingData billingData = billingDatas.get(iteratorBilling++);
        try {
            Map<String, Object> param = new HashMap<>();
            param.put("outlet_id", billingData.getOutlet_id());
            param.put("status", billingData.getStatus());
            param.put("nilai_transfer", billingData.getTransfer_value());
            param.put("nilai_cash", billingData.getCash_value());
            param.put("nominal_giro", billingData.getNominal_giro());
            param.put("nomor_giro", billingData.getNomor_giro());
            param.put("nama_bank", billingData.getBank_nm());
            param.put("note", billingData.getNote());
            param.put("metode_pembayaran", billingData.getPayment_method());
            param.put("due_date", billingData.getDue_date());
            param.put("draft", 1);
            Map<String, MultipartFile> paramFile = new HashMap<>();
            if (isStringNotEmpty(billingData.getGambar()) && !billingData.getGambar().equals("-")) {
                String[] path = billingData.getGambar().split("/");
                paramFile.put("path_image", new MultipartFile(path[path.length-1], Uri.parse(billingData.getGambar()), this));
            }
            viewModel.setBilling(param, paramFile, billingData).observe(this, status -> {
                if(status){
                    billingDatas.remove(billingData);
                    billingAdapter.notifyDataSetChanged();
                    iteratorBilling = 0;
                } else{
                    failed++;
                }
                uploadData();
            });
        } catch (IOException e) {
            failed++;
            uploadData();
            e.printStackTrace();
        }
    }

    private void uploadOrder(){
        TakeOrderData data = takeOrderData.get(iteratorOrder);
        try {
            viewModel.submitTakeOrder(new JSONObject(data.getJsonContent()), data).observe(this, status -> {
                if(status){
                    takeOrderData.remove(data);
                    takeOrderAdapter.notifyDataSetChanged();
                    iteratorOrder = 0;
                } else{
                    failed++;
                }
                uploadData();
            });
        } catch (JSONException e) {
            failed++;
            uploadData();
            e.printStackTrace();
        }
    }

    private void uploadData(){
        if(visitPlanDbs.size() > iteratorVisit){
            uploadVisit();
        } else if(billingDatas.size() > iteratorBilling){
            uploadBilling();
        } else if(takeOrderData.size() > iteratorOrder){
            uploadOrder();
        } else{
            if(failed == 0){
                showMessageSuccess("Semua data berhasil terunggah");
                finish();
            } else{
                showMessageFailed("" + failed + " data gagal terunggah, silahkan coba lagi");
            }
        }
    }
}