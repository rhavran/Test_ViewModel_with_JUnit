package com.dummy.myapplication.ui.utils;

import androidx.databinding.Observable;
import androidx.databinding.PropertyChangeRegistry;
import androidx.lifecycle.ViewModel;

public class ObservableViewModel extends ViewModel implements Observable {
    private final PropertyChangeRegistry registry = new PropertyChangeRegistry();

    @Override
    public void addOnPropertyChangedCallback(OnPropertyChangedCallback callback) {
        registry.add(callback);
    }

    @Override
    public void removeOnPropertyChangedCallback(OnPropertyChangedCallback callback) {
        registry.remove(callback);
    }

    protected void notifyChange() {
        registry.notifyCallbacks(this, 0, null);
    }

    protected void notifyPropertyChanged(int fieldId) {
        registry.notifyCallbacks(this, fieldId, null);
    }
}
