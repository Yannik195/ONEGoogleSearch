package de.yanniksimon.onegooglesearch;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {

    private TextView textView;

    String wikipediaAPIUrl;
    String wikipediaAPIUrl2;

    RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textView = findViewById(R.id.text_view);

        wikipediaAPIUrl = "https://en.wikipedia.org/w/api.php?format=json&action=query&prop=extracts&exintro&explaintext&redirects=1&titles=kuchen";

        //Hinten einfach anfügen
        wikipediaAPIUrl2 = "https://en.wikipedia.org/api/rest_v1/page/summary/";


        requestQueue = Volley.newRequestQueue(this);

    }

    private void wikipediaSearch(final String searchTerm, String url) {
        final String searchURL = url + searchTerm;
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, searchURL, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    String extract = response.getString("extract");
                    textView.setText(extract);
                    Log.i("wikipediaSearch", "Search Term: " + searchTerm + ", URL: " + searchURL + ", Text: " + extract);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        requestQueue.add(jsonObjectRequest);

    }

    private void duckDuckGoSearch(final String searchTerm){
        String url = "https://api.duckduckgo.com/?q=" + searchTerm + "&format=json";


        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    String abstractText = response.getString("AbstractText");

                    textView.setText(abstractText);
                    Log.i("DuckDuckGoSearch", "Search Term: " + searchTerm + " , Abstract Text: " + abstractText);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        requestQueue.add(jsonObjectRequest);
    }

    private void getJsonArray() {
        final String url = "https://jsonplaceholder.typicode.com/users";
        RequestQueue requestQueue = Volley.newRequestQueue(this);

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        for (int i = 0; i < response.length(); i++){

                            try {
                                JSONObject user = response.getJSONObject(i);
                                Log.i("JsonResponse", user.getString("email"));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        requestQueue.add(jsonArrayRequest);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.main_menu, menu);

        MenuItem searchItem = menu.findItem(R.id.search);
        android.support.v7.widget.SearchView searchView =
                (android.support.v7.widget.SearchView) searchItem.getActionView();

        searchView.setOnQueryTextListener(new android.support.v7.widget.SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                Toast.makeText(getApplicationContext(), s, Toast.LENGTH_SHORT).show();
                //duckDuckGoSearch(s);
                wikipediaSearch(s,wikipediaAPIUrl2);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                return false;
            }
        });
        return true;
    }
}
