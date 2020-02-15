package com.app.workschedule.Utils;

import android.content.Context;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.view.ContextThemeWrapper;

import com.app.workschedule.R;

public class CustomProgressBar {

    Context context;
    static CustomProgressBar instance;
    AlertDialog alertDialog;

    private CustomProgressBar(Context context) {
        this.context = context;
    }

    public static synchronized CustomProgressBar getInstance(Context context) {
        if(instance == null) {
            instance = new CustomProgressBar(context);
        }
        return instance;
    }

    public void showDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(context, android.R.style.Theme_Material_Light_NoActionBar));
        builder.setCancelable(false);
        builder.setView(R.layout.progress_container);

        alertDialog = builder.create();
        alertDialog.setCancelable(false);
        alertDialog.show();
    }

    public void addActivity(Context context) {
        this.context = context;
    }
    public void dismissDialog() {
        alertDialog.dismiss();
    }

    public boolean isDialogShowing() {
        return alertDialog.isShowing();
    }

}