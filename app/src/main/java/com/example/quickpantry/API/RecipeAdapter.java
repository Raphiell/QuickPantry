package com.example.quickpantry.API;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.quickpantry.Database.Item;
import com.example.quickpantry.ItemActivity;
import com.example.quickpantry.R;
import com.example.quickpantry.Recipe;

import java.util.List;

public class RecipeAdapter extends ArrayAdapter<Recipe> {
    private List<Recipe> _recipes;
    private Context _context;

    // Default constructor
    public RecipeAdapter(Context context, int resource, List<Recipe> recipes)
    {
        super(context, resource, recipes);
        _recipes = recipes;
        _context = context;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        // Get the specific item for this view
        final Recipe recipe = _recipes.get(position);

        // Still not sure what this is
        View view = convertView;

        // If convertView was null, inflate the view we want
        if(view == null)
        {
            // Get the view inflater
            LayoutInflater layoutInflater = (LayoutInflater)_context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            // Inflate the view
            view = layoutInflater.inflate(R.layout.recipe_list_item, null);
        }

        // If the item wasn't null, we can populate fields
        if(recipe != null)
        {
            ((TextView)view.findViewById(R.id.tvName)).setText(recipe.getName());
        }

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Open the item window in edit mode, passing this item's id
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(recipe.getUrl()));
                getContext().startActivity(intent);
            }
        });

        // Return the view we created
        return view;
    }
}
