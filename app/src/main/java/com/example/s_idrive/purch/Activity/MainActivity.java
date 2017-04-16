package com.example.s_idrive.purch.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.text.Html;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;
import com.bumptech.glide.Glide;
import com.example.s_idrive.purch.Adapter.Adapter;
import com.example.s_idrive.purch.Adapter.AdapterPO;
import com.example.s_idrive.purch.App.AppController;
import com.example.s_idrive.purch.BaseApp;
import com.example.s_idrive.purch.Data.Data;
import com.example.s_idrive.purch.Helper.Helper;
import com.example.s_idrive.purch.Model.detailPO;
import com.example.s_idrive.purch.R;
import com.example.s_idrive.purch.SessionManager;
import com.example.s_idrive.purch.Util.Server;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;



public class MainActivity extends BaseApp {
    SessionManager sessionManager;

    public TextView username, txtprofil, txtcoba;
    private Button btnCoba, btnPoProses, btnPoSelesai;

    private RequestQueue requestQueue;
    private StringRequest stringRequest;

    ArrayList<HashMap<String, String>> list_data;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sessionManager = new SessionManager(getApplicationContext());

        txtprofil = (TextView)findViewById(R.id.txtprofil);
        txtcoba = (TextView)findViewById(R.id.txtcoba);

        sessionManager = new SessionManager(getApplicationContext());

        HashMap<String, String> user = sessionManager.getUserDetails();
        String name = user.get(SessionManager.jabatan);
        txtprofil.setText(Html.fromHtml("<b>" + name + "</b>"));

        String url = "http://192.168.42.220:8080/maga1/tampil.php?id="+name;

        requestQueue = Volley.newRequestQueue(MainActivity.this);

        list_data = new ArrayList<HashMap<String, String>>();

        stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray jsonArray = jsonObject.getJSONArray("data");
                    for (int a = 0; a < jsonArray.length(); a ++){
                        JSONObject json = jsonArray.getJSONObject(a);
                        HashMap<String, String> map  = new HashMap<String, String>();
                        map.put("id", json.getString("id"));
                        map.put("username", json.getString("username"));
                        map.put("username", json.getString("password"));
                        map.put("nama", json.getString("nama"));
                        map.put("kode_sup", json.getString("kode_sup"));
                        list_data.add(map);
                    }
                  //  Glide.with(getApplicationContext())
                    txtcoba.setText(list_data.get(0).get("kode_sup"));
                    } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MainActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        requestQueue.add(stringRequest);


        btnCoba = (Button) findViewById(R.id.btncoba);
        btnCoba.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putString("data1", txtcoba.getText().toString());
                Intent intent = new Intent(MainActivity.this, EditPo.class);
                intent.putExtras(bundle);
                view.startAnimation(BtnAnimasi);
                startActivity(intent);
            }
        });

        btnPoProses = (Button) findViewById(R.id.btnpoproses);
        btnPoProses.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               Bundle bundle = new Bundle();
                bundle.putString("data2", txtcoba.getText().toString());
                Intent intent = new Intent(MainActivity.this, PoDalamProses.class);
                intent.putExtras(bundle);
                view.startAnimation(BtnAnimasi);
                startActivity(intent);
            }
        });

        btnPoSelesai = (Button) findViewById(R.id.btnposelesai);
        btnPoSelesai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putString("data3", txtcoba.getText().toString());
                Intent intent = new Intent(MainActivity.this, DaftarPoActivity.class);
                intent.putExtras(bundle);
                view.startAnimation(BtnAnimasi);
                startActivity(intent);
            }
        });

    }



}
