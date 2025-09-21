package com.mustofa27.banksampah.view.fragment;

import android.os.Bundle;


import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.mustofa27.banksampah.R;
import com.mustofa27.banksampah.databinding.FragmentHistoryTakeOrderBinding;
import com.mustofa27.banksampah.view.BaseFragment;
import com.mustofa27.banksampah.view.adapter.AdapterCallback;
import com.mustofa27.banksampah.view.adapter.GenericRecyclerAdapter;
import com.mustofa27.banksampah.viewmodel.BaseViewModel;
import com.mustofa27.banksampah.viewmodel.CustomViewModelFactory;
import com.mustofa27.banksampah.viewmodel.HistoryViewModel;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HistoryTakeOrderFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HistoryTakeOrderFragment extends BaseFragment {

    FragmentHistoryTakeOrderBinding binding;
    GenericRecyclerAdapter adapter;
//    ArrayList<ArrayList<TakeOrder>> groupedTakeOrders;
//    ArrayList<TakeOrder> takeOrders;
//    ArrayList<Satuan> satuans;
    HistoryViewModel viewModel;
    String[] isiStatus = new String[]{"All", "Open", "Pending", "Complete", "Cancel"};
    String querySearch = "";
    ArrayAdapter statusAdapter;
    String selectedStatus = "All";
//    UserPersonalize userPersonalize;
    boolean isAdapterNotified = false;

    public HistoryTakeOrderFragment() {
        // Required empty public constructor
    }

    public static HistoryTakeOrderFragment newInstance() {
        HistoryTakeOrderFragment fragment = new HistoryTakeOrderFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        groupedTakeOrders = new ArrayList<>();
//        satuans = new ArrayList<>();
//        takeOrders = new ArrayList<>();
//        adapter = new GenericRecyclerAdapter(groupedTakeOrders, R.layout.history_order_item, new AdapterCallback() {
//            @Override
//            public void bindView(View view, Object object) {
//                ArrayList<TakeOrder> current = (ArrayList<TakeOrder>) object;
//                TextView nama = view.findViewById(R.id.namaOutlet);
//                TextView alamat = view.findViewById(R.id.alamatOutlet);
//                TextView tipe = view.findViewById(R.id.tipeOutlet);
//                ImageView imageView = view.findViewById(R.id.im_outlet);
//                TextView kode = view.findViewById(R.id.tv_kode_order);
//                TextView tanggal = view.findViewById(R.id.tv_tanggal);
//                TextView note = view.findViewById(R.id.tv_note);
//                TextView showHide = view.findViewById(R.id.show_hide);
//                LinearLayout llContainer = view.findViewById(R.id.ll_container_to);
//                RecyclerView detail = view.findViewById(R.id.detail_list);
//                detail.setLayoutManager(new LinearLayoutManager(getActivity()));
//                if(!current.get(0).isHasSetVisible()){
//                    current.get(0).setVisible(userPersonalize != null ? (userPersonalize.getPilihan_tampil() == 1) : false);
//                    current.get(0).setHasSetVisible(true);
//                }
//                showHide.setText(current.get(0).isVisible() ? "Hide Details" : "Show Details");
//                llContainer.setVisibility(current.get(0).isVisible() ? View.VISIBLE : View.GONE);
//                if(current.get(0).getOutlets() == null) {
//                    viewModel.getOutlet(current.get(0).getOutlet_id()).observe(getActivity(), outlet -> {
//                        nama.setText(outlet != null ? outlet.getNm_outlet() : "Outlet telah terhapus");
//                        alamat.setText(outlet != null ? outlet.getAlmt_outlet() : "Outlet telah terhapus");
//                        Glide.with(getActivity()).load(outlet != null ? outlet.getFoto() : "Outlet telah terhapus")
//                                .into(imageView);
//                        if (outlet != null) {
//                            viewModel.getTipe(outlet.getKd_tipe()).observe(getActivity(), tipe1 -> {
//                                tipe.setText(tipe1 == null ? "Outlet telah terhapus" : tipe1.getNama());
//                            });
//                        } else {
//                            tipe.setText("Outlet telah terhapus");
//                        }
//                    });
//                } else{
//                    Outlet outlet = current.get(0).getOutlets();
//                    nama.setText(outlet != null ? outlet.getNm_outlet() : "Outlet telah terhapus");
//                    alamat.setText(outlet != null ? outlet.getAlmt_outlet() : "Outlet telah terhapus");
//                    Glide.with(getActivity()).load(outlet != null ? outlet.getFoto() : "Outlet telah terhapus")
//                            .into(imageView);
//                    if (outlet != null) {
//                        viewModel.getTipe(outlet.getKd_tipe()).observe(getActivity(), tipe1 -> {
//                            tipe.setText(tipe1 == null ? "Outlet telah terhapus" : tipe1.getNama());
//                        });
//                    } else {
//                        tipe.setText("Outlet telah terhapus");
//                    }
//                }
//                kode.setText(current.get(0).getKode_order());
//                tanggal.setText(getDate(current.get(0).getDate_order()));
//                note.setText(current.get(0).getNote());
//                if(current.get(0).getAdapter() == null) {
//                    current.get(0).setAdapter(new GenericRecyclerAdapter(current, R.layout.history_to_item, new AdapterCallback() {
//                        @Override
//                        public void bindView(View child, Object object) {
//                            TakeOrder takeOrder = (TakeOrder) object;
//                            TextView produk = child.findViewById(R.id.prod_name);
//                            TextView qty = child.findViewById(R.id.qty);
//                            TextView tvStatusItem = child.findViewById(R.id.status_item);
//                            TextView tvComment = child.findViewById(R.id.comment);
//                            Satuan currentSatuan = getSatuan(takeOrder.getSatuan_id());
//                            produk.setText(takeOrder.getOther_product());
//                            qty.setText("" + takeOrder.getQuantity() + " " + (currentSatuan == null ? "-" : currentSatuan.getNama()));
//                            if (takeOrder.getStatus() == null || takeOrder.getStatus().equals("null") || takeOrder.getStatus().equalsIgnoreCase("0")) {
//                                tvStatusItem.setText("belum ada status");
//                            } else {
//                                try {
//                                    if (takeOrder.getStatus().equals("open")) {
//                                        tvStatusItem.setTextColor(getResources().getColor(R.color.status_open));
//                                    } else if (takeOrder.getStatus().equals("pending")) {
//                                        tvStatusItem.setTextColor(getResources().getColor(R.color.status_pending));
//                                    } else if (takeOrder.getStatus().equals("complete")) {
//                                        tvStatusItem.setTextColor(getResources().getColor(R.color.status_complete));
//                                    } else if (takeOrder.getStatus().equals("cancel")) {
//                                        tvStatusItem.setTextColor(getResources().getColor(R.color.status_cancel));
//                                    }
//                                } catch (Exception e){
//
//                                }
//                            }
//
//                            if (takeOrder.getComment() == null) {
//                                tvComment.setText("belum ada comment");
//                            } else {
//                                tvComment.setText(takeOrder.getComment());
//                            }
//                        }
//
//                        @Override
//                        public View.OnClickListener onClickItem(Object object) {
//                            return null;
//                        }
//                    }));
//                }
//                detail.setAdapter(current.get(0).getAdapter());
//                showHide.setOnClickListener(view1 -> {
//                    llContainer.setVisibility(current.get(0).isVisible() ? View.GONE : View.VISIBLE);
//                    current.get(0).setVisible(!current.get(0).isVisible());
//                    showHide.setText(current.get(0).isVisible() ? "Hide Details" : "Show Details");
//                });
//            }
//
//            @Override
//            public View.OnClickListener onClickItem(Object object) {
//                return null;
//            }
//        });
        statusAdapter = new ArrayAdapter(getActivity(), R.layout.custom_spinner, isiStatus);
        viewModel = new ViewModelProvider(getParentFragment(), new CustomViewModelFactory(getActivity())).get(HistoryViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentHistoryTakeOrderBinding.inflate(getLayoutInflater());
        binding.rvHistoryTo.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL));
        binding.rvHistoryTo.setAdapter(adapter);
        binding.rvHistoryTo.setLayoutManager(new LinearLayoutManager(getActivity()));
        binding.status.setAdapter(statusAdapter);
        binding.status.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                selectedStatus = isiStatus[i];
                filter();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        binding.search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                querySearch = query.toLowerCase();
                filter();
                binding.search.clearFocus();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                querySearch = newText.toLowerCase();
                filter();
                return true;
            }
        });
        initObserver();
        return binding.getRoot();
    }

    @Override
    protected BaseViewModel getViewModel() {
        return viewModel;
    }

    @Override
    protected void showLoading(boolean isLoading) {
        binding.loading.getRoot().setVisibility(isLoading ? View.VISIBLE : View.GONE);
    }

    @Override
    protected boolean isValidInput() {
        return false;
    }

    @Override
    protected void initObserver() {
        viewModel.getStatus().observe(getActivity(), status -> {
            if(!status) {
                showMessage(status);
            }
        });
        viewModel.getLoading().observe(getActivity(), loading -> {
            showLoading(loading);
        });

//        viewModel.getAllSatuan(true).observe(getActivity(), satuans -> {
//            this.satuans.removeAll(this.satuans);
//            this.satuans.addAll(satuans);
//            viewModel.getTakeOrderData().observe(getActivity(), takeOrders -> {
//                this.takeOrders.removeAll(this.takeOrders);
//                this.takeOrders.addAll(takeOrders);
//                viewModel.getUserPersonalizeLive().observe(getActivity(), userPersonalize -> {
//                    this.userPersonalize = userPersonalize;
//                    viewModel.setUserPersonalize(userPersonalize);
//                    if (groupedTakeOrders.size() != 0) {
//                        for (ArrayList<TakeOrder> takeOrderArrayList : groupedTakeOrders) {
//                            takeOrderArrayList.get(0).setHasSetVisible(false);
//                        }
//                    }
//                    filter();
//                    if(viewModel.getPopupWindow().isShowing()){
//                        viewModel.getPopupWindow().dismiss();
//                        showMessageSuccess("Data berhasil tersimpan");
//                    }
//                    removeObservers();
//                });
//            });
//        });
    }

    private void removeObservers(){
//        viewModel.getSatuanLiveData().removeObservers(this);
//        viewModel.getTakeOrderLiveData().removeObservers(this);
//        viewModel.getUserPersonalizeLiveData().removeObservers(this);
    }

