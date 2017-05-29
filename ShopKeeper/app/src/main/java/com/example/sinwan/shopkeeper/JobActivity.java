package com.example.sinwan.shopkeeper;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class JobActivity extends AppCompatActivity implements View.OnClickListener {
    EditText eName,eRole,eSal,eDesc;
    Button update;
    String Sname,Srole,Ssal,Sdesc;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_job);
        eName=(EditText)findViewById(R.id.eName);
        eRole=(EditText)findViewById(R.id.eRole);
        eSal=(EditText)findViewById(R.id.eSal);
        eDesc=(EditText)findViewById(R.id.eDesc);
        update=(Button)findViewById(R.id.bAdd);

        update.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        Sname=eName.getText().toString();
        Srole=eRole.getText().toString();
        Ssal=eSal.getText().toString();
        Sdesc=eDesc.getText().toString();
        BackGround b = new BackGround();
        b.execute(Sname,Srole,Ssal,Sdesc);
    }

    class BackGround extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... params) {
            String name = params[0];
            String role = params[1];
            String  sal=params[2];
            String desc=params[3];


            String data="";
            int tmp;
            try {
                URL url = new URL("http://shopee.esy.es/login/job.php");
                String urlParams = "name="+name+"&role="+role+"&sal="+sal+"&desc="+desc;

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
            Toast.makeText(JobActivity.this,"Job added Successfully",Toast.LENGTH_LONG).show();

        }


    }
}
