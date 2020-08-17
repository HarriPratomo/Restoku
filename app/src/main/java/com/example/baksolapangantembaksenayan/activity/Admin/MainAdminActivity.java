package com.example.baksolapangantembaksenayan.activity.Admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.baksolapangantembaksenayan.R;
import com.example.baksolapangantembaksenayan.activity.LoginActivity;
import com.example.baksolapangantembaksenayan.adapter.TabAdapterAdmin;
import com.example.baksolapangantembaksenayan.util.LoadingDialogLogout;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class MainAdminActivity extends AppCompatActivity {

    @OnClick(R.id.logout)
    void toLogOut() {
        logOut();
    }

    @OnClick(R.id.btnSetting)
    void gotoSetting() {
       startActivity( new Intent( MainAdminActivity.this,SettingActivity.class ) );
    }

    @BindView(R.id.tabLayout2)
    TabLayout tabLayout;

    @BindView(R.id.viewPagerAdmin)
    ViewPager viewPager;

    @BindView(R.id.txtNameAdmin)
    TextView txtNameAdmin;

    @BindView(R.id.imageProfile)
    ImageView imageProfile;

    private FirebaseAuth firebaseAuth;
    private LoadingDialogLogout dialogLogout;
    private TabAdapterAdmin tabsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_main_admin );
        ButterKnife.bind( this );
        firebaseAuth = FirebaseAuth.getInstance();
        dialogLogout = new LoadingDialogLogout( this );
        initTab();
        checkUser();
    }

    @OnClick(R.id.imageProfile)
    void editProfile() {
        Intent intent = new Intent( MainAdminActivity.this, EditProfileAdmin.class );
        startActivity( intent );
    }

    private void initTab() {
        tabLayout.addTab( tabLayout.newTab().setText( "Tambah Menu" ) );
        tabLayout.addTab( tabLayout.newTab().setText( "Order" ) );
        tabsAdapter = new TabAdapterAdmin( getSupportFragmentManager(), tabLayout.getTabCount() );
        viewPager.setAdapter( tabsAdapter );
        viewPager.setOnPageChangeListener( new TabLayout.TabLayoutOnPageChangeListener( tabLayout ) );
        tabLayout.setOnTabSelectedListener( new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem( tab.getPosition() );
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        } );
    }

    private void logOut() {
        dialogLogout.startLoadingDialog();
        firebaseAuth.signOut();
        checkUser();
    }

    private void checkUser() {
        // dialogLogout.dismissDialog();
        FirebaseUser user = firebaseAuth.getCurrentUser();
        if (user == null) {
            startActivity( new Intent( MainAdminActivity.this, LoginActivity.class ) );
            finish();
        } else {
            loadMyInfo();
        }
    }

    private void loadMyInfo() {
        DatabaseReference reff = FirebaseDatabase.getInstance().getReference( "Users" );
        reff.orderByChild( "idUser" ).equalTo( firebaseAuth.getUid() )
                .addValueEventListener( new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot ds : dataSnapshot.getChildren()) {
                            String name = "" + ds.child( "namaDepanUser" ).getValue();
//                            String email = "" + ds.child( "email" ).getValue();
                            String profileImage = "" + ds.child( "gambar_profile" ).getValue();

                            txtNameAdmin.setText( "Halo, " + name );
                            try {
                                Picasso.get().load( profileImage ).into( imageProfile );
                            } catch (Exception e) {
                                imageProfile.setImageResource( R.drawable.ic_baseline_person_24 );
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                } );
    }
}