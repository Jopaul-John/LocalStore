package com.example.sinwan.final2;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Collections;
import java.util.List;

public class AdapterCart extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    public String JSON_STRING,id2;
    private Context context;
    private LayoutInflater inflater;
    List<DataCart> data= Collections.emptyList();
    DataFish current;
    int currentPos=0;

    // create constructor to initialize context and data sent from MainActivity
    public AdapterCart(Context context, List<DataCart> data){
        this.context=context;
        inflater= LayoutInflater.from(context);
        this.data=data;



    }

    // Inflate the layout when ViewHolder created
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view=inflater.inflate(R.layout.container_shop, parent,false);
        MyHolder holder=new MyHolder(view);
        return holder;

    }

    // Bind data
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        // Get current position of item in RecyclerView to bind data and assign values from list
        MyHolder myHolder= (MyHolder) holder;
        DataCart current=data.get(position);
        myHolder.textFishName.setText(current.fishName);
        myHolder.textSize.setText("Quantity: " + current.tagName);
        myHolder.textType.setText("Shop Name: " + current.catName);
        myHolder.textPrice.setText("Rs. " + current.price);
        myHolder.textPrice.setTextColor(ContextCompat.getColor(context, R.color.colorAccent));

    Picasso.with(context)
                .load(current.imageName)
                .into(myHolder.image1);

    }

    // return total item from List
    @Override
    public int getItemCount() {
        return data.size();
    }


    class MyHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        TextView textFishName;
        TextView textSize;
        TextView textType;
        TextView textPrice;
        ImageView image1;
        ImageButton ib1;

        // create constructor to get widget reference
        public MyHolder(View itemView) {
            super(itemView);
            textFishName= (TextView) itemView.findViewById(R.id.textFishName);
            textSize = (TextView) itemView.findViewById(R.id.textSize);
            textType = (TextView) itemView.findViewById(R.id.textType);
           textPrice = (TextView) itemView.findViewById(R.id.textPrice);
          image1 = (ImageView) itemView.findViewById(R.id.icon);
            ib1 = (ImageButton) itemView.findViewById(R.id.ib1);

            //itemView.setOnClickListener(this);

ib1.setOnClickListener(this);
        }

        // Click event for all items



        @Override
        public void onClick(View v) {

            int position=getPosition();
            DataCart current=data.get(position);
            String name=current.fishName;
            int id=current.id;
            id2=String.valueOf(id);
            Toast.makeText(context, "You clicked an item"+position+"!!"+"Item="+name+"Id2="+id2, Toast.LENGTH_SHORT).show();
            BackGround1 b = new BackGround1();
            b.execute(id2);
           Intent myIntent = new Intent(v.getContext(), Cart_Activity.class);
            v.getContext().startActivity(myIntent);


        }

        class BackGround1 extends AsyncTask<String, String, String> {

            @Override
            protected String doInBackground(String... params) {
                String id = params[0];

                String data="";
                int tmp;
                try {
                    URL url = new URL("http://shopee.esy.es/BRAND/delete.php");
                    String urlParams = "id="+id;

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
                super.onPostExecute(s);

                Toast.makeText(context, s, Toast.LENGTH_LONG).show();
            }

        }

    }



}
