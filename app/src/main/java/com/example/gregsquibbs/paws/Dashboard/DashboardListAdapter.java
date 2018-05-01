package com.example.gregsquibbs.paws.Dashboard;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.example.gregsquibbs.paws.DatabaseInteraction.GetData;
import com.example.gregsquibbs.paws.DatabaseInteraction.GetDogInfoRequest;
import com.example.gregsquibbs.paws.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class DashboardListAdapter extends RecyclerView.Adapter<DashboardListAdapter.CustomViewHolder> {

    String[] Values;
    Context appcontext;
    String userID;
    String Rest;
    String Active;
    String Play;
    String Average;
    String Timestamp;
    TextView Datetv;
    int NumberOfEntries; // Selected entry
    Dataobject[] entriesarray;
    int validentriess;



    DashboardFragment activity;



     public DashboardListAdapter(String[] values, Context context, String Userid, int entries, Dataobject[] validentries, int Splitentries, TextView datetv){

        Values = values;
        appcontext = context;
        userID = Userid;
        NumberOfEntries = entries;
        entriesarray = validentries;
        Datetv = datetv;



    }




    @Override
    public DashboardListAdapter.CustomViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {


        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerviewdesign, parent, false);


        return new CustomViewHolder(view);

    }

    @Override
    public void onBindViewHolder(DashboardListAdapter.CustomViewHolder holder, int position) {

         final int Position = position;
         final DashboardListAdapter.CustomViewHolder Holder = holder;

         // Get the values from the server //

        if(NumberOfEntries == -1){

            NumberOfEntries = 0;
        }

        Dataobject g = entriesarray[NumberOfEntries];

        Play = g.Play;
        Active = g.Active;
        Rest = g.Rest;
        Timestamp = g.Timestamp;
        Average = g.Average;

        SharedPreferences myprefs = appcontext.getSharedPreferences("PawsPrefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = myprefs.edit();
        editor.putString("timestamp", Timestamp);
        Datetv.setText(Timestamp);


        editor.commit();


                    switch (Position){



                        case 0:

                            Holder.Title1.setText("Active:");
                            Holder.Value1.setText(Active);

                            Holder.Title.setText("Rest:");
                            Holder.Value.setText(Rest);

                            Holder.Title.setBackgroundColor(Color.parseColor("#6aa4cc"));
                            Holder.Value.setBackgroundColor(Color.parseColor("#6aa4cc"));

                            Holder.Title1.setBackgroundColor(Color.parseColor("#78cc6a"));
                            Holder.Value1.setBackgroundColor(Color.parseColor("#78cc6a"));

                            break;
                        case 1:


                            Holder.Title.setText("Play:");
                            Holder.Title1.setText("Average:");

                            Holder.Title.setBackgroundColor(Color.parseColor("#cc6969"));
                            Holder.Value.setBackgroundColor(Color.parseColor("#cc6969"));

                            Holder.Title1.setBackgroundColor(Color.parseColor("#c368cc"));
                            Holder.Value1.setBackgroundColor(Color.parseColor("#c368cc"));

                            Holder.Value.setText(Play);
                            Holder.Value1.setText(Average);

                            break;

                    }





    }


    @Override
    public int getItemCount() {
        return Values.length -2;
    }




    public class CustomViewHolder extends RecyclerView.ViewHolder {

        TextView Value1;
        TextView Title1;
        TextView Value;
        TextView Title;


        public CustomViewHolder(View itemView) {
            super(itemView);

            Value = (TextView) itemView.findViewById(R.id.recycler_text);
            Title = (TextView) itemView.findViewById(R.id.recycler_title);
            Value1 = (TextView) itemView.findViewById(R.id.recycler_text1);
            Title1 = (TextView) itemView.findViewById(R.id.recycler_title1);

        }

    }


}
