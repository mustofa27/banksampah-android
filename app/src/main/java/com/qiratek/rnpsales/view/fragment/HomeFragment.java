package com.qiratek.rnpsales.view.fragment;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.qiratek.rnpsales.R;
import com.qiratek.rnpsales.databinding.FragmentHomeBinding;
import com.qiratek.rnpsales.model.datasource.network.ConnectionHandler;
import com.qiratek.rnpsales.model.entity.FeedOutlet;
import com.qiratek.rnpsales.model.entity.NewsClass;
import com.qiratek.rnpsales.model.entity.VisitPlanDb;
import com.qiratek.rnpsales.view.BaseFragment;
import com.qiratek.rnpsales.view.activity.BillingActivity;
import com.qiratek.rnpsales.view.activity.DetailFeed;
import com.qiratek.rnpsales.view.activity.MarketUpdateActivity;
import com.qiratek.rnpsales.view.activity.NewsDetail;
import com.qiratek.rnpsales.view.activity.OutletActivity;
import com.qiratek.rnpsales.view.activity.NewsActivity;
import com.qiratek.rnpsales.view.activity.TakeOrderActivity;
import com.qiratek.rnpsales.view.adapter.AdapterCallback;
import com.qiratek.rnpsales.view.adapter.BannerPromoAdapter;
import com.qiratek.rnpsales.view.adapter.GenericRecyclerAdapter;
import com.qiratek.rnpsales.viewmodel.BaseViewModel;
import com.qiratek.rnpsales.viewmodel.CustomViewModelFactory;
import com.qiratek.rnpsales.viewmodel.HomeViewModel;

import java.util.ArrayList;

public class HomeFragment extends BaseFragment implements View.OnClickListener {

