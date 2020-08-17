package com.example.baksolapangantembaksenayan.fragment.FragmentAdmin;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.example.baksolapangantembaksenayan.R;
import com.example.baksolapangantembaksenayan.adapter.Adapter_Transaction_Admin;
import com.example.baksolapangantembaksenayan.model.model_order_items;
import com.example.baksolapangantembaksenayan.model.model_transaction;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.stone.vega.library.VegaLayoutManager;

import java.util.ArrayList;


public class OrderFragment extends Fragment {

    @OnClick(R.id.btnPickStatus)
    void clikStatus(){
        String[] option = {"Semua Pesanan","Sedang Proses","Dibatalkan","Selesai"};
        AlertDialog.Builder builder = new AlertDialog.Builder( getContext() );
        builder.setTitle( "Filter Pesanan :" )
                .setItems( option, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (which==0){
                            txtOrderStatus.setText("Semua Pesanan" );
                            adapter.getFilter().filter( "" );
                        }
                        else
                        {
                           String optionClicked = option[which];
                           txtOrderStatus.setText( optionClicked );
                           adapter.getFilter().filter( optionClicked );
                        }
                    }
                } ).show();
    }

    @BindView( R.id. edtShowOrder)
    EditText edtShowOrder;

    @BindView( R.id. txtOrderStatus)
    TextView txtOrderStatus;

    @BindView( R.id. rv_all_order)
    RecyclerView rv_all_order;

    private ArrayList<model_transaction> listModel;
    private Adapter_Transaction_Admin adapter;
    private LinearLayoutManager llm;


    public OrderFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view =  inflater.inflate( R.layout.fragment_order, container, false );
        ButterKnife.bind( this,view );
        llm = new LinearLayoutManager( getContext() );
        rv_all_order.setLayoutManager( new VegaLayoutManager() );
        loadOrderFromUser();
        return view;
    }

    private void loadOrderFromUser() {
        listModel = new ArrayList<>(  );
        DatabaseReference reff = FirebaseDatabase.getInstance().getReference("DataOrder");
        reff.child( "Order" ).addValueEventListener( new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                listModel.clear();
                for(DataSnapshot ds: snapshot.getChildren()){
                    model_transaction model = ds.getValue(model_transaction.class);
                    listModel.add( model );
                }
                adapter = new Adapter_Transaction_Admin( getContext(),listModel );
                rv_all_order.setAdapter( adapter );
                adapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        } );
    }
}