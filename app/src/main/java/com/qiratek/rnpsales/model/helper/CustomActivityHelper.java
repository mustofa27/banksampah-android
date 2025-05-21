package com.qiratek.rnpsales.model.helper;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.location.Location;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;


import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.text.DateFormatSymbols;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Calendar;

public class CustomActivityHelper extends AppCompatActivity {

    protected String getVideoId(String link) {
        link = link.replace("https://", "");
        link = link.replace("http://", "");
        return link.split("/")[1];
    }

    protected boolean isStringNotEmpty(String string) {
        if(string == null){
            return false;
        }
        return string.trim().length() != 0;
    }

    protected boolean isStringNotEmpty(TextView textView) {
        return textView.getText().toString().trim().length() != 0;
    }

    protected boolean isStringNotEmpty(EditText editText) {
        return editText.getText().toString().trim().length() != 0;
    }

    protected Uri getResizedImage(Context context, Uri mCurrentPhotoPath, String name) throws IOException, URISyntaxException {
        Bitmap tmp = MediaStore.Images.Media.getBitmap(context.getContentResolver(), mCurrentPhotoPath);
        if (tmp != null) {
            tmp = getSignedBitmap(tmp);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            tmp.compress(Bitmap.CompressFormat.JPEG, 50, baos);
            String path = MediaStore.Images.Media.insertImage(context.getContentResolver(), tmp, name, null);
            return Uri.parse(path);
        }
        return null;
    }

