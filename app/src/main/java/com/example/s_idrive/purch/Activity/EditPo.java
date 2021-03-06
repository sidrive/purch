package com.example.s_idrive.purch.Activity;

/**
 * Created by s_idrive on 13-Apr-17.
 */
import android.content.Intent;
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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.s_idrive.purch.Adapter.Adapter;
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

public class EditPo extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {
    Toolbar toolbar;
    FloatingActionButton fab;
    ListView list;
    SwipeRefreshLayout swipe;
    List<Data> itemList = new ArrayList<Data>();
    Adapter adapter;
    int success;
    AlertDialog.Builder dialog;
    LayoutInflater inflater;
    View dialogView;
    EditText txt_id, txt_nama, txt_harga, txt_idpo, txt_jumlah;
    String id, idpo, nama, alamat, kode_po;
    Integer harga, jumlah, total;
    TextView txtbartotalpo, txtbaridpo;
    Button btnverifikasi;

    private static final String TAG = MainActivity.class.getSimpleName();

    private static String url_select     = Server.URL + "select.php?id=";
    private static String url_main    = Server.URL + "selectEditPo.php?id=";
    private static String url_insert     = Server.URL + "insert.php";
    private static String url_edit       = Server.URL + "edit.php";
    private static String url_update     = Server.URL + "update.php";
    private static String url_delete     = Server.URL + "delete.php";
    private static String url_verifikasi     = Server.URL + "verifikasi.php";

    public static final String TAG_ID       = "id";
    public static final String TAG_IDPO       = "id_po";
    public static final String TAG_NAMA     = "nama_brg";
    public static final String TAG_HARGA   = "hrg_sup";
    public static final String TAG_BARCODE   = "kode_brg";
    public static final String TAG_JUMLAH   = "jml_brg";
    public static final String TAG_TOTAL   = "total";
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_MESSAGE = "message";

    private StringRequest stringRequest;
    private RequestQueue requestQueue;
    ArrayList<HashMap<String, String>> list_data;

