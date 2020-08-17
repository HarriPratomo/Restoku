package com.example.baksolapangantembaksenayan.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;
import android.view.WindowManager;

import com.example.baksolapangantembaksenayan.R;
import com.example.baksolapangantembaksenayan.activity.Admin.MainAdminActivity;
import com.example.baksolapangantembaksenayan.activity.User.MainUserActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class SplashActivity extends AppCompatActivity {


    private FirebaseAuth firebaseAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        getWindow().requestFeature( Window.FEATURE_NO_TITLE );
        getWindow().setFlags( WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN );
        setContentView( R.layout.activity_splash );

        firebaseAuth = FirebaseAuth.getInstance();

        new Handler().postDelayed( new Runnable() {
            @Override
            public void run() {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user==null){
                    startActivity( new Intent( SplashActivity.this,LoginActivity.class ) );
                    finish();
                }
                else
                {
                    checkUserType();
                }
            }
        }, 1000 );
    }
    private void checkUserType() {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference( "Users" );
        ref.child( firebaseAuth.getUid() )
                .addListenerForSingleValueEvent( new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        String accountType = "" + dataSnapshot.child( "tipe_akun" ).getValue();
                        if (accountType.equals( "Admin" )) {
                            startActivity( new Intent( SplashActivity.this, MainAdminActivity.class ) );
                            finish();
                        } else {
                            startActivity( new Intent( SplashActivity.this, MainUserActivity.class ) );
                            finish();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                } );
    }

}