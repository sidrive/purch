package com.example.s_idrive.purch.Adapter;

/**
 * Created by s_idrive on 08-Apr-17.
 */

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.s_idrive.purch.BaseApp;
import com.example.s_idrive.purch.Model.detailPO;
import com.example.s_idrive.purch.R;

import java.util.ArrayList;


public class AdapterPO extends RecyclerView.Adapter<AdapterPO.ViewHolder> {

    private ArrayList<detailPO> purchase;
    Context context;

    public AdapterPO(Context context, ArrayList<detailPO> purchase) {
        this.purchase = purchase;
        this.context = context;
    }

    @Override
    public AdapterPO.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.detail_po, null);

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.barcode.setText(String.valueOf(purchase.get(position).getBarcode()));
        holder.namabrg.setText(String.valueOf(purchase.get(position).getNama_brg()));
        holder.harga.setText(String.valueOf("Rp. "+purchase.get(position).getHarga_sup()+",00"));
        holder.jumlah.setText(String.valueOf(purchase.get(position).getJumlah()));
    }

    @Override
    public int getItemCount() {
        return purchase.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView barcode, namabrg, harga, jumlah;

        public ViewHolder(View itemView) {
            super(itemView);
            barcode = (TextView)itemView.findViewById(R.id.maintxtkodebrg);
            namabrg = (TextView)itemView.findViewById(R.id.maintxtnamabrg);
            harga = (TextView)itemView.findViewById(R.id.maintxtharga);
            jumlah = (TextView)itemView.findViewById(R.id.maintxtjumlah);
        }
    }
}
