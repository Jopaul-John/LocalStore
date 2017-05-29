package com.example.sinwan.shopkeeper;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

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
    DataCart current;
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
        View view=inflater.inflate(R.layout.container_book, parent,false);
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
        myHolder.textType.setText("User id: " + current.catName);
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


        // create constructor to get widget reference
        public MyHolder(View itemView) {
            super(itemView);
            textFishName= (TextView) itemView.findViewById(R.id.textFishName);
            textSize = (TextView) itemView.findViewById(R.id.textSize);
            textType = (TextView) itemView.findViewById(R.id.textType);
           textPrice = (TextView) itemView.findViewById(R.id.textPrice);
          image1 = (ImageView) itemView.findViewById(R.id.icon);


            itemView.setOnClickListener(this);


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

        }



    }



}
