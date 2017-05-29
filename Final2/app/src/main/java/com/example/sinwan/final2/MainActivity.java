package com.example.sinwan.final2;

import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.internal.NavigationMenuItemView;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    public static final int CONNECTION_TIMEOUT = 10000;
    public static final int READ_TIMEOUT = 15000;
    private RecyclerView mRVFish;
    private AdapterFish mAdapter;

    String type,Uname,Uid,add,City="";
    SharedPreferences preferences,preferences1 ;
    String PREFS_NAME = "com.example.sinwan.mini1";
    String PREFS_NAME1 = "com.example.sinwan.mini2";
    MenuItem Loc;

    CollapsingToolbarLayout collapsingToolbarLayout;
    SearchView searchView = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);

        // get menu from navigationView
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


           // type = intent.getStringExtra("TYPE");
          //  Uname = intent.getStringExtra("NAME");
            type=preferences.getString("type", "").toString();
            Uname=preferences.getString("username", "").toString();
            Uid=preferences.getString("Uid", "").toString();
            add=preferences.getString("address", "").toString();
            Toast.makeText(MainActivity.this,type+Uname+Uid+add,Toast.LENGTH_SHORT).show();

        }
        collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);


        collapsingToolbarLayout.setTitle("WO STORES");


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

     //   NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


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
        if (id == R.id.action_cart) {
            Intent i = new Intent(this,Cart_Activity.class);
            this.startActivity(i);
            return true;
        }

        return super.onOptionsItemSelected(item);
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
       // getMenuInflater().inflate(R.menu.main, menu);
      //  return true;
        // adds item to action bar
        getMenuInflater().inflate(R.menu.search_main, menu);

        // Get Search item from action bar and Get Search service
        MenuItem searchItem = menu.findItem(R.id.action_search);

        SearchManager searchManager = (SearchManager) MainActivity.this.getSystemService(Context.SEARCH_SERVICE);
        if (searchItem != null) {
            searchView = (SearchView) searchItem.getActionView();
            searchView.setQueryHint("Mobile,Laptop,Lenovo,etc");
        }
        if (searchView != null) {
            searchView.setSearchableInfo(searchManager.getSearchableInfo(MainActivity.this.getComponentName()));
            searchView.setIconified(false);
            searchView.setQueryHint("Mobile,Laptop,Lenovo,etc");
        }

        return true;
    }



    @Override
    protected void onNewIntent(Intent intent) {
        // Get search query and create object of class AsyncFetch
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            if (searchView != null) {
                searchView.clearFocus();
            }
            new AsyncFetch(query).execute();

        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        if (id == R.id.nav_location) {
            Intent i1=new Intent(MainActivity.this,HomeActivity.class);
            startActivity(i1);
        }
        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {
            Intent i1=new Intent(MainActivity.this,ShopActivity.class);
            startActivity(i1);
        } else if (id == R.id.nav_slideshow) {
            Intent i=new Intent(MainActivity.this,JobActivity.class);
            startActivity(i);
        } else if (id == R.id.nav_manage) {
            Intent i=new Intent(MainActivity.this,Cart_Activity.class);
            startActivity(i);
        } else if (id == R.id.nav_login) {
            if (preferences.getString("logged", "").toString().equals("logged"))
            {
                SharedPreferences.Editor editor = preferences.edit();
                editor.clear();
                editor.commit();
                Intent i = new Intent(MainActivity.this, LActivity.class);
                startActivity(i);
            }
            else {
                Intent i = new Intent(MainActivity.this, LActivity.class);
                startActivity(i);
            }
        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    private class AsyncFetch extends AsyncTask<String, String, String> {

        ProgressDialog pdLoading = new ProgressDialog(MainActivity.this);
        HttpURLConnection conn;
        URL url = null;
        String searchQuery;

        public AsyncFetch(String searchQuery){
            this.searchQuery=searchQuery;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            //this method will be running on UI thread
            pdLoading.setMessage("\tLoading...");
            pdLoading.setCancelable(false);
            pdLoading.show();

        }

        @Override
        protected String doInBackground(String... params) {
            try {

                // Enter URL address where your php file resides
                url = new URL("http://shopee.esy.es/FISH/fish-search.php");

            } catch (MalformedURLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                return e.toString();
            }
            try {

                // Setup HttpURLConnection class to send and receive data from php and mysql
                conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(READ_TIMEOUT);
                conn.setConnectTimeout(CONNECTION_TIMEOUT);
                conn.setRequestMethod("POST");

                // setDoInput and setDoOutput to true as we send and recieve data
                conn.setDoInput(true);
                conn.setDoOutput(true);

                // add parameter to our above url
                Uri.Builder builder = new Uri.Builder().appendQueryParameter("searchQuery", searchQuery);
                String query = builder.build().getEncodedQuery();

                OutputStream os = conn.getOutputStream();
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
                writer.write(query);
                writer.flush();
                writer.close();
                os.close();
                conn.connect();

            } catch (IOException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
                return e1.toString();
            }

            try {

                int response_code = conn.getResponseCode();

                // Check if successful connection made
                if (response_code == HttpURLConnection.HTTP_OK) {

                    // Read data sent from server
                    InputStream input = conn.getInputStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(input));
                    StringBuilder result = new StringBuilder();
                    String line;

                    while ((line = reader.readLine()) != null) {
                        result.append(line);
                    }

                    // Pass data to onPostExecute method
                    return (result.toString());

                } else {
                    return("Connection error");
                }

            } catch (IOException e) {
                e.printStackTrace();
                return e.toString();
            } finally {
                conn.disconnect();
            }


        }

        @Override
        protected void onPostExecute(String result) {

            //this method will be running on UI thread
            pdLoading.dismiss();
            List<DataFish> data=new ArrayList<>();

            pdLoading.dismiss();
            if(result.equals("no rows")) {
                Toast.makeText(MainActivity.this, "No Results found for entered query", Toast.LENGTH_LONG).show();
            }else{

                try {

                    JSONArray jArray = new JSONArray(result);

                    // Extract data from json and store into ArrayList as class objects
                    for (int i = 0; i < jArray.length(); i++) {
                        JSONObject json_data = jArray.getJSONObject(i);
                        DataFish fishData = new DataFish();
                        fishData.fishName = json_data.getString("name");
                        fishData.catName = json_data.getString("cat_name");
                        fishData.tagName = json_data.getString("tag_name");
                        fishData.price = json_data.getInt("price");
                        fishData.imageName = json_data.getString("image");
                        fishData.id = json_data.getInt("id");
                        data.add(fishData);
                    }


                    // Setup and Handover data to recyclerview
                    mRVFish = (RecyclerView) findViewById(R.id.fishPriceList);
                    mAdapter = new AdapterFish(MainActivity.this, data);
                    mRVFish.setAdapter(mAdapter);
                    mRVFish.setLayoutManager(new LinearLayoutManager(MainActivity.this));

                } catch (JSONException e) {
                    // You to understand what actually error is and handle it appropriately
                    Toast.makeText(MainActivity.this, e.toString(), Toast.LENGTH_LONG).show();
                    Toast.makeText(MainActivity.this, result.toString(), Toast.LENGTH_LONG).show();
                }

            }

        }

    }
}
