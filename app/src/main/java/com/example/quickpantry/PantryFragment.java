package com.example.quickpantry;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.example.quickpantry.Database.DatabaseHelper;
import com.example.quickpantry.Database.Item;
import com.example.quickpantry.Database.Category;
import com.example.quickpantry.ListView.ItemAdapter;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.RealmResults;
import io.realm.Sort;

public class PantryFragment extends Fragment {
    private ItemAdapter itemAdapter;
    private ListView listView;

    // Required empty constructor
    public PantryFragment() {}

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.pantry_tab, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Grab the text view and change it
        listView = getView().findViewById(R.id.lvPantryItems);

        // Setup the array adpater
        itemAdapter = createAdapter();

        // Add it to the list view
        listView.setAdapter(itemAdapter);

        // Notify of changes
        itemAdapter.notifyDataSetChanged();

        // Get floating action button
        FloatingActionButton fab = getView().findViewById(R.id.fabAddItem);

        // Set on click listener
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), ItemActivity.class);
                intent.putExtra("mode", "new");

                startActivity(intent);
            }
        });
    }

    /**
     * Creates a new item adapter with ordering as specified in the settings
     * @return  Returns the adapter created
     */
    private ItemAdapter createAdapter()
    {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());

        // List to store items in
        List<Item> list = DatabaseHelper.GetRealm().where(Item.class).findAll();

        // Determine which way to order the list
        switch(Integer.valueOf(sharedPreferences.getString("orderBy", "0"))) {
            case 0:
                list = ((RealmResults<Item>) list).sort("purchased", Sort.DESCENDING);
                break;
            case 1:
                list = ((RealmResults<Item>) list).sort("bestBefore", Sort.ASCENDING);
                break;
            case 2:
                list = ((RealmResults<Item>) list).sort("name", Sort.ASCENDING);
                break;
            case 3:
                list = ((RealmResults<Item>) list).sort("brand", Sort.ASCENDING);
                break;
        }

        return new ItemAdapter(getContext(), R.layout.list_item, list);
    }

    @Override
    public void onResume() {
        super.onResume();
        // Recreate the adapter in case we changed the ordering
        itemAdapter = createAdapter();
        listView.setAdapter(itemAdapter);
        itemAdapter.notifyDataSetChanged();
    }
}