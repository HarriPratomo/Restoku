package com.example.baksolapangantembaksenayan.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.baksolapangantembaksenayan.R;
import com.example.baksolapangantembaksenayan.activity.CartActivity;
import com.example.baksolapangantembaksenayan.activity.User.MainUserActivity;
import com.example.baksolapangantembaksenayan.model.ModelCart;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import p32929.androideasysql_library.Column;
import p32929.androideasysql_library.EasyDB;

import static java.security.AccessController.getContext;

public class AdapterCart extends RecyclerView.Adapter<AdapterCart.ViewHolder> {

    private Context context;
    private List<ModelCart> modelCartList;


    public AdapterCart(Context context, List<ModelCart> modelCartList) {
        this.context = context;
        this.modelCartList = modelCartList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from( parent.getContext() ).inflate( R.layout.row_cart ,parent,false);
        return new ViewHolder( view );
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ModelCart modelCart = modelCartList.get( position );
        String id = modelCart.getItemId();
        String total_harga = modelCart.getTotal_harga_produk();
        holder.txtNamaProduk.setText( modelCart.getNama_produk() );
        holder.txtHargaAwal.setText("Rp. "+ modelCart.getHarga_produk()+" /Items");
        holder.txtTotalHarga.setText("Rp. "+ modelCart.getTotal_harga_produk() );
        holder.txtQuantity.setText( modelCart.getQuantity_produk()+" X" );



        holder.btnDelete.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Validation data
                EasyDB easyDB = EasyDB.init( context,"ITEMS_DB" )
                        .setTableName( "ITEMS_TABLE" )
                        .addColumn( new Column( "Item_Id",new String[]{"text","unique"} ) )
                        .addColumn( new Column( "Item_PID",new String[]{"text","not null"} ) )
                        .addColumn( new Column( "Item_Nama",new String[]{"text","not null"} ) )
                        .addColumn( new Column( "Item_Harga_Awal",new String[]{"text","not null"} ) )
                        .addColumn( new Column( "Item_Total_Harga",new String[]{"text","not null"} ) )
                        .addColumn( new Column( "Item_Quantity",new String[]{"text","not null"} ) )
                        .doneTableColumn();


                easyDB.deleteRow( 1,id );
                Toast.makeText( context, "Produk dihapus !", Toast.LENGTH_SHORT ).show();
                modelCartList.remove( position );
                notifyItemChanged( position );
                notifyDataSetChanged();

                double tx = Double.parseDouble( ((((CartActivity)context).Total_harga.getText().toString().trim().replace( "Rp. ","" ) )) );
                double totalHarga = tx - Double.parseDouble(total_harga );

                ((CartActivity)context).sub_total = 0.00;
                ((CartActivity)context).Total_harga.setText(String.valueOf(  totalHarga ) );

            }
        } );

    }

    @Override
    public int getItemCount() {
        return modelCartList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        @BindView( R.id.txtNamaProdukCart)
        TextView txtNamaProduk;

        @BindView( R.id.txtQuantity )
        TextView txtQuantity;

        @BindView( R.id.txtHargaAwal )
        TextView txtHargaAwal;

        @BindView( R.id.txtTotalHarga )
        TextView txtTotalHarga;

        @BindView( R.id.btnDelete )
        ConstraintLayout btnDelete;

        public ViewHolder(@NonNull View itemView) {
            super( itemView );
            ButterKnife.bind( this,itemView );
        }
    }
}