    String tag_json_obj = "json_obj_req";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_po);
       //toolbar = (Toolbar) findViewById(R.id.toolbar);
       //setSupportActionBar(toolbar);

        // menghubungkan variablel pada layout dan pada java
        swipe           = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_layout);
        list            = (ListView) findViewById(R.id.list);
        txtbartotalpo   = (TextView) findViewById(R.id.txtbartotalpo);
        txtbaridpo      = (TextView) findViewById(R.id.txtbaridpo);
        btnverifikasi   = (Button) findViewById(R.id.btnverifikasi);

        Bundle bundle = getIntent().getExtras();
        kode_po = bundle.getString("dataPoBaru");

        // untuk mengisi data dari JSON ke dalam adapter
        adapter = new Adapter(EditPo.this, itemList);
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
                           panggilData();
                       }
                   }
        );

        btnverifikasi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                verifikasi();
                panggilData();
                callVolley();

            }
        });


        // listview ditekan lama akan menampilkan dua pilihan edit atau delete data
        list.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

            @Override
            public boolean onItemLongClick(final AdapterView<?> parent, View view,
                                           final int position, long id) {
                // TODO Auto-generated method stub
                final String idx = itemList.get(position).getId();

                final CharSequence[] dialogitem = {"Edit", "Delete"};
                dialog = new AlertDialog.Builder(EditPo.this);
                dialog.setCancelable(true);
                dialog.setItems(dialogitem, new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // TODO Auto-generated method stub
                        switch (which) {
                            case 0:
                                edit(idx);
                                break;
                            case 1:
                                delete(idx);
                                break;
                        }
                    }
                }).show();
                return false;
            }
        });

    }

    public void panggilData(){
        requestQueue = Volley.newRequestQueue(EditPo.this);

        list_data = new ArrayList<HashMap<String, String>>();

        // fungsi untuk mengambil data total dari po dan ditampilkan di textview
        stringRequest = new StringRequest(Request.Method.GET, url_main+kode_po, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray jsonArray = jsonObject.getJSONArray("data");
                    for (int a = 0; a < jsonArray.length(); a ++){
                        JSONObject json = jsonArray.getJSONObject(a);
                        HashMap<String, String> map  = new HashMap<String, String>();
                        map.put("id_po", json.getString("id_po"));
                        map.put("kode_sup", json.getString("kode_sup"));
                        map.put("total", json.getString("total"));
                        list_data.add(map);
                    }
                    //  Glide.with(getApplicationContext())
                    txtbartotalpo.setText("Total PO : Rp. "+(list_data.get(0).get("total")));
                    txtbaridpo.setText(list_data.get(0).get("id_po"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(EditPo.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        requestQueue.add(stringRequest);
    }

    // fungsi untuk verifikasi data
    private void verifikasi() {
        String url = url_verifikasi;
        requestQueue = Volley.newRequestQueue(EditPo.this);

        StringRequest postRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response) {
                        // menampilkan respone
                        Log.d("Response", response);
                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // error
                        Toast.makeText(EditPo.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams()
            {         // Menambahkan parameters post
                Map<String, String>  params = new HashMap<String, String>();
                params.put("id_po", kode_po);

                return params;
            }
        };
        requestQueue.add(postRequest);
    }

    @Override
    public void onRefresh() {
        itemList.clear();
        adapter.notifyDataSetChanged();
        callVolley();
        panggilData();
    }

    // untuk mengosongi edittext pada form
    private void kosong(){
        txt_id.setText(null);
        txt_nama.setText(null);
        txt_harga.setText(null);
    }

    // untuk menampilkan dialog from biodata
    private void DialogForm(String idx, String idpox, String namax, String hargax, String jumlahx, String button) {
        dialog = new AlertDialog.Builder(EditPo.this);
        inflater = getLayoutInflater();
        dialogView = inflater.inflate(R.layout.form_edit_po, null);
        dialog.setView(dialogView);
        dialog.setCancelable(true);
        dialog.setIcon(R.mipmap.ic_launcher);
        dialog.setTitle("Update Jumlah Barang");

        txt_id      = (EditText) dialogView.findViewById(R.id.txt_id);
        txt_idpo      = (EditText) dialogView.findViewById(R.id.txt_idpo);
        txt_nama    = (EditText) dialogView.findViewById(R.id.txt_nama);
        txt_harga  = (EditText) dialogView.findViewById(R.id.txt_harga);
        txt_jumlah  = (EditText) dialogView.findViewById(R.id.txt_jumlah);

        if (!idx.isEmpty()){
            txt_id.setText(idx);
            txt_idpo.setText(idpox);
            txt_nama.setText(namax);
            txt_harga.setText(hargax);
            txt_jumlah.setText(jumlahx);
        } else {
            kosong();
        }

        dialog.setPositiveButton(button, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                id      = txt_id.getText().toString();
                idpo      = txt_idpo.getText().toString();
                nama    = txt_nama.getText().toString();
                harga   = Integer.valueOf(txt_harga.getText().toString());
                jumlah  = Integer.valueOf(txt_jumlah.getText().toString());
                total = jumlah * harga;

                simpan_update();
                dialog.dismiss();
            }
        });

        dialog.setNegativeButton("BATAL", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                kosong();
            }
        });

        dialog.show();
    }

    // untuk menampilkan semua data pada listview
    private void callVolley(){
        itemList.clear();
        adapter.notifyDataSetChanged();
        swipe.setRefreshing(true);

        // membuat request JSON
        JsonArrayRequest jArr = new JsonArrayRequest(url_select+kode_po, new Response.Listener<JSONArray>() {
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


    // fungsi untuk menyimpan atau update
    private void simpan_update() {
        String url;
        // jika id kosong maka simpan, jika id ada nilainya maka update
        if (id.isEmpty()){
            url = url_insert;
        } else {
            url = url_update;
        }

        StringRequest strReq = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Response: " + response.toString());

                try {
                    JSONObject jObj = new JSONObject(response);
                    success = jObj.getInt(TAG_SUCCESS);

                    // Cek error node pada json
                    if (success == 1) {
                        Log.d("Add/update", jObj.toString());

                        callVolley();
                        panggilData();
                        kosong();

                        Toast.makeText(EditPo.this, jObj.getString(TAG_MESSAGE), Toast.LENGTH_LONG).show();
                        adapter.notifyDataSetChanged();

                    } else {
                        Toast.makeText(EditPo.this, jObj.getString(TAG_MESSAGE), Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    // JSON error
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Error: " + error.getMessage());
                Toast.makeText(EditPo.this, error.getMessage(), Toast.LENGTH_LONG).show();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting parameters ke post url
                Map<String, String> params = new HashMap<String, String>();
                // jika id kosong maka simpan, jika id ada nilainya maka update
                if (id.isEmpty()){
                    params.put("nama", nama);
                    params.put("alamat", alamat);
                } else {
                    params.put("id", id);
                    params.put("id_po", idpo);
                    params.put("jumlah", String.valueOf(jumlah));
                    params.put("total", String.valueOf(total));
                }

                return params;
            }

        };

        AppController.getInstance().addToRequestQueue(strReq, tag_json_obj);
    }


    // fungsi untuk get edit data
    private void edit(final String idx){
        StringRequest strReq = new StringRequest(Request.Method.POST, url_edit, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Response: " + response.toString());

                try {
                    JSONObject jObj = new JSONObject(response);
                    success = jObj.getInt(TAG_SUCCESS);

                    // Cek error node pada json
                    if (success == 1) {
                        Log.d("get edit data", jObj.toString());
                        String idx      = jObj.getString(TAG_ID);
                        String idpox      = jObj.getString(TAG_IDPO);
                        String namax    = jObj.getString(TAG_NAMA);
                        String alamatx  = jObj.getString(TAG_HARGA);
                        String jumlahx  = jObj.getString(TAG_JUMLAH);

                        DialogForm(idx, idpox, namax, alamatx, jumlahx, "UPDATE");

                        adapter.notifyDataSetChanged();

                    } else {
                        Toast.makeText(EditPo.this, jObj.getString(TAG_MESSAGE), Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    // JSON error
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Error: " + error.getMessage());
                Toast.makeText(EditPo.this, error.getMessage(), Toast.LENGTH_LONG).show();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting parameters ke post url
                Map<String, String> params = new HashMap<String, String>();
                params.put("id", idx);

                return params;
            }

        };

        AppController.getInstance().addToRequestQueue(strReq, tag_json_obj);
    }

    // fungsi untuk menghapus
    private void delete(final String idx){
        StringRequest strReq = new StringRequest(Request.Method.POST, url_delete, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Response: " + response.toString());

                try {
                    JSONObject jObj = new JSONObject(response);
                    success = jObj.getInt(TAG_SUCCESS);

                    // Cek error node pada json
                    if (success == 1) {
                        Log.d("delete", jObj.toString());

                        callVolley();
                        panggilData();

                        Toast.makeText(EditPo.this, jObj.getString(TAG_MESSAGE), Toast.LENGTH_LONG).show();

                        adapter.notifyDataSetChanged();

                    } else {
                        Toast.makeText(EditPo.this, jObj.getString(TAG_MESSAGE), Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    // JSON error
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Error: " + error.getMessage());
                Toast.makeText(EditPo.this, error.getMessage(), Toast.LENGTH_LONG).show();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting parameters ke post url
                Map<String, String> params = new HashMap<String, String>();
                params.put("id", idx);

                return params;
            }

        };

        AppController.getInstance().addToRequestQueue(strReq, tag_json_obj);
    }
}
