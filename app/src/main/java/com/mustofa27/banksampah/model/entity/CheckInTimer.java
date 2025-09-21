package com.mustofa27.banksampah.model.entity;

import android.os.Parcel;
import android.os.Parcelable;

public class CheckInTimer implements Parcelable {

    private boolean isRunning;
    private long startTime;
    private long endTime;
    private long remainingTime;

    public CheckInTimer() {}

    protected CheckInTimer(Parcel in) {
        isRunning = in.readByte() != 0;
        startTime = in.readLong();
        endTime = in.readLong();
        remainingTime = in.readLong();
    }

    public static final Creator<CheckInTimer> CREATOR = new Creator<CheckInTimer>() {
        @Override
        public CheckInTimer createFromParcel(Parcel in) {
            return new CheckInTimer(in);
        }

        @Override
        public CheckInTimer[] newArray(int size) {
            return new CheckInTimer[size];
        }
    };

    public boolean isRunning() {
        return isRunning;
    }

    public void setRunning(boolean running) {
        isRunning = running;
    }

    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public long getEndTime() {
        return endTime;
    }

    public void setEndTime(long endTime) {
        this.endTime = endTime;
    }

    public long getRemainingTime() {
        return remainingTime;
    }

    public void setRemainingTime(long remainingTime) {
        this.remainingTime = remainingTime;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeByte((byte) (isRunning ? 1 : 0));
        parcel.writeLong(startTime);
        parcel.writeLong(endTime);
        parcel.writeLong(remainingTime);
    }
}
