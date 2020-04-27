package com.fact19.factapp;

import androidx.lifecycle.ViewModel;

public class FactAppViewModel extends ViewModel {

    public int handWashScheduleTime = 0;
    public int gargleScheduleTime = 0;
    public boolean handTrackerStatus = false;

    public int getHandWashScheduleTime() {
        return handWashScheduleTime;
    }

    public void setHandWashScheduleTime(int handWashScheduleTime) {
        this.handWashScheduleTime = handWashScheduleTime;
    }

    public int getGargleScheduleTime() {
        return gargleScheduleTime;
    }

    public void setGargleScheduleTime(int gargleScheduleTime) {
        this.gargleScheduleTime = gargleScheduleTime;
    }

    public boolean isHandTrackerStatus() {
        return handTrackerStatus;
    }

    public void setHandTrackerStatus(boolean handTrackerStatus) {
        this.handTrackerStatus = handTrackerStatus;
    }


}
