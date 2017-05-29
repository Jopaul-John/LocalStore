package com.example.sinwan.final2;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
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
import java.util.List;

public class ShopView_Activity extends AppCompatActivity  {
    private RecyclerView mRVFish;
    private AdapterShopView mAdapter1;
    String name,category,tag,url,sname;
    public String JSON_STRING;
    int id=1,price;
    String type,Uname,Uid;
    SharedPreferences preferences ;
    Button book;
    String PREFS_NAME = "com.example.sinwan.mini1";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop_view);
        sname = getIntent().getExtras().getString("sname");


        preferences = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        if (preferences.getString("logged", "").toString().equals("logged")) {

            // type = intent.getStringExtra("TYPE");
            //  Uname = intent.getStringExtra("NAME");
            type=preferences.getString("type", "").toString();
            Uname=preferences.getString("username", "").toString();
            Uid=preferences.getString("Uid", "").toString();
            Toast.makeText(ShopView_Activity.this,type+Uname,Toast.LENGTH_SHORT).show();

        }
        getJSON();

    }

    private void showPatients(){
        JSONObject jsonObject = null;


        try {
            jsonObject = new JSONObject(JSON_STRING);
            JSONArray result = jsonObject.getJSONArray("result");


            JSONObject jo = result.getJSONObject(0);

            name = jo.getString("name");

            category = jo.getString("category");

            //tag=jo.getString("quantity");

            price = jo.getInt("price");



            // video.setVisibility(View.VISIBLE);
            url = jo.getString("image");
            // info.setText("Model:"+name+"\n"+"Variant:"+variant+"\nTorque :"+torque+"\n Efficiency : "+eff);
            List<DataShopView> data1 = new ArrayList<>();
            for (int i = 0; i < result.length(); i++) {
                JSONObject json_data = result.getJSONObject(i);

                DataShopView fishData = new DataShopView();
                fishData.fishName = json_data.getString("name");
                fishData.catName = json_data.getString("category");
                //fishData.tagName = json_data.getString("quantity");
                fishData.price = json_data.getInt("price");
                fishData.imageName = json_data.getString("image");
               fishData.id = json_data.getInt("id");
                data1.add(fishData);
            }
            mRVFish = (RecyclerView) findViewById(R.id.fishPriceList3);
            mAdapter1 = new AdapterShopView(ShopView_Activity.this, data1);
            mRVFish.setAdapter(mAdapter1);
            mRVFish.setLayoutManager(new LinearLayoutManager(ShopView_Activity.this));



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
                loading = ProgressDialog.show(ShopView_Activity.this,"Fetching Data","Wait...",false,false);
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
                    URL url = new URL("http://shopee.esy.es/BRAND/view_shop.php");
                    String urlParams = "sname="+sname;

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

}
