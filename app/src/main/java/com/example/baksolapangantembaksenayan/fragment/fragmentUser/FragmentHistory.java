package com.example.baksolapangantembaksenayan.fragment.fragmentUser;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.baksolapangantembaksenayan.R;
import com.example.baksolapangantembaksenayan.adapter.Adapter_Transaction;
import com.example.baksolapangantembaksenayan.model.model_transaction;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.stone.vega.library.VegaLayoutManager;

import java.util.ArrayList;

public class FragmentHistory extends Fragment {

    @BindView(R.id.rv_transaction)
    RecyclerView rv_transaction;

    @BindView(R.id.loadingProgress)
    ProgressBar loadingProgress;

    @BindView(R.id.emptyData)
    ConstraintLayout emptyData;

    @OnClick(R.id.edtSearchTransaction)
    void chooseDate() {

    }

    private ArrayList<model_transaction> transactionList = new ArrayList<>();
    private Adapter_Transaction adapterTransaction;
    private LinearLayoutManager llm;
    private FirebaseAuth firebaseAuth;

    public FragmentHistory() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate( R.layout.fragment_history, container, false );
        ButterKnife.bind( this, view );
        firebaseAuth = FirebaseAuth.getInstance();
        llm = new LinearLayoutManager( getContext() );
        rv_transaction.setLayoutManager( new VegaLayoutManager() );
        rv_transaction.setHasFixedSize( true );
        loadData();
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated( view, savedInstanceState );
    }

    private void loadData() {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference( "Users" );
        ref.child( firebaseAuth.getCurrentUser().getUid() ).child( "Order" ).orderByChild( "idOrder" ).
                addValueEventListener( new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        transactionList.clear();
                        loadingProgress.setVisibility( View.GONE );
                            if (snapshot.exists()) {
                                for (DataSnapshot ds : snapshot.getChildren()) {
                                    model_transaction transaction = ds.getValue( model_transaction.class );
                                    transactionList.add( transaction );
                                }
                                adapterTransaction = new Adapter_Transaction( getContext(), transactionList );
                                emptyData.setVisibility( View.GONE );
                                rv_transaction.setAdapter( adapterTransaction );
                                adapterTransaction.notifyDataSetChanged();
                            }else {
                                emptyData.setVisibility( View.VISIBLE );
                            }
                        }


                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                } );
//        ref.addValueEventListener( new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                transactionList.clear();
//                for (DataSnapshot ds:snapshot.getChildren()){
//                    String idUser = ""+ds.getRef().getKey();
//                    DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Users").child(idUser).child( "Order" );
//                    ref.orderByChild( "idOrder" )
//                            .addValueEventListener( new ValueEventListener() {
//                                @Override
//                                public void onDataChange(@NonNull DataSnapshot snapshot) {
//                                    loadingProgress.setVisibility( View.GONE );
//                                    if (snapshot.exists()){
//                                        for (DataSnapshot ds: snapshot.getChildren()){
//                                            model_transaction transaction = ds.getValue(model_transaction.class);
//                                            transactionList.add( transaction );
//                                        }
//                                        adapterTransaction = new Adapter_Transaction( getContext(), transactionList );
//                                        rv_transaction.setAdapter( adapterTransaction );
//                                    }
//                                }
//
//                                @Override
//                                public void onCancelled(@NonNull DatabaseError error) {
//
//                                }
//                            } );
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        } );
    }
}