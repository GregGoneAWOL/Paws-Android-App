package com.example.gregsquibbs.paws.DatabaseInteraction;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.util.Log;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.example.gregsquibbs.paws.Dashboard.DashboardActivity;
import com.example.gregsquibbs.paws.Login.LoginActivity;
import com.example.gregsquibbs.paws.Register.DogInformation.RegisterDogActivity;
import com.example.gregsquibbs.paws.Register.UserInformation.RegisterActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by gregsquibbs on 21/03/2018.
 */

public class DatabasePOJO {

    Context context;
    String s;
    public DatabasePOJO (Context context) {
        this.context = context;
    }

    public String getData(String userID) {



        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {

                    int NumberofEntries;

                    JSONObject jsonResponse = new JSONObject(response);

                    JSONArray jsonArray = jsonResponse.getJSONArray("server_response");

                    // The jason object returns all entries so return the first only by entering 0 //


                    JSONObject jsonObject = jsonArray.getJSONObject(0);


                    // Use this line to get a specific value from the array
                    s = (String) jsonObject.get("user_id");
                    System.out.println("Value of S = " +s);

                    // Call the return method //




                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };

        GetData getData = new GetData(Integer.valueOf(userID), responseListener);
        RequestQueue queue = Volley.newRequestQueue(context);
        queue.add(getData);

        return s;

    }


    public void setData(int userID, int hourly_average, int min_play, int min_active, int min_rest) {
        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonResponse = new JSONObject(response);
                    boolean success = jsonResponse.getBoolean("success");

                    if (success) {
                        System.out.println("Successful");
                    } else {
                        System.out.println("Unsuccessful");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };

        NewEntryRequest newEntryRequest = new NewEntryRequest(userID, hourly_average, min_play, min_active, min_rest, responseListener);
        RequestQueue queue = Volley.newRequestQueue(context);
        queue.add(newEntryRequest);

    }

    public void registerDogInfo(String userID, String dog_name, String dog_age, String dog_weight) {

        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonResponse = new JSONObject(response);
                    boolean success = jsonResponse.getBoolean("success");
                    if (success) {
                        System.out.println("Success");

                    } else {
                        System.out.println("Failed");

                        AlertDialog.Builder builder = new AlertDialog.Builder(context);
                        builder.setMessage("Register was unsuccessful")
                                .setNegativeButton("Retry", null)
                                .create()
                                .show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };

        RegisterDogInfoRequest registerRequest = new RegisterDogInfoRequest(userID, dog_name, dog_age, dog_weight, responseListener);
        RequestQueue queue = Volley.newRequestQueue(context);
        queue.add(registerRequest);

    }



    public void getDogInfo(String userID) {

        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonResponse = new JSONObject(response);

                    // Parse data from the JSONObject and return a string response





                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };

        GetDogInfoRequest getData = new GetDogInfoRequest(Integer.valueOf(userID), responseListener);
        RequestQueue queue = Volley.newRequestQueue(context);
        queue.add(getData);

    }

}




