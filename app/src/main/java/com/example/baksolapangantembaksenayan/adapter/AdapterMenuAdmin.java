package com.example.baksolapangantembaksenayan.adapter;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.example.baksolapangantembaksenayan.R;
import com.example.baksolapangantembaksenayan.activity.Admin.EditDataMenu;
import com.example.baksolapangantembaksenayan.model.model_menu_admin;
import com.example.baksolapangantembaksenayan.utility.FilterProduct;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import stream.customalert.CustomAlertDialogue;

public class AdapterMenuAdmin extends RecyclerView.Adapter<AdapterMenuAdmin.ViewHolder> implements Filterable {


    private Context context;
    public ArrayList<model_menu_admin> listMenu,filterList;
    private FilterProduct filterProduct;

    public AdapterMenuAdmin(Context context, ArrayList<model_menu_admin> listMenu) {
        this.context = context;
        this.listMenu = listMenu;
        filterList = listMenu;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from( context ).inflate( R.layout.row_menu_admin, parent, false );
        return new ViewHolder( view );
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        model_menu_admin model = listMenu.get( position );
        holder.txtNameMenu.setText( model.getNamaMenu() );
        holder.txtDeskripsiMenu.setText( model.getDeskripsiMenu() );
        holder.txtKategori.setText( model.getKategoriMenu() );
        try {
            Picasso.get().load( model.getGambarMenu() ).fit().into( holder.image );
        } catch (Exception e) {
            holder.image.setImageResource( R.drawable.ic_baseline_camera_alt_24 );
        }

        holder.btnDetail.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                detailsBottomSheet( model );
            }
        } );

    }

    private void detailsBottomSheet(model_menu_admin model) {
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog( context, R.style.TransparentDialog );
        View view = LayoutInflater.from( context ).inflate( R.layout.bs_detail_admin_layout, null );
        bottomSheetDialog.setContentView( view );

        final String id = model.getIdMenu();
        TextView txtNamaMenu = view.findViewById( R.id.txtNamaMenu );
        TextView txtNamaProduks = view.findViewById( R.id.txtNamaProduks );
        TextView txtDeskripsi = view.findViewById( R.id.textView20 );
        TextView hargaMenu = view.findViewById( R.id.txtHargas );
        ConstraintLayout btnBack = view.findViewById( R.id.btnBack );
        ImageView image = view.findViewById( R.id.imageMenu );
        Button btnUbahData = view.findViewById( R.id.btnUbahData );
        Button btnHapusData = view.findViewById( R.id.btnHapusData );

        txtNamaMenu.setText( model.getNamaMenu() );
        txtNamaProduks.setText( model.getNamaMenu() );
        txtDeskripsi.setText( model.getDeskripsiMenu() );
        hargaMenu.setText( model.getHargaMenu() );
        try {
            Picasso.get().load( model.getGambarMenu() ).into( image );
        }catch (Exception e){
            image.setImageResource( R.drawable.ic_baseline_camera_alt_24 );
        }

        bottomSheetDialog.setCanceledOnTouchOutside( true );
        bottomSheetDialog.show();

        btnBack.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomSheetDialog.dismiss();
            }
        } );

        btnUbahData.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomSheetDialog.dismiss();
                Intent intent = new Intent( context, EditDataMenu.class );
                intent.putExtra( "IdProduk",id );
                context.startActivity( intent );
            }
        } );

        btnHapusData.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomSheetDialog.dismiss();
                CustomAlertDialogue.Builder alert = new CustomAlertDialogue.Builder( context )
                        .setStyle( CustomAlertDialogue.Style.DIALOGUE )
                        .setCancelable( false )
                        .setTitle( "Hapus Produk" )
                        .setMessage( "Hapus produk "+model.getNamaMenu()+" ?" )
                        .setPositiveText( "Hapus" )
                        .setPositiveColor( R.color.negative )
                        .setPositiveTypeface( Typeface.DEFAULT_BOLD )
                        .setOnPositiveClicked( new CustomAlertDialogue.OnPositiveClicked() {
                            @Override
                            public void OnClick(View view, Dialog dialog) {
                                dialog.dismiss();
                                deleteProduct( id );
                                bottomSheetDialog.dismiss();
                            }
                        } )
                        .setNegativeText( "Batalkan" )
                        .setNegativeColor( R.color.positive )
                        .setOnNegativeClicked( new CustomAlertDialogue.OnNegativeClicked() {
                            @Override
                            public void OnClick(View view, Dialog dialog) {
                                dialog.dismiss();
                                bottomSheetDialog.dismiss();
                            }
                        } )
                        .build();
                alert.show();

            }
        } );
    }
    private void deleteProduct(String id) {
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference( "Users" );
        ref.child( firebaseAuth.getUid() ).child( "Menu" ).child( id ).removeValue()
                .addOnSuccessListener( new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText( context, "", Toast.LENGTH_SHORT ).show();
                        deleteMenuUser(id);
                    }
                } ).addOnFailureListener( new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText( context, "" + e.getMessage(), Toast.LENGTH_SHORT ).show();
            }
        } );
    }

    private void deleteMenuUser(String id) {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference( "DataMenu" );
        ref.child( "Menu" ).child( id ).removeValue()
                .addOnSuccessListener( new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        //Toast.makeText( context, "", Toast.LENGTH_SHORT ).show();
                    }
                } ).addOnFailureListener( new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText( context, "" + e.getMessage(), Toast.LENGTH_SHORT ).show();
            }
        } );
    }

    @Override
    public int getItemCount() {
        return listMenu.size();
    }

    @Override
    public Filter getFilter() {
        if (filterProduct == null) {
            filterProduct = new FilterProduct( this, filterList );
        }
        return filterProduct;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.txtNameMenu)
        TextView txtNameMenu;
        @BindView(R.id.txtDeskripsiMenu)
        TextView txtDeskripsiMenu;
        @BindView(R.id.txtKategori)
        TextView txtKategori;
        @BindView(R.id.imageView13)
        ImageView image;
        @BindView(R.id.btnDetail)
        ConstraintLayout btnDetail;

        public ViewHolder(@NonNull View itemView) {
            super( itemView );
            ButterKnife.bind( this, itemView );
        }
    }
}
