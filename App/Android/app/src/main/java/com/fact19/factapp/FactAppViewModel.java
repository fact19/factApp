package com.fact19.factapp;

import androidx.lifecycle.ViewModel;

public class FactAppViewModel extends ViewModel {

    public boolean handTrackerStatus = false;

    public boolean isHandTrackerStatus() {
        return handTrackerStatus;
    }

    public void setHandTrackerStatus(boolean handTrackerStatus) {
        this.handTrackerStatus = handTrackerStatus;
    }


}
