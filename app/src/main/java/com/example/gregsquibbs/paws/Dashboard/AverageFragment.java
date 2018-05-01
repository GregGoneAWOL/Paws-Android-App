package com.example.gregsquibbs.paws.Dashboard;


import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.example.gregsquibbs.paws.DatabaseInteraction.GetData;
import com.example.gregsquibbs.paws.R;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class AverageFragment extends Fragment {


    public Toolbar dashboardToolbar;
    Dataobject[] ObjectList;
    Dataobject[] CalculatedList;
    List<BarEntry> activityvalues;
    List<String> daylables;
    Float f = 0f;
    BarChart barChart;
    int NumberofEntries;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        // Inflate the view to which contains the dashboard_fragement layout then return the view *******George LA

        View view = inflater.inflate(R.layout.exercise_fragment, container, false);

        activityvalues = new ArrayList<BarEntry>();
        daylables = new ArrayList<String>();
        barChart = (BarChart) view.findViewById(R.id.exercisebarchart);
        final RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.activity_recyclerview);

        final SharedPreferences myprefs = getActivity().getSharedPreferences("PawsPrefs", Context.MODE_PRIVATE);
        String UserID = myprefs.getString("userID","Error");

        final Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {

                    JSONObject jsonResponse = new JSONObject(response);
                    System.out.println("Responce = " + jsonResponse);
                    JSONArray jsonArray = jsonResponse.getJSONArray("server_response");
                    System.out.println("JSON RESPONCE" + jsonArray);

                    String[] split = jsonArray.toString().split(Pattern.quote("}"));
                    System.out.println("LENGTH " + split.length);


                    ObjectList = new Dataobject[jsonArray.length()];
                    CalculatedList = new Dataobject[30];

                    for (int d = 0; d < CalculatedList.length; d++) {

                        CalculatedList[d] = new Dataobject();

                    }


                    // Sort the JSON Response into entries which were submitted in the same day.

                    for (int i = 0; i < jsonArray.length(); i++) {

                        System.out.println("Count " + i);

                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        String Play = (String) jsonObject.get("min_play");
                        String Rest = (String) jsonObject.get("min_rest");
                        String Active = (String) jsonObject.get("min_active");
                        String Average = (String) jsonObject.get("hourly_average");
                        String Timestamp = (String) jsonObject.get("timestamp").toString().substring(0, 10);

                        ObjectList[i] = new Dataobject();
                        ObjectList[i].Setvalues(Average, Play, Active, Rest, Timestamp);


                    }


                    // Sort out the  Objects array by looping through and if the timestamp is not in the new array add it
                    // else add the data from that object to the one contained in the array.


                    for (Dataobject list : ObjectList) {

                        Boolean matchfound = false;

                        for (int i = 0; i < CalculatedList.length; i++) {

                            System.out.println("List timestamp is " + list.Timestamp);
                            System.out.println("Calc timestamp is " + CalculatedList[i].Timestamp);

                            if (list.Timestamp.trim().equals(CalculatedList[i].Timestamp.trim())) {

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


                            } else {


                                System.out.println("No Value found for space  " + i);

                            }

                        }

                        if (!matchfound) {

                            System.out.println("ERROR NO MATCH FOUND");

                            // no match has been found so loop through the calculated array and add it to the
                            // first empty space.

                            for (Dataobject s : CalculatedList) {

                                if (s.Timestamp.equals("Empty")) {

                                    s.Timestamp = list.Timestamp;
                                    s.Rest = list.Rest;
                                    s.Play = list.Play;
                                    s.Active = list.Active;
                                    s.Average = list.Average;

                                    System.out.println("The set timestamp is " + s.Timestamp);

                                    System.out.println("Match Added " + list.Timestamp);
                                    NumberofEntries++;

                                    break;
                                }

                            }


                        }


                    }


                    for (Dataobject g : CalculatedList) {


                        if (g.Timestamp != "Empty") {

                            System.out.println("Calculated Avarge is " + g.Average);
                            System.out.println("Calculated Avarge is " + g.Rest);
                            System.out.println("Calculated Avarge is " + g.Active);
                            System.out.println("Calculated Avarge is " + g.Play);
                        }
                    }


                    // Display the chart

                    System.out.println("This has been run!!!" + NumberofEntries);


                    for (int x = 0; x < NumberofEntries; x++) {

                        System.out.println("This has been run!!!");
                        activityvalues.add(new BarEntry(f, Float.parseFloat(CalculatedList[x].Average)));
                        ;
                        f++;
                        daylables.add(CalculatedList[x].Timestamp);

                    }

                    final String[] lables = new String[daylables.size()];

                    int x = 0;
                    for (String e : daylables) {


                        lables[x] = e;
                        x++;

                    }


                    IAxisValueFormatter iAxisValueFormatter = new IAxisValueFormatter() {
                        @Override
                        public String getFormattedValue(float value, AxisBase axis) {
                            return lables[(int) value];
                        }
                    };


                    XAxis axis = barChart.getXAxis();
                    axis.setValueFormatter(iAxisValueFormatter);
                    axis.setLabelCount(lables.length);


                    int color1 = Color.parseColor("#6aa4cc");
                    int color2 = Color.parseColor("#78cc6a");
                    int color3 = Color.parseColor("#cc6969");


                    int[] Colors = {color1, color2, color3};


                    BarDataSet set = new BarDataSet(activityvalues, "Time in minutes");
                    set.setColor(R.color.colorAccent);
                    set.setColors(Colors);
                    BarData barData = new BarData(set);


                    barData.setHighlightEnabled(false);
                    barChart.setData(barData);
                    barChart.setFitBars(true);
                    barChart.invalidate();
                    barChart.getDescription().setEnabled(false);


                    RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
                    recyclerView.setLayoutManager(layoutManager);

                    GraphListAdapter graphListAdapter = new GraphListAdapter(CalculatedList, NumberofEntries, 3);
                    recyclerView.setAdapter(graphListAdapter);


                    // Call the return method //

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };

        GetData getData = new GetData(Integer.valueOf(UserID), responseListener);
        RequestQueue queue = Volley.newRequestQueue(getContext());
        queue.add(getData);


        return view;

    }













}
