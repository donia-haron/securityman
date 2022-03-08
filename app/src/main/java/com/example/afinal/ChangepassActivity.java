package com.example.afinal;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.airbnb.lottie.L;
import com.airbnb.lottie.LottieAnimationView;
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
import java.util.regex.Pattern;

public class ChangepassActivity extends AppCompatActivity {
    LinearLayout pattern,total;
    LottieAnimationView animation;
    EditText currentpass, newpass, confirmpass;

    String oldpass;
    TextView atoz, AtoZ, num, charcount;
    private static final Pattern PASSWORD_PATTERN =
            Pattern.compile("^" +
                    "(?=.*[@#$%^&+=])" +     // at least 1 special character
                    "(?=\\S+$)" +            // no white spaces
                    ".{4,}" +                // at least 4 characters
                    "$");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_changepass);
        animation=(LottieAnimationView) findViewById(R.id.animationView);
        pattern = (LinearLayout) findViewById(R.id.pattern);
        total=(LinearLayout) findViewById(R.id.total);
        atoz = (TextView) findViewById(R.id.atoz);
        AtoZ = (TextView) findViewById(R.id.AtoZ);
        num = (TextView) findViewById(R.id.num);
        charcount = (TextView) findViewById(R.id.charcount);

        currentpass = (EditText) findViewById(R.id.currentpass);
        newpass = (EditText) findViewById(R.id.newpass);
        confirmpass = (EditText) findViewById(R.id.confirmpass);
        newpass.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                // get the password when we start typing
                String passwordx = newpass.getText().toString();
                validatepass(passwordx);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });


    }

    public void change(View V) {
        String URL = "http://192.168.154.207:8000/api/user/";
        SharedPreferences sh = this.getSharedPreferences("MySharedPref", Context.MODE_PRIVATE);
        String id = sh.getString("id", "");
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
                            oldpass = user.getString("password");
                        }
                        if (oldpass.equals(currentpass.getText().toString())) {
                            Log.i("iniffffffoldddd", oldpass);


                            if (!newpass.getText().toString().equals(confirmpass.getText().toString())) {

                                confirmpass.setError("password isnot equal confirm password");

                            } else {
                                if (validatepass(newpass.getText().toString())) {

                                    updatee();
                                } else {


                                    Log.i("faseeee", "falseeeeeeee");
                                }


                            }

                        } else {
                            currentpass.setError("Incorrect");

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

    public void updatee() {

        String url = "http://192.168.154.207:8000/api/user/updatepass/";
        SharedPreferences sh = this.getSharedPreferences("MySharedPref", Context.MODE_PRIVATE);
        String id = sh.getString("id", "");
        Log.i("iddd", id);
        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest request = new StringRequest(Request.Method.POST, url + id, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONObject respObj = new JSONObject(response);
                    Log.i("response", response);
                    total.setVisibility(View.GONE);
                    animation.setVisibility(View.VISIBLE);
                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {

                            Intent i = new Intent(getApplicationContext(), HomeActivity.class);
                            startActivity(i);

                        }
                    }, 5000);


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
                params.put("password", newpass.getText().toString());
                Log.i("param", String.valueOf(params));

                return params;
            }
        };

        queue.add(request);


    }


    public Boolean validatepass(String password) {
        Log.i("hererree", password);
        // check for pattern
        Boolean x = true;
        Pattern uppercase = Pattern.compile("[A-Z]");
        Pattern lowercase = Pattern.compile("[a-z]");
        Pattern digit = Pattern.compile("[0-9]");

        // if lowercase character is not present
        if (!lowercase.matcher(password).find()) {
            atoz.setTextColor(Color.RED);
            x = false;
        } else {
            // if lowercase character is  present
            atoz.setTextColor(Color.rgb(34, 139, 34));

        }

        // if uppercase character is not present
        if (!uppercase.matcher(password).find()) {
            AtoZ.setTextColor(Color.RED);
            x = false;
        } else {
            // if uppercase character is  present
            AtoZ.setTextColor(Color.rgb(34, 139, 34));
        }
        // if digit is not present
        if (!digit.matcher(password).find()) {
            num.setTextColor(Color.RED);
            x = false;
        } else {
            // if digit is present
            num.setTextColor(Color.rgb(34, 139, 34));
        }
        // if password length is less than 8
        if (password.length() < 8) {
            charcount.setTextColor(Color.RED);
            x = false;
        } else {
            charcount.setTextColor(Color.rgb(34, 139, 34));
        }
        return x;
    }
}