package com.example.taxiapp.ui.driver;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class DriverHomeViewModel extends ViewModel {
    private final MutableLiveData<Boolean> isActive = new MutableLiveData<>(true);

    public LiveData<Boolean> getIsActive() {
        return isActive;
    }

    public void toggleActive() {
        Boolean current = isActive.getValue();
        isActive.setValue(current != null && !current);
    }

    public void setActive(boolean active) {
        isActive.setValue(active);
    }
}
