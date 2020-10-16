package com.example.forestfiredetection.helperclass;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.viewpager.widget.PagerAdapter;

import com.example.forestfiredetection.R;

public class SliderAdapter extends PagerAdapter {

    Context context;
    LayoutInflater layoutInflater;


    // here the context is the context of the activity in which we want to show these data
    // we just need to pass the context of that activity by calling the constructor with its context
    public SliderAdapter(Context context) {
        this.context = context;
    }

    int[] images = {

            R.drawable.nature_background1,
            R.drawable.forest_2,
            R.drawable.forest_3,
            R.drawable.forest_4

    };

     public int[] headings = {

            R.string.heading1,
            R.string.heading1,
            R.string.heading1,
            R.string.heading1

    };

    int[] description = {

            R.string.para1,
            R.string.para2,
            R.string.para3,
            R.string.para4

    };


    @Override
    public int getCount() {
        return headings.length;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == (ConstraintLayout)object;
    }


    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {

        layoutInflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.slides_layout, container, false);

        ImageView imageView = view.findViewById(R.id.slider);
        TextView heading = view.findViewById(R.id.heading);
        TextView desc = view.findViewById(R.id.description);

        imageView.setImageResource(images[position]);
        heading.setText(headings[position]);
        desc.setText(description[position]);

        container.addView(view);

        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {

        container.removeView((ConstraintLayout)object);

    }
}
