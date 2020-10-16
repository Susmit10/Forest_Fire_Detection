package com.example.forestfiredetection.common;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.forestfiredetection.Dashboard;
import com.example.forestfiredetection.R;
import com.example.forestfiredetection.helperclass.SliderAdapter;

public class OnBoarding extends AppCompatActivity {

    ViewPager viewPager;
    LinearLayout dotsLayout;

    SliderAdapter sliderAdapter;

    TextView[] slider_dots;
    int length;

    Button skipBtn;
    Button getStartedBtn;
    Animation animation;

    int currentPosition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_on_boarding);

        // hooks
        viewPager = findViewById(R.id.slider);
        dotsLayout = findViewById(R.id.dots);

        skipBtn = findViewById(R.id.skip_btn);
        getStartedBtn = findViewById(R.id.get_started_btn);


        sliderAdapter = new SliderAdapter(this);
        viewPager.setAdapter(sliderAdapter);

        length = sliderAdapter.headings.length;

        addDots(0);
        viewPager.addOnPageChangeListener(pageChangeListener);

    }

    private void addDots(int position)
    {
        slider_dots = new TextView[length];
        dotsLayout.removeAllViews();

        for(int i = 0; i<length; i++)
        {
            slider_dots[i] = new TextView(this);
            slider_dots[i].setText(Html.fromHtml("&#8226;"));
            slider_dots[i].setTextColor(getResources().getColor(R.color.light_grey));
            slider_dots[i].setTextSize(35);
            dotsLayout.addView(slider_dots[i]);

        }

        if(slider_dots.length > 0)
        {
            slider_dots[position].setTextColor(getResources().getColor(R.color.yellow));
        }

    }

    ViewPager.OnPageChangeListener pageChangeListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {

            addDots(position);
            currentPosition = position;

            if(position == 0)
            {
                getStartedBtn.setVisibility(View.INVISIBLE);
                skipBtn.setVisibility(View.VISIBLE);

            }
            else if(position == 1)
            {
                getStartedBtn.setVisibility(View.INVISIBLE);
                skipBtn.setVisibility(View.VISIBLE);

            }
            else if(position == 2)
            {
                getStartedBtn.setVisibility(View.INVISIBLE);
                skipBtn.setVisibility(View.VISIBLE);

            }
            else
            {
                animation = AnimationUtils.loadAnimation(OnBoarding.this, R.anim.bottom_animation);
                animation.setDuration(600);
                getStartedBtn.setAnimation(animation);
                getStartedBtn.setVisibility(View.VISIBLE);
                skipBtn.setVisibility(View.INVISIBLE);
            }

        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };

    public void onSkip(View view) {

        Intent intent = new Intent(getApplicationContext(), Dashboard.class);
        startActivity(intent);
        finish();

    }

    public void onNext(View view) {

        viewPager.setCurrentItem(currentPosition + 1);

    }


    public void onContinue(View view) {
        Intent intent = new Intent(getApplicationContext(), Dashboard.class);
        startActivity(intent);
        finish();
    }
}