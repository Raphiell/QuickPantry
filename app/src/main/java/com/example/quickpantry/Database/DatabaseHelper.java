package com.example.quickpantry.Database;

import android.app.Service;
import android.content.Context;

import java.util.Date;
import java.util.Set;

import io.realm.Realm;
import io.realm.RealmResults;

/**
 * TODO:
 * Add AddItem(Item item)
 * Add AddCategory(...)
 */

public class DatabaseHelper {
    private static Realm _realm = null;

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

    /**
     * Creates and adds a new item to the item table
     * @param name          Name of the item
     * @param amount        Amount of the item
     * @param brand         Brand of the item
     * @param purchased     Date item was purchased
     * @param bestBefore    Best before date of item
     * @param image         Image name for item
     * @return              Returns the item created, or null if Realm wasn't initialized via SetRealm(realm)
     */
    public static Item AddItem(String name, String amount, String brand, Date purchased, Date bestBefore, String image)
    {
        // Check if the realm was set yet
        if(_realm != null) {
            // Start transaction
            _realm.beginTransaction();

            // Create object and set properties
            Item item = new Item();
            item.setName(name);
            item.setAmount(amount);
            item.setBrand(brand);
            item.setBestBefore(bestBefore);
            item.setPurchased(purchased);
            item.setImage(image);

            // Grab newest id
            item.setId(NextId(Item.class));

            // Add to database
            _realm.copyToRealm(item);

            // End transaction
            _realm.commitTransaction();

            // Return the created item
            return item;
        }

        return null;
    }

    /**
     * Deletes all records of the specified model class, does nothing if Realm wasn't set through SetRealm(realm)
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
}