    protected Bitmap rotateImage(Bitmap source, float angle) {
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(),
                matrix, true);
    }

    protected Bitmap getSignedBitmap(Bitmap tmp) {
        int height, width;
        if (tmp.getHeight() > tmp.getWidth()) {
            height = 600 * tmp.getHeight() / tmp.getWidth();
            width = 600;
        } else {
            width = 600 * tmp.getWidth() / tmp.getHeight();
            height = 600;
        }
        tmp = getResizedBitmap(tmp, width, height);
        Calendar now = Calendar.getInstance();
        Canvas canvas = new Canvas(tmp);
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setColor(Color.YELLOW);
        paint.setStyle(Paint.Style.FILL);
        String tanggal = (now.get(Calendar.HOUR_OF_DAY) > 9 ? now.get(Calendar.HOUR_OF_DAY) : "0" + now.get(Calendar.HOUR_OF_DAY)) + ":" +
                (now.get(Calendar.MINUTE) > 9 ? now.get(Calendar.MINUTE) : "0" + now.get(Calendar.MINUTE)) + ":" +
                (now.get(Calendar.SECOND) > 9 ? now.get(Calendar.SECOND) : "0" + now.get(Calendar.SECOND)) + " " +
                (now.get(Calendar.DAY_OF_MONTH) > 9 ? now.get(Calendar.DAY_OF_MONTH) : "0" + now.get(Calendar.DAY_OF_MONTH)) + "-" +
                ((now.get(Calendar.MONTH) + 1) > 9 ? (now.get(Calendar.MONTH) + 1) : "0" + (now.get(Calendar.MONTH) + 1)) + "-" +
                (now.get(Calendar.YEAR) > 9 ? now.get(Calendar.YEAR) : "0" + now.get(Calendar.YEAR));
        float size = 3 * (height > width ? height : width) / 100;
        paint.setTextSize(size);
        canvas.drawText(tanggal, tmp.getWidth() - (paint.measureText(tanggal) + size), tmp.getHeight() - size, paint);
        return tmp;
    }

    protected Bitmap getResizedBitmap(Bitmap bm, int newWidth, int newHeight) {
        int width = bm.getWidth();
        int height = bm.getHeight();
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleHeight);
        Bitmap resizedBitmap = Bitmap.createBitmap(
                bm, 0, 0, width, height, matrix, false);
        bm.recycle();
        return resizedBitmap;
    }

    protected String getDate(String date) {
        String regex = date.contains("T") ? "T" : " ";
        String[] tmp = date.split(regex);
        String[] tgl = tmp[0].split("-");
        String tanggal = "" + Integer.valueOf(tgl[2]) + " " + new DateFormatSymbols().getShortMonths()[Integer.valueOf(tgl[1]) - 1] + " " + Integer.valueOf(tgl[0]);
        return tanggal;
    }

    protected String getDateTime(String date) {
        String regex = date.contains("T") ? "T" : " ";
        String[] tmp = date.split(regex);
        String[] tgl = tmp[0].split("-");
        String tanggal = "" + Integer.valueOf(tgl[2]) + " " + new DateFormatSymbols().getShortMonths()[Integer.valueOf(tgl[1]) - 1] + " " + Integer.valueOf(tgl[0]);
        return tanggal + " " + tmp[1];
    }

    protected boolean isDateTodayOrAfter(String date) {
        String regex = date.contains("T") ? "T" : " ";
        String[] tmp = date.split(regex);
        String[] tgl = tmp[0].split("-");
        Calendar now = Calendar.getInstance();
        Calendar temp = Calendar.getInstance();
        temp.set(Calendar.DAY_OF_MONTH, Integer.valueOf(tgl[2]));
        temp.set(Calendar.MONTH, Integer.valueOf(tgl[1]) - 1);
        temp.set(Calendar.YEAR, Integer.valueOf(tgl[0]));
        return temp.getTimeInMillis() >= now.getTimeInMillis();
    }

    protected boolean isDateTodayOrBefore(String date) {
        String regex = date.contains("T") ? "T" : " ";
        String[] tmp = date.split(regex);
        String[] tgl = tmp[0].split("-");
        Calendar temp = Calendar.getInstance();
        Calendar now = Calendar.getInstance();
        temp.set(Calendar.DAY_OF_MONTH, Integer.valueOf(tgl[2]));
        temp.set(Calendar.MONTH, Integer.valueOf(tgl[1]) - 1);
        temp.set(Calendar.YEAR, Integer.valueOf(tgl[0]));
        return temp.getTimeInMillis() <= now.getTimeInMillis();
    }

    protected boolean isDateToday(String date) {
        String regex = date.contains("T") ? "T" : " ";
        String[] tmp = date.split(regex);
        String[] tgl = tmp[0].split("-");
        Calendar temp = Calendar.getInstance();
        Calendar now = Calendar.getInstance();
        temp.set(Calendar.DAY_OF_MONTH, Integer.valueOf(tgl[2]));
        temp.set(Calendar.MONTH, Integer.valueOf(tgl[1]) - 1);
        temp.set(Calendar.YEAR, Integer.valueOf(tgl[0]));
        temp.set(Calendar.HOUR_OF_DAY, 0);
        temp.set(Calendar.MINUTE, 0 );
        temp.set(Calendar.SECOND, 0);
        temp.set(Calendar.MILLISECOND, 0);
        now.set(Calendar.HOUR_OF_DAY, 0);
        now.set(Calendar.MINUTE, 0 );
        now.set(Calendar.SECOND, 0);
        now.set(Calendar.MILLISECOND, 0);
        return Math.abs(temp.getTimeInMillis()-now.getTimeInMillis()) < 24*60*60*1000;
    }

    protected String getDateFormal(Calendar calendar) {
        String tanggal = "" + calendar.get(Calendar.YEAR) + "-" + (calendar.get(Calendar.MONTH) + 1) + "-" +
                calendar.get(Calendar.DAY_OF_MONTH) + " " + calendar.get(Calendar.HOUR_OF_DAY) + ":" + calendar.get(Calendar.MINUTE) +
                ":" + calendar.get(Calendar.SECOND);
        return tanggal;
    }

    protected String getCurrentDate() {
        Calendar calendar = Calendar.getInstance();
        String tanggal = "" + calendar.get(Calendar.DAY_OF_MONTH) + " " +
                new DateFormatSymbols().getMonths()[calendar.get(Calendar.MONTH)] + " " + calendar.get(Calendar.YEAR);
        return tanggal;
    }

    protected String getDate(Calendar calendar) {
        String tanggal = "" + calendar.get(Calendar.DAY_OF_MONTH) + " " +
                new DateFormatSymbols().getMonths()[calendar.get(Calendar.MONTH)] + " " + calendar.get(Calendar.YEAR);
        return tanggal;
    }

    protected String getCurrentDateFormal() {
        Calendar calendar = Calendar.getInstance();
        return getDateFormal(calendar);
    }

    protected String getCurrentDateOnly() {
        Calendar calendar = Calendar.getInstance();
        int month = (calendar.get(Calendar.MONTH) + 1);
        int date = calendar.get(Calendar.DAY_OF_MONTH);
        String tanggal = date < 10 ? "0" + date : String.valueOf(date);
        String bulan = month < 10 ? "0" + month : String.valueOf(date);
        String tanggalFix = "" + calendar.get(Calendar.YEAR) + "-" + bulan + "-" +
                tanggal;
        return tanggalFix;
    }

    protected double calculateDistance(Location location, Location destination) {
        return location.distanceTo(destination);
    }

    private static double getRadian(double degree) {
        return degree * (Math.PI / 180);
    }

    protected void selectFile(Activity activity, int reqCode){
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        activity.startActivityForResult(Intent.createChooser(intent, "Select a File to Upload"), reqCode);
    }

    @SuppressLint("Range")
    protected String getFileName(Uri uri, Activity activity) {
        String result = null;
        if (uri.getScheme().equals("content")) {
            Cursor cursor = activity.getContentResolver().query(uri, null, null, null, null);
            try {
                if (cursor != null && cursor.moveToFirst()) {
                    result = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                }
            } finally {
                cursor.close();
            }
        }
        if (result == null) {
            result = uri.getPath();
            int cut = result.lastIndexOf('/');
            if (cut != -1) {
                result = result.substring(cut + 1);
            }
        }
        return result;
    }

    protected void hideKeyboardFrom(Context context, View view) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Activity.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    protected String getMoneyFormat(long value){
        NumberFormat format = new DecimalFormat("#,###");
        format.setMaximumFractionDigits(0);
        return "Rp " + format.format(value);
    }

    protected int diffToToday(String date) {
        String regex = date.contains("T") ? "T" : " ";
        String[] tmp = date.split(regex);
        String[] tgl = tmp[0].split("-");
        Calendar temp = Calendar.getInstance();
        Calendar now = Calendar.getInstance();
        temp.set(Calendar.DAY_OF_MONTH, Integer.valueOf(tgl[2]));
        temp.set(Calendar.MONTH, Integer.valueOf(tgl[1]) - 1);
        temp.set(Calendar.YEAR, Integer.valueOf(tgl[0]));
        temp.set(Calendar.HOUR_OF_DAY, 0);
        temp.set(Calendar.MINUTE, 0 );
        temp.set(Calendar.SECOND, 0);
        temp.set(Calendar.MILLISECOND, 0);
        now.set(Calendar.HOUR_OF_DAY, 0);
        now.set(Calendar.MINUTE, 0 );
        now.set(Calendar.SECOND, 0);
        now.set(Calendar.MILLISECOND, 0);
        long selisih = Math.abs(temp.getTimeInMillis()-now.getTimeInMillis());
        selisih = selisih / (24*60*60*1000);
        return (int)selisih;
    }

    protected String getTimePosted(String time){
        String regex = time.contains("T") ? "T" : " ";
        String tmp = "";
        int diff = diffToToday(time);
        if(isDateToday(time)){
            tmp += "Hari ini";
        } else if(diff < 7){
            tmp += diff + " hari yang lalu";
        } else if(diff < 30){
            tmp += diff/7 + " minggu yang lalu";
        } else{
            tmp += getDate(time);
        }
        tmp += " " + time.split(regex)[1].substring(0, 5);
        return tmp;
    }

    protected String getDateNow() {
        Calendar calendar = Calendar.getInstance();
        String tanggal = "" + calendar.get(Calendar.DAY_OF_MONTH) + " " + new DateFormatSymbols().getShortMonths()[calendar.get(Calendar.MONTH)] + " " + calendar.get(Calendar.YEAR);
        return tanggal;
    }

    protected String getTahun() {
        Calendar calendar = Calendar.getInstance();
        return " " + calendar.get(Calendar.YEAR);
    }

    protected String getDateNowFormal() {
        Calendar calendar = Calendar.getInstance();
        String tanggal = "" + calendar.get(Calendar.YEAR) + "-" + (calendar.get(Calendar.MONTH) + 1) + "-" + calendar.get(Calendar.DAY_OF_MONTH);
        return tanggal;
    }

    protected String getTimeNow() {
        Calendar calendar = Calendar.getInstance();
        String jam = "" + calendar.get(Calendar.HOUR_OF_DAY) + ":" + calendar.get(Calendar.MINUTE);
        return jam;
    }

    protected String getTimeSelesai(String jamMulai, int durasi) {
        Calendar calendar = Calendar.getInstance();
        String[] jamPisah = jamMulai.split(":");
        calendar.set(Calendar.HOUR_OF_DAY, Integer.valueOf(jamPisah[0]));
        calendar.set(Calendar.MINUTE, Integer.valueOf(jamPisah[0]));
        calendar.setTimeInMillis(calendar.getTimeInMillis() + durasi * 60 * 60 * 1000);
        String jam = "" + calendar.get(Calendar.HOUR_OF_DAY) + ":" + calendar.get(Calendar.MINUTE);
        return jam;
    }

    protected String getTimeConsul() {
        Calendar calendar = Calendar.getInstance();
        String jam = "" + calendar.get(Calendar.HOUR_OF_DAY) + ":" + calendar.get(Calendar.MINUTE);
        calendar.setTimeInMillis(calendar.getTimeInMillis() + 60 * 60 *1000);
        jam = jam + " - " + calendar.get(Calendar.HOUR_OF_DAY) + ":" + calendar.get(Calendar.MINUTE);
        return jam;
    }
}
