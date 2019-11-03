package com.dummy.myapplication.ui.utils;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.TextView;

import androidx.databinding.BindingAdapter;

import io.reactivex.rxjava3.subjects.BehaviorSubject;

public class RxUiAdapters {

    @BindingAdapter("rxText")
    public static void rxText(TextView editText, final BehaviorSubject<String> subject) {
        // Initial value
        editText.setText(subject.getValue());

        // Text changes
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                subject.onNext(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }
}
