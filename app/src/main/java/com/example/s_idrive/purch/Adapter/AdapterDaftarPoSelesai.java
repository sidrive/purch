package com.example.s_idrive.purch.Adapter;

/**
 * Created by s_idrive on 16-Apr-17.
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

public class AdapterDaftarPoSelesai extends BaseAdapter{

    private Activity activity;
    private LayoutInflater inflater;
    private List<Data> items;

    public AdapterDaftarPoSelesai(Activity activity, List<Data> items) {
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
            convertView = inflater.inflate(R.layout.list_row_po_selesai, null);

        TextView id = (TextView) convertView.findViewById(R.id.id);
        TextView nama = (TextView) convertView.findViewById(R.id.nama);
        TextView alamat = (TextView) convertView.findViewById(R.id.alamat);
        TextView barcode = (TextView) convertView.findViewById(R.id.barcode);
        TextView jumlah = (TextView) convertView.findViewById(R.id.jumlah);
        TextView total = (TextView) convertView.findViewById(R.id.total);

        Data data = items.get(position);

        id.setText(data.getId());
        nama.setText(data.getNama());
        alamat.setText("Harga : Rp. "+data.getAlamat());
        barcode.setText("Barcode : "+data.getBarcode());
        jumlah.setText("Jumlah : "+data.getJumlah());
        total.setText("Total : "+data.getTotal());

        return convertView;
    }
}
