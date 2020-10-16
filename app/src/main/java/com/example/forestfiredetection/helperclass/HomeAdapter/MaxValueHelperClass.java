package com.example.forestfiredetection.helperclass.HomeAdapter;

import android.graphics.drawable.GradientDrawable;

public class MaxValueHelperClass {

    int image;
    String heading, value;
    GradientDrawable gradientDrawable;

    public MaxValueHelperClass(int image, String heading, String value, GradientDrawable gradientDrawable) {
        this.image = image;
        this.heading = heading;
        this.value = value;
        this.gradientDrawable = gradientDrawable;
    }

    public GradientDrawable getGradientDrawable() {
        return gradientDrawable;
    }

    public void setGradientDrawable(GradientDrawable gradientDrawable) {
        this.gradientDrawable = gradientDrawable;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public String getHeading() {
        return heading;
    }

    public void setHeading(String heading) {
        this.heading = heading;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
