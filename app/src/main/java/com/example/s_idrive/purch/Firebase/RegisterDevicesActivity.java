package com.example.s_idrive.purch.Firebase;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.s_idrive.purch.Helper.Helper;
import com.example.s_idrive.purch.R;
import android.app.ProgressDialog;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class RegisterDevicesActivity extends AppCompatActivity implements View.OnClickListener {

    private Button buttonRegister;
    private EditText editTextEmail, editTextPassword1, editTextPassword2;
    private TextView txtViewToken;
    private ProgressDialog progressDialog;

    private static final String URL_REGISTER_DEVICE = "http://nurmuha.hostzi.com/maga1/RegisterDevice.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_devices);

        //getting views from xml
        editTextEmail = (EditText) findViewById(R.id.editTextEmail);
        buttonRegister = (Button) findViewById(R.id.buttonRegister);
        txtViewToken = (TextView) findViewById(R.id.txtToken1);
        editTextPassword2 = (EditText) findViewById(R.id.regtxtPassword2);
        editTextPassword1 = (EditText) findViewById(R.id.regtxtPassword1);

        //adding listener to view
        buttonRegister.setOnClickListener(this);
    }

    //storing token to mysql server
    private void sendTokenToServer() {
        editTextEmail.setError(null);
        editTextPassword1.setError(null);
        editTextPassword2.setError(null);
        /*check keberadaan teks*/
        if (Helper.isEmpty(editTextEmail)) {
            editTextEmail.setError("Email masih kosong");
            editTextEmail.requestFocus();
        } else if (!Helper.isEmailValid(editTextEmail)) {
            editTextEmail.setError("Format email salah");
            editTextEmail.requestFocus();
        } else if (Helper.isEmpty(editTextPassword1)) {
            editTextPassword1.setError("Password masih kosong");
            editTextPassword1.requestFocus();
        } else if (Helper.isEmpty(editTextPassword2)) {
            editTextPassword2.setError("Konfirmasi password masih kosong");
            editTextPassword2.requestFocus();
            /*check kesamaan password*/
        } else if (Helper.isCompare(editTextPassword1, editTextPassword2)) {
            editTextPassword2.setError("Password tidak cocok");
            editTextPassword2.requestFocus();
        } else {

            progressDialog = new ProgressDialog(this);
            progressDialog.setMessage("Registering Device...");
            progressDialog.show();

            final String token = SharedPrefManager.getInstance(this).getDeviceToken();
            final String email = editTextEmail.getText().toString();
            final String password = editTextPassword2.getText().toString();

            if (token == null) {
                progressDialog.dismiss();
                Toast.makeText(this, "Token not generated", Toast.LENGTH_LONG).show();
                return;
            }

            StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_REGISTER_DEVICE,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            progressDialog.dismiss();
                            try {
                                JSONObject obj = new JSONObject(response);
                                Toast.makeText(RegisterDevicesActivity.this, obj.getString("message"), Toast.LENGTH_LONG).show();
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            progressDialog.dismiss();
                            Toast.makeText(RegisterDevicesActivity.this, error.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }) {

                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();
                    params.put("email", email);
                    params.put("token", token);
                    params.put("password", password);
                    return params;
                }
            };
            RequestQueue requestQueue = Volley.newRequestQueue(this);
            requestQueue.add(stringRequest);
        }
    }

    private void vieToken(){
        String token = SharedPrefManager.getInstance(this).getDeviceToken();

        //if token is not null
        if (token != null) {
            //displaying the token
            txtViewToken.setText(token);
        } else {
            //if token is null that means something wrong
            txtViewToken.setText("Token not generated");
        }
    }

    @Override
    public void onClick(View view) {
        if (view == buttonRegister) {
            sendTokenToServer();
        }
    }
}
