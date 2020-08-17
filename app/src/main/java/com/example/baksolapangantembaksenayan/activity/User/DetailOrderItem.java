package com.example.baksolapangantembaksenayan.activity.User;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.baksolapangantembaksenayan.R;
import com.example.baksolapangantembaksenayan.adapter.AdapterOrderItems;
import com.example.baksolapangantembaksenayan.adapter.Adapter_Transaction;
import com.example.baksolapangantembaksenayan.model.model_order_items;
import com.example.baksolapangantembaksenayan.model.model_transaction;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.stone.vega.library.VegaLayoutManager;

import java.util.ArrayList;

public class DetailOrderItem extends AppCompatActivity {


    @OnClick(R.id.btnBack)
    void back() {
        onBackPressed();
    }

    @BindView(R.id.rv_items_order)
    RecyclerView rv_items_order;

    @BindView(R.id.txtIdPesanan)
    TextView txtIdPesanan;

    @BindView(R.id.txtTanggalPesan)
    TextView txtTanggalPesan;

    @BindView(R.id.txtStatus)
    TextView txtStatus;

    @BindView(R.id.txtTotalQuantity)
    TextView txtTotalQuantity;

    @BindView(R.id.txtAlamat)
    TextView txtAlamat;

    @BindView(R.id.txtHargaTotal)
    TextView txtHargaTotal;

    @BindView(R.id.txtJamOrder)
    TextView txtJamOrder;

    private String id;
    private FirebaseAuth firebaseAuth;
    private ArrayList<model_order_items> itemList;
    private AdapterOrderItems adapter;
    private LinearLayoutManager llm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_detail_order_item );
        ButterKnife.bind( this );
        Intent intent = getIntent();
        id = intent.getStringExtra( "idOrder" );
        firebaseAuth = firebaseAuth.getInstance();
        llm = new LinearLayoutManager( this );
        rv_items_order.setLayoutManager(llm);
        rv_items_order.setHasFixedSize( true );
        loadOrderDetail();
        loadOrderItems();
    }
    private void loadOrderItems() {
        itemList = new ArrayList<>();
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference( "Users" );
        ref.child( firebaseAuth.getCurrentUser().getUid() ).child( "Order" ).child( id ).child( "Items" )
                .addValueEventListener( new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        itemList.clear();
                        for (DataSnapshot ds : snapshot.getChildren()) {
                            model_order_items model = ds.getValue( model_order_items.class );
                            itemList.add( model );
                        }
                        adapter = new AdapterOrderItems( DetailOrderItem.this, itemList );
                        rv_items_order.setAdapter( adapter );
                        txtTotalQuantity.setText( ""+snapshot.getChildrenCount() );
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                } );
    }

    private void loadOrderDetail() {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference( "Users" );
        ref.child( firebaseAuth.getCurrentUser().getUid() ).child( "Order" ).child( id ).
                addValueEventListener( new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        String idOrder = "" + snapshot.child( "idOrder" ).getValue();
                        String alamat = "" + snapshot.child( "alamat" ).getValue();
                        String statusOrder = "" + snapshot.child( "statusOrder" ).getValue();
                        String jamOrder = "" + snapshot.child( "jamOrder" ).getValue();
                        String tanggalOrder = "" + snapshot.child( "tanggalOrder" ).getValue();
                        String totalPesanan = "" + snapshot.child( "totalPesanan" ).getValue();

                        txtIdPesanan.setText( idOrder );
                        txtAlamat.setText( alamat );
                        txtStatus.setText( statusOrder );
                        txtTanggalPesan.setText( tanggalOrder );
                        txtHargaTotal.setText(  totalPesanan );
                        txtJamOrder.setText( "Jam : " + jamOrder );

                        if (statusOrder.equals( "Sedang Proses" )) {
                            txtStatus.setTextColor( getResources().getColor( R.color.progress ) );
                        } else if (statusOrder.equals( "Selesai" )) {
                            txtStatus.setTextColor( getResources().getColor( R.color.complete ) );
                        }
                        if (statusOrder.equals( "Dibatalkan" )) {
                            txtStatus.setTextColor( getResources().getColor( R.color.red ) );
                        }
                    }


                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                } );
    }
}