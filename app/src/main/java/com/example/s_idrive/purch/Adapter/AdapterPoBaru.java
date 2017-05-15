package com.example.s_idrive.purch.Adapter;

/**
 * Created by IT Server on 4/17/2017.
 */

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.s_idrive.purch.R;
import com.example.s_idrive.purch.App.AppController;
import com.example.s_idrive.purch.Data.Data;

import java.util.List;

public class AdapterPoBaru extends BaseAdapter {

    private Activity activity;
    private LayoutInflater inflater;
    private List<Data> items;
    public String status_maga, status_suplier, magastatus, suplierstatus;

    public AdapterPoBaru(Activity activity, List<Data> items) {
        this.activity = activity;
        this.items = items;
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Object getItem(int location) {
        return items.get(location);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (inflater == null)
            inflater = (LayoutInflater) activity
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if (convertView == null)
            convertView = inflater.inflate(R.layout.list_row_po_baru, null);

        TextView id_po = (TextView) convertView.findViewById(R.id.id_po);
        TextView kode_sup = (TextView) convertView.findViewById(R.id.kode_sup);
        TextView tanggal = (TextView) convertView.findViewById(R.id.tgl_po);
        TextView totalpo = (TextView) convertView.findViewById(R.id.totalpo);
        TextView maga = (TextView) convertView.findViewById(R.id.status_maga);
        TextView suplier = (TextView) convertView.findViewById(R.id.status_suplier);

        Data data = items.get(position);

        status_maga = data.getMaga().toString();    status_suplier = data.getSuplier().toString();
        String sup ="N", mag = "N";
        id_po.setText(data.getIdpo());
        kode_sup.setText(data.getKodesup());
        tanggal.setText("Tanggal dibuat : "+data.getTanggal());
        totalpo.setText("Total : Rp. "+data.getTotalpo());


        if (status_maga.equals(mag)){
            magastatus = "Belum Konfirmasi";
            maga.setText("Status Maga : "+magastatus);
        }else {
            magastatus = "Konfirmasi";
            maga.setText("Status Maga : "+magastatus);
        }

        if (status_suplier.equals(sup)){
            suplierstatus = "Belum Konfirmasi";
            suplier.setText("Status Suplier : "+suplierstatus);
        }  else{
            suplierstatus = "Konfirmasi";
            suplier.setText("Status Suplier : "+suplierstatus);}

        return convertView;
    }
}
