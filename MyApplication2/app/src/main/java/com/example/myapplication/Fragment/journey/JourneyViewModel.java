package com.example.myapplication.Fragment.journey;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class JourneyViewModel extends ViewModel {
    private final MutableLiveData<String> mText;

    public JourneyViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is dashboard fragment");
    }
}