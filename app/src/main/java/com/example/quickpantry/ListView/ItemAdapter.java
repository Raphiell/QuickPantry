package com.example.quickpantry.ListView;

import android.content.Context;
import android.content.Intent;
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

import java.text.SimpleDateFormat;
import java.util.List;

public class ItemAdapter extends ArrayAdapter<Item> {
    private List<Item> _items;
    private Context _context;
    private SimpleDateFormat _formatter = new SimpleDateFormat("E, MMM dd yyyy");

    public ItemAdapter(Context context, int resource, List<Item> items) {
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
            ((TextView)view.findViewById(R.id.tvName)).setText(item.getBrand() + " " + item.getName());
            ((TextView)view.findViewById(R.id.tvAmount)).setText("I have " + item.getAmount());
            ((TextView)view.findViewById(R.id.tvBestBefore)).setText("Expires " + _formatter.format(item.getBestBefore()));
        }

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Open the item window in edit mode, passing this item's id
                Intent intent = new Intent(getContext(), ItemActivity.class);
                intent.putExtra("id", item.getId());
                intent.putExtra("mode", "edit");
                getContext().startActivity(intent);
            }
        });

        // Return the view we created
        return view;
    }
}
