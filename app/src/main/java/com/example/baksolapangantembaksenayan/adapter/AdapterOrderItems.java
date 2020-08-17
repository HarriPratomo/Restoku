package com.example.baksolapangantembaksenayan.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.baksolapangantembaksenayan.R;
import com.example.baksolapangantembaksenayan.model.model_order_items;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;

public class AdapterOrderItems extends RecyclerView.Adapter<AdapterOrderItems.ViewHolder> {


    private Context context;
    private ArrayList<model_order_items> list;

    public AdapterOrderItems(Context context, ArrayList<model_order_items> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
       View view = LayoutInflater.from( context ).inflate( R.layout.row_bs_order_items,null );
       return new ViewHolder( view );
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
       model_order_items model = list.get( position );
       holder.txtNamaMenu.setText( model.getNamaProduk() );
       holder.txtHarga.setText("Rp."+ model.getHargaProduk() );
       holder.txtHargaTotal.setText( "Rp."+model.getTotal_harga() );
       holder.txtQuantity.setText( model.getQuantity()+"X");
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        @BindView( R.id.txtNamaMenu )
        TextView txtNamaMenu;

        @BindView( R.id.txtHarga )
        TextView txtHarga;

        @BindView( R.id.txtHargaTotal )
        TextView txtHargaTotal;

        @BindView( R.id.txtQuantity )
        TextView txtQuantity;

        public ViewHolder(@NonNull View itemView) {
            super( itemView );
            ButterKnife.bind( this,itemView );
        }
    }
}
