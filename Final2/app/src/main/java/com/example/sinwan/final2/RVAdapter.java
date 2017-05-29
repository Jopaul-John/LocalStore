package com.example.sinwan.final2;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.design.widget.NavigationView;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.koushikdutta.urlimageviewhelper.UrlImageViewHelper;

import java.util.List;

public class RVAdapter extends RecyclerView.Adapter<RVAdapter.PersonViewHolder> {
    public static String Sname;
    public static class PersonViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        Context context;


        CardView cv;
        TextView personName;
        TextView personAge;

        TextView efficiency,personVariant;

        ImageView CarPhoto;
        Button bMsg;



        // set new title to the MenuItem






        PersonViewHolder(View itemView) {
            super(itemView);
            cv = (CardView)itemView.findViewById(R.id.cv);
            cv.setOnClickListener(this);
            personName = (TextView)itemView.findViewById(R.id.person_name);
            personVariant = (TextView)itemView.findViewById(R.id.personvar);

            personAge = (TextView)itemView.findViewById(R.id.person_joke);
            efficiency = (TextView)itemView.findViewById(R.id.tv_eff);
            CarPhoto = (ImageView)itemView.findViewById(R.id.iv1);
            bMsg = (Button) itemView.findViewById(R.id.bMsg);
            bMsg.setOnClickListener(this);

            /*itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v){
                    Toast.makeText(v.getContext(), "Awaken the Creator Within!!", Toast.LENGTH_SHORT).show();

                }

            });*/
        }

        @Override
        public void onClick(View v) {
            if(v == cv)
            {
            int position = getAdapterPosition();
           // Toast.makeText(v.getContext(),Integer.toString(position),Toast.LENGTH_SHORT).show();

            Intent i = new Intent(v.getContext(),ShopView_Activity.class);
           // i.putExtra("name",efficiency.getText().toString());
            i.putExtra("sname",Sname);
            v.getContext().startActivity(i);
        }
        if(v == bMsg)

        {

            Intent i = new Intent(Intent.ACTION_SEND);
            i.setType("text/plain");
            i.putExtra(Intent.EXTRA_EMAIL, new String[]{efficiency.getText().toString()});
            i.putExtra(Intent.EXTRA_SUBJECT, "Queries??");
            i.putExtra(Intent.EXTRA_TEXT, "");
            try {
                v.getContext().startActivity(Intent.createChooser(i, ""));
            } catch (android.content.ActivityNotFoundException ex) {
                Toast.makeText(context, "There are no email clients installed.", Toast.LENGTH_SHORT).show();
            }
        }
        }
    }

    List<Person> persons;

    RVAdapter(List<Person> persons){
        this.persons = persons;
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public PersonViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item, viewGroup, false);
        PersonViewHolder pvh = new PersonViewHolder(v);
        return pvh;
    }


    @Override
    public void onBindViewHolder(PersonViewHolder personViewHolder, int i) {
        Sname=persons.get(i).title;

        personViewHolder.personName.setText(persons.get(i).title+"\nPh:"+persons.get(i).brand);
        personViewHolder.personVariant.setText(persons.get(i).variant);
        Sname=persons.get(i).title;
        personViewHolder.personAge.setText(persons.get(i).joke);
        personViewHolder.efficiency.setText(persons.get(i).eff);
        UrlImageViewHelper.setUrlDrawable(personViewHolder.CarPhoto,persons.get(i).url);

    }

    @Override
    public int getItemCount() {
        return persons.size();
    }
}
