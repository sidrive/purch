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
import com.example.s_idrive.purch.Helper.Helper;
import com.example.s_idrive.purch.Holder.PenawaranHolder;
import com.example.s_idrive.purch.R;
import com.example.s_idrive.purch.Util.Server;

import java.util.ArrayList;
import java.util.HashMap;

public class AdapterPenawaran extends RecyclerView.Adapter<PenawaranHolder> {

    private ArrayList<HashMap<String, String>> list_data;
    Context context;

    public AdapterPenawaran(PenawaranActivity penawaranActivity, ArrayList<HashMap<String, String>> list_data) {
        this.list_data = list_data;
        this.context = penawaranActivity;
    }

    @Override
    public PenawaranHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_row_daftar_penawaran, null);
        return new PenawaranHolder(view);
    }

    @Override
    public void onBindViewHolder(PenawaranHolder holder, int position) {
        holder.txtnama.setText(list_data.get(position).get("nama_brg"));
        holder.txtharga.setText(list_data.get(position).get("harga_brg"));
        holder.txtbarcode.setText(list_data.get(position).get("barcode"));
        Glide.with(context).load(Helper.BASE_URL+ "img/" + list_data.get(position).get("foto"))
                .crossFade().placeholder(R.mipmap.ic_launcher).into(holder.imgbrg);
    }

    @Override
    public int getItemCount() {
        return list_data.size();
    }
}
