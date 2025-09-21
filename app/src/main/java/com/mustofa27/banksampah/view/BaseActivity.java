package com.mustofa27.banksampah.view;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DownloadManager;
import android.content.ActivityNotFoundException;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.mustofa27.banksampah.R;

import androidx.activity.result.ActivityResultLauncher;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;


import com.mustofa27.banksampah.model.helper.CustomActivityHelper;
import com.mustofa27.banksampah.viewmodel.BaseViewModel;

import java.io.File;

public abstract class BaseActivity extends CustomActivityHelper {
    protected abstract BaseViewModel getViewModel();
    protected abstract void showLoading(boolean isLoading);
    protected abstract void initObserver();
    public static int SELECT_OUTLET_CODE = 221;
    public static int SELECT_PRODUCT_CODE = 222;
    private Dialog progressDialog;
    ActivityResultLauncher<Intent> activityResultLaunch;

    protected void showMessageSuccess(String message) {
        if(isStringNotEmpty(message)) {
            try {
                LayoutInflater inflater = getLayoutInflater();
                View toastLayout = inflater.inflate(R.layout.toast_ok, findViewById(R.id.toast_ok));
                TextView tv_toast = toastLayout.findViewById(R.id.tv_toast_ok);
                tv_toast.setText(message);
                Toast toast = new Toast(getApplicationContext());
                toast.setDuration(Toast.LENGTH_LONG);
                toast.setView(toastLayout);
                toast.show();
            } catch (Exception exception){
                Log.d("View error", "showMessageFailed: " + exception.getMessage());
            }
        }
    }

    protected void showLoadingDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
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

    protected void showMessageFailed(String message) {
        if(isStringNotEmpty(message)) {
            try {
                LayoutInflater inflater = getLayoutInflater();
                View toastLayout = inflater.inflate(R.layout.toast_error, findViewById(R.id.toast_error));
                TextView tv_toast = toastLayout.findViewById(R.id.tv_toast_error);
                tv_toast.setText(message);
                Toast toast = new Toast(getApplicationContext());
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

    protected boolean isPermissionGranted(String permission){
        return ActivityCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_GRANTED;
    }

    protected void openDownloadedAttachment(final Context context, final long downloadId) {
        DownloadManager downloadManager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
        DownloadManager.Query query = new DownloadManager.Query();
        query.setFilterById(downloadId);
        Cursor cursor = downloadManager.query(query);
        if (cursor.moveToFirst()) {
            @SuppressLint("Range") int downloadStatus = cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_STATUS));
            @SuppressLint("Range") String downloadLocalUri = cursor.getString(cursor.getColumnIndex(DownloadManager.COLUMN_LOCAL_URI));
            @SuppressLint("Range") String downloadMimeType = cursor.getString(cursor.getColumnIndex(DownloadManager.COLUMN_MEDIA_TYPE));
            if ((downloadStatus == DownloadManager.STATUS_SUCCESSFUL) && downloadLocalUri != null) {
                Toast.makeText(this, "Download image berhasil", Toast.LENGTH_LONG).show();
//                openDownloadedAttachment(context, Uri.parse(downloadLocalUri), downloadMimeType);
//                Uri selectedUri = Uri.parse(downloadLocalUri.substring(0, downloadLocalUri.lastIndexOf("PMPSales") + 9));
//                Intent intent = new Intent(Intent.ACTION_VIEW);
//                intent.setDataAndType(selectedUri, "resource/folder");
//                startActivity(intent);
                Intent intent = new Intent();
                intent.setAction(android.content.Intent.ACTION_VIEW);
                intent.setType("image/*");
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
//                startActivity(new Intent(DownloadManager.ACTION_VIEW_DOWNLOADS));
            } else{
                Toast.makeText(this, "Download gagal", Toast.LENGTH_LONG).show();
            }
        }
        cursor.close();
    }

    protected void openDownloadedAttachment(final Context context, Uri attachmentUri, final String attachmentMimeType) {
        if(attachmentUri!=null) {
            // Get Content Uri.
            if (ContentResolver.SCHEME_FILE.equals(attachmentUri.getScheme())) {
                // FileUri - Convert it to contentUri.
                File file = new File(attachmentUri.getPath());
                attachmentUri = FileProvider.getUriForFile(this, "com.qiratekindo.pmpsales.provider", file);
            }

            Intent openAttachmentIntent = new Intent(Intent.ACTION_VIEW);
            openAttachmentIntent.setDataAndType(attachmentUri, attachmentMimeType);
            openAttachmentIntent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            try {
                context.startActivity(openAttachmentIntent);
            } catch (ActivityNotFoundException e) {
                Toast.makeText(context, context.getString(R.string.file_not_found), Toast.LENGTH_LONG).show();
            }
        }
    }

    public ActivityResultLauncher<Intent> getActivityResultLaunch() {
        return activityResultLaunch;
    }

    public void setActivityResultLaunch(ActivityResultLauncher<Intent> activityResultLaunch) {
        this.activityResultLaunch = activityResultLaunch;
    }
}
