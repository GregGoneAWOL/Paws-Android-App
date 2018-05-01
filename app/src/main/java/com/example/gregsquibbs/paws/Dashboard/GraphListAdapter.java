package com.example.gregsquibbs.paws.Dashboard;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.TextView;

import com.example.gregsquibbs.paws.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class GraphListAdapter extends RecyclerView.Adapter<GraphListAdapter.customholder>{

    Dataobject[] CalculatedList;
    int NumberofValid;
    int Which;


    public GraphListAdapter(Dataobject[] values, int numberofvalid, int which) {

        CalculatedList = values;
        NumberofValid = numberofvalid;
        Which = which;


    }

    @Override
    public customholder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.graphlist, parent, false);
        return  new customholder(view);

    }

    @Override
    public void onBindViewHolder(customholder holder, int position) {


        String changeddate;
        Date changed;
        String correctdate = CalculatedList[position].Timestamp;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat simpleDateFormatt = new SimpleDateFormat("dd-MM-yyyy");

        try {


            changed = simpleDateFormat.parse(correctdate);


        } catch (ParseException e) {
            e.printStackTrace();
            changed = null;

        }

        changeddate = simpleDateFormatt.format(changed);

        holder.Datee.setText(changeddate + ":    ");

        if(Which == 0){

            holder.Valuee.setText(CalculatedList[position].Active);
        } else
        if(Which == 1){

            holder.Valuee.setText(CalculatedList[position].Rest);
        }else
        if(Which == 2){

            holder.Valuee.setText(CalculatedList[position].Play);
        }else
        if(Which == 3){

            holder.Valuee.setText(CalculatedList[position].Average);
        }





    }


    @Override
    public int getItemCount() {
        return NumberofValid;
    }




    public class customholder extends RecyclerView.ViewHolder {

        TextView Datee;
        TextView Valuee;


        public customholder(View itemView) {
            super(itemView);

            Datee = (TextView) itemView.findViewById(R.id.graph_dates);
            Valuee = (TextView) itemView.findViewById(R.id.graph_values);


        }

    }


}













