package com.example.baksolapangantembaksenayan.activity.Admin;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.baksolapangantembaksenayan.R;
import com.example.baksolapangantembaksenayan.constants.Constants;
import com.example.baksolapangantembaksenayan.util.LoadingDialog;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;

public class AddProductAdminActivity extends AppCompatActivity {

    @OnClick(R.id.btnBack)
    void back() {
        onBackPressed();
    }

    @OnClick(R.id.imageMenu)
    void pickImage() {
        showPickImageDialog();
    }

    @OnClick(R.id.txtSatuanProduk)
    void chooseSatuan() {
       showSatuanDialog();
    }

    @OnClick(R.id.txtKategori)
    void chooseCategory() {
        showKategoriDialog();
    }

    @OnClick(R.id.btnTambah)
    void save() {
        savingData();
    }


    @BindView(R.id.edtNamaMenu)
    EditText edtNamaMenu;
    @BindView(R.id.imageMenu)
    ImageView imageMenu;
    @BindView(R.id.edtDeskripsi)
    EditText edtDeskripsi;
    @BindView(R.id.edtHargaMenu)
    EditText edtHargaMenu;
    @BindView(R.id.txtSatuanProduk)
    TextView txtSatuanProduk;
    @BindView(R.id.txtKategori)
    TextView txtKategori;


    private static final int CAMERA_REQUEST_CODE = 200;
    private static final int STORAGE_REQUEST_CODE = 300;
    private static final int IMAGE_PICK_GALLERY_CODE = 400;
    private static final int IMAGE_PICK_CAMERA_CODE = 500;

    private String[] cameraPermission;
    private String[] storagePermission;

