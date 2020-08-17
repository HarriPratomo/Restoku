package com.example.baksolapangantembaksenayan.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import p32929.androideasysql_library.Column;
import p32929.androideasysql_library.EasyDB;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.baksolapangantembaksenayan.R;
import com.example.baksolapangantembaksenayan.adapter.AdapterCart;
import com.example.baksolapangantembaksenayan.constants.Constants;
import com.example.baksolapangantembaksenayan.model.ModelCart;
import com.example.baksolapangantembaksenayan.util.LoadingDialog;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.stone.vega.library.VegaLayoutManager;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class CartActivity extends AppCompatActivity implements LocationListener {
    private AdapterCart adapter;
    private List<ModelCart> list;
    private RecyclerView rv_cart;
    public TextView Total_harga;
    private LinearLayoutManager llm;
    private LoadingDialog progressDialog;
    private FirebaseAuth firebaseAuth;
    private LocationManager locationManager;
    private double latitude, longitude;
    private static final int LOCATION_REQUEST_CODE = 100;
    private String[] locationPermission;
    private BottomSheetDialog bottomSheetDialog;
    private  EditText telepon,alamat,nama;
    String alamat_s, telepon_s,nama_s;
    private String email,idUser;

    public CartActivity() {
    }

    @OnClick(R.id.btnBack)
    void back() {
        onBackPressed();
    }

    @OnClick(R.id.button)
    void saveOrder() {
        save_order();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_cart );
        ButterKnife.bind( this );
        bottomSheetDialog = new BottomSheetDialog( this );
        firebaseAuth = FirebaseAuth.getInstance();
        locationPermission = new String[]{Manifest.permission.ACCESS_FINE_LOCATION};
        progressDialog = new LoadingDialog( this );
        init();
    }
    private void init() {
        list = new ArrayList<>();
        llm = new LinearLayoutManager( this );
        rv_cart = findViewById( R.id.rv_cart );
        rv_cart.setLayoutManager( new VegaLayoutManager() );
        Total_harga = findViewById( R.id.txtSubTotal );
        initDatabase();
    }
    public double sub_total = 0.00;
    private void initDatabase() {
        EasyDB easyDB = EasyDB.init( this, "ITEMS_DB" )
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
            String id = res.getString( 1 );
            String IdMenu = res.getString( 2 );
            String nama = res.getString( 3 );
            String harga = res.getString( 4 );
            String total_harga = res.getString( 5 );
            String quantity = res.getString( 6 );


            sub_total = sub_total + Double.parseDouble( total_harga );
            ModelCart modelCart = new ModelCart( "" + id, "" + IdMenu,
                    "" + nama, "" + harga, ""
                    + total_harga, "" + quantity );
            list.add( modelCart );
        }
        adapter = new AdapterCart( this, list );
        rv_cart.setAdapter( adapter );
        if (list.size() == 0) {

            rv_cart.setVisibility( View.GONE );

        } else {

            rv_cart.setVisibility(View.VISIBLE );
        }
        adapter.notifyDataSetChanged();
        Total_harga.setText( "Rp. " + sub_total );
    }
    private void save_order() {
        if (list.size()==0){
            Snackbar.make( findViewById( R.id.activity_cart ),"Keranjang belanja kosong  ...",Snackbar.LENGTH_LONG ).show();
            return;
        }
        chooseAddress();
    }



    private void chooseAddress() {
        bottomSheetDialog = new BottomSheetDialog( this,R.style.TransparentDialog );
        View view = LayoutInflater.from( this ).inflate( R.layout.bs_address, null );
        bottomSheetDialog.setContentView( view );
        Button btnKirim = view.findViewById( R.id.btnKirim );
        ImageButton back = view.findViewById( R.id.backBtn );
        ImageButton gps = view.findViewById( R.id.imageGps );

        bottomSheetDialog.setCanceledOnTouchOutside( true );
        bottomSheetDialog.show();

        btnKirim.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alamat = (EditText) view.findViewById( R.id.edtAlamat );
                telepon = (EditText) view.findViewById( R.id.edtTelepon );
                nama = (EditText) view.findViewById( R.id.edtNamaCustomer );
                alamat_s = alamat.getText().toString().trim();
                telepon_s = telepon.getText().toString().trim();
                nama_s = nama.getText().toString().trim();

                if (alamat_s.equals( "" )){
                    Toast.makeText( CartActivity.this, "masukkan nama lengkap", Toast.LENGTH_SHORT ).show();
                    return;
                }
                if (alamat_s.equals( "" )){
                    Toast.makeText( CartActivity.this, "masukkan alamat atau pilih dari GPS", Toast.LENGTH_SHORT ).show();
                    return;
                }
                else if(telepon_s.equals( "" )){
                    Toast.makeText( CartActivity.this, "masukkan nomor telepon ...", Toast.LENGTH_SHORT ).show();
                    return;
                }
                else if(telepon_s.length()<=11){
                    Toast.makeText( CartActivity.this, "nomor telepon terlalu pendek ...", Toast.LENGTH_SHORT ).show();
                    return;
                }
                else {
                    submitOrder();
                }
            }
        } );

        gps.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkLoactionPermission()) {
                    detectLocation();
                } else {
                    requestLocationPermission();
                }
            }
        } );

        back.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomSheetDialog.dismiss();
            }
        } );
    }



    private void submitOrder() {
        progressDialog.startLoadingDialog();
        String currentDate = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());
        String currentTime = new SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(new Date());
        final String timestamp = ""+System.currentTimeMillis();
        String total_harga = Total_harga.getText().toString().trim().replace( "Rp","" );
        email = firebaseAuth.getCurrentUser().getEmail();
        idUser = firebaseAuth.getCurrentUser().getUid();


        HashMap<String,String> hashMap = new HashMap<>(  );
        hashMap.put( "idOrder",""+timestamp );
        hashMap.put( "jamOrder",""+currentTime );
        hashMap.put( "tanggalOrder",""+currentDate );
        hashMap.put( "statusOrder","Sedang Proses" );
        hashMap.put( "email",""+email );
        hashMap.put( "idUser",""+idUser );
        hashMap.put( "nama_customer",""+nama_s );
        hashMap.put( "totalPesanan","Rp"+total_harga );
        hashMap.put( "alamat",""+alamat_s );
        hashMap.put( "telepon",""+telepon_s );

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Users").child( firebaseAuth.getUid() ).child( "Order" );
        ref.child( timestamp ).setValue( hashMap )
                .addOnSuccessListener( new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {

                        for (int i=0;i<list.size();i++){
                            String pId = list.get( i ).getItemId();
                            String idProduk = list.get( i ).getIdMenu();
                            String namaProduk = list.get( i ).getNama_produk();
                            String hargaProduk = list.get( i ).getHarga_produk();
                            String quantity = list.get( i ).getQuantity_produk();
                            String total_harga = list.get( i ).getTotal_harga_produk();

                            HashMap<String,String> hashMap1 = new HashMap<>(  );
                            hashMap1.put( "pId",pId );
                            hashMap1.put( "namaProduk",namaProduk );
                            hashMap1.put( "hargaProduk",hargaProduk );
                            hashMap1.put( "quantity",quantity );
                            hashMap1.put( "total_harga",total_harga );

                            ref.child( timestamp ).child( "Items" ).child( pId ).setValue( hashMap1 );
                        }
                        progressDialog.dismissDialog();
                        prepareNotificationMessage( timestamp );
                        deleteCartData();
                        Toast.makeText( CartActivity.this, "Transaksi Berhasil...", Toast.LENGTH_SHORT ).show();
                        finish();
                    }
                } )
                .addOnFailureListener( new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText( CartActivity.this, ""+e.getMessage(), Toast.LENGTH_SHORT ).show();
                    }
                } );
        DatabaseReference reffs = FirebaseDatabase.getInstance().getReference("DataOrder").child( "Order" );
        reffs.child( timestamp ).setValue( hashMap )
                .addOnSuccessListener( new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {

                        for (int i=0;i<list.size();i++){
                            String pId = list.get( i ).getItemId();
                            String idProduk = list.get( i ).getIdMenu();
                            String namaProduk = list.get( i ).getNama_produk();
                            String hargaProduk = list.get( i ).getHarga_produk();
                            String quantity = list.get( i ).getQuantity_produk();
                            String total_harga = list.get( i ).getTotal_harga_produk();

                            HashMap<String,String> hashMap1 = new HashMap<>(  );
                            hashMap1.put( "pId",pId );
                            hashMap1.put( "namaProduk",namaProduk );
                            hashMap1.put( "hargaProduk",hargaProduk );
                            hashMap1.put( "quantity",quantity );
                            hashMap1.put( "total_harga",total_harga );

                            reffs.child( timestamp ).child( "Items" ).child( pId ).setValue( hashMap1 );
                        }
                        progressDialog.dismissDialog();
                        deleteCartData();
                    }
                } )
                .addOnFailureListener( new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText( CartActivity.this, ""+e.getMessage(), Toast.LENGTH_SHORT ).show();
                    }
                } );

    }
    private void deleteCartData() {
        EasyDB easyDB = EasyDB.init( this, "ITEMS_DB" )
                .setTableName( "ITEMS_TABLE" )
                .addColumn( new Column( "Item_Id", new String[]{"text", "unique"} ) )
                .addColumn( new Column( "Item_PID", new String[]{"text", "not null"} ) )
                .addColumn( new Column( "Item_Nama", new String[]{"text", "not null"} ) )
                .addColumn( new Column( "Item_Harga_Awal", new String[]{"text", "not null"} ) )
                .addColumn( new Column( "Item_Total_Harga", new String[]{"text", "not null"} ) )
                .addColumn( new Column( "Item_Quantity", new String[]{"text", "not null"} ) )
                .doneTableColumn();

        easyDB.deleteAllDataFromTable();
    }
    private void requestLocationPermission() {
        ActivityCompat.requestPermissions( this, locationPermission, LOCATION_REQUEST_CODE );
    }
    private boolean checkLoactionPermission() {
        boolean result = ContextCompat.checkSelfPermission( this,
                Manifest.permission.ACCESS_FINE_LOCATION ) == (PackageManager.PERMISSION_GRANTED
        );
        return result;
    }
    private void detectLocation() {
        Toast.makeText( this, "Loading ....", Toast.LENGTH_LONG ).show();
        locationManager = (LocationManager) getSystemService( Context.LOCATION_SERVICE );
        locationManager.requestLocationUpdates( LocationManager.GPS_PROVIDER, 0, 0, this );
    }
    @Override
    public void onLocationChanged(Location location) {
        latitude = location.getLatitude();
        longitude = location.getLongitude();
        findAddress();
    }
    private void findAddress() {
        Geocoder geocoder;
        List<Address> addresses;
        geocoder = new Geocoder( this, Locale.getDefault() );

        try {
            addresses = geocoder.getFromLocation( latitude, longitude, 1 );
            String address = addresses.get( 0 ).getAddressLine( 0 );
            String city = addresses.get( 0 ).getLocality();
            String state = addresses.get( 0 ).getAdminArea();
            String country = addresses.get( 0 ).getCountryName();

           alamat.setText(  address+", "+city+", "+state+", "+country );
        } catch (Exception e) {
            e.getMessage();
        }
    }
    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }
    @Override
    public void onProviderEnabled(String provider) {

    }
    @Override
    public void onProviderDisabled(String provider) {
        Toast.makeText( this, "Hidupkn GPS ....", Toast.LENGTH_SHORT ).show();
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        switch (requestCode) {
            case LOCATION_REQUEST_CODE: {
                if (grantResults.length > 0) {
                    boolean locationAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    if (locationAccepted) {
                        detectLocation();
                    } else {
                        Toast.makeText( this, "Location Permission is neccessarry", Toast.LENGTH_SHORT ).show();
                    }
                }
            }
            break;
        }
        super.onRequestPermissionsResult( requestCode, permissions, grantResults );
    }


    private String idSeller;
    private void prepareNotificationMessage(String orderId) {
        DatabaseReference reff = FirebaseDatabase.getInstance().getReference( "Users" );
        reff.orderByChild( "idUser" ).equalTo( "LZUih5AKmWbixCYmJT66HpOtpY23" )
                .addValueEventListener( new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        idSeller = "" + dataSnapshot.child( "idUser" ).getValue();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                } );
        String NOTOFICATION_TOPIC = "/topics/"+ Constants.FCM_TOPIC;
        String NOTOFICATION_TITLE = "Pesanan BAru"+orderId;
        String NOTOFICATION_MESSAGE = "Selamat..! Kamu mempunyai pesanan baru.";
        String NOTOFICATION_TYPE = "Pesanan Baru";

        JSONObject notificationJO = new JSONObject(  );
        JSONObject notificationBodyJO = new JSONObject(  );

        try {
            notificationBodyJO.put( "notificationType",NOTOFICATION_TYPE );
            notificationBodyJO.put( "buyerId",firebaseAuth.getCurrentUser().getUid() );
            notificationBodyJO.put( "sellerId",idSeller );
            notificationBodyJO.put( "orderId",orderId );
            notificationBodyJO.put( "notificationTitle",NOTOFICATION_TITLE );
            notificationBodyJO.put( "notificationMessage",NOTOFICATION_MESSAGE);

            notificationJO.put( "to",NOTOFICATION_TOPIC );
            notificationJO.put( "data",notificationBodyJO );
        }catch (Exception e){
            Toast.makeText(CartActivity.this, ""+e.getMessage(), Toast.LENGTH_SHORT ).show();
        }

        sendNotificationFcm(notificationJO,orderId);
    }

    private void sendNotificationFcm(JSONObject notificationJO, String orderId) {
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest( "https://fcm.googleapis.com/fcm/send", notificationJO, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        } ){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String,String> headers = new HashMap<>(  );
                headers.put( "Content-type","application/json" );
                headers.put( "Authorization","key="+Constants.FCM_KEY );
                return headers;
            }
        };
        Volley.newRequestQueue( this ).add( jsonObjectRequest );
    }
}