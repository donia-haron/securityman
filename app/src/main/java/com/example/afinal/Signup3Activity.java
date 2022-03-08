package com.example.afinal;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class Signup3Activity extends AppCompatActivity {
    user user;
    user car;
    private TextView username;
    private TextView email;
    private TextView phone;
    private TextView address;
    private TextView dob;
    private TextView nid;
    private TextView gender;
    private TextView carnum;
    private TextView category;
    private TextView type;
    private TextView color;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup3);

        username = (TextView) findViewById(R.id.name);
        email = (TextView) findViewById(R.id.email);
        phone = (TextView) findViewById(R.id.phone);
        dob = (TextView) findViewById(R.id.dob);
        nid = (TextView) findViewById(R.id.nid);
        gender = (TextView) findViewById(R.id.gender);
        address = (TextView) findViewById(R.id.address);


        carnum = (TextView) findViewById(R.id.carnum);
        category = (TextView) findViewById(R.id.category);
        type = (TextView) findViewById(R.id.type);
        color = (TextView) findViewById(R.id.color);
        user = (user) getIntent().getSerializableExtra("user");
        car = (user) getIntent().getSerializableExtra("car");
        Log.i("user", user.username);
        Log.i("car", car.carnumber);
        username.setText(user.username);
        email.setText(user.email);
        phone.setText(user.phone);
        dob.setText(user.dob);
        nid.setText(user.nid);
        gender.setText(user.gender);
        address.setText(user.address);
        carnum.setText(car.carnumber);
        category.setText(car.category);
        type.setText(car.type);
        color.setText(car.colors);


    }

    public void gohome(View V) {
        postUserData();

        Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
        startActivity(intent);
    }

    private void postUserData() {
        // url to post our data
        String url = "http://192.168.154.207:8000/api/user/insert";

        RequestQueue queue = Volley.newRequestQueue(Signup3Activity.this);

        StringRequest request = new StringRequest(Request.Method.POST, url, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {

                    JSONObject respObj = new JSONObject(response);
                    Log.i("response", response);
                    postCarData();

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


                params.put("id", user.nid);
                params.put("username", user.username);
                params.put("address", user.address);
                params.put("email", user.email);
                params.put("gender", user.gender);
                params.put("phone", user.phone);
                params.put("role", "carowner");
                params.put("dob", user.dob);
                params.put("password", user.password);

                Log.i("param", String.valueOf(params));
                // at last we are
                // returning our params.
                return params;
            }
        };
        // below line is to make
        // a json object request.
        queue.add(request);
    }

    private void postCarData() {
        // url to post our data
        String url = "http://192.168.15" +
                "4.207:8000/api/car/insert";

        RequestQueue queue = Volley.newRequestQueue(Signup3Activity.this);

        StringRequest request = new StringRequest(Request.Method.POST, url, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {

                    JSONObject respObj = new JSONObject(response);
                    Log.i("response", response);

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

                Map<String, String> param = new HashMap<>();


                param.put("id", car.carnumber);
                param.put("category", car.category);
                param.put("type", car.type);
                param.put("color", car.colors);
                param.put("user_id", user.nid);


                Log.i("param", String.valueOf(param));
                // at last we are
                // returning our params.
                return param;
            }
        };
        // below line is to make
        // a json object request.
        queue.add(request);
    }
}

