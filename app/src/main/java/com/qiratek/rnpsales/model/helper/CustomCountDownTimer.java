package com.qiratek.rnpsales.model.helper;

import android.content.Context;
import android.os.CountDownTimer;

import com.qiratek.rnpsales.model.entity.CheckInTimer;

import java.util.Calendar;

public class CustomCountDownTimer extends CountDownTimer {

    private static CustomCountDownTimer instance;
    private TimerPreference timerPreference;
    private CheckInTimer timer;
    private OnTimerTickCallback timerTickCallback;
    private OnTimerTickCallback timerFinishCallback;
    private boolean isRunning;

    public static CustomCountDownTimer getInstance(Context context){
        if(instance == null){
            TimerPreference tmp = new TimerPreference(context);
            CheckInTimer timerTmp = tmp.getTimerPreference();
            long mRemainingTime = timerTmp.getEndTime() - Calendar.getInstance().getTimeInMillis();
            if(timerTmp.getRemainingTime() > mRemainingTime){
                timerTmp.setRemainingTime(mRemainingTime);
            }
            instance = new CustomCountDownTimer(timerTmp.getRemainingTime(), 1000);
            instance.timerPreference = tmp;
            instance.timer = timerTmp;
            instance.isRunning = false;
        }
        return instance;
    }

    private CustomCountDownTimer(long millisInFuture, long countDownInterval) {
        super(millisInFuture, countDownInterval);
    }

    @Override
    public void onTick(long l) {
        timer.setRemainingTime(l);
        timerPreference.setTimerPreference(timer);
        if(timerTickCallback != null){
            timerTickCallback.onTick(l);
        }
    }

    @Override
    public void onFinish() {
        timer.setRemainingTime(-1);
        timerPreference.setTimerPreference(timer);
        if(timerFinishCallback != null){
            timerFinishCallback.onTick(-1);
        }
    }

    public OnTimerTickCallback getTimerTickCallback() {
        return timerTickCallback;
    }

    public void setTimerTickCallback(OnTimerTickCallback timerTickCallback) {
        this.timerTickCallback = timerTickCallback;
    }

    public boolean isRunning() {
        return isRunning;
    }

    public void setRunning(boolean running) {
        isRunning = running;
    }

    public OnTimerTickCallback getTimerFinishCallback() {
        return timerFinishCallback;
    }

    public void setTimerFinishCallback(OnTimerTickCallback timerFinishCallback) {
        this.timerFinishCallback = timerFinishCallback;
    }
}
