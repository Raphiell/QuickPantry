package com.example.quickpantry;

import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.example.quickpantry.Database.DatabaseHelper;
import com.example.quickpantry.Database.Item;
import com.example.quickpantry.Database.Category;
import com.example.quickpantry.ListView.ItemAdapter;

import java.util.ArrayList;

import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.RealmResults;

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

    }
}

/*
TextView tvTwo = getView().findViewById(R.id.tvTwo);
        TextView tvThree = getView().findViewById(R.id.tvThree);
        TextView tvFour = getView().findViewById(R.id.tvFour);

        // Grab the realm instance
        Realm realm = Realm.getDefaultInstance();

        // Grab items
        //RealmResults<Item> items = realm.where(Item.class).findAll();

        // Grab all items in produce category
        // Category category = realm.where(Category.class).findFirst();

        // Grab an last item from this category - Peanut butter bars
        // Item item = category.getItems().last();

        // Grab last added item - Chocolate bars
        Item item = realm.where(Item.class).findAll().last();

        // Grab first available item - Fudge bars
        //Item item = realm.where(Item.class).findFirst();

        // Set the textviews to something
        tvOne.setText("Name: " + item.getName());
        tvTwo.setText("Amount: " + item.getAmount());
        tvThree.setText("Purchased: " + item.getPurchased().toString());
        tvFour.setText("Best Before: " + item.getBestBefore().toString());
 */