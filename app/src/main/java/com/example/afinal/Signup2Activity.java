package com.example.afinal;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import java.lang.reflect.Array;

public class Signup2Activity extends AppCompatActivity {
    private EditText carnumber;


    String[] colors = new String[]{"Select color", "RED", "BLACK", "SILVER", "BLUE", "GREEN", "MAROON"};
    String[] items = new String[]{"select category", "BMW", "FIAT", "Kia", "JEEP", "OPEL", "TOYOTA", "MINI", "RENAULT"};
    String[][] cars = {{"select type"},
            {"select type", "X6", "X1", "X3", "I8", "Z4", "IX3", "1 SERIES", "2SERIES", "3SERIES"},
            {"select type", "TIPO", "BUNTO", "500", "PANDA", "SPIDER"},
            {"select type", "EV6", "CARENZ", "APORTAGE", "SOUL", "RIO"},
            {"select type", "CHEROKEE", "COMPASS", "RENEGADE"},
            {"select type", "YARIS", "COROLA", "RUSH"},
            {"select type", "ASTRA", "CORSA", "CROSSLAND"},
            {"select type", "COOPER", "CLUBMAN", "COUNTRYMAN"},
            {"select type", "MEGAN", "LOGAN", "CLIO", "CAPTUR"}};
    user x;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup2);
        //car category
        Spinner dropdown = findViewById(R.id.spinner1);


        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, items);
        dropdown.setAdapter(adapter);
        dropdown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedItem = parent.getItemAtPosition(position).toString();
                Log.i("ss", selectedItem);
                    selected(position, selectedItem);

            }

            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    //car type
    public void selected(int positionc, String category) {

        Spinner dropdown2 = findViewById(R.id.spinner2);
        ArrayAdapter<String> adapter2 = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, cars[positionc]);

        dropdown2.setAdapter(adapter2);
        dropdown2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedItem = parent.getItemAtPosition(position).toString();
                Log.i("ss", selectedItem);

                    color(selectedItem, category, positionc ,position);

            } // to close the onItemSelected

            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    public void color(String type, String category,int pc,int pt) {
        //color
        Spinner dropdown3 = findViewById(R.id.spinner3);
        ArrayAdapter<String> adapter3 = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, colors);
        dropdown3.setAdapter(adapter3);
        dropdown3.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedItem = parent.getItemAtPosition(position).toString();
                Log.i("ss", selectedItem);
                if (position !=0 && pc !=0 && pt !=0) {
                    x = finish(type, category, selectedItem);
                }
            } // to close the onItemSelected

            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }
    public user finish(String type, String category, String color) {
        Log.i("type", type);
        Log.i("category", category);
        Log.i("color", color);
        carnumber = (EditText) findViewById(R.id.carnum);
        Log.i("carnumber", carnumber.getText().toString());
        user xc = new user(carnumber.getText().toString(), category, type, color);
        return xc;
    }
    public void next(View V) {
        user z = (user) getIntent().getSerializableExtra("user");
        Intent intent = new Intent(getApplicationContext(), Signup3Activity.class);
        intent.putExtra("car", x);
        intent.putExtra("user", z);
        startActivity(intent);

    }
}