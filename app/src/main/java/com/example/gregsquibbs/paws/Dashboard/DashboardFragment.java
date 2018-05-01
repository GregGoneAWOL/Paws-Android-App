package com.example.gregsquibbs.paws.Dashboard;

import android.app.ActionBar;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.res.TypedArrayUtils;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.example.gregsquibbs.paws.DatabaseInteraction.DatabasePOJO;
import com.example.gregsquibbs.paws.DatabaseInteraction.GetData;
import com.example.gregsquibbs.paws.DatabaseInteraction.GetDogInfoRequest;
import com.example.gregsquibbs.paws.DatabaseInteraction.NewEntryRequest;
import com.example.gregsquibbs.paws.R;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.DefaultValueFormatter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.regex.Pattern;

/**
 * Created by George PC on 06/03/2018.
 */

public class DashboardFragment extends Fragment {

    int NumberofEntries;
    int SelectedEntrie;
    String[] list;
    Dataobject[] ObjectList;
    Dataobject[] CalculatedList;
    RecyclerView recyclerView;
    TextView currentdate;
    TextView next;
    TextView previous;
    TextView DogsName;
    DashboardListAdapter dashboardListAdapter;
    int numberofvalid =0;
    PieChart pieChart;
     String UserID;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        // Inflate the view to which contains the dashboard_fragement layout then return the view *******George LA

        final View view = inflater.inflate(R.layout.dashboard_fragment, container, false);

        // get the user id //
        final SharedPreferences myprefs = getActivity().getSharedPreferences("PawsPrefs", Context.MODE_PRIVATE);
       UserID = myprefs.getString("userID","Error");

        Log.d("USERID" , ""+ Integer.parseInt(UserID));


        DatabasePOJO db = new DatabasePOJO(getContext());

        // Set up the recylerview list and display its contents programatically.

