package com.example.sinwan.shopkeeper;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Button;
import android.widget.Toast;

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

public class JobViewActivity extends AppCompatActivity {
    private RecyclerView mRVFish;
    private AdapterJob mAdapter;
    String name,category,url,abc;
    public String JSON_STRING;
    int id=1,price,tag;
    String type,Uname,Uid,Phone;
    SharedPreferences preferences ;
    Button apply;
    String PREFS_NAME = "com.example.sinwan.mini1";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_job_view);

        preferences = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        if (preferences.getString("logged", "").toString().equals("logged")) {

            // type = intent.getStringExtra("TYPE");
            //  Uname = intent.getStringExtra("NAME");
            type=preferences.getString("type", "").toString();
            Uname=preferences.getString("username", "").toString();
            Uid=preferences.getString("Uid", "").toString();
            Phone=preferences.getString("Phone", "").toString();
            Toast.makeText(JobViewActivity.this,type+Uname+Phone,Toast.LENGTH_SHORT).show();
            getJSON();
        }
        else
        {
            Toast.makeText(JobViewActivity.this,"Please Login",Toast.LENGTH_SHORT).show();
            Intent i=new Intent(JobViewActivity.this,MainActivity.class);
            startActivity(i);
        }

    }

    private void showPatients(){
        JSONObject jsonObject = null;


        try {
            jsonObject = new JSONObject(JSON_STRING);
            JSONArray result = jsonObject.getJSONArray("result");


            JSONObject jo = result.getJSONObject(0);

            name = jo.getString("name");

            category = jo.getString("role");

            tag=jo.getInt("id");

            price = jo.getInt("phone");




            // video.setVisibility(View.VISIBLE);

            // info.setText("Model:"+name+"\n"+"Variant:"+variant+"\nTorque :"+torque+"\n Efficiency : "+eff);
            List<DataJob> data = new ArrayList<>();
            for (int i = 0; i < result.length(); i++) {
                JSONObject json_data = result.getJSONObject(i);

                DataJob fishData = new DataJob();
                fishData.fishName = json_data.getString("name");
                fishData.catName = json_data.getString("role");

                fishData.price = json_data.getInt("phone");

                fishData.id = json_data.getInt("id");
                fishData.Phone = Phone;
                fishData.Uname = Uname;

                data.add(fishData);
            }
            mRVFish = (RecyclerView) findViewById(R.id.fishPriceList4);
            mAdapter = new AdapterJob(JobViewActivity.this, data);
            mRVFish.setAdapter(mAdapter);
            mRVFish.setLayoutManager(new LinearLayoutManager(JobViewActivity.this));



        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(JobViewActivity.this,"ERROR"+String.valueOf(e),Toast.LENGTH_SHORT).show();
        }


    }

    private void getJSON(){
        class GetJSON extends AsyncTask<Void,Void,String> {

            ProgressDialog loading;
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(JobViewActivity.this,"Fetching Data","Wait...",false,false);
            }


            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                JSON_STRING = s;
                Toast.makeText(JobViewActivity.this,s,Toast.LENGTH_SHORT).show();
                showPatients();

            }

            @Override
            protected String doInBackground(Void... params) {

                String data="";
                int tmp;

                try {
                    URL url = new URL("http://shopee.esy.es/BRAND/job_view.php");
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





}

