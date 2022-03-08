package com.example.afinal;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class SigninActivity extends AppCompatActivity {
    String URL = "http://192.168.154.207:8000/api/useremail/";
    EditText email;
    EditText password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);
        email = (EditText) findViewById(R.id.email);
        password = (EditText) findViewById(R.id.password);


        ImageView showpass = (ImageView) findViewById(R.id.imageView2);
        ImageView hidepass = (ImageView) findViewById(R.id.imageView1);


        //show and hide password
        showpass.setOnClickListener(v -> {
            showpass.setVisibility(View.GONE);
            hidepass.setVisibility(View.VISIBLE);
            password.setTransformationMethod(HideReturnsTransformationMethod.getInstance());

        });
        hidepass.setOnClickListener(v -> {
            hidepass.setVisibility(View.GONE);
            showpass.setVisibility(View.VISIBLE);
            password.setTransformationMethod(PasswordTransformationMethod.getInstance());

        });



    }

    public void signup(View v) {
        Intent i = new Intent(getApplicationContext(), SignupActivity.class);
        startActivity(i);
    }

    public void forget(View v) {
        Intent i = new Intent(getApplicationContext(), ForgetActivity.class);
        startActivity(i);
    }

    public void home(View v) {
        parseApiData();

    }


    public void parseApiData() {
        StringRequest s = new StringRequest(Request.Method.GET, URL + email.getText(), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.i("result", response);
                try {
                    JSONObject o = new JSONObject(response);
                    JSONArray x = o.getJSONArray("user");
                    if (x.length() > 0) {
                        for (int i = 0; i < x.length(); i++) {
                            JSONObject user = x.getJSONObject(i);
                            if (user.getString("password").equals(password.getText().toString())) {
                                Log.i("tmmmmm", "tmmmmmmmmmmmmm");

                                SharedPreferences sharedPreferences = getSharedPreferences("MySharedPref",MODE_PRIVATE);
                                SharedPreferences.Editor myEdit = sharedPreferences.edit();
                                myEdit.putString("id",user.getString("id"));
                                myEdit.commit();

                                Intent z = new Intent(getApplicationContext(), HomeActivity.class);
                                startActivity(z);
                            } else {
                                password.setError("incorrect password");

                            }
                        }
                    } else {
                        email.setError("email doesnt exist");

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i("nelaa", String.valueOf(error));

            }
        });
        RequestQueue r = Volley.newRequestQueue(this);
        r.add(s);

    }


}