package com.example.sinwan.final2;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.daimajia.slider.library.Animations.DescriptionAnimation;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.TextSliderView;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.HashMap;

public class HomeActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener {
    MapView mapView;
    GoogleMap googleMap;
    ImageView im1,im2,im3,im4;
    private SliderLayout mDemoSlider;
    Button bProduct,bShop;
    SharedPreferences preferences,preferences1 ;
    String PREFS_NAME = "com.example.sinwan.mini1",City;
    String PREFS_NAME1 = "com.example.sinwan.mini2";
    MenuItem Loc;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        bProduct=(Button)findViewById(R.id.bProduct);
        bShop=(Button)findViewById(R.id.bShop);
        preferences= getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);

        bProduct.setOnClickListener(this);
        bShop.setOnClickListener(this);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        mDemoSlider = (SliderLayout)findViewById(R.id.slider);

        HashMap<String,Integer> file_maps = new HashMap<String, Integer>();

        file_maps.put("ba",R.drawable.ba);
        file_maps.put("bc",R.drawable.bc);
        file_maps.put("bd",R.drawable.bd);
        file_maps.put("bal",R.drawable.bal);


        for(String name : file_maps.keySet()){
            TextSliderView textSliderView = new TextSliderView(this);
            // initialize a SliderLayout
            textSliderView
                    .description(name)
                    .image(file_maps.get(name))
                    .setScaleType(BaseSliderView.ScaleType.Fit);
            //.setOnSliderClickListener(this);

            //add your extra information
            textSliderView.bundle(new Bundle());
            textSliderView.getBundle()
                    .putString("extra",name);

            mDemoSlider.addSlider(textSliderView);


            Menu menu = navigationView.getMenu();

            // find MenuItem you want to change
            MenuItem nav_login = menu.findItem(R.id.nav_login);

            // set new title to the MenuItem

            Loc=menu.findItem(R.id.nav_location);


            preferences1 = getSharedPreferences(PREFS_NAME1, Context.MODE_PRIVATE);
            City=preferences1.getString("city", "").toString();
            Loc.setTitle("Location: "+City);
            preferences = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
            if (preferences.getString("logged", "").toString().equals("logged")) {
                nav_login.setTitle("Logout");
            }
        }
        mDemoSlider.setPresetTransformer(SliderLayout.Transformer.Accordion);
        mDemoSlider.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
        mDemoSlider.setCustomAnimation(new DescriptionAnimation());
        mDemoSlider.setDuration(4000);


    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_location) {
            Intent i1=new Intent(HomeActivity.this,HomeActivity.class);
            startActivity(i1);
        }
        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {
            Intent i1=new Intent(HomeActivity.this,ShopActivity.class);
            startActivity(i1);
        } else if (id == R.id.nav_slideshow) {
            Intent i=new Intent(HomeActivity.this,JobActivity.class);
            startActivity(i);
        } else if (id == R.id.nav_manage) {
            Intent i=new Intent(HomeActivity.this,Cart_Activity.class);
            startActivity(i);
        } else if (id == R.id.nav_login) {
            if (preferences.getString("logged", "").toString().equals("logged"))
            {
                SharedPreferences.Editor editor = preferences.edit();
                editor.clear();
                editor.commit();
                Intent i = new Intent(HomeActivity.this, LActivity.class);
                startActivity(i);
            }
            else {
                Intent i = new Intent(HomeActivity.this, LActivity.class);
                startActivity(i);
            }
        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onClick(View v) {
        if(v == bProduct){
            Intent i=new Intent(HomeActivity.this,MainActivity.class);
            startActivity(i);
        }
        if(v == bShop){
            Intent i=new Intent(HomeActivity.this,ShopActivity.class);
            startActivity(i);
        }
    }
}
