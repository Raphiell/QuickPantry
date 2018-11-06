package com.example.quickpantry;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.quickpantry.Database.Item;

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
        TextView tvOne = getView().findViewById(R.id.tvOne);
        TextView tvTwo = getView().findViewById(R.id.tvTwo);
        TextView tvThree = getView().findViewById(R.id.tvThree);
        TextView tvFour = getView().findViewById(R.id.tvFour);

        // Grab the realm instance
        Realm realm = Realm.getDefaultInstance();

        // Grab items
        RealmResults<Item> items = realm.where(Item.class).findAll();

        // Grab last added item
        Item item = realm.where(Item.class).findAll().last();

        // Grab first available item
        //Item item = realm.where(Item.class).findFirst();

        // Set the textviews to something
        tvOne.setText("Name: " + item.getName());
        tvTwo.setText("Amount: " + item.getAmount());
        tvThree.setText("Purchased: " + item.getPurchased().toString());
        tvFour.setText("Best Before: " + item.getBestBefore().toString());
//        tvFour.setText("Total Items: " + String.valueOf(items.size()));
    }
}
