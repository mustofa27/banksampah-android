package com.qiratek.rnpsales.view.activity;

import androidx.appcompat.app.ActionBar;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.qiratek.rnpsales.R;
import com.qiratek.rnpsales.databinding.ActivityCountDownTimerBinding;
import com.qiratek.rnpsales.model.entity.CheckInTimer;
import com.qiratek.rnpsales.model.entity.VisitPlanDb;
import com.qiratek.rnpsales.model.helper.CustomCountDownTimer;
import com.qiratek.rnpsales.model.helper.TimerPreference;
import com.qiratek.rnpsales.view.BaseActivity;
import com.qiratek.rnpsales.viewmodel.BaseViewModel;
import com.qiratek.rnpsales.viewmodel.CountDownTimerViewModel;
import com.qiratek.rnpsales.viewmodel.CustomViewModelFactory;

import java.util.Calendar;
import java.util.Locale;

public class CountDownTimerActivity extends BaseActivity {

    ActivityCountDownTimerBinding binding;
    CountDownTimerViewModel viewModel;
    TimerPreference timerPreference;
    CheckInTimer countdownTimer;
    static boolean isActiveActivity = false;
    private boolean isRunning;
    private CustomCountDownTimer mCountDownTimerLive;
    private long mStartTime;
    private long mEndTime;
    private long mRemainingTime;
    VisitPlanDb visitPlanDb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCountDownTimerBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        viewModel = new ViewModelProvider(this, new CustomViewModelFactory(this)).get(CountDownTimerViewModel.class);
        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
            getSupportActionBar().setCustomView(R.layout.main_topbar);
            ((TextView) getSupportActionBar().getCustomView().findViewById(R.id.title)).setText("COUNTDOWN TIMER");
            (getSupportActionBar().getCustomView().findViewById(R.id.back)).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onBackPressed();
                }
            });
        }
        isActiveActivity = true;
        visitPlanDb = (VisitPlanDb) getIntent().getSerializableExtra("data");
        timerPreference = new TimerPreference(this);
        countdownTimer = timerPreference.getTimerPreference();
        mRemainingTime = countdownTimer.getRemainingTime();
        long tmp = countdownTimer.getEndTime() - Calendar.getInstance().getTimeInMillis();
        if(mRemainingTime > tmp){
            mRemainingTime = tmp;
        }
        binding.btnClear.setOnClickListener(view -> {
            Intent intent = new Intent(CountDownTimerActivity.this, CheckoutActivity.class);
            intent.putExtra("data", visitPlanDb);
            startActivity(intent);
            finish();
        });
        runLocalTimer();
        //initObserver();
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
    protected void initObserver() {
        viewModel.getStatus().observe(this, status -> {
            viewModel.getLoading().setValue(false);
            if(!status) {
                showMessage(status);
            }
        });
        viewModel.getLoading().observe(this, loading -> {
            showLoading(loading);
        });
        /*viewModel.getCheckInTimerLiveData().observe(this, data -> {
            try {
                showLoading(false);
                if(data != null) {
                    mStartTime = Long.parseLong(data.getString("epcstart") + "000");
                    isRunning = countdownTimer.isRunning();
                    if (isRunning) {
                        mEndTime = countdownTimer.getEndTime();
                        mRemainingTime = mEndTime - mStartTime;

                        if (mRemainingTime < 0) {
                            mRemainingTime = 0;
                            isRunning = false;
                            updateLiveCountDownText();

                            timerPreference.clearTimerPreference(false, 0, 0);
                            if (isActiveActivity) {
                                if (mCountDownTimerLive != null) {
                                    mCountDownTimerLive.cancel();
                                }
                                binding.btnClear.setVisibility(View.VISIBLE);
                            }
                        } else {
                            runLiveTimer();
                        }
                    } else {
                        mEndTime = Long.parseLong(data.getString("epcend") + "000");
                        mRemainingTime = mEndTime - mStartTime;
                        runLiveTimer();
                    }
                } else{
                    mStartTime = Calendar.getInstance().getTimeInMillis();
                    isRunning = countdownTimer.isRunning();
                    mEndTime = countdownTimer.getEndTime();
                    mRemainingTime = mEndTime - mStartTime;
                    if (isRunning) {

                        if (mRemainingTime < 0) {
                            mRemainingTime = 0;
                            isRunning = false;
                            updateLiveCountDownText();

                            timerPreference.clearTimerPreference(false, 0, 0);
                            if (isActiveActivity) {
                                if (mCountDownTimerLive != null) {
                                    mCountDownTimerLive.cancel();
                                }
                                binding.btnClear.setVisibility(View.VISIBLE);
                            }
                        } else {
                            runLiveTimer();
                        }
                    } else {
                        runLiveTimer();
                    }
                }
            } catch (JSONException e) {
                showMessageFailed("Json Error");
                e.printStackTrace();
            }
        });*/
    }

    private void runLocalTimer(){
        if (mRemainingTime < 0) {
            mRemainingTime = 0;
            isRunning = false;
            updateLiveCountDownText();

            timerPreference.clearTimerPreference(false, 0, 0);
            if (isActiveActivity) {
                binding.btnClear.setVisibility(View.VISIBLE);
            }
        } else {
            mCountDownTimerLive = CustomCountDownTimer.getInstance(getApplicationContext());
            if(mCountDownTimerLive.getTimerTickCallback() == null){
                mCountDownTimerLive.setTimerTickCallback(mRemainingTime -> {
                    this.mRemainingTime = mRemainingTime;
                    updateLiveCountDownText();
                });
            }
            if(mCountDownTimerLive.getTimerFinishCallback() == null){
                mCountDownTimerLive.setTimerFinishCallback(mRemainingTime -> {
                    if(mRemainingTime <= 0){
                        binding.btnClear.setVisibility(View.VISIBLE);
                        timerPreference.clearTimerPreference(false, 0,0);
                        isRunning = false;
                    }
                });
            }
            if(!mCountDownTimerLive.isRunning()){
                mCountDownTimerLive.start();
                mCountDownTimerLive.setRunning(true);
            }
        }
    }

    /*private void runLiveTimer() {
        mCountDownTimerLive = new CountDownTimer(mRemainingTime, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                mRemainingTime = millisUntilFinished;
                updateLiveCountDownText();
            }
            @Override
            public void onFinish() {
                mCountDownTimerLive.cancel();
                timerPreference.clearTimerPreference(false, 0,0);
                isRunning = false;
                binding.btnClear.setVisibility(View.VISIBLE);
            }
        }.start();
        isRunning = true;
    }*/

    private void updateLiveCountDownText() {
        int minutes = (int) (mRemainingTime / 1000) / 60;
        int seconds = (int) (mRemainingTime / 1000) % 60;
        String timeLeftFormatted = String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds);
        binding.textViewCountdownLive.setText(timeLeftFormatted);
    }

    @Override
    protected void onPause() {
        super.onPause();
        isActiveActivity = false;
    }

    @Override
    protected void onResume() {
        super.onResume();
        isActiveActivity = true;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        /*countdownTimer.setRunning(isRunning);
        countdownTimer.setRemainingTime(mRemainingTime);
        countdownTimer.setEndTime(mEndTime);
        timerPreference.setTimerPreference(countdownTimer);*/
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        /*countdownTimer.setRunning(isRunning);
        countdownTimer.setRemainingTime(mRemainingTime);
        countdownTimer.setEndTime(mEndTime);
        timerPreference.setTimerPreference(countdownTimer);
        isActiveActivity = false;
        mCountDownTimerLive.cancel();*/
        isActiveActivity = false;
        try {
            mCountDownTimerLive.setTimerTickCallback(null);
            mCountDownTimerLive.setTimerFinishCallback(null);
        } catch (Exception ex){

        }
    }
}