package com.example.s_idrive.purch.Activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
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

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.example.s_idrive.purch.R;
import com.example.s_idrive.purch.RequestHandler;
import com.example.s_idrive.purch.Util.Server;

import static com.example.s_idrive.purch.R.id.fab;

public class InputActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText txtnama, txtharga, txtbarcode;

    private Button buttonChoose, btnSimpan;
    private ImageView imageView;
    public static final String KEY_IMAGE = "image";
    public static final String UPLOAD_URL = Server.URL+"simpanPenawaran.php";
    private int PICK_IMAGE_REQUEST = 1;
    private Bitmap bitmap;

    ProgressDialog pd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        buttonChoose = (Button) findViewById(R.id.buttonChooseImage);
        btnSimpan = (Button) findViewById(R.id.btnSimpan);
        imageView = (ImageView) findViewById(R.id.imageView);

        buttonChoose.setOnClickListener(this);
        btnSimpan.setOnClickListener(this);

        txtnama = (EditText) findViewById(R.id.txtnama);
        txtharga = (EditText) findViewById(R.id.txtharga);
        txtbarcode = (EditText) findViewById(R.id.txtbarcode);

        pd = new ProgressDialog(this);



      /* fab.setOnClickListener(new View.OnClickListener() {
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
        }); */
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void showFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri filePath = data.getData();
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                imageView.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public String getStringImage(Bitmap bmp){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;
    }

    public void uploadImage(){
        final String image = getStringImage(bitmap);
        final String namabrg = txtnama.getText().toString().trim();
        final String hargabrg = txtharga.getText().toString().trim();
        final String barcode = txtbarcode.getText().toString().trim();
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
        class UploadImage extends AsyncTask<Void,Void,String> {
            ProgressDialog loading;
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(InputActivity.this,"Please wait...","uploading",false,false);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                Toast.makeText(InputActivity.this,s,Toast.LENGTH_LONG).show();
            }

            @Override
            protected String doInBackground(Void... params) {
                RequestHandler rh = new RequestHandler();
                HashMap<String,String> param = new HashMap<String,String>();
                param.put(KEY_IMAGE,image);
                param.put("nama_brg", namabrg);
                param.put("harga", hargabrg);
                param.put("barcode", barcode);
                String result = rh.sendPostRequest(UPLOAD_URL, param);
                return result;
            }
        }
        UploadImage u = new UploadImage();
        u.execute();
    }

    @Override
    public void onClick(View v) {
        if(v == buttonChoose){
            showFileChooser();
        }
        if(v == btnSimpan){
            uploadImage();
        }
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
