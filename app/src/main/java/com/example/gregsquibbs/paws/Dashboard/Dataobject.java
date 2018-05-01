package com.example.gregsquibbs.paws.Dashboard;

public class Dataobject {

    public String Average;
    public String Play;
    public String Active;
    public String Rest;
    public String Timestamp;


    public Dataobject() {

        Timestamp = "Empty";


    }

    public void Setvalues (String average, String play, String active, String rest, String timestamp) {

        Average = average;
        Play = play;
        Active = active;
        Rest =rest;
        Timestamp = timestamp;

    }


}