//    private Satuan getSatuan(int id){
//        for(Satuan satuan : satuans){
//            if(satuan.getId() == id){
//                return satuan;
//            }
//        }
//        return null;
//    }

    private void filter(){
//        groupedTakeOrders.removeAll(groupedTakeOrders);
//        ArrayList<TakeOrder> takeOrderArrayList = new ArrayList<>();
//        for(int i = 0; i < takeOrders.size(); i++){
//            if((!isStringNotEmpty(querySearch) || takeOrders.get(i).getOutlets() != null && takeOrders.get(i).getOutlets().getNm_outlet().toLowerCase().contains(querySearch)) && ((takeOrderArrayList.size() == 0 || takeOrderArrayList.get(0).getKode_order().equalsIgnoreCase(takeOrders.get(i).getKode_order())) &&
//                    (takeOrders.get(i).getStatus() != null && takeOrders.get(i).getStatus().equalsIgnoreCase(selectedStatus) || selectedStatus.equalsIgnoreCase("all")))){
//                takeOrderArrayList.add(takeOrders.get(i));
//            } else{
//                if(takeOrderArrayList.size() > 0) {
//                    groupedTakeOrders.add(takeOrderArrayList);
//                    takeOrderArrayList = new ArrayList<>();
//                    if((!isStringNotEmpty(querySearch) || takeOrders.get(i).getOutlets() != null && takeOrders.get(i).getOutlets().getNm_outlet().toLowerCase().contains(querySearch)) &&
//                            (takeOrders.get(i).getStatus() != null && takeOrders.get(i).getStatus().equalsIgnoreCase(selectedStatus) ||
//                            selectedStatus.equalsIgnoreCase("all"))) {
//                        takeOrderArrayList.add(takeOrders.get(i));
//                    }
//                }
//            }
//            if(i == takeOrders.size()-1 && takeOrderArrayList.size() > 0) {
//                groupedTakeOrders.add(takeOrderArrayList);
//            }
//        }
//        binding.noData.getRoot().setVisibility(groupedTakeOrders.size() > 0 ? View.GONE : View.VISIBLE);
        adapter.notifyDataSetChanged();
        viewModel.getLoading().setValue(false);
    }
}