package com.fact19.factapp;

import androidx.lifecycle.ViewModel;

public class FactAppViewModel extends ViewModel {

    public int gargleScheduleTime = 0;
    public boolean handTrackerStatus = false;

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
