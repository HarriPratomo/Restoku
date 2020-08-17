package com.example.baksolapangantembaksenayan.activity.User;

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
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Patterns;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.baksolapangantembaksenayan.R;
import com.example.baksolapangantembaksenayan.activity.Admin.EditProfileAdmin;
import com.example.baksolapangantembaksenayan.util.LoadingDialog;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

public class EditProfileUser extends AppCompatActivity {

    @OnClick(R.id.btnPickImage)
    void pilihGambar() {
        showPickImageDialog();
    }

    @OnClick(R.id.btnBack)
    void backBtn() {
        onBackPressed();
    }

    @OnClick(R.id.btnDaftar)
    void daftar() {
        validate();
    }


    @BindView(R.id.imageProfile)
    ImageView imageProfile;
    @BindView(R.id.edtNamaDepan)
    EditText edtNamaDepan;
    @BindView(R.id.edtNamaBelakang)
    EditText edtNamaBelakang;
    @BindView(R.id.edtEmail)
    EditText edtEmail;

    private FirebaseAuth firebaseAuth;
    private LoadingDialog loadingDialog;
    private static final int CAMERA_REQUEST_CODE = 200;
    private static final int STORAGE_REQUEST_CODE = 300;
    private static final int IMAGE_PICK_GALLERY_CODE = 400;
    private static final int IMAGE_PICK_CAMERA_CODE = 500;

    private String[] cameraPermission;
    private String[] storagePermission;
    private Uri image_uri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_edit_profile_user );
        ButterKnife.bind( this );
        firebaseAuth = FirebaseAuth.getInstance();
        loadingDialog = new LoadingDialog( this );
        cameraPermission = new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};
        storagePermission = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE};
        loadDetail();
    }
    private void loadDetail() {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Users");
        databaseReference.orderByChild( "idUser").equalTo( firebaseAuth.getUid() ).addValueEventListener( new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds:snapshot.getChildren()){
                    String namaDepanUser = ""+ds.child( "namaDepanUser" ).getValue();
                    String namaBelakangUser = ""+ds.child( "namaBelakangUser" ).getValue();
                    String email = ""+ds.child( "email" ).getValue();
                    String gambar_profile = ""+ds.child( "gambar_profile" ).getValue();

                    edtNamaDepan.setText( namaDepanUser );
                    edtNamaBelakang.setText( namaBelakangUser );
                    edtEmail.setText( email );
                    try {
                        Picasso.get().load( gambar_profile ).into( imageProfile );
                    }catch (Exception e){
                        imageProfile.setImageResource( R.drawable.ic_baseline_camera_alt_24 );
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        } );
    }


    String namaDepan, namaBelakang, email;

    private void validate() {
        namaDepan = edtNamaDepan.getText().toString().trim();
        namaBelakang = edtNamaBelakang.getText().toString().trim();
        email = edtEmail.getText().toString().trim();


        if (TextUtils.isEmpty( namaDepan )) {
            Snackbar.make( findViewById( R.id.register_admin ), "Masukkan nama depan..", Snackbar.LENGTH_SHORT ).show();
            return;
        }
        if (TextUtils.isEmpty( namaBelakang )) {
            Snackbar.make( findViewById( R.id.register_admin ), "Masukkan nama belakang..", Snackbar.LENGTH_SHORT ).show();
            return;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher( email ).matches()) {
            Snackbar.make( findViewById( R.id.register_admin ), "Masukkan email yang valid..", Snackbar.LENGTH_SHORT ).show();
            return;
        }
        updateData();

    }

    private void updateData() {
        if (image_uri == null) {
            HashMap<String, Object> hashMap = new HashMap<>();
            hashMap.put( "namaDepanUser", "" + namaDepan );
            hashMap.put( "namaBelakangUser", "" + namaBelakang );
            hashMap.put( "email", "" + email );

            DatabaseReference ref = FirebaseDatabase.getInstance().getReference( "Users" );
            ref.child( firebaseAuth.getUid() ).updateChildren( hashMap )
                    .addOnSuccessListener( new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText( EditProfileUser.this, "Update berhasil ..", Toast.LENGTH_SHORT ).show();
                            finish();
                        }
                    } )
                    .addOnFailureListener( new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText( EditProfileUser.this, ""+e.getMessage(), Toast.LENGTH_SHORT ).show();
                        }
                    } );

        } else {
            String filePathAndName = "gambar_profile/" + "" + firebaseAuth.getUid();
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
                                hashMap.put( "namaDepanUser", "" + namaDepan );
                                hashMap.put( "namaBelakangUser", "" + namaBelakang );
                                hashMap.put( "email", "" + email );
                                hashMap.put( "gambar_profile", ""+downloadImageUri );

                                DatabaseReference ref = FirebaseDatabase.getInstance().getReference( "Users" );
                                ref.child( firebaseAuth.getUid() ).updateChildren( hashMap )
                                        .addOnSuccessListener( new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                loadingDialog.dismissDialog();
                                                Toast.makeText( EditProfileUser.this, "Update berhasil ..", Toast.LENGTH_SHORT ).show();
                                                finish();
                                            }
                                        } )
                                        .addOnFailureListener( new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Toast.makeText( EditProfileUser.this, ""+e.getMessage(), Toast.LENGTH_SHORT ).show();
                                                loadingDialog.dismissDialog();
                                            }
                                        } );
                            }
                        }
                    } );
        }
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

    private boolean checkCameraPermission() {
        boolean result = ContextCompat.checkSelfPermission( this,
                Manifest.permission.CAMERA ) == (PackageManager.PERMISSION_GRANTED);

        boolean result1 = ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE ) == (PackageManager.PERMISSION_GRANTED);
        return result && result1;
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

    private void requestCameraPermission() {
        ActivityCompat.requestPermissions( this, cameraPermission, CAMERA_REQUEST_CODE );
    }

    private boolean checkStoragePermission() {
        boolean result = ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE ) == (PackageManager.PERMISSION_GRANTED);
        return result;
    }

    private void pickFromGalery() {
        Intent intent = new Intent( Intent.ACTION_PICK );
        intent.setType( "image/*" );
        startActivityForResult( intent, IMAGE_PICK_GALLERY_CODE );
    }
    private void requestStoragePermission() {
        ActivityCompat.requestPermissions( this, storagePermission, STORAGE_REQUEST_CODE );
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == IMAGE_PICK_GALLERY_CODE) {
                image_uri = data.getData();
                imageProfile.setImageURI( image_uri );
            } else if (requestCode == IMAGE_PICK_CAMERA_CODE) {
                imageProfile.setImageURI( image_uri );
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
                        Toast.makeText( this, "Izin Kamera dibutuhkan", Toast.LENGTH_SHORT ).show();
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
                        Toast.makeText( this, "Izin penyimpanan dibutuhkan ", Toast.LENGTH_SHORT ).show();
                    }
                }
            }
            break;
        }
        super.onRequestPermissionsResult( requestCode, permissions, grantResults );
    }

}