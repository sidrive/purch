package com.example.s_idrive.purch.Activity;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.NotificationCompat;
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
import com.example.s_idrive.purch.AndroidService;
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
    private Button btnPoBaru, btnPoProses, btnPoSelesai, btnlogout, btnPenawaran, btn;

    private RequestQueue requestQueue;
    private StringRequest stringRequest;

    ArrayList<HashMap<String, String>> list_data;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sessionManager = new SessionManager(getApplicationContext());
        startService(new Intent(MainActivity.this, AndroidService.class));

        txtprofil = (TextView)findViewById(R.id.txtprofil);
        txtcoba = (TextView)findViewById(R.id.txtcoba);

        HashMap<String, String> user = sessionManager.getUserDetails();
        String name = user.get(SessionManager.jabatan);
        txtprofil.setText(Html.fromHtml("<b>" + name + "</b>"));

        String urlmain = Server.URL+ "tampil.php?id="+name;

        requestQueue = Volley.newRequestQueue(MainActivity.this);

        list_data = new ArrayList<HashMap<String, String>>();

        stringRequest = new StringRequest(Request.Method.GET, urlmain, new Response.Listener<String>() {
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

        btn = (Button) findViewById(R.id.btn);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Notify("You've received new message", "message");
            }
        });


        btnPoBaru = (Button) findViewById(R.id.btnpobaru);
        btnPoBaru.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putString("data1", txtcoba.getText().toString());
                Intent intent = new Intent(MainActivity.this, PoBaruActivity.class);
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

        btnPenawaran = (Button) findViewById(R.id.btnpenawaran);
        btnPenawaran.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               /* Bundle bundle = new Bundle();
                bundle.putString("data3", txtcoba.getText().toString()); */
                Intent intent = new Intent(MainActivity.this, PenawaranActivity.class);
               /* intent.putExtras(bundle); */
                view.startAnimation(BtnAnimasi);
                startActivity(intent);
            }
        });

        btnlogout = (Button)findViewById(R.id.btnlogout);
        btnlogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sessionManager.logout();
            }
        });

    }

    public void Notify(String notificationTitle, String notificationMessage){

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
        builder.setSmallIcon(R.drawable.ic_stat_pesan_notif);
        builder.setContentTitle(notificationTitle);//"Notifications Example")
        builder.setContentText(notificationMessage);//"This is a test notification");
        builder.setTicker("New Message Alert!");
        builder.setSmallIcon(R.drawable.ic_stat_pesan_notif);

        Intent notificationIntent = new Intent(MainActivity.this, PoBaruActivity.class);
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0, notificationIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(contentIntent);
        // Add as notification
        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        manager.notify(1, builder.build());
    }


}
