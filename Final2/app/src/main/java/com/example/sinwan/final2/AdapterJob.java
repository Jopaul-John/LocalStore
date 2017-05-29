package com.example.sinwan.final2;

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
    public String JSON_STRING,name,Sname,Role,address;
    String phone;
    private Context context;
    private LayoutInflater inflater;
    SharedPreferences preferences ;
    String PREFS_NAME = "com.example.sinwan.mini1";
    List<DataJob> data= Collections.emptyList();
    DataFish current;
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
        myHolder.textFishName.setText("Role:"+current.fishName+"\nSalary:"+current.price+"\nShopName:"+current.fishName+"\nDescripton:"+current.description);
    }

    // return total item from List
    @Override
    public int getItemCount() {
        return data.size();
    }


    class MyHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        TextView textFishName;
       Button Apply;

        // create constructor to get widget reference
        public MyHolder(View itemView) {
            super(itemView);
            textFishName= (TextView) itemView.findViewById(R.id.tv_info1);
            Apply= (Button) itemView.findViewById(R.id.cmp);

            itemView.setOnClickListener(this);
            Apply.setOnClickListener(this);
        }

        // Click event for all items
        @Override
        public void onClick(View v) {

            if(v == Apply){
                int position=getPosition();
                DataJob current=data.get(position);
                 name=current.Uname;
                phone=current.Phone;
                Sname=current.fishName;
                Role=current.catName;
                address=current.address;

                getJSON2();
            }
            int position=getPosition();
            DataJob current=data.get(position);
            String name=current.fishName;
            if(v == itemView){
                Toast.makeText(context, "You clicked an item"+position+"!!"+"Item="+name, Toast.LENGTH_SHORT).show();
                int id=current.id;
                String id2=String.valueOf(id);

                Intent myIntent = new Intent(v.getContext(), Product_Spec.class);

                myIntent.putExtra("pid",id2);
                v.getContext().startActivity(myIntent);

            }
        }



    }
    private void getJSON2(){
        class GetJSON2 extends AsyncTask<Void,Void,String> {

            ProgressDialog loading;
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(context,"Fetching Data","Wait...",false,false);
            }


            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                JSON_STRING = s;
                Toast.makeText(context,s,Toast.LENGTH_SHORT).show();

            }

            @Override
            protected String doInBackground(Void... params) {

                String data="";
                int tmp;

                try {
                    URL url = new URL("http://shopee.esy.es/BRAND/job_book.php");
                    String urlParams = "name="+name+"&phone="+phone+"&Sname="+Sname+"&Role="+Role+"&address="+address;

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
