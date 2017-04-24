package com.example.s_idrive.purch.Holder;

/**
 * Created by IT Server on 4/24/2017.
 */

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.s_idrive.purch.R;

public class PenawaranHolder extends RecyclerView.ViewHolder {

    public TextView txtnama, txtharga, txtbarcode;
    public ImageView imgbrg;

    public PenawaranHolder(View itemView) {
        super(itemView);

        txtnama = (TextView)itemView.findViewById(R.id.txtnama);
        txtharga = (TextView)itemView.findViewById(R.id.txtharga);
        txtbarcode = (TextView)itemView.findViewById(R.id.txtbarcode);
        imgbrg = (ImageView)itemView.findViewById(R.id.imgbrg);
    }
}
