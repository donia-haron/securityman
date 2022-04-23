package com.example.afinal;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ProfileActivity extends AppCompatActivity {
    String URL = "http://192.168.220.207:8000/api/user/";
    EditText name;
    EditText email;
    EditText phone;
    EditText gender;
    EditText nid;
    EditText address;
    EditText dob;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        name = (EditText) findViewById(R.id.name);
        email = (EditText) findViewById(R.id.email);
        phone = (EditText) findViewById(R.id.phone);
        address = (EditText) findViewById(R.id.address);
        nid = (EditText) findViewById(R.id.nid);
        dob = (EditText) findViewById(R.id.dob);
        gender = (EditText) findViewById(R.id.gender);
        parseApiData();


    }

    public void parseApiData() {
        SharedPreferences sh = this.getSharedPreferences("MySharedPref", Context.MODE_PRIVATE);
        String id = sh.getString("sid", "");
        Log.i("iddd", id);

        StringRequest s = new StringRequest(Request.Method.GET, URL + id, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.i("result", response);
                try {
                    JSONArray x = new JSONArray(response);

                    if (x.length() > 0) {
                        for (int i = 0; i < x.length(); i++) {
                            JSONObject user = x.getJSONObject(i);
                            int id1 = user.getInt("id");
                            String name1 = user.getString("username");
                            String email1 = user.getString("email");
                            String address1 = user.getString("address");
                            String phone1 = user.getString("phone");
                            String gender1 = user.getString("gender");
                            String dob1 = user.getString("dob");
                            dob.setText(dob1);
                            gender.setText(gender1);
                            name.setText(name1);
                            email.setText(email1);
                            address.setText(address1);
                            phone.setText(phone1);
                            nid.setText(id);
                        }


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

    public void logout(View view) {
        SharedPreferences preferences = getSharedPreferences("MySharedPref", Context.MODE_PRIVATE);
        preferences.edit().remove("sid").commit();
        Intent i = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(i);

    }

    public void update(View view) {

        String url = "http://192.168.220.207:8000/api/user/update/";
        SharedPreferences sh = this.getSharedPreferences("MySharedPref", Context.MODE_PRIVATE);
        String id = sh.getString("sid", "");
        Log.i("iddd", id);

        RequestQueue queue = Volley.newRequestQueue(this);

        StringRequest request = new StringRequest(Request.Method.POST, url+id, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONObject respObj = new JSONObject(response);
                    Log.i("response", response);
                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            Intent i = new Intent(getApplicationContext(), HomeActivity.class);
                            startActivity(i);
                        }
                    }, 2000);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i("eroororr", String.valueOf(error));
            }
        }) {
            @Override
            protected Map getParams() {

                Map<String, Object> params = new HashMap<>();


                params.put("username",name.getText().toString() );
                params.put("address", address.getText().toString());
                params.put("email", email.getText().toString());
                params.put("gender", gender.getText().toString());
                params.put("phone", phone.getText().toString());
                params.put("role", "securityman");
                params.put("dob", dob.getText().toString());


                Log.i("param", String.valueOf(params));

                return params;
            }
        };

        queue.add(request);






    }

}