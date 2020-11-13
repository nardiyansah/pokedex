package com.example.pokedex;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class PokemonActivity extends AppCompatActivity {
    private TextView nameTextView;
    private TextView numberTextView;
    private TextView type1textview;
    private TextView type2textview;
    private String url;
    private RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pokemon);

        url = getIntent().getStringExtra("url");
        requestQueue = Volley.newRequestQueue(getApplicationContext());
        nameTextView = findViewById(R.id.pokemon_name);
        numberTextView = findViewById(R.id.pokemon_number);
        type1textview = findViewById(R.id.pokemon_type1);
        type2textview = findViewById(R.id.pokemon_type2);

        load();
    }

    public void load(){
        type1textview.setText("");
        type2textview.setText("");

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    nameTextView.setText(response.getString("name"));
                    numberTextView.setText(String.format("#%03d", response.getInt("id")));

                   JSONArray typeEntries = response.getJSONArray("types");
                   for(int i=0; i<typeEntries.length(); i++){
                       JSONObject typeEntry = typeEntries.getJSONObject(i);
                       int slot = typeEntry.getInt("slot");
                       String type = typeEntry.getJSONObject("type").getString("name");

                       if (slot == 1){
                           type1textview.setText(type);
                       }else if(slot == 2){
                           type2textview.setText("name");
                       }
                   }
                } catch (JSONException e) {
                    Log.e("cs50", "Pokemon json error", e);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("cs50","Pokemon details error");
            }
        });

        requestQueue.add(request);
    }
}