package com.example.forestfiredetection;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.forestfiredetection.database.SessionManager;
import com.example.forestfiredetection.graphs.FireGraph;
import com.example.forestfiredetection.graphs.GraphicalAnalysis;
import com.example.forestfiredetection.graphs.HumidityGraph;
import com.example.forestfiredetection.graphs.PirGraph;
import com.example.forestfiredetection.graphs.RainGraph;
import com.example.forestfiredetection.graphs.SmokeGraph;
import com.example.forestfiredetection.graphs.TempGraph;
import com.example.forestfiredetection.helperclass.HomeAdapter.GraphHelperClass;
import com.example.forestfiredetection.helperclass.HomeAdapter.MainGraphAdapter;
import com.example.forestfiredetection.helperclass.HomeAdapter.MainMaxValueAdapter;
import com.example.forestfiredetection.helperclass.HomeAdapter.MaxValueHelperClass;
import com.example.forestfiredetection.login.Login;
import com.example.forestfiredetection.users.AllCategories;
import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;

public class Dashboard extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    RecyclerView graph_recycler, max_values_recycler;
    MainGraphAdapter adapter;
    MainMaxValueAdapter maxValueAdapter;
    ImageView mainMenu;

    //Drawer Menu
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    ImageView menuIcon;
    LinearLayout contentView;

    static final float END_SCALE = 0.7f;

    // variables for back press
    long backpressedtime;
    Toast backToast;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dashboard);

        // hooks
        graph_recycler = findViewById(R.id.graphical_recycler);
        max_values_recycler = findViewById(R.id.max_values);

        mainMenu = findViewById(R.id.menu);


        //Menu Hooks
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.navigation_view);
        contentView = findViewById(R.id.content);

        navigationDrawer();

        graph_recycler();
        max_values_recycler();

    }

    private void navigationDrawer() {

        //Navigation Drawer
        navigationView.bringToFront();
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setCheckedItem(R.id.nav_home);

        mainMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (drawerLayout.isDrawerVisible(GravityCompat.START))
                    drawerLayout.closeDrawer(GravityCompat.START);
                else drawerLayout.openDrawer(GravityCompat.START);
            }
        });

        animateNavigationDrawer();
    }

    private void animateNavigationDrawer() {

        //Add any color or remove it to use the default one!
        //To make it transparent use Color.Transparent in side setScrimColor();
        //drawerLayout.setScrimColor(Color.TRANSPARENT);
        drawerLayout.addDrawerListener(new DrawerLayout.SimpleDrawerListener() {
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {

                // Scale the View based on current slide offset
                final float diffScaledOffset = slideOffset * (1 - END_SCALE);
                final float offsetScale = 1 - diffScaledOffset;
                contentView.setScaleX(offsetScale);
                contentView.setScaleY(offsetScale);

                // Translate the View, accounting for the scaled width
                final float xOffset = drawerView.getWidth() * slideOffset;
                final float xOffsetDiff = contentView.getWidth() * diffScaledOffset / 2;
                final float xTranslation = xOffset - xOffsetDiff;
                contentView.setTranslationX(xTranslation);
            }
        });

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {
            case R.id.nav_home:
                // Handle menu click
                return true;
            case R.id.nav_search:
                // Handle settings click
                return true;
            case R.id.nav_all_categories:
                // Handle logout click
                startActivity(new Intent(getApplicationContext(), AllCategories.class));
                return true;
            case R.id.nav_add_missing_place:
                // Handle logout click
                return true;
            case R.id.nav_login:
                // Handle logout click
//                SessionManager sessionManager = new SessionManager(getApplicationContext());
//                if(sessionManager.checkLogin())
//                {
//                    Toast.makeText(this, "You are already logged in", Toast.LENGTH_SHORT).show();
//                    Intent intent = new Intent(getApplicationContext(), UserProfile.class);
////                    intent.putExtra("whatToDo", "fromLogin");
//                    startActivity(intent);
//                }
//                else {
                startActivity(new Intent(getApplicationContext(), Login.class));
//                }
                return true;
            case R.id.nav_profile:
                // Handle logout click
                SessionManager sessionManager1 = new SessionManager(getApplicationContext(), SessionManager.SESSION_USER_SESSION);
                if (sessionManager1.checkLogin()) {
                    startActivity(new Intent(getApplicationContext(), UserProfile.class));
                } else {
                    Toast.makeText(this, "Please login before going to your profile", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(getApplicationContext(), Login.class));
                }
                return true;
            case R.id.nav_logout:
                // Handle logout click
                SessionManager sessionManager = new SessionManager(getApplicationContext(), SessionManager.SESSION_USER_SESSION);
                if (sessionManager.checkLogin()) {
                    Toast.makeText(this, "Logged Out", Toast.LENGTH_SHORT).show();
                    sessionManager.logoutUserFromSession();
                    Intent intent = new Intent(getApplicationContext(), Login.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(this, "Already logged out.\nPlease log in", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(getApplicationContext(), Login.class));
                }
                return true;
            case R.id.nav_fire:
                // Handle logout click
                startActivity(new Intent(getApplicationContext(), FireGraph.class));
                return true;
            case R.id.nav_rain:
                // Handle logout click
                startActivity(new Intent(getApplicationContext(), RainGraph.class));
                return true;
            case R.id.nav_smoke:
                // Handle logout click
                startActivity(new Intent(getApplicationContext(), SmokeGraph.class));
                return true;
            case R.id.nav_temp:
                // Handle logout click
                startActivity(new Intent(getApplicationContext(), TempGraph.class));
                return true;
            case R.id.nav_humidity:
                // Handle logout click
                startActivity(new Intent(getApplicationContext(), HumidityGraph.class));
                return true;
            case R.id.nav_PIR:
                // Handle logout click
                startActivity(new Intent(getApplicationContext(), PirGraph.class));
                return true;
            case R.id.nav_share:
                // Handle logout click
                return true;
            case R.id.nav_rate_us:
                // Handle logout click
                return true;
            default:
                return false;
        }

    }


    private void graph_recycler() {

        // defining the qualities of the recycler view
        graph_recycler.setHasFixedSize(true);
        graph_recycler.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));


        // array list of the assets inside each card view
        ArrayList<GraphHelperClass> graphLocations = new ArrayList<>();

        graphLocations.add(new GraphHelperClass(R.drawable.graph_1, "Flame Sensor", "Realtime data of the flame sensor"));
        graphLocations.add(new GraphHelperClass(R.drawable.graph_2, "Rain Sensor", "Amount of rainfall recently occurred"));
        graphLocations.add(new GraphHelperClass(R.drawable.graph_3, "Smoke Sensor", "Checkout if there was any smoke"));
        graphLocations.add(new GraphHelperClass(R.drawable.graph_4, "Temp. Sensor", "Realtime Temperature of the forest"));
        graphLocations.add(new GraphHelperClass(R.drawable.graph_5, "Humidity Sensor", "Realtime Humidity of the forest"));
        graphLocations.add(new GraphHelperClass(R.drawable.graph_6, "PIR Sensor", "Presence of animals in the forest"));

        // sending the array list to the adapter
        adapter = new MainGraphAdapter(graphLocations);

        // setting the adapter to the recycler view
        graph_recycler.setAdapter(adapter);

    }


    private void max_values_recycler() {

        max_values_recycler.setHasFixedSize(true);
        max_values_recycler.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

        //All Gradients
        GradientDrawable gradient2 = new GradientDrawable(GradientDrawable.Orientation.LEFT_RIGHT, new int[]{0xffd4cbe5, 0xffd4cbe5});
        GradientDrawable gradient1 = new GradientDrawable(GradientDrawable.Orientation.LEFT_RIGHT, new int[]{0xff7adccf, 0xff7adccf});
        GradientDrawable gradient3 = new GradientDrawable(GradientDrawable.Orientation.LEFT_RIGHT, new int[]{0xfff7c59f, 0xFFf7c59f});
        GradientDrawable gradient4 = new GradientDrawable(GradientDrawable.Orientation.LEFT_RIGHT, new int[]{0xffb8d7f5, 0xffb8d7f5});


        ArrayList<MaxValueHelperClass> maxValueLocations = new ArrayList<>();

        maxValueLocations.add(new MaxValueHelperClass(R.drawable.gauge, "Max. Fire Sensor Value", "48", gradient1));
        maxValueLocations.add(new MaxValueHelperClass(R.drawable.gauge, "Highest Rain Value", "43", gradient2));
        maxValueLocations.add(new MaxValueHelperClass(R.drawable.gauge, "Maximum Smoke Value", "487", gradient3));
        maxValueLocations.add(new MaxValueHelperClass(R.drawable.gauge, "Maximum Temperature", "553", gradient4));
        maxValueLocations.add(new MaxValueHelperClass(R.drawable.gauge, "Maximum Humidity", "83", gradient1));
        maxValueLocations.add(new MaxValueHelperClass(R.drawable.gauge, "Maximum PIR Value", "553", gradient2));

        maxValueAdapter = new MainMaxValueAdapter(maxValueLocations);
        max_values_recycler.setAdapter(maxValueAdapter);
    }


    public void onPlusClick(View view) {

        SessionManager sessionManager = new SessionManager(getApplicationContext(), SessionManager.SESSION_USER_SESSION);
        if (sessionManager.checkLogin()) {
            Toast.makeText(this, "You are already logged in", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(getApplicationContext(), UserProfile.class);
//            intent.putExtra("whatToDo", "fromLogin");
            startActivity(intent);
        } else {

            Intent intent = new Intent(getApplicationContext(), Login.class);
            startActivity(intent);
        }

    }


    @Override
    public void onBackPressed() {

        if (drawerLayout.isDrawerVisible(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {

            if (backpressedtime + 2000 > System.currentTimeMillis()) {
                backToast.cancel();
                super.onBackPressed();
                finish();
                return;
            } else {
                backToast = Toast.makeText(this, "Press back again to exit", Toast.LENGTH_SHORT);
                backToast.show();
            }

            backpressedtime = System.currentTimeMillis();
        }
    }


    public void onClickFire(View view) {

        startActivity(new Intent(getApplicationContext(), FireGraph.class));

    }

    public void onClickRain(View view) {

        startActivity(new Intent(getApplicationContext(), RainGraph.class));

    }

    public void onClickSmoke(View view) {

        startActivity(new Intent(getApplicationContext(), SmokeGraph.class));


    }

    public void onClickTemp(View view) {

        startActivity(new Intent(getApplicationContext(), TempGraph.class));

    }

    public void graphViewAll(View view) {
        startActivity(new Intent(getApplicationContext(), GraphicalAnalysis.class));
    }

    public void maxValueViewAll(View view) {
        startActivity(new Intent(getApplicationContext(), AllCategories.class));
    }


}