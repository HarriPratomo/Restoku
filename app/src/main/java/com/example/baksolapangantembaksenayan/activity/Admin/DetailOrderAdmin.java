package com.example.baksolapangantembaksenayan.activity.Admin;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.example.baksolapangantembaksenayan.R;
import com.example.baksolapangantembaksenayan.adapter.AdapterOrderItems;
import com.example.baksolapangantembaksenayan.model.model_order_items;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.stone.vega.library.VegaLayoutManager;

import java.util.ArrayList;
import java.util.HashMap;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class DetailOrderAdmin extends AppCompatActivity {


    @OnClick(R.id.btnBack)
    void back() {
        onBackPressed();
    }

    @OnClick(R.id.btnEdit)
    void edit() {
        editOrderStatusDialog();
    }

    @BindView(R.id.rv_items_order)
    RecyclerView rv_items_order;

    @BindView(R.id.txtIdPesanan)
    TextView txtIdPesanan;

    @BindView(R.id.txtNamaCustomers)
    TextView txtNamaCustomers;

    @BindView(R.id.txtTelepon)
    TextView txtTelepon;

    @BindView(R.id.txtEmail)
    TextView txtEmail;

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

    private String id,idUser;
    private FirebaseAuth firebaseAuth;
    private ArrayList<model_order_items> itemList;
    private AdapterOrderItems adapter;
    private LinearLayoutManager llm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_detailorder_admin );
        ButterKnife.bind( this );
        Intent intent = getIntent();
        id = intent.getStringExtra( "idOrder" );
        idUser = intent.getStringExtra( "idUser" );
        firebaseAuth = firebaseAuth.getInstance();
        llm = new LinearLayoutManager( this );
        rv_items_order.setLayoutManager(llm );
        rv_items_order.setHasFixedSize( true );
        loadOrderDetail();
        loadOrderItems();
    }
    private void editOrderStatusDialog(){
        final String [] options = {"Sedang Proses","Selesai","Dibatalkan"};
        AlertDialog.Builder builder = new AlertDialog.Builder( this );
        builder.setTitle( "Ubah Status Order : " )
                .setItems( options, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String selectedOption = options[which];
                        editStatusOrder(selectedOption);
                    }
                } ).show();
    }

    private void editStatusOrder(String selectedOption) {
        HashMap<String,Object> hashmap = new HashMap<>(  );
        hashmap.put( "statusOrder",""+selectedOption );

        DatabaseReference reff = FirebaseDatabase.getInstance().getReference("DataOrder");
        reff.child( "Order" ).child( id ).updateChildren( hashmap )
                .addOnSuccessListener( new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText( DetailOrderAdmin.this, "Status Pesanan "+selectedOption, Toast.LENGTH_SHORT ).show();
                        updateOrderUser(selectedOption);
                    }
                } );
    }

    private void updateOrderUser(String selectedOption) {
        HashMap<String,Object> hashmap = new HashMap<>(  );
        hashmap.put( "statusOrder",""+selectedOption );
        DatabaseReference reff = FirebaseDatabase.getInstance().getReference("Users");
        reff.child( idUser ).child( "Order" ).child( id ).updateChildren( hashmap )
                .addOnSuccessListener( new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {

                    }
                } );
    }

    private void loadOrderItems() {
        itemList = new ArrayList<>();
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference( "DataOrder" );
        ref.child( "Order" ).child( id ).child( "Items" )
                .addValueEventListener( new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        itemList.clear();
                        for (DataSnapshot ds : snapshot.getChildren()) {
                            model_order_items model = ds.getValue( model_order_items.class );
                            itemList.add( model );
                        }
                        adapter = new AdapterOrderItems( DetailOrderAdmin.this, itemList );
                        rv_items_order.setAdapter( adapter );
                        txtTotalQuantity.setText( ""+snapshot.getChildrenCount() );

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                } );
    }

    private void loadOrderDetail() {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference( "DataOrder" );
        ref.child( "Order" ).child( id ).
                addValueEventListener( new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        String idOrder = "" + snapshot.child( "idOrder" ).getValue();
                        String alamat = "" + snapshot.child( "alamat" ).getValue();
                        String statusOrder = "" + snapshot.child( "statusOrder" ).getValue();
                        String jamOrder = "" + snapshot.child( "jamOrder" ).getValue();
                        String tanggalOrder = "" + snapshot.child( "tanggalOrder" ).getValue();
                        String totalPesanan = "" + snapshot.child( "totalPesanan" ).getValue();
                        String email = "" + snapshot.child( "email" ).getValue();
                        String customer = "" + snapshot.child( "nama_customer" ).getValue();

                        txtIdPesanan.setText( idOrder );
                        txtAlamat.setText( alamat );
                        txtStatus.setText( statusOrder );
                        txtTanggalPesan.setText( tanggalOrder );
                        txtEmail.setText( email );
                        txtNamaCustomers.setText( customer );
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