        TextView date = (TextView) view.findViewById(R.id.dashboard_Date);
        Date todaysdate = new Date();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
        DogsName = (TextView) view.findViewById(R.id.dashboard_dogs_name);
        currentdate = (TextView) view.findViewById(R.id.dashboard_selected_date);
        date.setText(""+simpleDateFormat.format(todaysdate));
        previous = (TextView) view.findViewById(R.id.dashboard_previous);
        next = (TextView) view.findViewById(R.id.dashboard_next);
        recyclerView = (RecyclerView) view.findViewById(R.id.dashboard_recyclerview);
        pieChart  = (PieChart) view.findViewById(R.id.dashboard_piechart);




        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {

                    getdoginfo();
                    DogsName.setText(myprefs.getString("Dogs_Name", "Dog"));
                    pieChart.setNoDataText("Please sync fitbark to show data.");
                    pieChart.invalidate();

                    JSONObject jsonResponse = new JSONObject(response);
                    JSONArray jsonArray = jsonResponse.getJSONArray("server_response");

                    String[] split = jsonArray.toString().split(Pattern.quote("}"));
                    System.out.println("LENGTH "+ split.length);


                    ObjectList = new Dataobject[jsonArray.length()];
                    CalculatedList = new Dataobject[30];

                    for(int d =0; d<CalculatedList.length; d++){

                        CalculatedList[d] = new Dataobject();

                    }



                    // Sort the JSON Response into entries which were submitted in the same day.

                    for(int i =0;  i<jsonArray.length(); i++){

                        System.out.println("Count " + i);

                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        String Play = (String) jsonObject.get("min_play");
                        String  Rest = (String) jsonObject.get("min_rest");
                        String Active = (String) jsonObject.get("min_active");
                        String  Average = (String) jsonObject.get("hourly_average");
                        String Timestamp = jsonObject.get("timestamp").toString().substring(0, 10);

                        ObjectList[i] = new Dataobject();
                        ObjectList[i].Setvalues(Average, Play,Active,Rest,Timestamp);








                    }


                    // Sort out the  Objects array by looping through and if the timestamp is not in the new array add it
                    // else add the data from that object to the one contained in the array.


                    for(Dataobject list : ObjectList){

                        Boolean matchfound = false;

                        for(int i = 0; i<CalculatedList.length; i++){


                            if(list.Timestamp.trim().equals(CalculatedList[i].Timestamp.trim()) ){

                                // DO nothing the value is not there

                                matchfound = true;

                                int Av = Integer.parseInt(list.Average) -  Integer.parseInt(CalculatedList[i].Average) ;
                                int RE = Integer.parseInt(list.Rest)-Integer.parseInt(CalculatedList[i].Rest);
                                int Ac =  Integer.parseInt(list.Active) -Integer.parseInt(CalculatedList[i].Active);
                                int Pl =  Integer.parseInt(list.Play) - Integer.parseInt(CalculatedList[i].Play);

                                int calcav = (Integer.parseInt(CalculatedList[i].Average) + Av);
                                int calres = (Integer.parseInt(CalculatedList[i].Rest) + RE);
                                int calpl = (Integer.parseInt(CalculatedList[i].Play) + Pl);
                                int calcac = (Integer.parseInt(CalculatedList[i].Active) + Ac);

                                CalculatedList[i].Average = calcav+"";
                                CalculatedList[i].Rest = calres+"";
                                CalculatedList[i].Active = calcac+"";
                                CalculatedList[i].Play = calpl+"";


                                i = 100; //breaks the loop


                            }else{


                                System.out.println("No Value found for space  " +i);

                            }

                        }

                        if(!matchfound){

                            System.out.println("ERROR NO MATCH FOUND");

                            // no match has been found so loop through the calculated array and add it to the
                            // first empty space.

                            for(Dataobject s : CalculatedList){

                                if(s.Timestamp.equals("Empty")){

                                    s.Timestamp = list.Timestamp;
                                    s.Rest = list.Rest;
                                    s.Play = list.Play;
                                    s.Active = list.Active;
                                    s.Average = list.Average;

                                    System.out.println("The set timestamp is " + s.Timestamp);

                                    System.out.println("Match Added " + list.Timestamp);
                                    NumberofEntries ++;

                                    break;
                                }

                            }


                        }



                    }


                    for(Dataobject g : CalculatedList){


                        if(g.Timestamp != "Empty") {

                            System.out.println("Calculated Avarge is " + g.Average);
                            System.out.println("Calculated Avarge is " + g.Rest);
                            System.out.println("Calculated Avarge is " + g.Active);
                            System.out.println("Calculated Avarge is " + g.Play);
                        }
                    }





                    SelectedEntrie = NumberofEntries-1;


                    //Reorder the array //

                    System.out.println("NUMBERRRRR" +SelectedEntrie);



                    list = new String[4];
                    dashboardListAdapter = new DashboardListAdapter(list, getContext(), UserID, SelectedEntrie, CalculatedList, numberofvalid, currentdate );
                    recyclerView.setAdapter(dashboardListAdapter);
                    recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                    //currentdate.setText(myprefs.getString("timestamp","fail"));


                    Log.d("NUMBERRRRRof", ""+NumberofEntries);

                    if(NumberofEntries <=1){

                        previous.setVisibility(View.INVISIBLE);
                        next.setVisibility(view.INVISIBLE);

                    }else {
                        previous.setVisibility(View.VISIBLE);
                        next.setVisibility(view.INVISIBLE);

                    }


                    ArrayList<PieEntry> pieetries = new ArrayList();
                    ArrayList<String> DATA = new ArrayList();

                    int color1 = Color.parseColor("#6aa4cc");
                    int color2 = Color.parseColor("#78cc6a");
                    int color3 = Color.parseColor("#cc6969");


                    int[] Colors ={color1,color2,color3};



                    DATA.add("Rest");
                    DATA.add("Active");
                    DATA.add("Play");

                    try{

                        pieetries.add(new PieEntry(Float.parseFloat(CalculatedList[SelectedEntrie].Rest),0));
                        pieetries.add(new PieEntry(Float.parseFloat(CalculatedList[SelectedEntrie].Active),1));
                        pieetries.add(new PieEntry(Float.parseFloat(CalculatedList[SelectedEntrie].Play),2));

                        PieDataSet values = new PieDataSet(pieetries , "Activies");
                        values.setColors(Colors);
                        PieData pieData = new PieData(values);
                        pieData.setValueFormatter(new DefaultValueFormatter(0));

                        pieChart.getDescription().setEnabled(false);
                        pieChart.setData(pieData);
                        pieChart.invalidate();


                    }catch (Exception e){



                    }



                    previous.setOnClickListener(new View.OnClickListener() {

                        @Override
                        public void onClick(View view) {

                            if((SelectedEntrie -1) != 0){

                                next.setVisibility(View.VISIBLE);
                                SelectedEntrie = SelectedEntrie-1;
                                dashboardListAdapter = new DashboardListAdapter(list, getContext(), UserID, SelectedEntrie, CalculatedList, numberofvalid, currentdate );
                                recyclerView.setAdapter(dashboardListAdapter);
                               // currentdate.setText(new StringBuilder(myprefs.getString("timestamp","fail").substring(0,10)).toString());
                                System.out.println("Selected Entry" + SelectedEntrie);

                                ArrayList<PieEntry> entrys = new ArrayList();


                                entrys.add(new PieEntry(Float.parseFloat(CalculatedList[SelectedEntrie].Rest),0));
                                entrys.add(new PieEntry(Float.parseFloat(CalculatedList[SelectedEntrie].Active),1));
                                entrys.add(new PieEntry(Float.parseFloat(CalculatedList[SelectedEntrie].Play),2));

                                PieDataSet valuess = new PieDataSet(entrys , "Activies");

                                int color1 = Color.parseColor("#6aa4cc");
                                int color2 = Color.parseColor("#78cc6a");
                                int color3 = Color.parseColor("#cc6969");


                                int[] Colors ={color1,color2,color3};
                                valuess.setColors(Colors);
                                PieData pieDataa = new PieData(valuess);
                                pieDataa.setValueFormatter(new DefaultValueFormatter(0));

                                pieChart.setData(pieDataa);
                                pieChart.notifyDataSetChanged();
                                pieChart.invalidate();
                                pieChart.refreshDrawableState();


                            }else if((SelectedEntrie-1) == 0) {

                                next.setVisibility(View.VISIBLE);
                                SelectedEntrie = SelectedEntrie-1;
                                dashboardListAdapter = new DashboardListAdapter(list, getContext(), UserID, SelectedEntrie,  CalculatedList, numberofvalid ,currentdate);
                                recyclerView.setAdapter(dashboardListAdapter);
                               // currentdate.setText(new StringBuilder(myprefs.getString("timestamp","fail").substring(0,10)).toString());
                                System.out.println("Selected Entry" + SelectedEntrie);
                                previous.setVisibility(View.INVISIBLE);


                                ArrayList<PieEntry> entrys = new ArrayList();


                                entrys.add(new PieEntry(Float.parseFloat(CalculatedList[SelectedEntrie].Rest),0));
                                entrys.add(new PieEntry(Float.parseFloat(CalculatedList[SelectedEntrie].Active),1));
                                entrys.add(new PieEntry(Float.parseFloat(CalculatedList[SelectedEntrie].Play),2));

                                PieDataSet valuess = new PieDataSet(entrys , "Activies");

                                int color1 = Color.parseColor("#6aa4cc");
                                int color2 = Color.parseColor("#78cc6a");
                                int color3 = Color.parseColor("#cc6969");


                                int[] Colors ={color1,color2,color3};
                                valuess.setColors(Colors);


                                PieData pieDataa = new PieData(valuess);
                                pieDataa.setValueFormatter(new DefaultValueFormatter(0));
                                pieChart.setData(pieDataa);
                                pieChart.notifyDataSetChanged();
                                pieChart.invalidate();
                                pieChart.refreshDrawableState();



                            }else{


                                previous.setVisibility(View.INVISIBLE);
                                next.setVisibility(View.VISIBLE);


                                ArrayList<PieEntry> entrys = new ArrayList();


                                entrys.add(new PieEntry(Float.parseFloat(CalculatedList[SelectedEntrie].Rest),0));
                                entrys.add(new PieEntry(Float.parseFloat(CalculatedList[SelectedEntrie].Active),1));
                                entrys.add(new PieEntry(Float.parseFloat(CalculatedList[SelectedEntrie].Play),2));

                                PieDataSet valuess = new PieDataSet(entrys , "Activies");

                                int color1 = Color.parseColor("#6aa4cc");
                                int color2 = Color.parseColor("#78cc6a");
                                int color3 = Color.parseColor("#cc6969");


                                int[] Colors ={color1,color2,color3};
                                valuess.setColors(Colors);
                                PieData pieDataa = new PieData(valuess);
                                pieDataa.setValueFormatter(new DefaultValueFormatter(0));
                                pieChart.setData(pieDataa);
                                pieChart.notifyDataSetChanged();
                                pieChart.invalidate();
                                pieChart.refreshDrawableState();




                            }

                        }

                    });


                    System.out.println("NUMBER OF ENTRYS " +NumberofEntries);

                    next.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            if((SelectedEntrie +1) <= NumberofEntries -1){

                                SelectedEntrie = SelectedEntrie +1;
                                dashboardListAdapter = new DashboardListAdapter(list, getContext(), UserID, SelectedEntrie,  CalculatedList, numberofvalid, currentdate );
                                recyclerView.setAdapter(dashboardListAdapter);
                             //   currentdate.setText(new StringBuilder(myprefs.getString("timestamp","fail").substring(0,10)).toString());
                                previous.setVisibility(View.VISIBLE);
                                System.out.println("Selected Entry" + SelectedEntrie);


                                ArrayList<PieEntry> entrys = new ArrayList();


                                entrys.add(new PieEntry(Float.parseFloat(CalculatedList[SelectedEntrie].Rest),0));
                                entrys.add(new PieEntry(Float.parseFloat(CalculatedList[SelectedEntrie].Active),1));
                                entrys.add(new PieEntry(Float.parseFloat(CalculatedList[SelectedEntrie].Play),2));

                                PieDataSet valuess = new PieDataSet(entrys , "Activies");

                                int color1 = Color.parseColor("#6aa4cc");
                                int color2 = Color.parseColor("#78cc6a");
                                int color3 = Color.parseColor("#cc6969");


                                int[] Colors ={color1,color2,color3};
                                valuess.setColors(Colors);
                                PieData pieDataa = new PieData(valuess);
                                pieDataa.setValueFormatter(new DefaultValueFormatter(0));
                                pieChart.setData(pieDataa);
                                pieChart.notifyDataSetChanged();
                                pieChart.invalidate();
                                pieChart.refreshDrawableState();

                                if((SelectedEntrie +1 )  >= NumberofEntries){

                                    next.setVisibility(View.INVISIBLE);

                                }


                            }
                           // currentdate.setText(new StringBuilder(myprefs.getString("timestamp","fail").substring(0,10)).toString());
                        }
                    });



                    // Call the return method //

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };

        GetData getData = new GetData(Integer.valueOf(UserID), responseListener);
        RequestQueue queue = Volley.newRequestQueue(getContext());
        queue.add(getData);



       System.out.println("Data which has been returned!!!!!!!!!!!!!!!  + " + db.getData("54"));




        return view;

    }


    public void getdoginfo (){

        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonResponse = new JSONObject(response);

                    JSONArray jsonArray = jsonResponse.getJSONArray("server_response");
                    JSONObject object = jsonArray.getJSONObject(0);
                    String dog_name = object.getString("dog_name");
                    // Parse data from the JSONObject and return a string response

                    Log.d("CheckMe", "" +dog_name);
                    DogsName.setText(""+dog_name);


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };

        GetDogInfoRequest getData = new GetDogInfoRequest(Integer.valueOf(UserID), responseListener);
        RequestQueue queue = Volley.newRequestQueue(getContext());
        queue.add(getData);

    }




}
