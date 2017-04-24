package com.example.s_idrive.purch.Activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.s_idrive.purch.App.AppController;
import com.example.s_idrive.purch.Helper.Helper;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import com.example.s_idrive.purch.R;

public class InputActivity extends AppCompatActivity {

    private EditText txtnama, txtharga, txtbarcode;

    ProgressDialog pd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        txtnama = (EditText) findViewById(R.id.txtnama);
        txtharga = (EditText) findViewById(R.id.txtharga);
        txtbarcode = (EditText) findViewById(R.id.txtbarcode);

        pd = new ProgressDialog(this);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.btnsimpan);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String namabrg = txtnama.getText().toString().trim();
                String hargabrg = txtharga.getText().toString().trim();
                String barcode = txtbarcode.getText().toString().trim();
                if (!namabrg.isEmpty() && !hargabrg.isEmpty() && !barcode.isEmpty()) {
                    simpanData(namabrg, hargabrg, barcode);
                } else if (namabrg.isEmpty()) {
                    txtnama.setError("Nama Barang tidak boleh kosong");
                    txtnama.requestFocus();
                } else if (hargabrg.isEmpty()) {
                    txtharga.setError("Harga Barang tidak boleh kosong");
                    txtharga.requestFocus();
                } else if (barcode.isEmpty()) {
                    txtbarcode.setError("Barcode tidak boleh kosong");
                    txtbarcode.requestFocus();
                }
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void simpanData(final String namabrg, final String hargabrg, final String barcode) {
        String url_simpan = Helper.BASE_URL+"simpanpenawaran.php";

        String tag_json = "tag_json";

        pd.setCancelable(false);
        pd.setMessage("Menyimpan...");
        showDialog();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url_simpan, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("response", response.toString());
                hideDialog();

                try {
                    JSONObject jObject = new JSONObject(response);
                    String pesan = jObject.getString("pesan");
                    String hasil = jObject.getString("result");
                    if (hasil.equalsIgnoreCase("true")) {
                        Toast.makeText(InputActivity.this, pesan, Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(getApplicationContext(), PenawaranActivity.class));
                        finish();
                    } else {
                        Toast.makeText(InputActivity.this, pesan, Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(InputActivity.this, "Error JSON", Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("ERROR", error.getMessage());
                Toast.makeText(InputActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                hideDialog();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> param = new HashMap<String, String>();
                param.put("nama_brg", namabrg);
                param.put("harga", hargabrg);
                param.put("barcode", barcode);
                return param;
            }
        };

        AppController.getInstance().addToRequestQueue(stringRequest, tag_json);
    }

    private void showDialog() {
        if (!pd.isShowing())
            pd.show();
    }

    private void hideDialog() {
        if (pd.isShowing())
            pd.dismiss();
    }

}
