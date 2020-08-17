package com.example.baksolapangantembaksenayan.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.baksolapangantembaksenayan.R;
import com.example.baksolapangantembaksenayan.activity.User.MainUserActivity;
import com.example.baksolapangantembaksenayan.model.model_menu_admin;
import com.example.baksolapangantembaksenayan.utility.FilterProductUser;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import p32929.androideasysql_library.Column;
import p32929.androideasysql_library.EasyDB;

public class AdapterMenuUser extends RecyclerView.Adapter<AdapterMenuUser.ViewHolder> implements Filterable {


    private Context context;
    public ArrayList<model_menu_admin> listMenu,filterlist;
    private FilterProductUser filterProductUser;


    public AdapterMenuUser(Context context, ArrayList<model_menu_admin> listMenu) {
        this.context = context;
        this.listMenu = listMenu;
          filterlist = listMenu;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from( context ).inflate( R.layout.row_new_menu, parent, false );
        return new ViewHolder( view );
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        model_menu_admin model = listMenu.get( position );
        holder.txtNameMenu.setText( model.getNamaMenu() );
        holder.txtHarga.setText( "Rp."+model.getHargaMenu() );
        try {
            Picasso.get().load( model.getGambarMenu() ).fit().into( holder.imageMenu );
        } catch (Exception e) {
            holder.imageMenu.setImageResource( R.drawable.ic_baseline_camera_alt_24 );
        }

        holder.btnDetail.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                detailBottomSheet(model);
            }
        } );

    }

    private void detailBottomSheet(model_menu_admin model) {

        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog( context,R.style.TransparentDialog );
        View view = LayoutInflater.from( context ).inflate( R.layout.bs_detail_layout, null );
        bottomSheetDialog.setContentView( view );
        TextView namaMenu = view.findViewById( R.id.txtNamaProduks );
        TextView namaMenu1 = view.findViewById( R.id.txtNamaMenu);
        TextView deskripsi = view.findViewById( R.id.textView20);
        TextView harga = view.findViewById( R.id.txtHargas );
        ImageView image = view.findViewById( R.id.imageMenu );
        Button btnBeli = view.findViewById( R.id.btnBeli );
        ConstraintLayout btnBack = view.findViewById( R.id.btnBack );
        namaMenu.setText( model.getNamaMenu() );
        namaMenu1.setText( model.getNamaMenu() );
        deskripsi.setText( model.getDeskripsiMenu() );
        harga.setText("Rp."+ model.getHargaMenu() );
        try {
            Picasso.get().load( model.getGambarMenu() ).fit().into( image );
        }catch (Exception e){
            image.setImageResource( R.drawable.ic_baseline_camera_alt_24 );
        }

        bottomSheetDialog.setCanceledOnTouchOutside( false );
        bottomSheetDialog.show();

        btnBack.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomSheetDialog.dismiss();
            }
        } );

        btnBeli.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showQuantityDialog(model);
                bottomSheetDialog.dismiss();
            }
        } );


    }

    private double harga = 0;
    private double total_harga = 0;
    private int jumlah_pesan = 0;
    private void showQuantityDialog(model_menu_admin product) {
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog( context, R.style.TransparentDialog );
        View view = LayoutInflater.from( context ).inflate( R.layout.dialog_to_cart, null );
        bottomSheetDialog.setContentView( view );
        //init data
        TextView nama = view.findViewById( R.id.txtNamaProduks );
        TextView harga_asli = view.findViewById( R.id.txtHargas );
        TextView harga_total = view.findViewById( R.id.txtHargaTotal );
        TextView quantity = view.findViewById( R.id.txtJumlah );

        ConstraintLayout tombol_kurangi  = view.findViewById( R.id.kurangi );
        ConstraintLayout tombol_tambah  = view.findViewById( R.id.tambah );
        ConstraintLayout tambah_keranjang  = view.findViewById( R.id.btnAddToCart );


        String IdMenu = product.getIdMenu();
        String t_harga = product.getHargaMenu();
        harga = Double.parseDouble(t_harga.replaceAll( "Rp","" )  );
        total_harga = Double.parseDouble(t_harga.replaceAll( "Rp","" )  );
        jumlah_pesan = 1;

        //getData
        nama.setText( product.getNamaMenu() );
        quantity.setText(""+ jumlah_pesan );
        harga_asli.setText( "Rp. "+product.getHargaMenu() );
        harga_total.setText( "Rp. "+total_harga );

        tombol_tambah.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                total_harga = total_harga+harga;
                jumlah_pesan++;

                harga_total.setText( "Rp. "+total_harga );
                quantity.setText( ""+jumlah_pesan );
            }
        } );

        tombol_kurangi.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (jumlah_pesan>1){
                    total_harga = total_harga-harga;
                    jumlah_pesan--;
                }

                harga_total.setText( "Rp. "+total_harga );
                quantity.setText( ""+jumlah_pesan );
            }
        } );

        tambah_keranjang.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nama_produk = nama.getText().toString().trim();
                String harga_produk = t_harga;
                String total_harga_produk = harga_total.getText().toString().trim().replace( "Rp. ","" );
                String quantity_produk = quantity.getText().toString().trim();
               // String image = product.getGambarMenu();

                addToCart(IdMenu,nama_produk,harga_produk,total_harga_produk,quantity_produk);
                bottomSheetDialog.dismiss();
            }
        } );

        bottomSheetDialog.setCanceledOnTouchOutside( true );
        bottomSheetDialog.show();

    }
    private int itemId = 1;
    private void addToCart(String idMenu, String nama_produk, String harga_produk, String total_harga_produk, String quantity_produk) {
        itemId++;
        EasyDB easyDB = EasyDB.init( context,"ITEMS_DB" )
                .setTableName( "ITEMS_TABLE" )
                .addColumn( new Column( "Item_Id",new String[]{"text","unique"} ) )
                .addColumn( new Column( "Item_PID",new String[]{"text","not null"} ) )
                .addColumn( new Column( "Item_Nama",new String[]{"text","not null"} ) )
                .addColumn( new Column( "Item_Harga_Awal",new String[]{"text","not null"} ) )
                .addColumn( new Column( "Item_Total_Harga",new String[]{"text","not null"} ) )
                .addColumn( new Column( "Item_Quantity",new String[]{"text","not null"} ) )
                .doneTableColumn();


        Boolean b = easyDB.addData( "Item_Id",itemId )
                .addData( "Item_PID",idMenu )
                .addData( "Item_Nama",nama_produk )
                .addData( "Item_Harga_Awal",harga_produk )
                .addData( "Item_Total_Harga",total_harga_produk )
                .addData( "Item_Quantity",quantity_produk )
               .doneDataAdding();

        ((MainUserActivity)context).cartCount();
         success();
    }

    private void success() {
        Toast.makeText( context, "Ditambahkan ke Keranjang", Toast.LENGTH_SHORT ).show();

    }

    @Override
    public int getItemCount() {
        return listMenu.size();
    }

    @Override
    public Filter getFilter() {
        if (filterProductUser == null) {
            filterProductUser = new FilterProductUser( this, filterlist );
        }
        return filterProductUser;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.txtNamaMenu)
        TextView txtNameMenu;
        @BindView(R.id.txtHargas)
        TextView txtHarga;
        @BindView(R.id.imageMenu)
        ImageView imageMenu;
        @BindView(R.id.btnDetail)
        ConstraintLayout btnDetail;

        public ViewHolder(@NonNull View itemView) {
            super( itemView );
            ButterKnife.bind( this, itemView );
        }
    }
}
