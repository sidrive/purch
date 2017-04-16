package com.example.s_idrive.purch.Activity;

import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.example.s_idrive.purch.Adapter.AdapterPoProses;
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
import com.example.s_idrive.purch.R;

public class PoDalamProses extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener  {

 /*  Toolbar toolbarPoProses; */
    ListView list;
    SwipeRefreshLayout swipe;
    List<Data> itemList = new ArrayList<Data>();
    AdapterPoProses adapter;
    int success;
    AlertDialog.Builder dialog;
    LayoutInflater inflater;
    View dialogView;
    EditText txt_id, txt_nama, txt_harga, txt_idpo, txt_jumlah;
    String id_po, kode_sup, tgl_po, total, status_maga, status_suplier, kode_po;

    private static final String TAG = MainActivity.class.getSimpleName();

    private static String url_selectpo     = Server.URL + "selectPo.php?id=";

    public static final String TAG_IDPO       = "id_po";
    public static final String TAG_KODESUP     = "kode_sup";
    public static final String TAG_TANGGAL   = "tgl_po";
    public static final String TAG_TOTAL   = "total";
    public static final String TAG_MAGA   = "status_maga";
    public static final String TAG_SUPLIER  = "status_suplier";
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_MESSAGE = "message";

    String tag_json_obj = "json_obj_req";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_po_dalam_proses);
 /*       toolbarPoProses = (Toolbar) findViewById(R.id.toolbarPoProses);
        setSupportActionBar(toolbarPoProses);
*/
       // menghubungkan variablel pada layout dan pada java
        swipe   = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_layout);
        list    = (ListView) findViewById(R.id.list);

       Bundle bundle = getIntent().getExtras();
       kode_po = bundle.getString("data2");

        // untuk mengisi data dari JSON ke dalam adapter
        adapter = new AdapterPoProses(PoDalamProses.this, itemList);
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

        // listview ditekan lama akan menampilkan dua pilihan edit atau delete data
        list.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

            @Override
            public boolean onItemLongClick(final AdapterView<?> parent, View view,
                                           final int position, long id) {
                // TODO Auto-generated method stub
                final String idx = itemList.get(position).getIdpo();

                Bundle bundle = new Bundle();
                bundle.putString("data1", idx);
                Intent intent = new Intent(PoDalamProses.this, EditPo.class);
                intent.putExtras(bundle);
                startActivity(intent);
                return false;
            }
        });
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
        JsonArrayRequest jArr = new JsonArrayRequest(url_selectpo+kode_po, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                Log.d(TAG, response.toString());

                // Parsing json
                for (int i = 0; i < response.length(); i++) {
                    try {
                        JSONObject obj = response.getJSONObject(i);

                        Data item = new Data();

                        item.setIdpo(obj.getString(TAG_IDPO));
                        item.setKodesup(obj.getString(TAG_KODESUP));
                        item.setTanggal(obj.getString(TAG_TANGGAL));
                        item.setTotalpo(obj.getString(TAG_TOTAL));
                        item.setMaga(obj.getString(TAG_MAGA));
                        item.setSuplier(obj.getString(TAG_SUPLIER));

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
