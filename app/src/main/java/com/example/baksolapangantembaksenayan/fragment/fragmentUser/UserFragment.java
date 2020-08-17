package com.example.baksolapangantembaksenayan.fragment.fragmentUser;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import butterknife.ButterKnife;
import butterknife.OnClick;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.baksolapangantembaksenayan.R;
import com.example.baksolapangantembaksenayan.activity.Admin.MainAdminActivity;
import com.example.baksolapangantembaksenayan.activity.LoginActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class UserFragment extends Fragment {

    @OnClick(R.id.logout)
    void toLogOut() {
        logOut();
    }

    FirebaseAuth firebaseAuth;




    public UserFragment() {
        // Required empty public constructor
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view =  inflater.inflate( R.layout.fragment_user, container, false );
        ButterKnife.bind( this,view );
        firebaseAuth = FirebaseAuth.getInstance();
        return view;
    }

    private void logOut() {
        firebaseAuth.signOut();
        checkUser();
    }

    private void checkUser() {
        FirebaseUser user = firebaseAuth.getCurrentUser();
        if (user == null) {
            startActivity( new Intent( getContext(), LoginActivity.class ) );
        } else {
            //loadMyInfo();
        }
    }
}