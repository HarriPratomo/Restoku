package com.example.baksolapangantembaksenayan.util;

import android.app.Activity;
import android.app.AlertDialog;
import android.view.LayoutInflater;

import com.example.baksolapangantembaksenayan.R;

public class LoadingDialogLogout {
    private Activity activity;
    private AlertDialog dialog;

    public LoadingDialogLogout(Activity myActivity) {
        activity = myActivity;
    }

    public void startLoadingDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder( activity, R.style.Dialog );

        LayoutInflater layoutInflater = activity.getLayoutInflater();
        builder.setView( layoutInflater.inflate( R.layout.dialog_logout, null ) );
        builder.setCancelable( false );

        dialog = builder.create();
        dialog.show();
    }

    public void dismissDialog(){
      dialog.dismiss();
    }
}
