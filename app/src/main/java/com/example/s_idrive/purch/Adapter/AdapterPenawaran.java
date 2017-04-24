package com.example.s_idrive.purch.Adapter;

/**
 * Created by IT Server on 4/24/2017.
 */

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.s_idrive.purch.Activity.PenawaranActivity;
import com.example.s_idrive.purch.R;
import com.example.s_idrive.purch.Util.Server;

import java.util.ArrayList;
import java.util.HashMap;

public class AdapterPenawaran extends RecyclerView.Adapter<AdapterPenawaran.ViewHolder> {

    Context context;
    ArrayList<HashMap<String, String>> list_data;

    public AdapterPenawaran(PenawaranActivity penawaranActivity, ArrayList<HashMap<String, String>> list_data) {
        this.context = penawaranActivity;
        this.list_data = list_data;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_row_daftar_penawaran, null);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Glide.with(context)
                .load(Server.URL+ "/img/" + list_data.get(position).get("foto"))
                .crossFade()
                .placeholder(R.mipmap.ic_launcher)
                .into(holder.imgbrg);
        holder.txtnama.setText(list_data.get(position).get("nama_brg"));
        holder.txtharga.setText(list_data.get(position).get("harga_brg"));
        holder.txtbarcode.setText(list_data.get(position).get("barcode"));
    }

    @Override
    public int getItemCount() {
        return list_data.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtnama;
        TextView txtharga;
        TextView txtbarcode;
        ImageView imgbrg;

        public ViewHolder(View itemView) {
            super(itemView);

            txtnama = (TextView) itemView.findViewById(R.id.txtnama);
            txtharga = (TextView) itemView.findViewById(R.id.txtharga);
            txtbarcode = (TextView) itemView.findViewById(R.id.txtbarcode);
            imgbrg = (ImageView) itemView.findViewById(R.id.imgbrg);
        }
    }
}
