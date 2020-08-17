package com.example.baksolapangantembaksenayan.activity.User;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import p32929.androideasysql_library.Column;
import p32929.androideasysql_library.EasyDB;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.baksolapangantembaksenayan.R;
import com.example.baksolapangantembaksenayan.activity.Admin.MainAdminActivity;
import com.example.baksolapangantembaksenayan.activity.Admin.SettingActivity;
import com.example.baksolapangantembaksenayan.activity.CartActivity;
import com.example.baksolapangantembaksenayan.activity.LoginActivity;
import com.example.baksolapangantembaksenayan.adapter.AdapterCart;
import com.example.baksolapangantembaksenayan.fragment.fragmentUser.FragmentHistory;
import com.example.baksolapangantembaksenayan.fragment.fragmentUser.FragmentFavorite;
import com.example.baksolapangantembaksenayan.fragment.fragmentUser.HomeFragment;
import com.example.baksolapangantembaksenayan.fragment.fragmentUser.UserFragment;
import com.example.baksolapangantembaksenayan.model.ModelCart;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class MainUserActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {

    @BindView(R.id.bottomNavigationView)
    BottomNavigationView bottomNavigationView;

    @BindView(R.id.frame_navigation)
    FrameLayout frame;
    @BindView(R.id.textView17)
    TextView txtCount;
    @BindView(R.id.indicator)
    ConstraintLayout indicator;
    @BindView(R.id.imageProfile)
    ImageView imageProfile;
    @BindView(R.id.txtNameUser)
    TextView txtNameUser;

    @OnClick(R.id.btnCart)
    void toCart(){
        startActivity( new Intent( MainUserActivity.this, CartActivity.class ) );
    }
    @OnClick(R.id.imageProfile)
    void toProfile(){
        startActivity( new Intent( MainUserActivity.this, EditProfileUser.class ) );
    }
    @OnClick(R.id.btnSetting)
    void gotoSetting() {
        startActivity( new Intent( MainUserActivity.this, SettingActivity.class ) );
    }


    private EasyDB easyDB;
    private ArrayList<ModelCart> modelCartList = new ArrayList<>(  );
    private AdapterCart adapterCart;
    private FirebaseAuth firebaseAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_main_user );
        ButterKnife.bind( this );
        firebaseAuth = FirebaseAuth.getInstance();
        easyDB = EasyDB.init( this, "ITEMS_DB" )
                .setTableName( "ITEMS_TABLE" )
                .addColumn( new Column( "Item_Id", new String[]{"text", "unique"} ) )
                .addColumn( new Column( "Item_PID", new String[]{"text", "not null"} ) )
                .addColumn( new Column( "Item_Nama", new String[]{"text", "not null"} ) )
                .addColumn( new Column( "Item_Harga_Awal", new String[]{"text", "not null"} ) )
                .addColumn( new Column( "Item_Total_Harga", new String[]{"text", "not null"} ) )
                .addColumn( new Column( "Item_Quantity", new String[]{"text", "not null"} ) )
                .doneTableColumn();

        Cursor res = easyDB.getAllData();
        while (res.moveToNext()) {
            String id = res.getString( 0 );
            String IdProduk = res.getString( 1 );
            String nama = res.getString( 2 );
            String harga = res.getString( 3 );
            String total_harga = res.getString( 4 );
            String quantity = res.getString( 5 );

            ModelCart modelCart = new ModelCart( "" + id, "" + IdProduk,
                    "" + nama, "" + harga, ""
                    + total_harga, "" + quantity);
            modelCartList.add( modelCart );
        }
        adapterCart = new AdapterCart( this, modelCartList );
        adapterCart.notifyDataSetChanged();
        loadFragment( new HomeFragment());
        deleteCartData();
        cartCount();
        checkUser();
        bottomNavigationView.setOnNavigationItemSelectedListener(  this );
    }

    private void checkUser() {
        FirebaseUser user = firebaseAuth.getCurrentUser();
        if (user == null) {
            startActivity( new Intent( MainUserActivity.this, LoginActivity.class ) );
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
                             String profileImage = "" + ds.child( "gambar_profile" ).getValue();

                             txtNameUser.setText( "Halo, "+name );
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


    private void deleteCartData() {
        easyDB.deleteAllDataFromTable();
    }

    public void cartCount() {
        int count = easyDB.getAllData().getCount();
        if (count<=0){
            indicator.setVisibility( View.GONE );
            txtCount.setText( "0 Pesanan" );
        }else {
            indicator.setVisibility( View.VISIBLE );
            txtCount.setVisibility( View.VISIBLE );
            txtCount.setText( ""+count+" Pesanan" );
        }
    }

    private boolean loadFragment(Fragment fragment) {
        if (fragment != null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace( R.id.frame_navigation, fragment )
                    .commit();
            return true;
        }
        return false;
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        Fragment fragment = null;

        switch (item.getItemId()) {
            case R.id.action_home:
                fragment = new HomeFragment();
                break;
            case R.id.action_fav:
                fragment = new FragmentFavorite();
                break;
            case R.id.action_history:
                fragment = new FragmentHistory();
                break;
            case R.id.action_user:
                fragment = new UserFragment();
                break;

        }

        return loadFragment(fragment);
    }

    @Override
    protected void onResume() {
        super.onResume();
        cartCount();
    }
}