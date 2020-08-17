package com.example.baksolapangantembaksenayan.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.baksolapangantembaksenayan.R;
import com.example.baksolapangantembaksenayan.activity.User.DetailOrderItem;
import com.example.baksolapangantembaksenayan.model.model_transaction;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;

public class Adapter_Transaction extends RecyclerView.Adapter<Adapter_Transaction.ViewHolder> {

    private Context context;
    public ArrayList<model_transaction> list;

    public Adapter_Transaction(Context context, ArrayList<model_transaction> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from( parent.getContext() ).inflate( R.layout.row_transaction, parent, false );
        return new ViewHolder( view );
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        model_transaction model = list.get( position );
        holder.txtIdOrder.setText( "Id Order : " + model.getIdOrder() );
        holder.txtTotalTransaksi.setText( "Total Transaksi : " + model.getTotalPesanan() );
        holder.txtJamTransaksi.setText( "Jam Order: " + model.getJamOrder() );
        holder.txtTanggalTransaksi.setText( "Tgl : " + model.getTanggalOrder() );
        holder.txtStatusOrder.setText(  model.getStatusOrder() );

        if (holder.txtStatusOrder.equals( "Sedang Proses" )){
            holder.txtStatusOrder.setTextColor( context.getResources().getColor( R.color.progress ) );
        }
       else if (holder.txtStatusOrder.equals( "Dibatalkan" )){
            holder.txtStatusOrder.setTextColor( context.getResources().getColor( R.color.red ) );
        }
        else if (holder.txtStatusOrder.equals( "Selesai" )){
            holder.txtStatusOrder.setTextColor( context.getResources().getColor( R.color.complete ) );
        }
        holder.itemView.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent( context, DetailOrderItem.class );
                intent.putExtra( "idOrder",model.getIdOrder() );
                context.startActivity( intent );
            }
        } );
    }


    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.txtIdOrder)
        TextView txtIdOrder;
        @BindView(R.id.txtTotalTransaksi)
        TextView txtTotalTransaksi;
        @BindView(R.id.txtTanggalTransaksi)
        TextView txtTanggalTransaksi;
        @BindView(R.id.txtJamTransaksi)
        TextView txtJamTransaksi;
        @BindView(R.id.txtStatusOrder)
        TextView txtStatusOrder;


        public ViewHolder(@NonNull View itemView) {
            super( itemView );
            ButterKnife.bind( this, itemView );
        }
    }
}
