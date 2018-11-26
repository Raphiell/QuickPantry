package com.example.quickpantry;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
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

import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.RealmResults;

/**
 * TODO: Remove the dumb popup, just make another activity
 */

public class PantryFragment extends Fragment {

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
        ListView listView = getView().findViewById(R.id.lvPantryItems);

        // Setup the array adpater
        ItemAdapter itemAdapter = new ItemAdapter(getContext(),R.layout.list_item, DatabaseHelper.GetRealm().where(Item.class).findAll());

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
                Intent intent = new Intent();

            }
        });
    }
}