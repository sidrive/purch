package com.example.s_idrive.purch.Activity;

import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.example.s_idrive.purch.Adapter.Adapter;
import com.example.s_idrive.purch.Adapter.AdapterDaftarPoSelesai;
import com.example.s_idrive.purch.App.AppController;
import com.example.s_idrive.purch.Data.Data;
import com.example.s_idrive.purch.R;
import com.example.s_idrive.purch.Util.Server;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DaftarPoSelesai extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {

    Toolbar toolbar;
    FloatingActionButton fab;
    ListView list;
    SwipeRefreshLayout swipe;
    List<Data> itemList = new ArrayList<Data>();
    AdapterDaftarPoSelesai adapter;
    int success;
    AlertDialog.Builder dialog;
    LayoutInflater inflater;
    View dialogView;
    String id, idpo, nama, alamat, kode_po;
    TextView txtbaridpo, txtbartotalpo;
    Integer harga;
    Integer jumlah;
    String total;

    private static final String TAG = MainActivity.class.getSimpleName();

    private static String url_selectposelesai     = Server.URL + "selectPoSelesai.php?id=";

    public static final String TAG_ID       = "id";
    public static final String TAG_IDPO       = "id_po";
    public static final String TAG_NAMA     = "nama_brg";
    public static final String TAG_HARGA   = "hrg_sup";
    public static final String TAG_BARCODE   = "kode_brg";
    public static final String TAG_JUMLAH   = "jml_brg";
    public static final String TAG_TOTAL   = "total";
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_MESSAGE = "message";

    String tag_json_obj = "json_obj_req";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daftar_po_selesai);

        // menghubungkan variablel pada layout dan pada java
        swipe   = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_layout);
        list    = (ListView) findViewById(R.id.list);


        Bundle bundle = getIntent().getExtras();
        kode_po = bundle.getString("id_po");
        total = bundle.getString("total");

        txtbaridpo = (TextView)findViewById(R.id.txtbaridpo);
        txtbartotalpo = (TextView)findViewById(R.id.txtbartotalpo);
        txtbaridpo.setText(kode_po);
        txtbartotalpo.setText("Total PO : Rp. "+total);

        // untuk mengisi data dari JSON ke dalam adapter
        adapter = new AdapterDaftarPoSelesai(DaftarPoSelesai.this, itemList);
        list.setAdapter(adapter);

        // menamilkan widget refresh
        swipe.setOnRefreshListener(this);
        swipe.post(new Runnable() {
                       @Override
                       public void run() {
                           swipe.setRefreshing(true);
                           itemList.clear();
                           adapter.notifyDataSetChanged();
                           callVolley();
                       }
                   }
        );
    }

    @Override
    public void onRefresh() {
        itemList.clear();
        adapter.notifyDataSetChanged();
        callVolley();
    }

    // untuk menampilkan semua data pada listview
    private void callVolley(){
        itemList.clear();
        adapter.notifyDataSetChanged();
        swipe.setRefreshing(true);

        // membuat request JSON
        JsonArrayRequest jArr = new JsonArrayRequest(url_selectposelesai+kode_po, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                Log.d(TAG, response.toString());

                // Parsing json
                for (int i = 0; i < response.length(); i++) {
                    try {
                        JSONObject obj = response.getJSONObject(i);

                        Data item = new Data();

                        item.setId(obj.getString(TAG_ID));
                        item.setNama(obj.getString(TAG_NAMA));
                        item.setAlamat(obj.getString(TAG_HARGA));
                        item.setBarcode(obj.getString(TAG_BARCODE));
                        item.setJumlah(obj.getString(TAG_JUMLAH));
                        item.setTotal(obj.getString(TAG_TOTAL));

                        // menambah item ke array
                        itemList.add(item);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                // notifikasi adanya perubahan data pada adapter
                adapter.notifyDataSetChanged();

                swipe.setRefreshing(false);
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
                swipe.setRefreshing(false);
            }
        });

        // menambah request ke request queue
        AppController.getInstance().addToRequestQueue(jArr);
    }
}
