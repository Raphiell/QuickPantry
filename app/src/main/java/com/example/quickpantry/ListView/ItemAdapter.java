package com.example.quickpantry.ListView;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.quickpantry.Database.Item;
import com.example.quickpantry.R;

import java.util.ArrayList;

public class ItemAdapter extends ArrayAdapter<Item> {
    private ArrayList<Item> _items;
    private Context _context;

    public ItemAdapter(Context context, int resource, ArrayList<Item> items) {
        super(context, resource, items);
        _items = items;
        _context = context;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        // Get the specific item for this view
        final Item item = _items.get(position);

        // Still not sure what this is
        View view = convertView;

        // If convertView was null, inflate the view we want
        if(view == null)
        {
            // Get the view inflater
            LayoutInflater layoutInflater = (LayoutInflater)_context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            // Inflate the view
            view = layoutInflater.inflate(R.layout.list_item, null);
        }

        // If the item wasn't null, we can populate fields
        if(item != null)
        {
            TextView tvName = view.findViewById(R.id.tvName);
            tvName.setText(item.getBrand() + " " + item.getName());
        }

        // Return the view we created
        return view;
    }
}
