package com.example.baksolapangantembaksenayan.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.widget.EditText;
import android.widget.Toast;

import com.example.baksolapangantembaksenayan.R;
import com.example.baksolapangantembaksenayan.activity.Admin.MainAdminActivity;
import com.example.baksolapangantembaksenayan.activity.User.MainUserActivity;
import com.example.baksolapangantembaksenayan.activity.User.RegisterUser;
import com.example.baksolapangantembaksenayan.model.model_user;
import com.example.baksolapangantembaksenayan.util.LoadingDialog;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LoginActivity extends AppCompatActivity {

    @OnClick( R.id.txtDaftar )
    void Register(){
        startActivity( new Intent( LoginActivity.this, RegisterUser.class ) );
    }

    @OnClick(R.id.btnMasuk)
    void onLogin() {
        loginUser();
    }

    @BindView(R.id.edtEmail)
    EditText edtEmail;
    @BindView(R.id.edtPassword)
    EditText edtPassword;

    private FirebaseAuth firebaseAuth;
    private LoadingDialog loadingDialog;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_login );
        firebaseAuth = FirebaseAuth.getInstance();
        loadingDialog = new LoadingDialog( this );
        ButterKnife.bind( this );
    }

    private String email, password;

    private void loginUser() {

        loadingDialog.startLoadingDialog();
        email = edtEmail.getText().toString().trim();
        password = edtPassword.getText().toString().trim();
        if (!Patterns.EMAIL_ADDRESS.matcher( email ).matches()) {
            Snackbar.make( findViewById( R.id.login_activity ), "Email tidak valid", Snackbar.LENGTH_LONG ).show();
            return;
        }
        if (TextUtils.isEmpty( password )) {
            Snackbar.make( findViewById( R.id.login_activity ), "Masukkan Password", Snackbar.LENGTH_LONG ).show();

            if (password.length() < 6) {
                Snackbar.make( findViewById( R.id.login_activity ), "Password harus 6 karakter", Snackbar.LENGTH_LONG ).show();
            }
            if (password.length() > 6) {
                Snackbar.make( findViewById( R.id.login_activity ), "Password harus 6 karakter", Snackbar.LENGTH_LONG ).show();
            }
            return;
        }
        firebaseAuth.signInWithEmailAndPassword( email, password )
                .addOnSuccessListener( new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        checkUserType();
                    }
                } )
                .addOnFailureListener( new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        loadingDialog.dismissDialog();
                        Toast.makeText( LoginActivity.this, "" + e.getMessage(), Toast.LENGTH_SHORT ).show();
                    }
                } );
    }

    private void checkUserType() {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference( "Users" );
        ref.orderByChild( "idUser" ).equalTo( firebaseAuth.getUid() )
                .addListenerForSingleValueEvent( new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot ds : dataSnapshot.getChildren()) {
                            String accountType = "" + ds.child( "tipe_akun" ).getValue();
                            if (accountType.equals( "Admin" )) {
                                loadingDialog.dismissDialog();
                                startActivity( new Intent( LoginActivity.this, MainAdminActivity.class ) );
                                finish();
                            } else {
                                loadingDialog.dismissDialog();
                                startActivity( new Intent( LoginActivity.this, MainUserActivity.class ) );
                                finish();
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                } );
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        System.exit( 0 );
    }
}