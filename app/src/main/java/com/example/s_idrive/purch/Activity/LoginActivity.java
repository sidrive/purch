package com.example.s_idrive.purch.Activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;
import com.example.s_idrive.purch.BaseApp;
import com.example.s_idrive.purch.Firebase.RegisterDevicesActivity;
import com.example.s_idrive.purch.Helper.Helper;
import com.example.s_idrive.purch.R;
import com.example.s_idrive.purch.SessionManager;
import com.rengwuxian.materialedittext.MaterialEditText;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends BaseApp {

    private MaterialEditText logtxtEmail, logtxtPassword;
    private TextView loglblRegister;
    private Button logbtnLogin;

    SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        sessionManager = new SessionManager(getApplicationContext());

        setupView();
        loglblRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view.startAnimation(BtnAnimasi);
                startActivity(new Intent(getApplicationContext(), RegisterDevicesActivity.class));
            }
        });

        logbtnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loginUser();
            }
        });
    }

    private void loginUser() {
        logtxtEmail.setError(null);
        logtxtPassword.setError(null);
        /*check kebaradan teks*/
        if (Helper.isEmpty(logtxtEmail)) {
            logtxtEmail.setError("Email masih kosong");
            logtxtEmail.requestFocus();
        } else if (Helper.isEmpty(logtxtPassword)) {
            logtxtPassword.setError("Password masih kosong");
            logtxtPassword.requestFocus();
        } else {
            /*kirim data ke server*/
            String URL = Helper.BASE_URL + "loginAndro.php";
            Map<String, String> param = new HashMap<>();
            param.put("email", logtxtEmail.getText().toString());
            param.put("password", logtxtPassword.getText().toString());

            /*menampilkan progressbar saat mengirim data*/
            ProgressDialog pd = new ProgressDialog(context);
            pd.setIndeterminate(true);
            pd.setCancelable(false);
            pd.setInverseBackgroundForced(false);
            pd.setCanceledOnTouchOutside(false);
            pd.setTitle("Info");
            pd.setMessage("Login");
            pd.show();

            try {
                /*format ambil data*/
                aQuery.progress(pd).ajax(URL, param, String.class, new AjaxCallback<String>() {
                    @Override
                    public void callback(String url, String object, AjaxStatus status) {
                        /*jika objek tidak kosong*/
                        if (object != null) {
                            try {
                                JSONObject jsonObject = new JSONObject(object);
                                String result = jsonObject.getString("result");
                                String msg = jsonObject.getString("msg");
                                if (result.equalsIgnoreCase("true")) {
                                    sessionManager.createSession(logtxtEmail.getText().toString());
                                    startActivity(new Intent(context, MainActivity.class));
                                    Helper.pesan(context, msg);
                                    finish();
                                } else {
                                    Helper.pesan(context, msg);
                                }
                            } catch (JSONException e) {
                                Helper.pesan(context, "Error convert data json");
                            }
                        }
                    }
                });
            } catch (Exception e) {
                Helper.pesan(context, "Gagal mengambil data");
            }
        }
    }

    private void setupView() {
        logtxtEmail = (MaterialEditText) findViewById(R.id.logtxtEmail);
        logtxtPassword = (MaterialEditText) findViewById(R.id.logtxtPassword);
        logbtnLogin = (Button) findViewById(R.id.logbtnLogin);
        loglblRegister = (TextView) findViewById(R.id.loglblRegister);
    }
}
