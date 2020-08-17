package com.example.baksolapangantembaksenayan.activity.Admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.example.baksolapangantembaksenayan.R;
import com.example.baksolapangantembaksenayan.constants.Constants;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.messaging.FirebaseMessaging;

public class SettingActivity extends AppCompatActivity {

    @OnClick(R.id.backBtn)
    void gotoback() {
        onBackPressed();
    }

    @BindView(R.id.switchSetting)
    Switch fcmSwitch;

    @BindView(R.id.statusNotifikasi)
    TextView statusNotifikasi;


    private static final String enableNotification = "Notifikasi dihidupkan";
    private static final String disableNotification = "Notifikasi dimatikan";
    private FirebaseAuth firebaseAuth;
    private SharedPreferences sp;
    private SharedPreferences.Editor spEditor;
    private Boolean isChecked = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_setting );
        ButterKnife.bind( this );
        firebaseAuth = FirebaseAuth.getInstance();
        sp = getSharedPreferences( "SETTINGS_SP",MODE_PRIVATE );
        isChecked = sp.getBoolean( "FCM_ENABLED",false );
        fcmSwitch.setChecked(isChecked);
        if (isChecked){
            statusNotifikasi.setText( enableNotification );
        }else {
            statusNotifikasi.setText( disableNotification );
        }

        fcmSwitch.setOnCheckedChangeListener( new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    subscribeToTopic();
                } else {
                    unSubscribeToTopic();
                }
            }
        } );
    }

    private void subscribeToTopic() {
        FirebaseMessaging.getInstance().subscribeToTopic( Constants.FCM_TOPIC )
                .addOnSuccessListener( new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        spEditor = sp.edit();
                        spEditor.putBoolean( "FCM_ENABLED", true );
                        spEditor.apply();
                        Toast.makeText( SettingActivity.this, ""+enableNotification, Toast.LENGTH_SHORT ).show();
                        statusNotifikasi.setText( enableNotification );
                    }
                } ).addOnFailureListener( new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText( SettingActivity.this, ""+e.getMessage(), Toast.LENGTH_SHORT ).show();
            }
        } );
    }

    private void unSubscribeToTopic() {
        FirebaseMessaging.getInstance().unsubscribeFromTopic( Constants.FCM_TOPIC )
                .addOnSuccessListener( new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        spEditor = sp.edit();
                        spEditor.putBoolean( "FCM_ENABLED", false );
                        spEditor.apply();
                        Toast.makeText( SettingActivity.this, ""+disableNotification, Toast.LENGTH_SHORT ).show();
                        statusNotifikasi.setText( disableNotification );

                    }
                } ).addOnFailureListener( new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText( SettingActivity.this, ""+e.getMessage(), Toast.LENGTH_SHORT ).show();
            }
        } );
    }
}