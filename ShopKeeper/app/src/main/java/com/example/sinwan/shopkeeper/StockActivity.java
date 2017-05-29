package com.example.sinwan.shopkeeper;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.Image;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TabHost;
import android.widget.Toast;

import com.koushikdutta.urlimageviewhelper.UrlImageViewHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class StockActivity extends AppCompatActivity {

    private RecyclerView mRVFish;
    private AdapterStock mAdapter;
    private AdapterOStock mAdapter2;
    String name,category,tag,url;
    String type="",Uname="";
    int id,price;
    private String JSON_STRING,image,Check,URL;
    int i,getid;
    SharedPreferences preferences ;
    String PREFS_NAME = "com.example.sinwan.mini1";
    int c;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stock);
        preferences = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        Intent intent = getIntent();

        //  Intent intent = getIntent();
        if (preferences.getString("logged", "").toString().equals("logged")) {

            type =preferences.getString("type", "").toString();
            Uname =preferences.getString("type", "").toString();
            Toast.makeText(StockActivity.this, "Shop=" + type, Toast.LENGTH_SHORT).show();
        }
        else
        {
            type = intent.getStringExtra("TYPE");
            Uname = intent.getStringExtra("NAME");
            if(Uname==null)
            Toast.makeText(StockActivity.this, "You must Login", Toast.LENGTH_SHORT).show();
        }
        TabHost host = (TabHost)findViewById(R.id.tabHost);
        host.setup();

        //Tab 1
        TabHost.TabSpec spec = host.newTabSpec("IN STOCK");
        spec.setContent(R.id.tab1);
        spec.setIndicator("IN STOCK");
        host.addTab(spec);

        //Tab 2
        TabHost.TabSpec spec2 = host.newTabSpec("OUT OF STOCK");
        spec2.setContent(R.id.tab2);
        spec2.setIndicator("OUT OF STOCK");
        host.addTab(spec2);



        getJSON();
        getJSON2();
    }

    private void showPatients(){
        JSONObject jsonObject = null;


        try {
            jsonObject = new JSONObject(JSON_STRING);
            JSONArray result = jsonObject.getJSONArray("result");


            JSONObject jo = result.getJSONObject(0);

            name = jo.getString("name");

            category = jo.getString("stock");

            tag=jo.getString("quantity");

            price = jo.getInt("price");

            id=jo.getInt("id");


            // video.setVisibility(View.VISIBLE);
            url = jo.getString("image");
            // info.setText("Model:"+name+"\n"+"Variant:"+variant+"\nTorque :"+torque+"\n Efficiency : "+eff);
            List<DataStock> data = new ArrayList<>();
            for (int i = 0; i < result.length(); i++) {
                JSONObject json_data = result.getJSONObject(i);

                DataStock fishData = new DataStock();
                fishData.fishName = json_data.getString("name");
                fishData.catName = json_data.getString("stock");
                fishData.tagName = json_data.getString("quantity");
                fishData.price = json_data.getInt("price");
                fishData.imageName = json_data.getString("image");
                fishData.id= json_data.getInt("id");
                fishData.ShopName=type;
                data.add(fishData);
            }
            mRVFish = (RecyclerView) findViewById(R.id.fishPriceList2);
            mAdapter = new AdapterStock(StockActivity.this, data);
            mRVFish.setAdapter(mAdapter);
            mRVFish.setLayoutManager(new LinearLayoutManager(StockActivity.this));



        } catch (JSONException e) {
            e.printStackTrace();
        }


    }


    private void getJSON(){
        class GetJSON extends AsyncTask<Void,Void,String> {

            ProgressDialog loading;
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(StockActivity.this,"Fetching Data","Wait...",false,false);
            }


            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                JSON_STRING = s;
                showPatients();

            }

            @Override
            protected String doInBackground(Void... params) {

                String data="";
                int tmp;

                try {
                    java.net.URL url = new URL("http://shopee.esy.es/BRAND/stock.php");
                    String urlParams = "type="+type;

                    HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                    httpURLConnection.setDoOutput(true);
                    OutputStream os = httpURLConnection.getOutputStream();
                    os.write(urlParams.getBytes());
                    os.flush();
                    os.close();

                    InputStream is = httpURLConnection.getInputStream();
                    while((tmp=is.read())!=-1){
                        data+= (char)tmp;
                    }

                    is.close();
                    httpURLConnection.disconnect();

                    return data;
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                    return "Exception: "+e.getMessage();
                } catch (IOException e) {
                    e.printStackTrace();
                    return "Exception: "+e.getMessage();
                }

            }
        }
        GetJSON gj = new GetJSON();
        gj.execute();
    }

    private void showPatients2(){
        JSONObject jsonObject = null;
String name,category,tag;
        int price,id;

        try {
            jsonObject = new JSONObject(JSON_STRING);
            JSONArray result = jsonObject.getJSONArray("result");


            JSONObject jo = result.getJSONObject(0);

            name = jo.getString("name");

            category = jo.getString("stock");

            tag=jo.getString("quantity");

            price = jo.getInt("price");

            id = jo.getInt("id");



            // video.setVisibility(View.VISIBLE);
            url = jo.getString("image");
            // info.setText("Model:"+name+"\n"+"Variant:"+variant+"\nTorque :"+torque+"\n Efficiency : "+eff);
            List<DataStock> data = new ArrayList<>();
            for (int i = 0; i < result.length(); i++) {
                JSONObject json_data = result.getJSONObject(i);

                DataStock fishData = new DataStock();
                fishData.fishName = json_data.getString("name");
                fishData.catName = json_data.getString("stock");
                fishData.tagName = json_data.getString("quantity");
                fishData.price = json_data.getInt("price");
                fishData.imageName = json_data.getString("image");
                fishData.id = json_data.getInt("id");
                fishData.ShopName=type;

                data.add(fishData);
            }
            mRVFish = (RecyclerView) findViewById(R.id.fishPriceList3);
            mAdapter2 = new AdapterOStock(StockActivity.this, data);
            mRVFish.setAdapter(mAdapter2);
            mRVFish.setLayoutManager(new LinearLayoutManager(StockActivity.this));



        } catch (JSONException e) {
            e.printStackTrace();
        }


    }


    private void getJSON2(){
        class GetJSON2 extends AsyncTask<Void,Void,String> {

            ProgressDialog loading;
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(StockActivity.this,"Fetching Data","Wait...",false,false);
            }


            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                JSON_STRING = s;
                showPatients2();

            }

            @Override
            protected String doInBackground(Void... params) {

                String data="";
                int tmp;

                try {
                    java.net.URL url = new URL("http://shopee.esy.es/BRAND/ostock.php");
                    String urlParams = "type="+type;

                    HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                    httpURLConnection.setDoOutput(true);
                    OutputStream os = httpURLConnection.getOutputStream();
                    os.write(urlParams.getBytes());
                    os.flush();
                    os.close();

                    InputStream is = httpURLConnection.getInputStream();
                    while((tmp=is.read())!=-1){
                        data+= (char)tmp;
                    }

                    is.close();
                    httpURLConnection.disconnect();

                    return data;
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                    return "Exception: "+e.getMessage();
                } catch (IOException e) {
                    e.printStackTrace();
                    return "Exception: "+e.getMessage();
                }

            }
        }
        GetJSON2 gj2 = new GetJSON2();
        gj2.execute();
    }

}
