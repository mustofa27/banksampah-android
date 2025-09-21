package com.mustofa27.banksampah.view;

import android.app.AlertDialog;
import android.app.Dialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;


import com.mustofa27.banksampah.R;
import com.mustofa27.banksampah.model.helper.CustomFragmentHelper;
import com.mustofa27.banksampah.viewmodel.BaseViewModel;


public abstract class BaseFragment extends CustomFragmentHelper {

    protected abstract BaseViewModel getViewModel();
    protected abstract void showLoading(boolean isLoading);
    protected abstract boolean isValidInput();
    protected abstract void initObserver();
    private Dialog progressDialog;
    protected void showLoadingDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        View view = getLayoutInflater().inflate(R.layout.loading_dialog, null);
        TextView titletv = view.findViewById(R.id.title);
        TextView messagetv = view.findViewById(R.id.message);
        titletv.setText("Processing");
        messagetv.setText("Please Wait");
        builder.setView(view);
        progressDialog = builder.create();
        progressDialog.show();
    }

    protected void dismissLoadingDialog(){
        if(progressDialog != null) {
            progressDialog.dismiss();
        }
    }

    protected void showMessageSuccess(String message) {
        if(message.length() > 0) {
            try{
                LayoutInflater inflater = getLayoutInflater();
                View toastLayout = inflater.inflate(R.layout.toast_ok, (ViewGroup) getActivity().findViewById(R.id.toast_ok));
                TextView tv_toast = toastLayout.findViewById(R.id.tv_toast_ok);
                tv_toast.setText(message);
                Toast toast = new Toast(getContext().getApplicationContext());
                toast.setDuration(Toast.LENGTH_LONG);
                toast.setView(toastLayout);
                toast.show();
            } catch (Exception exception){
                Log.d("View error", "showMessageFailed: " + exception.getMessage());
            }
        }
    }

    protected void showMessageFailed(String message) {
        if(message.length() > 0) {
            try {
                LayoutInflater inflater = getLayoutInflater();
                View toastLayout = inflater.inflate(R.layout.toast_error, (ViewGroup) getActivity().findViewById(R.id.toast_error));
                TextView tv_toast = toastLayout.findViewById(R.id.tv_toast_error);
                tv_toast.setText(message);
                Toast toast = new Toast(getContext().getApplicationContext());
                toast.setDuration(Toast.LENGTH_LONG);
                toast.setView(toastLayout);
                toast.show();
            } catch (Exception exception){
                Log.d("View error", "showMessageFailed: " + exception.getMessage());
            }
        }
    }

    protected void showMessage(Boolean status) {
        if(status){
            showMessageSuccess(getViewModel().getMessage());
        } else{
            showMessageFailed(getViewModel().getMessage());
        }
    }


}