    FragmentHomeBinding binding;
    HomeViewModel viewModel;
    ProgressDialog progressDialog;
    BannerPromoAdapter bannerPromoAdapter;
    ArrayList<NewsClass> promos;
    GenericRecyclerAdapter adapter;
    ArrayList<FeedOutlet> feedOutlets;
    GenericRecyclerAdapter visitAdapter;
    ArrayList<VisitPlanDb> visitPlanDbs;

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = new ViewModelProvider(this, new CustomViewModelFactory(getContext())).get(HomeViewModel.class);
        progressDialog = new ProgressDialog(getContext());
        progressDialog.setCancelable(false);
        progressDialog.setTitle("Processing");
        progressDialog.setMessage("Please Wait");
        promos = new ArrayList<>();
        feedOutlets = new ArrayList<>();
        visitPlanDbs = new ArrayList<>();
        bannerPromoAdapter = new BannerPromoAdapter(getContext(), promos);
        adapter = new GenericRecyclerAdapter(feedOutlets, R.layout.item_feed, new AdapterCallback() {
            @Override
            public void bindView(View view, Object object) {
                FeedOutlet current = (FeedOutlet) object;
                ImageView imOutlet = (ImageView) view.findViewById(R.id.imageview);
                Glide.with(getActivity()).load(current.getGambar1())
                        .into(imOutlet);
                view.setVisibility(View.VISIBLE);
            }

            @Override
            public View.OnClickListener onClickItem(Object object) {
                return view -> {
                    Intent intent = new Intent(getContext(), DetailFeed.class);
                    intent.putExtra("data", (FeedOutlet)object);
                    startActivity(intent);
                };
            }
        });
        visitAdapter = new GenericRecyclerAdapter(visitPlanDbs, R.layout.item_visit, new AdapterCallback() {
            @Override
            public void bindView(View view, Object object) {
                VisitPlanDb current = (VisitPlanDb) object;
                ImageView imOutlet = (ImageView) view.findViewById(R.id.im_outlet);
                Glide.with(getActivity()).load(current.getOutlets().getFoto())
                        .into(imOutlet);
                view.setVisibility(View.VISIBLE);
                TextView nama = view.findViewById(R.id.namaOutlet);
                TextView tipe = view.findViewById(R.id.tipeOutlet);
                TextView alamat = view.findViewById(R.id.alamatOutlet);
                TextView checkin = view.findViewById(R.id.checkin);
                TextView checkout = view.findViewById(R.id.checkout);
                nama.setText(current.getOutlets().getNm_outlet());
                alamat.setText(current.getOutlets().getAlmt_outlet());
                tipe.setText(current.getOutlets().getTipe().getNama());
                checkin.setText("Checkin : " + (current.getDate_visit() != null ? getDate(current.getDate_visit()) : "-"));
                checkout.setText("Checkout : " + (current.getDate_checkout() != null ? getDate(current.getDate_checkout()) : "-"));
            }

            @Override
            public View.OnClickListener onClickItem(Object object) {
                return view -> {
//                    Intent intent = new Intent(getContext(), NewsDetail.class);
//                    intent.putExtra("data", (NewsClass)object);
//                    startActivity(intent);
                };
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentHomeBinding.inflate(getLayoutInflater());
        binding.menuOutlet.setOnClickListener(this);
        binding.menuBilling.setOnClickListener(this);
        binding.menuCheckout.setOnClickListener(this);
        binding.menuSubmit.setOnClickListener(this);
        binding.menuTakeOrder.setOnClickListener(this);
        binding.menuTakePhoto.setOnClickListener(this);
        binding.menuHistory.setOnClickListener(this);
        binding.menuProfile.setOnClickListener(this);
        binding.menuSync.setOnClickListener(this);
        binding.menuRefresh.setOnClickListener(this);
        binding.imageSlider.setSliderAdapter(bannerPromoAdapter);
        binding.seeAllPromo.setOnClickListener(this);
        binding.buttonOrder.setOnClickListener(this);
        binding.buttonBill.setOnClickListener(this);
        binding.buttonOutlet.setOnClickListener(this);
        binding.addFeed.setOnClickListener(this);
        binding.feed.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        binding.feed.setAdapter(adapter);
        binding.visit.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.visit.setAdapter(visitAdapter);
        binding.swipe.setOnRefreshListener(() -> {
            refreshSwipe();
        });
        initObserver();
        return binding.getRoot();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.see_all_promo:
                getActivity().startActivity(new Intent(getContext(), NewsActivity.class));
                break;
            case R.id.button_outlet:
                getActivity().startActivity(new Intent(getContext(), OutletActivity.class));
                break;
            case R.id.button_order:
                getActivity().startActivity(new Intent(getContext(), TakeOrderActivity.class));
                break;
            case R.id.add_feed:
                getActivity().startActivity(new Intent(getContext(), MarketUpdateActivity.class));
                break;
            case R.id.button_bill:
                getActivity().startActivity(new Intent(getContext(), BillingActivity.class));
                break;
            case R.id.menu_refresh:
                refreshData();
                break;
        }
    }

    private void refreshData(){
        viewModel.getLoading().setValue(true);
        viewModel.getAllCity().observe(this, cities -> {
            viewModel.getAllTipe().observe(this, tipes -> {
                viewModel.getAllOutlet().observe(this, outlets -> {
                    viewModel.getAllBrand().observe(this, brands -> {
                        viewModel.getAllProduct().observe(this, products -> {
                            viewModel.getAllSatuan().observe(this, satuans -> {
                                viewModel.getConfiguration().observe(this, configuration -> {
                                    viewModel.getLoading().setValue(false);
                                    removeObservers();
                                });
                            });
                        });
                    });
                });
            });
        });
    }

    private void removeObservers(){
        viewModel.getCities().removeObservers(this);
        viewModel.getTipes().removeObservers(this);
        viewModel.getOutlets().removeObservers(this);
        viewModel.getBrandLiveData().removeObservers(this);
        viewModel.getProductLiveData().removeObservers(this);
        viewModel.getSatuanLiveData().removeObservers(this);
        viewModel.getConfigurationLiveData().removeObservers(this);
    }

    @Override
    protected BaseViewModel getViewModel() {
        return viewModel;
    }

    @Override
    protected void showLoading(boolean isLoading) {
        if(isLoading){
            progressDialog.show();
        } else {
            progressDialog.dismiss();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    protected boolean isValidInput() {
        return false;
    }

    @Override
    protected void initObserver() {
        viewModel.getStatus().observe(getViewLifecycleOwner(), status -> {
            viewModel.getLoading().setValue(false);
            if(!status) {
                showMessage(status);
            }
        });
        viewModel.getLoading().observe(getViewLifecycleOwner(), loading -> {
            showLoading(loading);
        });
        refreshSwipe();
    }

    private void refreshSwipe(){
        viewModel.getAllNews().observe(getViewLifecycleOwner(), newsClasses -> {
            if(promos.size() > 0){
                promos.removeAll(promos);
            }
            promos.addAll(newsClasses);
            bannerPromoAdapter.notifyDataSetChanged();
            if(promos.size() > 0) {
                binding.imageSlider.startAutoCycle();
            }
            viewModel.getAllFeed().observe(getViewLifecycleOwner(), feedOutlets -> {
                if(this.feedOutlets.size() > 0){
                    this.feedOutlets.removeAll(this.feedOutlets);
                }
                this.feedOutlets.addAll(feedOutlets);
                adapter.notifyDataSetChanged();
                viewModel.getVisitPlanDbLiveData().observe(getViewLifecycleOwner(), visitPlanDbs -> {
                    if(this.visitPlanDbs.size()>0){
                        this.visitPlanDbs.removeAll(this.visitPlanDbs);
                    }
                    this.visitPlanDbs.addAll(visitPlanDbs);
                    visitAdapter.notifyDataSetChanged();
                    viewModel.getLoading().setValue(false);
                    viewModel.getVisitPlanLiveData().removeObservers(this);
                    viewModel.getFeedLiveData().removeObservers(this);
                    viewModel.getNewsLiveData().removeObservers(this);
                    if(binding.swipe.isRefreshing()){
                        binding.swipe.setRefreshing(false);
                    }
                });
            });
        });
    }
}