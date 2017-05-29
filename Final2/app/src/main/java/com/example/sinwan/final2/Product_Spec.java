package com.example.sinwan.final2;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.slider.library.Animations.DescriptionAnimation;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.TextSliderView;
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

public class Product_Spec extends AppCompatActivity implements View.OnClickListener, RatingBar.OnRatingBarChangeListener, AdapterView.OnItemSelectedListener {
    public String JSON_STRING;
    Button book;
    TextView info;
    String id,name,price,category,tag,url,sname,sid,speci,color,nRating,latitude,longitude,url2,url3;
    ImageView car;
    private SimpleCursorAdapter myAdapter;
    private RatingBar ratingBar;
    private SliderLayout mDemoSlider;

    TextView tr,tn,tc,tt,tp,tcl,toth,trat;
    int times;
    String type,Uname,Uid,gpname,cat,Rating,Lat,Lon;
    SharedPreferences preferences ;
    String PREFS_NAME = "com.example.sinwan.mini1";
    private String[] strArrData = {"No Suggestions"};
    private Spinner spinnerShop;
    private ArrayList<Category> categoriesList;
    ProgressDialog pDialog;
    final String[] from = new String[] {"sname"};
    final int[] to = new int[] {android.R.id.text1};

    private String URL_CATEGORIES;

    SharedPreferences preferences1 ;

