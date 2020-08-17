package com.example.baksolapangantembaksenayan.util;

import android.app.Activity;
import android.app.AlertDialog;
import android.view.LayoutInflater;

import com.example.baksolapangantembaksenayan.R;

public class LoadingDialog {
    private Activity activity;
    private AlertDialog dialog;

    public LoadingDialog(Activity myActivity) {
        activity = myActivity;
    }

    public void startLoadingDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder( activity, R.style.Dialog );

        LayoutInflater layoutInflater = activity.getLayoutInflater();
        builder.setView( layoutInflater.inflate( R.layout.dialog_loading, null ) );
        builder.setCancelable( false );

        dialog = builder.create();
        dialog.show();
    }

    public void dismissDialog(){
        dialog.dismiss();
    }
}
