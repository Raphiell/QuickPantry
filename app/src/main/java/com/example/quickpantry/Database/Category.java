package com.example.quickpantry.Database;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.RealmResults;
import io.realm.annotations.Index;
import io.realm.annotations.PrimaryKey;

public class Category extends RealmObject {
    @PrimaryKey @Index
    private long id;

    private String name;

    // Reference
    private RealmList<Item> items;

    public void setItems(RealmList<Item> items) {
        this.items = items;
    }

    public RealmList<Item> getItems() {
        return items;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