    private LoadingDialog progressDialog;
    private FirebaseAuth firebaseAuth;
    private Uri image_uri;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_add_product_admin );
        ButterKnife.bind( this );
        firebaseAuth = FirebaseAuth.getInstance();
        progressDialog = new LoadingDialog( this );
        cameraPermission = new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};
        storagePermission = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE};
    }


    private void showSatuanDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder( this );
        builder.setTitle( "Satuan Menu" )
                .setItems( Constants.satuan_menu, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String satuan = Constants.satuan_menu[which];
                        txtSatuanProduk.setText( satuan );
                    }
                } )
                .show();
    }

    private void showKategoriDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder( this );
        builder.setTitle( "Kategori Menu" )
                .setItems( Constants.kategori_menu, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String kategori = Constants.kategori_menu[which];
                        txtKategori.setText( kategori );
                    }
                } )
                .show();
    }

    private String namaMenu, deskripsiMenu, satuanMenu, kategoriMenu, hargaMenu;

    private void savingData() {
        namaMenu = edtNamaMenu.getText().toString().trim();
        deskripsiMenu = edtDeskripsi.getText().toString().trim();
        satuanMenu = txtSatuanProduk.getText().toString().trim();
        kategoriMenu = txtKategori.getText().toString().trim();
        hargaMenu = edtHargaMenu.getText().toString().trim();

        if (TextUtils.isEmpty( namaMenu )) {
            Snackbar.make( findViewById( R.id.add_product_admin ), "Masukkan nama menu ...", Snackbar.LENGTH_SHORT ).show();
            return;
        }
        if (TextUtils.isEmpty( deskripsiMenu )) {
            Snackbar.make( findViewById( R.id.add_product_admin ), "Masukkan deskripsi menu ...", Snackbar.LENGTH_SHORT ).show();
            return;
        }
        if (satuanMenu.equals( "Satuan Menu" )) {
            Snackbar.make( findViewById( R.id.add_product_admin ), "Pilih satuan  menu ...", Snackbar.LENGTH_SHORT ).show();
            return;
        }
        if (kategoriMenu.equals( "Kategori Menu" )) {
            Snackbar.make( findViewById( R.id.add_product_admin ), "Pilih kategori  menu ...", Snackbar.LENGTH_SHORT ).show();
            return;
        }
        if (TextUtils.isEmpty( hargaMenu )) {
            Snackbar.make( findViewById( R.id.add_product_admin ), "Masukkan harga menu ...", Snackbar.LENGTH_SHORT ).show();
            return;
        }
        addProduct();
    }

    private void addProduct() {

        progressDialog.startLoadingDialog();
        final String timestamp = "" + System.currentTimeMillis();
        if (image_uri == null) {
            HashMap<String, Object> hashMap = new HashMap<>();
            hashMap.put( "IdMenu", "" + timestamp );
            hashMap.put( "namaMenu", "" + namaMenu );
            hashMap.put( "deskripsiMenu", "" + deskripsiMenu );
            hashMap.put( "satuanMenu", "" + satuanMenu );
            hashMap.put( "kategoriMenu", "" + kategoriMenu );
            hashMap.put( "gambarMenu", "" + "" );
            hashMap.put( "hargaMenu", "" + hargaMenu );
            hashMap.put( "timestamp", "" + timestamp );
            hashMap.put( "uid", "" + firebaseAuth.getUid() );

            DatabaseReference ref = FirebaseDatabase.getInstance().getReference( "Users" );
            ref.child( firebaseAuth.getUid() ).child( "Menu" ).child( timestamp ).setValue( hashMap )
                    .addOnSuccessListener( new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            progressDialog.dismissDialog();
                            Toast.makeText( AddProductAdminActivity.this, "Menu ditambahkan", Toast.LENGTH_SHORT ).show();
                            clearData();
                        }
                    } )
                    .addOnFailureListener( new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressDialog.dismissDialog();
                            Toast.makeText( AddProductAdminActivity.this, "" + e.getMessage(), Toast.LENGTH_SHORT ).show();
                        }
                    } );
            DatabaseReference reff = FirebaseDatabase.getInstance().getReference( "DataMenu" );
            reff.child( "Menu" ).child( timestamp ).setValue( hashMap )
                    .addOnSuccessListener( new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            progressDialog.dismissDialog();
                            Toast.makeText( AddProductAdminActivity.this, "Menu ditambahkan", Toast.LENGTH_SHORT ).show();
                            clearData();
                        }
                    } )
                    .addOnFailureListener( new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressDialog.dismissDialog();
                            Toast.makeText( AddProductAdminActivity.this, "" + e.getMessage(), Toast.LENGTH_SHORT ).show();
                        }
                    } );
        } else {
            String filePathAndName = "gambar_menu/" + "" + timestamp;
            StorageReference storageReference = FirebaseStorage.getInstance().getReference( filePathAndName );
            storageReference.putFile( image_uri )
                    .addOnSuccessListener( new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                            while (!uriTask.isSuccessful()) ;
                            Uri downloadImageUri = uriTask.getResult();
                            if (uriTask.isSuccessful()) {
                                HashMap<String, Object> hashMap = new HashMap<>();
                                hashMap.put( "IdMenu", "" + timestamp );
                                hashMap.put( "namaMenu", "" + namaMenu );
                                hashMap.put( "deskripsiMenu", "" + deskripsiMenu );
                                hashMap.put( "satuanMenu", "" + satuanMenu );
                                hashMap.put( "kategoriMenu", "" + kategoriMenu );
                                hashMap.put( "gambarMenu", "" + "" + downloadImageUri );
                                hashMap.put( "hargaMenu", "" + hargaMenu );
                                hashMap.put( "timestamp", "" + timestamp );
                                hashMap.put( "uid", "" + firebaseAuth.getUid() );

                                DatabaseReference ref = FirebaseDatabase.getInstance().getReference( "Users" );
                                ref.child( firebaseAuth.getUid() ).child( "Menu" ).child( timestamp ).setValue( hashMap )
                                        .addOnSuccessListener( new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                progressDialog.dismissDialog();
                                                Toast.makeText( AddProductAdminActivity.this, "Menu ditambahkan..", Toast.LENGTH_SHORT ).show();
                                                clearData();
                                            }
                                        } )
                                        .addOnFailureListener( new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                progressDialog.dismissDialog();
                                                Toast.makeText( AddProductAdminActivity.this, "" + e.getMessage(), Toast.LENGTH_SHORT ).show();

                                            }
                                        } );
                                DatabaseReference reff = FirebaseDatabase.getInstance().getReference( "DataMenu" );
                                reff.child( "Menu" ).child( timestamp ).setValue( hashMap )
                                        .addOnSuccessListener( new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                progressDialog.dismissDialog();
                                                Toast.makeText( AddProductAdminActivity.this, "Menu ditambahkan..", Toast.LENGTH_SHORT ).show();
                                                clearData();
                                            }
                                        } )
                                        .addOnFailureListener( new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                progressDialog.dismissDialog();
                                                Toast.makeText( AddProductAdminActivity.this, "" + e.getMessage(), Toast.LENGTH_SHORT ).show();

                                            }
                                        } );
                            }
                        }

                    } )
                    .addOnFailureListener( new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressDialog.dismissDialog();
                            Toast.makeText( AddProductAdminActivity.this, "" + e.getMessage(), Toast.LENGTH_SHORT ).show();
                        }
                    } );

        }
    }

    private void clearData() {
        edtNamaMenu.setText( "" );
        edtDeskripsi.setText( "" );
        txtSatuanProduk.setText( "Satuan Menu" );
        txtKategori.setText( "Kategori Menu" );
        edtHargaMenu.setText( "" );
        imageMenu.setImageResource( R.drawable.ic_baseline_camera_alt_24 );
        image_uri = null;
    }

    private void showPickImageDialog(){
        String[] options = {"Kamera", "Galeri"};
        AlertDialog.Builder builder = new AlertDialog.Builder( this );
        builder.setTitle( "Pick image" )
                .setItems( options, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (which == 0) {
                            if (checkCameraPermission()) {
                                pickFromCamera();
                            } else {
                                requestCameraPermission();
                            }
                        } else {
                            if (checkStoragePermission()) {
                                pickFromGalery();
                            } else {
                                requestStoragePermission();
                            }
                        }
                    }
                } )
                .show();
    }

    private void requestStoragePermission() {
        ActivityCompat.requestPermissions( this, storagePermission, STORAGE_REQUEST_CODE );
    }

    private boolean checkStoragePermission() {
        boolean result = ContextCompat.checkSelfPermission( this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE ) == (PackageManager.PERMISSION_GRANTED);
        return result;
    }

    private void pickFromGalery() {
        Intent intent = new Intent( Intent.ACTION_PICK );
        intent.setType( "image/*" );
        startActivityForResult( intent, IMAGE_PICK_GALLERY_CODE );
    }

    private void requestCameraPermission() {
        ActivityCompat.requestPermissions( this, cameraPermission, CAMERA_REQUEST_CODE );
    }

    private void pickFromCamera() {
        ContentValues contentValues = new ContentValues();
        contentValues.put( MediaStore.Images.Media.TITLE, "Temp_image Title" );
        contentValues.put( MediaStore.Images.Media.DESCRIPTION, "Temp_image Description" );

        image_uri = getContentResolver().insert( MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues );
        Intent intent = new Intent( MediaStore.ACTION_IMAGE_CAPTURE );
        intent.putExtra( MediaStore.EXTRA_OUTPUT, image_uri );
        startActivityForResult( intent, IMAGE_PICK_CAMERA_CODE );
    }

    private boolean checkCameraPermission() {
        boolean result = ContextCompat.checkSelfPermission( this,
                Manifest.permission.CAMERA ) == (PackageManager.PERMISSION_GRANTED);

        boolean result1 = ContextCompat.checkSelfPermission( this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE ) == (PackageManager.PERMISSION_GRANTED);
        return result && result1;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == IMAGE_PICK_GALLERY_CODE) {
                image_uri = data.getData();
                imageMenu.setImageURI( image_uri );
            } else if (requestCode == IMAGE_PICK_CAMERA_CODE) {
                imageMenu.setImageURI( image_uri );
            }
        }
        super.onActivityResult( requestCode, resultCode, data );
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case CAMERA_REQUEST_CODE: {
                if (grantResults.length > 0) {
                    boolean cameraAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    boolean storageAccepted = grantResults[1] == PackageManager.PERMISSION_GRANTED;
                    if (cameraAccepted && storageAccepted) {
                        pickFromCamera();
                    } else {
                        Toast.makeText( this, "Camera Permission are neccessarry", Toast.LENGTH_SHORT ).show();
                    }
                }
            }
            break;
            case STORAGE_REQUEST_CODE: {
                if (grantResults.length > 0) {
                    boolean storageAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    if (storageAccepted) {
                        pickFromGalery();
                    } else {
                        Toast.makeText( this, "Storage Permission are neccessarry", Toast.LENGTH_SHORT ).show();
                    }
                }
            }
            break;
        }
        super.onRequestPermissionsResult( requestCode, permissions, grantResults );
    }
}