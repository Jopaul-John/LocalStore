package com.example.sinwan.final2;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by Admin on 12-02-2017.
 */

public class Jobportal extends MainActivity {

    Button a1, a2, a3, a4;

    TextView t1, t2, t3, t4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.j_layout);
        t1 = (TextView) findViewById(R.id.textView2);
        t2 = (TextView) findViewById(R.id.textView);
        t3 = (TextView) findViewById(R.id.textView3);
        t4 = (TextView) findViewById(R.id.textView4);


        a1 = (Button) findViewById(R.id.button5);
        a2 = (Button) findViewById(R.id.button8);
        a3 = (Button) findViewById(R.id.button9);
        a4 = (Button) findViewById(R.id.button10);



        // set new title to the MenuItem



        preferences = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        if (preferences.getString("logged", "").toString().equals("logged")) {

            Intent intent = getIntent();

            // type = intent.getStringExtra("TYPE");
            //  Uname = intent.getStringExtra("NAME");
            type=preferences.getString("type", "").toString();
            Uname=preferences.getString("username", "").toString();
            Toast.makeText(Jobportal.this,type+Uname,Toast.LENGTH_SHORT).show();

        }
        a1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (preferences.getString("logged", "").toString().equals("logged")) {
                    Toast.makeText(Jobportal.this,Uname+",Successfully applied for Job"+1 ,Toast.LENGTH_SHORT).show();
                    Intent i = new Intent(Jobportal.this, Jobportal.class);
                    startActivity(i);

                }
                else {
                    Intent i = new Intent(Jobportal.this, LActivity.class);
                    i.putExtra("id", "1");
                    startActivity(i);
                }
            }
        });
       a2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (preferences.getString("logged", "").toString().equals("logged")) {
                    Toast.makeText(Jobportal.this,Uname+",Successfully applied for Job"+2 ,Toast.LENGTH_SHORT).show();
                    Intent i = new Intent(Jobportal.this, Jobportal.class);
                    startActivity(i);

                }
                else {
                    Intent i = new Intent(Jobportal.this, LActivity.class);
                    i.putExtra("id", "2");
                    startActivity(i);
                }
            }
        });
        a3.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (preferences.getString("logged", "").toString().equals("logged")) {
                    Toast.makeText(Jobportal.this,Uname+",Successfully applied for Job"+3 ,Toast.LENGTH_SHORT).show();
                    Intent i = new Intent(Jobportal.this, Jobportal.class);
                    startActivity(i);

                }
                else {
                    Intent i = new Intent(Jobportal.this, LActivity.class);
                    i.putExtra("id", "3");
                    startActivity(i);
                }
            }
        });
        a4.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (preferences.getString("logged", "").toString().equals("logged")) {

                    Toast.makeText(Jobportal.this,Uname+",Successfully applied for Job"+4 ,Toast.LENGTH_SHORT).show();
                    Intent i = new Intent(Jobportal.this, Jobportal.class);
                    startActivity(i);
                }
                else {
                    Intent i = new Intent(Jobportal.this, LActivity.class);
                    i.putExtra("id", "4");
                    startActivity(i);
                }
            }
        });

    }
    @Override
    public void onBackPressed() {
        Intent i=new Intent(Jobportal.this,MainActivity.class);
        startActivity(i);
    }
}
