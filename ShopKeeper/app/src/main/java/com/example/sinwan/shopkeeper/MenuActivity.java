package com.example.sinwan.shopkeeper;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MenuActivity extends AppCompatActivity implements View.OnClickListener {
Button bJobs,bStock,bBook,bApplicaations,bNew;
    String type,Uname;
    SharedPreferences preferences ;
    String PREFS_NAME = "com.example.sinwan.mini1";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        Intent intent = getIntent();
        preferences = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);

        if (preferences.getString("logged", "").toString().equals("logged")) {

            type =preferences.getString("type", "").toString();
            Uname =preferences.getString("type", "").toString();
            Toast.makeText(MenuActivity.this, "Shop=" + type, Toast.LENGTH_SHORT).show();
        }
        else
        {
            type = intent.getStringExtra("TYPE");
            Uname = intent.getStringExtra("NAME");
            if(Uname==null)
                Toast.makeText(MenuActivity.this, "You must Login", Toast.LENGTH_SHORT).show();
        }

bJobs=(Button)findViewById(R.id.bJobs);
        bStock=(Button)findViewById(R.id.bStock);
        bBook=(Button)findViewById(R.id.bBooking);
        bApplicaations=(Button)findViewById(R.id.bApplications);
        bNew=(Button)findViewById(R.id.bNew);
        bJobs.setOnClickListener(this);
        bStock.setOnClickListener(this);
        bBook.setOnClickListener(this);
        bNew.setOnClickListener(this);
        bApplicaations.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        if(v == bJobs){
            Intent i=new Intent(MenuActivity.this,JobActivity.class);
            i.putExtra("NAME",Uname);
            i.putExtra("TYPE",type);
            startActivity(i);
        }
        if(v == bStock){
            Intent i1=new Intent(MenuActivity.this,StockActivity.class);
            i1.putExtra("NAME",Uname);
            i1.putExtra("TYPE",type);
            startActivity(i1);
        }
        if(v == bApplicaations){
            Intent i1=new Intent(MenuActivity.this,JobViewActivity.class);
            i1.putExtra("NAME",Uname);
            i1.putExtra("TYPE",type);
            startActivity(i1);
        }
        if(v == bBook){
            Intent i1=new Intent(MenuActivity.this,Cart_Activity.class);
            i1.putExtra("NAME",Uname);
            i1.putExtra("TYPE",type);
            startActivity(i1);
        }
        if(v == bNew){
            Intent i1=new Intent(MenuActivity.this,photo.class);
            startActivity(i1);
        }

    }
}
