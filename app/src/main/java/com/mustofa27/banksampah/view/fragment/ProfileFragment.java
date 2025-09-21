package com.mustofa27.banksampah.view.fragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.mustofa27.banksampah.R;
import com.mustofa27.banksampah.databinding.FragmentProfileBinding;
import com.mustofa27.banksampah.model.datasource.network.ConnectionHandler;
import com.mustofa27.banksampah.model.entity.User;
import com.mustofa27.banksampah.model.entity.UserToken;
import com.mustofa27.banksampah.model.helper.SharedPreferenceHelper;
import com.mustofa27.banksampah.view.BaseFragment;
import com.mustofa27.banksampah.view.activity.SplashActivity;
import com.mustofa27.banksampah.viewmodel.BaseViewModel;
import com.mustofa27.banksampah.viewmodel.CustomViewModelFactory;
import com.mustofa27.banksampah.viewmodel.UserViewModel;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfileFragment extends BaseFragment {

    FragmentProfileBinding binding;
    UserViewModel viewModel;
    AlertDialog.Builder builder;

    public ProfileFragment() {
        // Required empty public constructor
    }

    public static ProfileFragment newInstance() {
        ProfileFragment fragment = new ProfileFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = new ViewModelProvider(this, new CustomViewModelFactory(getContext())).get(UserViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentProfileBinding.inflate(getLayoutInflater());
        binding.containerWa.setOnClickListener(view -> {
            String url = "https://api.whatsapp.com/send?phone=6287855063917";
            try {
                PackageManager pm = view.getContext().getPackageManager();
                pm.getPackageInfo("com.whatsapp", PackageManager.GET_ACTIVITIES);
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                view.getContext().startActivity(i);
            } catch (PackageManager.NameNotFoundException e) {
                view.getContext().startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
            }
        });
        binding.containerFaq.setOnClickListener(view -> {
            view.getContext().startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(ConnectionHandler.web)));
        });
        builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Konfirmasi");
        builder.setMessage("Apakah anda yakin akan keluar dari aplikasi ini?");
        builder.setPositiveButton("Ya", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                SharedPreferenceHelper.getInstance(getContext()).removePreference(UserToken.table);
                startActivity(new Intent(getContext(), SplashActivity.class));
                getActivity().finish();
            }
        });
        builder.setNegativeButton("Tidak", null);
        binding.containerLogout.setOnClickListener(v -> builder.show());
        initObserver();
        return binding.getRoot();
    }

    @Override
    protected BaseViewModel getViewModel() {
        return viewModel;
    }

    @Override
    protected void showLoading(boolean isLoading) {

    }

    @Override
    protected boolean isValidInput() {
        return false;
    }

    @Override
    protected void initObserver() {
        viewModel.getUserLiveData().observe(getViewLifecycleOwner(), user -> {
            binding.profNama.setText(isStringNotEmpty(user.getName()) ? user.getName() : "-");
            binding.profEmail.setText(isStringNotEmpty(user.getEmail()) ? user.getEmail() : "-");
            binding.profTanggal.setText(isStringNotEmpty(user.getCreated_at()) ? getDate(user.getCreated_at()) : "-");
            Glide.with(this).load(ConnectionHandler.IMAGE_URL + user.getProfile_picture()).
                    placeholder(R.drawable.user_default).error(R.drawable.user_default).into(binding.imUser);
        });
    }
}