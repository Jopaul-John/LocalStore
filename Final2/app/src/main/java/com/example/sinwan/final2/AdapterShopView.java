package com.example.sinwan.final2;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.Collections;
import java.util.List;

public class AdapterShopView extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    public String JSON_STRING,id2;
    private Context context;
    private LayoutInflater inflater;
    List<DataShopView> data= Collections.emptyList();
    DataFish current;
    int currentPos=0;

    // create constructor to initialize context and data sent from MainActivity
    public AdapterShopView(Context context, List<DataShopView> data){
        this.context=context;
        inflater= LayoutInflater.from(context);
        this.data=data;



    }

    // Inflate the layout when ViewHolder created
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view=inflater.inflate(R.layout.container_shopview, parent,false);
        MyHolder holder=new MyHolder(view);
        return holder;

    }

    // Bind data
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        // Get current position of item in RecyclerView to bind data and assign values from list
        MyHolder myHolder= (MyHolder) holder;
        DataShopView current=data.get(position);

        myHolder.info.setText("Name:" + current.fishName + "\nCategory:" + current.catName + "\n" + "Price:" + current.price);


    Picasso.with(context)
                .load(current.imageName)
                .into(myHolder.car);

    }

    // return total item from List
    @Override
    public int getItemCount() {
        return data.size();
    }


    class MyHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        String pname,sname,price,category;
        TextView info;
        ImageView car;
        Button compare;

        TextView textPrice;

        ImageButton ib1;

        // create constructor to get widget reference
        public MyHolder(View itemView) {
            super(itemView);



            compare = (Button)itemView.findViewById(R.id.cmp);
            compare.setOnClickListener(this);
            info = (TextView)itemView.findViewById(R.id.tv_info1);
            car = (ImageView)itemView.findViewById(R.id.iv_car);

            itemView.setOnClickListener(this);


        }

        // Click event for all items



        @Override
        public void onClick(View v) {
            if(v == compare){

            }
            int position=getPosition();
            DataShopView current=data.get(position);
            String name=current.fishName;
            if(v == itemView){
                Toast.makeText(context, "You clicked an item"+position+"!!"+"Item="+name, Toast.LENGTH_SHORT).show();
                int id=current.id;
                String id2=String.valueOf(id);
                pname=current.fishName;
                Intent myIntent = new Intent(v.getContext(), Product_Spec.class);
                myIntent.putExtra("pname",pname);
                myIntent.putExtra("pid",id2);
                v.getContext().startActivity(myIntent);

            }



        }


    }



}
