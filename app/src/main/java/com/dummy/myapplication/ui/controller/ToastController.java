package com.dummy.myapplication.ui.controller;

import android.content.Context;
import android.widget.Toast;

public class ToastController {
    private Context appContext;

    public ToastController(Context context) {
        appContext = context.getApplicationContext();
    }

    public void showToast(int string) {
        Toast.makeText(appContext, string, Toast.LENGTH_SHORT).show();
    }

    public void showToast(int string, String... params) {
        Toast.makeText(appContext, appContext.getString(string, params), Toast.LENGTH_SHORT).show();
    }
}
