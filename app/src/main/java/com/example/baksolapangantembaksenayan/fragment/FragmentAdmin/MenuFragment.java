package com.example.baksolapangantembaksenayan.fragment.FragmentAdmin;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.example.baksolapangantembaksenayan.R;
import com.example.baksolapangantembaksenayan.activity.Admin.AddProductAdminActivity;
import com.example.baksolapangantembaksenayan.adapter.AdapterMenuAdmin;
import com.example.baksolapangantembaksenayan.constants.Constants;
import com.example.baksolapangantembaksenayan.model.model_menu_admin;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.stone.vega.library.VegaLayoutManager;

import java.util.ArrayList;

public class MenuFragment extends Fragment {

    @OnClick(R.id.btnAddProduk)
    void addProduct(){
        startActivity( new Intent( getContext(), AddProductAdminActivity.class ) );
    }
    @OnClick( R.id.imageView12 )
    void onFilterProduct() {
        AlertDialog.Builder builder = new AlertDialog.Builder( getContext() );
        builder.setTitle( "Pilih Kategori :" )
                .setItems( Constants.kategori_menu, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String selected = Constants.kategori_menu[which];
                        txtFilter.setText( selected );
                        if (selected.equals( "Semua Menu" )) {
                            loadAllMenu();
                        } else {
                            loadFilteredProduct( selected );
                        }
                    }
                } )
                .show();
    }

    @BindView( R.id.recyclerView )
    RecyclerView rv_menu;
    @BindView( R.id.edtSearch )
    EditText edtSearch;
    @BindView( R.id.txtFilter )
    TextView txtFilter;

    private FirebaseAuth firebaseAuth;
    private ArrayList<model_menu_admin> productsList = new ArrayList<>();
    private AdapterMenuAdmin adapter;
    private LinearLayoutManager llm;

    public MenuFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate( R.layout.fragment_menu, container, false );
        ButterKnife.bind( this,view );
        return view;
    }

    private void loadFilteredProduct(String selected) {
        DatabaseReference databaseRef = FirebaseDatabase.getInstance().getReference( "Users" );
        databaseRef.child( firebaseAuth.getUid() ).child( "Menu" )
                .addValueEventListener( new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        productsList.clear();
                        for (DataSnapshot ds : dataSnapshot.getChildren()) {
                            String productCategory = "" + ds.child( "kategoriMenu" ).getValue();
                            if (selected.equals( productCategory )) {
                                model_menu_admin modelProduct = ds.getValue( model_menu_admin.class );
                                productsList.add( modelProduct );
                            }
                        }
                        adapter = new AdapterMenuAdmin( getContext(), productsList );
                        rv_menu.setAdapter( adapter );
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                } );
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated( view, savedInstanceState );
        firebaseAuth = FirebaseAuth.getInstance();
        llm = new LinearLayoutManager( getContext() );
        rv_menu.setLayoutManager( new VegaLayoutManager() );
        loadAllMenu();
        edtSearch.addTextChangedListener( new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                try {
                    adapter.getFilter().filter( s );
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        } );
    }

    private void loadAllMenu() {
        DatabaseReference databaseRef = FirebaseDatabase.getInstance().getReference( "Users" );
        databaseRef.child( firebaseAuth.getUid() ).child( "Menu" )
                .addValueEventListener( new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        productsList.clear();
                        for (DataSnapshot ds : dataSnapshot.getChildren()) {
                            model_menu_admin modelMenu = ds.getValue( model_menu_admin.class );
                            productsList.add( modelMenu );
                        }
                        adapter = new AdapterMenuAdmin( getContext(), productsList );
                        rv_menu.setAdapter( adapter );
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                } );
    }
}