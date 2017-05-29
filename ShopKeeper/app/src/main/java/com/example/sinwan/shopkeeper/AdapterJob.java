package com.example.sinwan.shopkeeper;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Collections;
import java.util.List;

public class AdapterJob extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    public String JSON_STRING,name,Sname,Role;
    String phone;
    private Context context;
    private LayoutInflater inflater;
    SharedPreferences preferences ;
    String PREFS_NAME = "com.example.sinwan.mini1";
    List<DataJob> data= Collections.emptyList();
    DataJob current;
    int currentPos=0;

    // create constructor to initialize context and data sent from MainActivity
    public AdapterJob(Context context, List<DataJob> data){
        this.context=context;
        inflater= LayoutInflater.from(context);
        this.data=data;

    }

    // Inflate the layout when ViewHolder created
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view=inflater.inflate(R.layout.container_job, parent,false);
        MyHolder holder=new MyHolder(view);
        return holder;

    }

    // Bind data
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        // Get current position of item in RecyclerView to bind data and assign values from list
        MyHolder myHolder= (MyHolder) holder;
        DataJob current=data.get(position);
        myHolder.textFishName.setText("Role:"+current.catName+"\nName:"+current.fishName+"\nPhone:"+current.price);
    }

    // return total item from List
    @Override
    public int getItemCount() {
        return data.size();
    }


    class MyHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        TextView textFishName;


        // create constructor to get widget reference
        public MyHolder(View itemView) {
            super(itemView);
            textFishName= (TextView) itemView.findViewById(R.id.tv_info1);


            itemView.setOnClickListener(this);

        }

        // Click event for all items
        @Override
        public void onClick(View v) {


            int position=getPosition();
            DataJob current=data.get(position);
            String name=current.fishName;

                int id=current.id;
                String id2=String.valueOf(id);
                Toast.makeText(context, "You clicked an item"+position+"!!"+"Item="+name, Toast.LENGTH_SHORT).show();




        }



    }

}