    String mylat,mylon;
    String PREFS_NAME1 = "com.example.sinwan.mini2";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product__spec);
        preferences1 = getSharedPreferences(PREFS_NAME1, Context.MODE_PRIVATE);
        mylat=preferences1.getString("mylat", "").toString();
        mylon=preferences1.getString("mylon", "").toString();
        Toast.makeText(Product_Spec.this,"Lat="+mylat,Toast.LENGTH_SHORT).show();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Intent intent = getIntent();
        gpname= intent.getStringExtra("pname");

        id = intent.getStringExtra("pid");


        Toast.makeText(Product_Spec.this,"id and pname="+gpname+id,Toast.LENGTH_SHORT).show();
        spinnerShop = (Spinner) findViewById(R.id.spinner);


        spinnerShop.setOnItemSelectedListener(this);
        categoriesList = new ArrayList<Category>();
        // set new title to the MenuItem
     //   myAdapter = new SimpleCursorAdapter(Product_Spec.this, android.R.layout.simple_spinner_dropdown_item, null, from, to, CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);



        preferences = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        if (preferences.getString("logged", "").toString().equals("logged")) {


            // type = intent.getStringExtra("TYPE");
            //  Uname = intent.getStringExtra("NAME");
            type=preferences.getString("type", "").toString();
            Uname=preferences.getString("username", "").toString();
            Uid=preferences.getString("Uid", "").toString();
            Toast.makeText(Product_Spec.this,type+Uname,Toast.LENGTH_SHORT).show();

        }

       // info = (TextView)findViewById(R.id.tv_info1);
     //   car = (ImageView)findViewById(R.id.iv_car);
        tn= (TextView)findViewById(R.id.tName);
        tc= (TextView)findViewById(R.id.tCategory);
        tt= (TextView)findViewById(R.id.tTags);
        tp= (TextView)findViewById(R.id.tPrice);
        tcl= (TextView)findViewById(R.id.tColour);
        toth= (TextView)findViewById(R.id.tOther);
        trat= (TextView)findViewById(R.id.tRating);
        tr= (TextView)findViewById(R.id.TRating);


        ratingBar = (RatingBar) findViewById(R.id.ratingBar);
        ratingBar.setOnRatingBarChangeListener(this);
        book = (Button) findViewById(R.id.book);
        book.setOnClickListener(this);


        URL_CATEGORIES = "http://shopee.esy.es/BRAND/car_search_brand.php?name="+gpname;

        getJSON();

        new GetCategories().execute();

        mDemoSlider = (SliderLayout)findViewById(R.id.slider);



    }

    private void populateSpinner() {
        List<String> lables = new ArrayList<String>();



        for (int i = 0; i < categoriesList.size(); i++) {
            lables.add(categoriesList.get(i).getName());
        }

        // Creating adapter for spinner
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, lables);

        // Drop down layout style - list view with radio button
        spinnerAdapter
                .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        spinnerShop.setAdapter(spinnerAdapter);
    }
    private class GetCategories extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(Product_Spec.this);
            pDialog.setMessage("Fetching details..");
            pDialog.setCancelable(false);
            pDialog.show();

        }

        @Override
        protected Void doInBackground(Void... arg0) {
            ServiceHandler jsonParser = new ServiceHandler();
            String json = jsonParser.makeServiceCall(URL_CATEGORIES, ServiceHandler.GET);
            //List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();

            //nameValuePairs.add(new BasicNameValuePair("name",name));


            Log.e("Response: ", "> " + json);

            if (json != null) {
                try {
                    JSONObject jsonObj = new JSONObject(json);
                    if (jsonObj != null) {
                        JSONArray categories = jsonObj
                                .getJSONArray("result");

                        for (int i = 0; i < categories.length(); i++) {
                            JSONObject catObj = (JSONObject) categories.get(i);
                            Lat= catObj.getString("latitude");
                            Lon= catObj.getString("longitude");

                            Location startPoint=new Location("locationA");
                            startPoint.setLatitude(Double.parseDouble(mylat));
                            startPoint.setLongitude(Double.parseDouble(mylon));

                            Location endPoint=new Location("locationA");
                            endPoint.setLatitude(Double.parseDouble(Lat));
                            endPoint.setLongitude(Double.parseDouble(Lon));

                            double distance=(startPoint.distanceTo(endPoint))/1000;
                            Category cat = new Category(
                                    catObj.getString("model")+" ("+(int)distance+"Kms)");

                         //   Toast.makeText(Product_Spec.this,"Lat="+Lat,Toast.LENGTH_SHORT).show();
                            categoriesList.add(cat);

                        }
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            } else {
                Log.e("JSON Data", "Didn't receive any data from server!");
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            if (pDialog.isShowing())
                pDialog.dismiss();
            populateSpinner();
        }

    }


    private void showPatients(){
        JSONObject jsonObject = null;


        try {
            jsonObject = new JSONObject(JSON_STRING);
            JSONArray result = jsonObject.getJSONArray("result");


            JSONObject jo = result.getJSONObject(0);



            name = jo.getString("name");


            category = jo.getString("category");

           tag=jo.getString("tag");

            price = jo.getString("price");

            sname = jo.getString("sname");

            sid = jo.getString("sid");

            speci = jo.getString("speci");

            color = jo.getString("color");

            Rating = jo.getString("rating");

            times = jo.getInt("ratetimes");






            // video.setVisibility(View.VISIBLE);
            url = jo.getString("image");
            url2 = jo.getString("image2");
            url3 = jo.getString("image3");
            // info.setText("Model:"+name+"\n"+"Variant:"+variant+"\nTorque :"+torque+"\n Efficiency : "+eff);
            tn.setText(name);
            tc.setText(category);
            tt.setText(tag);
            tp.setText(price);
            tcl.setText(color);
            toth.setText(speci);
            trat.setText(Rating);

       //     info.setText("Name:"+name+"\nCategory:"+category+"\n"+"Tags:"+tag+
         //           "\nPrice :"+price+"\n"+"\n"+"\nColor :"+color+"\n"+"\nOther Specs :"+speci+"\n"+"\nRating :"+Rating);
           // UrlImageViewHelper.setUrlDrawable(car,url);


            HashMap<String,String> file_maps = new HashMap<String, String>();

            file_maps.put("Image1",url);
            file_maps.put("Image2",url2);
            file_maps.put("Image3",url3);
            Toast.makeText(Product_Spec.this,"Image1"+url,Toast.LENGTH_SHORT).show();
            Toast.makeText(Product_Spec.this,"Image1"+url2,Toast.LENGTH_SHORT).show();
            Toast.makeText(Product_Spec.this,"Image1"+url3,Toast.LENGTH_SHORT).show();

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
            }
            mDemoSlider.setPresetTransformer(SliderLayout.Transformer.Accordion);
            mDemoSlider.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
            mDemoSlider.setCustomAnimation(new DescriptionAnimation());
            mDemoSlider.setDuration(4000);



        } catch (JSONException e) {

        }


    }

    private void getJSON(){
        class GetJSON extends AsyncTask<Void,Void,String> {

            ProgressDialog loading;
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(Product_Spec.this,"Fetching Data","Wait...",false,false);
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
                    URL url = new URL("http://shopee.esy.es/BRAND/car_info.php");
                    String urlParams = "pname="+gpname;

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

            }//e.printStackTrace();
        }
        GetJSON gj = new GetJSON();
        gj.execute();
    }

    @Override
    public void onRatingChanged(RatingBar ratingBar, float rating, boolean b) {
        Toast.makeText(Product_Spec.this,"Rated"+rating,Toast.LENGTH_LONG).show();
        float oRtng=Float.parseFloat(Rating);
        float nRtng=rating;
        float avg=((oRtng*times)+nRtng)/(++times);
        nRating= String.valueOf(avg);
       tr.setVisibility(View.INVISIBLE);
        ratingBar.setVisibility(View.INVISIBLE);

        getJSON2();
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        // On selecting a spinner item
        cat = parent.getItemAtPosition(position).toString();



    }

    public void onNothingSelected(AdapterView<?> arg0) {
        // TODO Auto-generated method stub

    }


    class BackGround1 extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... params) {
            String name = params[0];
            String tid = params[1];
            String tprice = params[2];
            String image = params[3];
            String sname = params[4];
            String sid = params[5];
            String uid = params[6];


            String data="";
            int tmp;
            try {
                URL url = new URL("http://shopee.esy.es/login/cart.php");
                String urlParams = "name="+name+"&id="+tid+"&price="+tprice+"&image="+image+"&sname="+sname+"&sid="+sid+"&uid="+uid;

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
        @Override
        protected void onPostExecute(String s) {
            Toast.makeText(Product_Spec.this,name+"Added to cart", Toast.LENGTH_LONG).show();

        }


    }


    @Override
    public void onClick(View view) {
        if  (Uid!=null) {
            Product_Spec.BackGround1 b = new Product_Spec.BackGround1();
            b.execute(name, id, price, url, sname, sid, Uid);
        }
        else
        {
            Toast.makeText(Product_Spec.this,"Please login",Toast.LENGTH_SHORT).show();
        }
    }

    private void getJSON2(){
        class GetJSON2 extends AsyncTask<Void,Void,String> {

            ProgressDialog loading;
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(Product_Spec.this,"Fetching Data","Wait...",false,false);
            }


            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                JSON_STRING = s;

                Toast.makeText(Product_Spec.this,s,Toast.LENGTH_SHORT).show();

            }

            @Override
            protected String doInBackground(Void... params) {

                String data="";
                int tmp;

                try {
                    URL url = new URL("http://shopee.esy.es/BRAND/update_rating.php");
                    String urlParams = "name="+name+"&rating="+nRating+"&times="+times;

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
