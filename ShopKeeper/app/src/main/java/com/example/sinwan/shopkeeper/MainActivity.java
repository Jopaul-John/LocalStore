package com.example.sinwan.shopkeeper;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    EditText name, password;
    String Name, Password;
    Context ctx=this;
    String NAME=null,PASSWORD=null;
    int counter = 3;
    TextView tx1;
    CheckBox remember;
    SharedPreferences preferences ;
    String susname,spasw,type="",sOp,con,sid;
    String PREFS_NAME = "com.example.sinwan.mini1";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        name = (EditText) findViewById(R.id.main_name);
        password = (EditText) findViewById(R.id.main_password);
        tx1=(TextView)findViewById(R.id.textCount);
        tx1.setVisibility(View.GONE);
        name.setText("");
        password.setText("");
        remember = (CheckBox)findViewById(R.id.rememberme);

        preferences = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);

        if (preferences.getString("logged", "").toString().equals("logged"))
        {


                Intent i = new Intent(MainActivity.this, MenuActivity.class);
                i.putExtra("USERNAME", preferences.getString("username", "").toString());
                //        i.putExtra("PASSWORD", preferences.getString("password", "").toString());
                i.putExtra("type",preferences.getString("username", "").toString());
                startActivity(i);


        }

        ConnectivityManager cn=(ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo nf=cn.getActiveNetworkInfo();
        if(nf != null && nf.isConnected()==true )
        {
            Toast.makeText(this, "Network Available", Toast.LENGTH_LONG).show();
            con="1";
        }
        else
        {
            Toast.makeText(this, "Network Not Available", Toast.LENGTH_LONG).show();
            con="0";
        }

    }


    public void main_login(View v){
        Name = name.getText().toString();
        Password = password.getText().toString();
        BackGround b = new BackGround();
        b.execute(Name, Password);

    }
    class BackGround extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... params) {
            String name = params[0];
            String password = params[1];
            String data="";
            int tmp;
            try {
                URL url = new URL("http://shopee.esy.es/login/login.php");
                String urlParams = "name="+name+"&password="+password;

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

        @TargetApi(Build.VERSION_CODES.GINGERBREAD)
        @Override
        protected void onPostExecute(String s) {
            String err=null;
            try {
                JSONObject root = new JSONObject(s);
                JSONObject user_data = root.getJSONObject("user_data");
                NAME = user_data.getString("name");
                PASSWORD=user_data.getString("password");
                type = user_data.getString("type");
                sid = user_data.getString("sid");

            } catch (JSONException e) {
                e.printStackTrace();
                err = "Exception: "+e.getMessage();
            }
            if (NAME==null || NAME.isEmpty()){
                if(con.equals("1")) {
                    Toast.makeText(ctx, "Wrong Credentials", Toast.LENGTH_LONG).show();
                    tx1.setVisibility(View.VISIBLE);
                    tx1.setBackgroundColor(Color.RED);
                    counter--;
                    tx1.setText(Integer.toString(counter));
                }
                else
                {
                    Toast.makeText(ctx, "Please check your connection ", Toast.LENGTH_LONG).show();
                    counter--;
                    tx1.setText(Integer.toString(counter));
                }

            }

            else if((NAME.equals(Name))&&(PASSWORD.equals(Password)))
            {
                Toast.makeText(ctx, "Login success", Toast.LENGTH_LONG).show();
                if (remember.isChecked()) {
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putString("username", name.getText().toString());
                    editor.putString("type", type);
                    editor.putString("sid", sid);
                    susname= name.getText().toString();
                    spasw = password.getText().toString();
                    editor.putString("logged", "logged");
                    editor.commit();
                }

                    Intent myIntent = new Intent(MainActivity.this, MenuActivity.class);
                    myIntent.putExtra("NAME",Name);
                    myIntent.putExtra("TYPE",type);
                    startActivity(myIntent);

            }

            if (counter == 0)
            {
                System.exit(1);
            }

        }

    }

    @Override
    public void onBackPressed() {
        Intent jk=new Intent(Intent.ACTION_MAIN);
        jk.addCategory(Intent.CATEGORY_HOME);
        jk.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(jk);
    }
}
