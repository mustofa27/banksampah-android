package com.qiratek.rnpsales.view.fragment.registeroutlet;

import static com.google.android.gms.location.LocationRequest.PRIORITY_HIGH_ACCURACY;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.CancellationToken;
import com.google.android.gms.tasks.OnTokenCanceledListener;
import com.qiratek.rnpsales.databinding.FragmentRegistOutletStepThreeBinding;
import com.qiratek.rnpsales.view.BaseFragment;
import com.qiratek.rnpsales.viewmodel.BaseViewModel;
import com.qiratek.rnpsales.viewmodel.CustomViewModelFactory;
import com.qiratek.rnpsales.viewmodel.RegisterOutletViewModel;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link RegistOutletStepThreeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RegistOutletStepThreeFragment extends BaseFragment {

    RegisterOutletViewModel viewModel;
    FragmentRegistOutletStepThreeBinding binding;
    private FusedLocationProviderClient fusedLocationClient;
    String latitude = "",longitude = "";

    public RegistOutletStepThreeFragment() {
        // Required empty public constructor
    }

    public static RegistOutletStepThreeFragment newInstance() {
        RegistOutletStepThreeFragment fragment = new RegistOutletStepThreeFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = new ViewModelProvider(getActivity(), new CustomViewModelFactory(getContext())).get(RegisterOutletViewModel.class);
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(getActivity());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentRegistOutletStepThreeBinding.inflate(getLayoutInflater());
        binding.roBtLocation.setOnClickListener(v -> {
            if(ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                    ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                ProgressDialog progressDialog = new ProgressDialog(getContext());
                progressDialog.setTitle("Location Getter");
                progressDialog.setMessage("Getting location, please wait!");
                progressDialog.setCancelable(false);
                progressDialog.show();
                fusedLocationClient.getCurrentLocation(PRIORITY_HIGH_ACCURACY, new CancellationToken() {
                    @Override
                    public boolean isCancellationRequested() {
                        return false;
                    }

                    @NonNull
                    @Override
                    public CancellationToken onCanceledRequested(@NonNull OnTokenCanceledListener onTokenCanceledListener) {
                        return null;
                    }
                }).addOnSuccessListener(getActivity(), location -> {
                    progressDialog.dismiss();
                    if (location != null) {
                        binding.roEtLocation.setText(String.valueOf(location.getLatitude()) + " , " + String.valueOf(location.getLongitude()));
                        latitude = String.valueOf(location.getLatitude());
                        longitude = String.valueOf(location.getLongitude());
                    } else{
                        showMessageFailed("Get location failed, please try again later");
                    }
                });
            } else{
                if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) &&
                        ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION)) {
                    showMessageFailed("This application need permission to access location");
                } else {
                    ActivityCompat.requestPermissions(getActivity(),
                            new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
                            1);
                }
            }
        });
        binding.roSubmit.setOnClickListener(v -> {
            if(!isStringNotEmpty(longitude) || !isStringNotEmpty(latitude) || !isStringNotEmpty(binding.roNmPic) || !isStringNotEmpty(binding.roTlpPic)) {
                showMessageFailed("Pastikan semua sudah terisi");
            } else{
                viewModel.getParam().put("latitude", latitude);
                viewModel.getParam().put("longitude", longitude);
                viewModel.getParam().put("nama_pic", binding.roNmPic.getText().toString());
                viewModel.getParam().put("telepon_pic", binding.roTlpPic.getText().toString());
                viewModel.getParam().put("reg_status","YES");
                viewModel.getParam().put("status_aktivasi",1);
                showLoading(true);
                viewModel.createOutlet().observe(getActivity(), outlet -> {
                    if(outlet != null){
                        showMessageSuccess("Pembuatan outlet berhasil");
                        getActivity().finish();
                    } else{
                        showMessageFailed("Pembuatan outlet gagal, silahkan coba lagi");
                    }
                });

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
        binding.loading.setVisibility(isLoading ? View.VISIBLE : View.GONE);
    }

    @Override
    protected boolean isValidInput() {
        return false;
    }

    @Override
    protected void initObserver() {
        viewModel.getStatus().observe(getActivity(), status -> {
            viewModel.getLoading().setValue(false);
            if(!status) {
                showMessage(status);
            }
        });
        viewModel.getLoading().observe(getActivity(), loading -> {
            showLoading(loading);
        });
    }
}