package com.example.afinal;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
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

public class HomeActivity extends AppCompatActivity {
    TextView name, parkingname;
    String URL = "http://192.168.220.207:8000/api/user/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        name = (TextView) findViewById(R.id.name);
        parkingname = (TextView) findViewById(R.id.parking);
        SharedPreferences sh = this.getSharedPreferences("MySharedPref", Context.MODE_PRIVATE);
        String id = sh.getString("sid", "");

        Log.i("sid", id);
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


                            name.setText(name1);
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
        parking();
    }

    public void checkin(View view) {
        Intent i = new Intent(getApplicationContext(), CheckinActivity.class);
        startActivity(i);

    }

    public void unreg(View view) {
        Intent i = new Intent(getApplicationContext(), UnregActivity.class);
        startActivity(i);

    }

    public void reg(View view) {
        Intent i = new Intent(getApplicationContext(), RegistrationsActivity.class);
        startActivity(i);

    }
    public void profile(View view) {
        Intent i = new Intent(getApplicationContext(), ProfileActivity.class);
        startActivity(i);

    }
    public void parking() {
        SharedPreferences sh = this.getSharedPreferences("MySharedPref", Context.MODE_PRIVATE);
        String id = sh.getString("sid", "");
        String URL1 = "http://192.168.220.207:8000/api/user/security/";

        StringRequest s = new StringRequest(Request.Method.GET, URL1 + id, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.i("result", response);
                try {
                    JSONArray x = new JSONArray(response);
                    Log.i("xxx", x.getString(0));
                    JSONObject user = x.getJSONObject(0);
                    String parking_id = user.getString("parking_id");
                    Log.i("parking", parking_id);
                    parking(parking_id);

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

    public void parking(String x) {
        String URL = "http://192.168.220.207:8000/api/parkingspace/";
        StringRequest s = new StringRequest(Request.Method.GET, URL + x, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.i("result", response);
                try {
                    JSONArray o = new JSONArray(response);
                    Log.i("location", String.valueOf(o));


                    if (o.length() > 0) {
                        for (int i = 0; i < o.length(); i++) {
                            JSONObject parking = o.getJSONObject(i);
                            String name1 = parking.getString("name");
                            Log.i("name", name1);
                            parkingname.setText(name1);


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
}