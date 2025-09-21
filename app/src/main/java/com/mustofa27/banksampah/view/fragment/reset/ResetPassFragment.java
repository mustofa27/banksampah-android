package com.mustofa27.banksampah.view.fragment.reset;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mustofa27.banksampah.R;
import com.mustofa27.banksampah.databinding.FragmentResetPassBinding;
import com.mustofa27.banksampah.view.BaseFragment;
import com.mustofa27.banksampah.viewmodel.BaseViewModel;
import com.mustofa27.banksampah.viewmodel.CustomViewModelFactory;
import com.mustofa27.banksampah.viewmodel.ResetPassViewModel;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ResetPassFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ResetPassFragment extends BaseFragment {

    FragmentResetPassBinding binding;
    ResetPassViewModel viewModel;
    public ResetPassFragment() {
        // Required empty public constructor
    }

    public static ResetPassFragment newInstance() {
        ResetPassFragment fragment = new ResetPassFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = new ViewModelProvider(getActivity(), new CustomViewModelFactory(getActivity())).get(ResetPassViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentResetPassBinding.inflate(inflater);
        binding.send.setOnClickListener(view -> {
            if(isStringNotEmpty(binding.otp) && isStringNotEmpty(binding.password) && isStringNotEmpty(binding.cpassword)){
                if(binding.password.getText().toString().equals(binding.cpassword.getText().toString())){
                    viewModel.resetPass(binding.otp.getText().toString(), binding.password.getText().toString(), binding.cpassword.getText().toString());
                } else{
                    showMessageFailed("Password tidak cocok");
                }
            } else{
                showMessageFailed("Silahkan isi email terlebih dahulu");
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
        if (isLoading) {
            showLoadingDialog();
        } else {
            dismissLoadingDialog();
        }
    }

    @Override
    protected boolean isValidInput() {
        return false;
    }

    @Override
    protected void initObserver() {
        viewModel.getStatus().observe(getActivity(), status -> {
            viewModel.getLoading().setValue(false);
            showMessage(status);
            if(status){
                getActivity().finish();
            }
        });
        viewModel.getLoading().observe(getActivity(), loading -> {
            showLoading(loading);
        });
    }
}