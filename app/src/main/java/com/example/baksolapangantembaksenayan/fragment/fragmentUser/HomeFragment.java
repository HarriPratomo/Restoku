package com.example.baksolapangantembaksenayan.fragment.fragmentUser;

import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.baksolapangantembaksenayan.R;
import com.example.baksolapangantembaksenayan.adapter.AdapterMenuUser;
import com.example.baksolapangantembaksenayan.constants.Constants;
import com.example.baksolapangantembaksenayan.model.model_menu_admin;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class HomeFragment extends Fragment {

    @BindView(R.id.rv_newMenu)
    RecyclerView rv_newMenu;

    @BindView(R.id.txtKategori)
    TextView txtKategori;

    @BindView(R.id.edtSearch)
    TextView edtSearch;

    @BindView(R.id.emptyData)
    ConstraintLayout emptyData;

    @OnClick(R.id.imgFilter)
    void kategori() {
        AlertDialog.Builder builder = new AlertDialog.Builder( getContext() );
        builder.setTitle( "Pilih Kategori :" )
                .setItems( Constants.kategori_menu, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String selected = Constants.kategori_menu[which];
                        txtKategori.setText( selected );
                        if (selected.equals( "Semua Menu" )) {
                            loadDataMenu();
                        } else {
                            loadFilteredMenu( selected );
                        }
                    }
                } )
                .show();
    }

    private FirebaseAuth firebaseAuth;
    private ArrayList<model_menu_admin> menuList = new ArrayList<>();
    private AdapterMenuUser adapter;

    public HomeFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate( R.layout.fragment_home, container, false );
        ButterKnife.bind( this, view );
        return view;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated( view, savedInstanceState );
        firebaseAuth = FirebaseAuth.getInstance();
        loadDataMenu();
        edtSearch.addTextChangedListener( new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                try {
                    adapter.getFilter().filter( s );
                    emptyData.setVisibility( View.GONE );
                    rv_newMenu.setVisibility( View.VISIBLE );
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        } );
    }

    private void loadFilteredMenu(String selected) {
        DatabaseReference databaseRef = FirebaseDatabase.getInstance().getReference( "DataMenu" );
        databaseRef.child( "Menu" )
                .addValueEventListener( new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        menuList.clear();
                        for (DataSnapshot ds : dataSnapshot.getChildren()) {
                            String productCategory = "" + ds.child( "kategoriMenu" ).getValue();
                            if (selected.equals( productCategory )) {
                                model_menu_admin modelProduct = ds.getValue( model_menu_admin.class );
                                menuList.add( modelProduct );
                            }
                        }
                        adapter = new AdapterMenuUser( getContext(), menuList );
                        rv_newMenu.setAdapter( adapter );
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                } );
    }

    private void loadDataMenu() {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference( "DataMenu" );
        ref.child( "Menu" )
                .addValueEventListener( new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        menuList.clear();
                        for (DataSnapshot ds : snapshot.getChildren()) {
                            model_menu_admin modelProduct = ds.getValue( model_menu_admin.class );
                            menuList.add( modelProduct );
                        }
                        adapter = new AdapterMenuUser( getContext(), menuList );
                        if (adapter.getItemCount() == 0) {
                            emptyData.setVisibility( View.VISIBLE );
                        } else {
                            rv_newMenu.setAdapter( adapter );
                            adapter.notifyDataSetChanged();
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                } );

    }

}


