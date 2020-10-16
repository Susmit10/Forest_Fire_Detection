package com.example.forestfiredetection.common;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.forestfiredetection.Dashboard;
import com.example.forestfiredetection.R;

public class MainActivity extends AppCompatActivity {

    Animation topamin, bottomanim;
    ImageView image;
    TextView text;

    SharedPreferences onBoardingScreen_SP;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        requestWindowFeature(Window.FEATURE_NO_TITLE);
//        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setTheme(R.style.SplashScreenTheme);

        setContentView(R.layout.activity_main);

        topamin = AnimationUtils.loadAnimation(this, R.anim.top_animation);
        bottomanim = AnimationUtils.loadAnimation(this, R.anim.bottom_animation);

        topamin.setDuration(1800);
        bottomanim.setDuration(1800);

        image = findViewById(R.id.imageView);
        text = findViewById(R.id.slogan);

        image.setAnimation(topamin);
        text.setAnimation(bottomanim);

        int SPLASH_SCREEN = 3000;
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {


                onBoardingScreen_SP = getSharedPreferences("onBoardingScreen", MODE_PRIVATE);
                boolean isFirstTime = onBoardingScreen_SP.getBoolean("firstTime", true);

                if(isFirstTime)
                {
                    SharedPreferences.Editor editor = onBoardingScreen_SP.edit();
                    editor.putBoolean("firstTime", false);
                    editor.apply();

                    Intent intent = new Intent(MainActivity.this, OnBoarding.class);
                    startActivity(intent);

                }
                else
                {
//                    Intent intent = new Intent(MainActivity.this, Login.class);
//
//                    Pair[] pairs = new Pair[2];
//                    pairs[0] = new Pair<View,String>(image, "logo_image");
//                    pairs[1] = new Pair<View,String>(text, "slogan_text");
//
//                    ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(MainActivity.this, pairs);
//                    startActivity(intent, options.toBundle());
                    Intent intent = new Intent(getApplicationContext(), Dashboard.class);
                    startActivity(intent);

                }
                finish();

            }
        }, SPLASH_SCREEN);


    }

}