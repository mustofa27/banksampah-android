package com.mustofa27.banksampah.view.fragment;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;

import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.mustofa27.banksampah.R;
import com.mustofa27.banksampah.databinding.FragmentHomeBinding;
import com.mustofa27.banksampah.model.datasource.network.ConnectionHandler;
import com.mustofa27.banksampah.model.entity.NewsClass;
import com.mustofa27.banksampah.model.entity.Product;
import com.mustofa27.banksampah.view.BaseFragment;
import com.mustofa27.banksampah.view.activity.NewsActivity;
import com.mustofa27.banksampah.view.adapter.AdapterCallback;
import com.mustofa27.banksampah.view.adapter.BannerPromoAdapter;
import com.mustofa27.banksampah.view.adapter.GenericRecyclerAdapter;
import com.mustofa27.banksampah.viewmodel.BaseViewModel;
import com.mustofa27.banksampah.viewmodel.CustomViewModelFactory;
import com.mustofa27.banksampah.viewmodel.HomeViewModel;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

public class HomeFragment extends BaseFragment implements View.OnClickListener {

    FragmentHomeBinding binding;
    HomeViewModel viewModel;
    ProgressDialog progressDialog;
    BannerPromoAdapter bannerPromoAdapter;
    ArrayList<NewsClass> promos;
    ArrayList<Product> all;
    GenericRecyclerAdapter productAdapter,sampahAdapter;
    NumberFormat kursIndonesia;

    Locale localeID = new Locale("in", "ID");
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
        kursIndonesia = NumberFormat.getCurrencyInstance(localeID);
        kursIndonesia.setMaximumFractionDigits(0);
        promos = new ArrayList<>();
        all = new ArrayList<>();
        bannerPromoAdapter = new BannerPromoAdapter(getContext(), promos);
        productAdapter = new GenericRecyclerAdapter(all, R.layout.item_product, new AdapterCallback() {
            @Override
            public void bindView(View view, Object object) {
                Product tmp = (Product) object;
                ImageView imageView = view.findViewById(R.id.imageview);
                TextView productName = view.findViewById(R.id.product_name);
                TextView productPrice = view.findViewById(R.id.product_price);
                TextView productPriceDiscount = view.findViewById(R.id.product_price_discount);
                TextView productPriceHanya = view.findViewById(R.id.product_price_title);
                Glide.with(getContext()).load(ConnectionHandler.IMAGE_URL + tmp.getImage_path()).
                        placeholder(R.drawable.icons8_no_image).error(R.drawable.icons8_no_image).centerCrop().into(imageView);
                productName.setText(tmp.getName());
                if(!tmp.getDiscounts().isEmpty()){
                    productPrice.setVisibility(View.VISIBLE);
                    productPriceHanya.setVisibility(View.VISIBLE);
                    productPrice.setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG);
                    productPrice.setText(kursIndonesia.format(tmp.getPrice()));
                    productPriceDiscount.setText(kursIndonesia.format((100-tmp.getDiscounts().get(0).getPercentage()) * tmp.getPrice()/100));
                } else{
                    productPrice.setVisibility(View.GONE);
                    productPriceHanya.setVisibility(View.GONE);
                    productPriceDiscount.setText(kursIndonesia.format(tmp.getPrice()));
                }
                view.findViewById(R.id.beli).setOnClickListener(this.onClickItem(tmp));
            }

            @Override
            public View.OnClickListener onClickItem(Object object) {
                return null;
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentHomeBinding.inflate(getLayoutInflater());
        binding.imageSlider.setSliderAdapter(bannerPromoAdapter);
        binding.product.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.HORIZONTAL, false));
        binding.product.setAdapter(productAdapter);
        binding.seeAllPromo.setOnClickListener(this);
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
        }
    }

    private void refreshData(){
        viewModel.getLoading().setValue(true);
        viewModel.getAllProduct().observe(this, products -> {

        });
    }

    private void removeObservers(){
        viewModel.getProductLiveData().removeObservers(this);
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
            if(!promos.isEmpty()){
                promos.clear();
            }
            promos.addAll(newsClasses);
            bannerPromoAdapter.notifyDataSetChanged();
            if(!promos.isEmpty()) {
                binding.imageSlider.startAutoCycle();
            }
            viewModel.getAllProduct().observe(getViewLifecycleOwner(), products -> {
                if(!all.isEmpty()){
                    all.clear();
                }
                all.addAll(products);
                showLoading(false);
                productAdapter.notifyDataSetChanged();
                if(binding.swipe.isRefreshing()){
                    binding.swipe.setRefreshing(false);
                }
            });
        });
    }
}