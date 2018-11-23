package com.example.quickpantry.Database;

import android.app.Service;
import android.content.Context;

import java.util.Date;
import java.util.Set;

import io.realm.Realm;
import io.realm.RealmResults;

public class DatabaseHelper {
    private static Realm _realm = null;

    // Private so you can't initialize a copy of this class
    private DatabaseHelper(){}

    /**
     * Initialize the realm within this class
     * @param context   The context to create this realm within
     * @return          Returns the initialized realm to be used elsewhere
     */
    public static Realm InitializeRealm(Context context)
    {
        // Initialize Realm
        Realm.init(context);

        // Get current instance
        Realm realm = Realm.getDefaultInstance();

        // Set the realm
        SetRealm(realm);

        return realm;
    }

    /**
     * Set the realm to an existing Realm
     * @param realm     The realm to use
     */
    public static void SetRealm(Realm realm)
    {
        _realm = realm;
    }

    public static Realm GetRealm() { return _realm; }

    /**
     * Deletes all records of the specified model class, does nothing if Realm wasn't set through Initialize Realm or SetRealm
     * @param _class    Model to delete
     */
    public static void DeleteAll(Class _class)
    {
        // Check if the realm was set yet
        if(_realm != null) {
            // Start transaction
            _realm.beginTransaction();

            // Grab all records and delete them
            _realm.where(_class).findAll().deleteAllFromRealm();

            // Commit
            _realm.commitTransaction();
        }
    }

    /**
     * Grabs the next available id for the specified model class
     * @param _class    Model to find the id of
     * @return          Returns the next available id, returns -1 if the realm is not initialized via SetRealm(realm)
     */
    public static long NextId(Class _class)
    {
        if(_realm != null) {
            // Grab the highest number
            Number id = _realm.where(_class).max("id");

            // Return the number if there was an existing record, 0 (initial record) if no number was returned
            return (id != null) ? id.longValue() + 1 : 0;
        }
        else
        {
            return -1;
        }
    }

    public static Item AddItem(String name, String amount, String brand, Date purchased, Date bestBefore, String image, Category category)
    {
        // Begin the transaction
        _realm.beginTransaction();

        // Create a new managed object
        Item item = _realm.createObject(Item.class, NextId(Item.class));

        // This is unmanaged
        // Item item = new Item()

        // Set it's properties
        item.setName(name);
        item.setAmount(amount);
        item.setBrand(brand);
        item.setPurchased(purchased);
        item.setBestBefore(bestBefore);
        item.setImage(image);

        // Category needs to be a managed object
        category.getItems().add(item);
        // Add inverse relationship
        item.setCategory(category);

        // Commit
        _realm.commitTransaction();

        return item;
    }

    public static Category AddCategory(String name)
    {
        // Begin transaction
        _realm.beginTransaction();

        // Create new category
        Category category = _realm.createObject(Category.class, NextId(Category.class));

        // Set properties
        category.setName(name);

        // Commit
        _realm.commitTransaction();

        return category;
    }
}
