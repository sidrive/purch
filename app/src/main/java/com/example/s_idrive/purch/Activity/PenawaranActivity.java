package com.example.s_idrive.purch.Activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;


import com.example.s_idrive.purch.Adapter.AdapterPenawaran;
import com.example.s_idrive.purch.R;
import com.example.s_idrive.purch.Util.Server;

public class PenawaranActivity extends AppCompatActivity {

    private RecyclerView lvpenawaran;

    private RequestQueue requestQueue;
    private StringRequest stringRequest;

    ArrayList<HashMap<String, String>> list_data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_penawaran);

        String url = Server.URL+"getdatapenawaran.php";

        lvpenawaran = (RecyclerView) findViewById(R.id.lvpenawaran);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        lvpenawaran.setLayoutManager(llm);

        requestQueue = Volley.newRequestQueue(PenawaranActivity.this);

        list_data = new ArrayList<HashMap<String, String>>();

        stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("response ", response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray jsonArray = jsonObject.getJSONArray("penawaran");
                    for (int a = 0; a < jsonArray.length(); a++) {
                        JSONObject json = jsonArray.getJSONObject(a);
                        HashMap<String, String> map = new HashMap<String, String>();
                        map.put("id", json.getString("id"));
                        map.put("id_penawaran", json.getString("id_penawaran"));
                        map.put("nama_brg", json.getString("nama_brg"));
                        map.put("barcode", json.getString("barcode"));
                        map.put("harga_brg", json.getString("harga_brg"));
                        map.put("foto", json.getString("foto"));
                        list_data.add(map);
                        AdapterPenawaran adapter = new AdapterPenawaran(PenawaranActivity.this, list_data);
                        lvpenawaran.setAdapter(adapter);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(PenawaranActivity.this, error.getMessage(), Toast.LENGTH_LONG).show();
            }
        });

        requestQueue.add(stringRequest);
    }
}
