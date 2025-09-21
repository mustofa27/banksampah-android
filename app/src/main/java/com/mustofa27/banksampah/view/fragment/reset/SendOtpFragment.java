package com.mustofa27.banksampah.view.fragment.reset;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mustofa27.banksampah.R;
import com.mustofa27.banksampah.databinding.FragmentSendOtpBinding;
import com.mustofa27.banksampah.view.BaseFragment;
import com.mustofa27.banksampah.viewmodel.BaseViewModel;
import com.mustofa27.banksampah.viewmodel.CustomViewModelFactory;
import com.mustofa27.banksampah.viewmodel.ResetPassViewModel;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SendOtpFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SendOtpFragment extends BaseFragment {

    FragmentSendOtpBinding binding;
    ResetPassViewModel viewModel;

    public SendOtpFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static SendOtpFragment newInstance() {
        SendOtpFragment fragment = new SendOtpFragment();
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
        binding = FragmentSendOtpBinding.inflate(inflater);
        binding.send.setOnClickListener(view -> {
            if(isStringNotEmpty(binding.email)){
                viewModel.sendOTP(binding.email.getText().toString());
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
                removeObserver();
                viewModel.getStatus().setValue(false);
                viewModel.getNavController().navigate(R.id.action_otp_to_reset);
            }
        });
        viewModel.getLoading().observe(getActivity(), loading -> {
            showLoading(loading);
        });
    }

    private void removeObserver(){
        viewModel.getStatus().removeObservers(getActivity());
        viewModel.getLoading().removeObservers(getActivity());
    }
}