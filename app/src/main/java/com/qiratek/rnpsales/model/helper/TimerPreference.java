package com.qiratek.rnpsales.model.helper;

import android.content.Context;
import android.content.SharedPreferences;

import com.qiratek.rnpsales.model.entity.CheckInTimer;

public class TimerPreference {

    private static final String PREFS_NAME = "timer_pref";
    private static final String FIRST_TIME = "isFirstTime";
    private static final String IS_RUNNING = "isRunning";
    private static final String REMAINING_TIME = "remainingTime";
    private static final String END_TIME = "endTime";
    private static final String TAG = "Trace " + TimerPreference.class.getSimpleName();

    private final SharedPreferences preferences;
    private boolean isFirstTime;

    public TimerPreference(Context context) {
        preferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        isFirstTime = preferences.getBoolean(FIRST_TIME, true);
    }

    public void initTimerPreference() {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean(IS_RUNNING, false);
        editor.putLong(REMAINING_TIME, 0);
        editor.putLong(END_TIME, 0);
        editor.apply();
    }

    public void clearTimerPreference(boolean isRunning, long remainingTime, long endTime) {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean(IS_RUNNING, isRunning);
        editor.putLong(REMAINING_TIME, remainingTime);
        editor.putLong(END_TIME, endTime);
        editor.apply();
    }

    public void setTimerPreference(CheckInTimer model) {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean(IS_RUNNING, model.isRunning());
        editor.putLong(REMAINING_TIME, model.getRemainingTime());
        editor.putLong(END_TIME, model.getEndTime());
        editor.apply();
    }

    public CheckInTimer getTimerPreference() {
        CheckInTimer model = new CheckInTimer();
        model.setRunning(preferences.getBoolean(IS_RUNNING, false));
        model.setRemainingTime(preferences.getLong(REMAINING_TIME, 0));
        model.setEndTime(preferences.getLong(END_TIME, 0));
        return model;
    }

    public void isPreferenceExist() {
        if (isFirstTime) {
            SharedPreferences.Editor editor = preferences.edit();
            editor.putBoolean(FIRST_TIME, false);
            editor.apply();
            initTimerPreference();
        }
    }
}
