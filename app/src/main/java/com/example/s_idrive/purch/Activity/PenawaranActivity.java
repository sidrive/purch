package com.example.s_idrive.purch.Activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;


import com.example.s_idrive.purch.Adapter.AdapterPenawaran;
import com.example.s_idrive.purch.App.AppController;
import com.example.s_idrive.purch.Helper.Helper;
import com.example.s_idrive.purch.R;
import com.example.s_idrive.purch.Util.Server;

public class PenawaranActivity extends AppCompatActivity {

    private RecyclerView lvpenawaran;

    private ArrayList<HashMap<String, String>> list_data;

    private String tag_json = "tag_json";

    private String url = Helper.BASE_URL+ "getdatapenawaran.php";

    ProgressDialog pd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_penawaran);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.btninput);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), InputActivity.class));
            }
        });

        pd = new ProgressDialog(this);

        lvpenawaran = (RecyclerView)findViewById(R.id.lvpenawaran);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        lvpenawaran.setLayoutManager(linearLayoutManager);

        list_data = new ArrayList<HashMap<String, String>>();

        showData();
    }

    private void showData() {

        pd.setMessage("Loading...");
        pd.setCancelable(false);
        showDialog();

        JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d("RESPONSE ", response.toString());
                hideDialog();
                try {
                    JSONArray jray = response.getJSONArray("penawaran");
                    for (int a = 0; a < jray.length(); a++){
                        JSONObject json = jray.getJSONObject(a);
                        HashMap<String, String> map = new HashMap<>();
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
                VolleyLog.d("ERROR", error.getMessage());
                hideDialog();
            }
        });

        AppController.getInstance().addToRequestQueue(jsonRequest, tag_json);
    }

 /*   @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
*/
    private void showDialog() {
        if (!pd.isShowing())
            pd.show();
    }

    private void hideDialog() {
        if (pd.isShowing())
            pd.dismiss();
    }
}
