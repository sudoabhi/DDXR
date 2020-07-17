package com.example.androdev.ddxr.UserFragments;

import android.graphics.drawable.Drawable;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by Asus on 8/3/2019.
 */

public class PostHintsModel {

    int image;
    String text;

    PostHintsModel(int image,String text){
        this.image=image;
        this.text=text;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
