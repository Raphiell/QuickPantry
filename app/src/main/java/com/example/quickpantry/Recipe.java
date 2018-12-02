package com.example.quickpantry;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;

public class Recipe {
    private String url, name;
    private Drawable image;

    public Recipe(String name, String url, Drawable image)
    {
        this.url = url;
        this.name = name;
        this.image = image;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Drawable getImage() {
        return image;
    }

    public void setImage(Drawable image) {
        this.image = image;
    }
}
