package com.example.quickpantry;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.quickpantry.API.RecipeAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class RecipeFragment extends Fragment {

    // Required empty constructor
    public RecipeFragment() {}

    // These have since changed!
    private static String APP_ID = "235ba005";
    private static String APP_KEY = "52bc5ee83ef4b5d31a00f611232b069d";

    private String query = "chicken";

    private String tag = "QueueTag";
    private String url;

    private ArrayList<Recipe> recipes;

    private RequestQueue requestQueue;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.recipe_tab, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Get the volley request Queue
        requestQueue = Volley.newRequestQueue(getContext());

        // Get Edit text
        final EditText etSearch = getView().findViewById(R.id.etSearch);

        // Setup search button
        Button btnSearch = getView().findViewById(R.id.btnSearch);

        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Clear out the old search results
                recipes = new ArrayList<>();

                // Setup url
                if(!etSearch.getText().toString().isEmpty()) {
                    query = etSearch.getText().toString();
                }

                url = "https://api.edamam.com/search?q=" + query + "&app_id=" + APP_ID + "&app_key=" + APP_KEY + "&to=30";

                // Grab the data from the recipes website, populating the recipes list
                getData();
            }
        });
    }

    private void getData()
    {
        // Grab list view
        final ListView listView = getView().findViewById(R.id.lvRecipes);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(url, new JSONObject(), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d("Ralf", response.toString());
                try {
                    JSONArray hits = response.getJSONArray("hits");

                    Log.d("Ralf", hits.toString());

                    // Loop through each hit
                    for(int i = 0; i < hits.length(); i++)
                    {
                        JSONObject hit = hits.getJSONObject(i).getJSONObject("recipe");

                        // Image
                        Drawable drawable = null;

                        /* Get the image
                        try {
                            InputStream inputStream = (InputStream) new URL(hit.getString("image")).getContent();
                            drawable = Drawable.createFromStream(inputStream, "");
                        } catch (IOException e) {
                            e.printStackTrace();
                        } */

                        recipes.add(new Recipe(hit.getString("label"), hit.getString("url"), drawable));
                    }

                    // Populate the list view
                    RecipeAdapter recipeAdapter = new RecipeAdapter(getContext(), R.layout.recipe_list_item, recipes);
                    listView.setAdapter(recipeAdapter);
                    recipeAdapter.notifyDataSetChanged();

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        // Sets tag for the request
        jsonObjectRequest.setTag(tag);
        // Adds jsonArrayRequest to the request queue
        requestQueue.add(jsonObjectRequest);
    }

    @Override
    public void onStop() {
        super.onStop();

        // if queue is not empty
        // cancels all the requests with given tag
        if (requestQueue != null){
            requestQueue.cancelAll(tag);
        }
    }
}
