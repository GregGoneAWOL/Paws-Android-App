package com.example.gregsquibbs.paws.Dashboard;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;



import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.gregsquibbs.paws.API.APIAuthenticate;
import com.example.gregsquibbs.paws.DatabaseInteraction.DatabasePOJO;
import com.example.gregsquibbs.paws.R;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by George PC on 06/03/2018.
 */

public class DashboardActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    private DashboardFragment dashboardFragment;

    private String clientId = "911ce76a438c18142c15065be22aff080b4784c0a35ba728ac7f0ac4036a08e3";
    private String clientSecret ="b43251eb06e1475a5445c225a8839e2a605a5cf0e34a2223ccd3d80587b73ea2";
    private String redirectUri = "futurestudio://callback";
    private String authCode = "authorization_code";

    String slug = "" ;
    String houravarage = "";
    String min_play = "";
    String min_active = "";
    String min_rest = "";

    // Fragment Manager will be used later on in the class to change the fragment when the item is clicked.
    FragmentManager fm;

    String UserID;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final SharedPreferences myprefs = getSharedPreferences("PawsPrefs", Context.MODE_PRIVATE);
        UserID = myprefs.getString("userID","Error");
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        fm = getSupportFragmentManager();
        Fragment fragment = fm.findFragmentById(R.id.fragment_framelayout);

        if (fragment == null){

            dashboardFragment = new DashboardFragment();

            fm.beginTransaction()
                    .add(R.id.fragment_framelayout, dashboardFragment)
                    .commit();
        }



        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        setTitle("Dashboard");
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);

        // set the navigation to have the dashboard selected as default
        navigationView.getMenu().getItem(0).setChecked(true);
        navigationView.setNavigationItemSelectedListener(this);

    }

    @Override
    protected void onResume() {
        super.onResume();

        Uri uri = getIntent().getData();

        if (uri != null && uri.toString().startsWith(redirectUri)) {
            String code = uri.getQueryParameter("code");
            String URL = "https://app.fitbark.com/oauth/token";
            JSONObject object = new JSONObject();
            try {
                object.put("client_id", clientId);
                object.put("client_secret", clientSecret);
                object.put("grant_type", authCode);
                object.put("redirect_uri", redirectUri);
                object.put("code", code);

            } catch (JSONException e) {
                e.printStackTrace();
            }

            final RequestQueue requestQueue = Volley.newRequestQueue(this);
            JsonObjectRequest objectRequest = new JsonObjectRequest(
                    Request.Method.POST,
                    URL,
                    object,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {

                            System.out.println("SUCCESS (TOKEN) - " +response.toString());
                            //PARSE access_token

                            try {
                                String Accesstoken = response.getString("access_token");
                                System.out.println("Access token " + Accesstoken);
                                getRelatedDogs(Accesstoken);

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }


                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            System.out.println("ERROR - " + error.toString());
                        }
                    }

            );

            requestQueue.add(objectRequest);
        }

    }

    public void getRelatedDogs(String token) {

        final String accessToken = token;
        String url = "https://app.fitbark.com/api/v2/dog_relations";

        final RequestQueue requestQueue = Volley.newRequestQueue(this);
        JsonObjectRequest req = new JsonObjectRequest(
                Request.Method.GET,
                url,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {



                        try {
                            final SharedPreferences myprefs = getSharedPreferences("PawsPrefs", Context.MODE_PRIVATE);
                            final String UserIDd = myprefs.getString("userID","Error");

                            String string = response.toString();
                            Log.d("DOG RETURN", "onResponse: " + string);
                            String cut = string.substring(88, (string.length() - 3));
                            JSONObject object = new JSONObject(cut);

                            slug = object.getString("slug");
                            houravarage = object.getString("hourly_average");
                            min_play = object.getString("min_play");
                            min_active = object.getString("min_active");
                            min_rest= object.getString("min_rest");


                            Log.d("data1", "onResponse: " + houravarage);
                            Log.d("data1", "onResponse: " + min_play);
                            Log.d("data1", "onResponse: " + min_active);
                            Log.d("data1", "onResponse: " + min_rest);



                            AlertDialog dialog = new AlertDialog.Builder(DashboardActivity.this).create();
                            dialog.setTitle("Fitbark Data Data Synced!");
                            dialog.setMessage( "Average: "+ houravarage + " Play: " +min_play + " Active: "+ min_active + " Rest: " + min_rest);
                            dialog.setButton(AlertDialog.BUTTON_NEUTRAL, "Ok", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    setTitle("DashBoard");
                                    DatabasePOJO databasePOJO = new DatabasePOJO(getApplicationContext());
                                    databasePOJO.setData(Integer.parseInt(UserIDd),Integer.parseInt(houravarage),Integer.parseInt(min_play),Integer.parseInt(min_active), Integer.parseInt(min_rest));
                                    fm.beginTransaction().replace(R.id.fragment_framelayout, new DashboardFragment()).commit();
                                }
                            });

                            dialog.show();





                        } catch (JSONException e) {

                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println("FAILED - CODE 155");
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Authorization", "Bearer " +accessToken);
                headers.put("Cache-Control", "no-cache");
                //headers.put("Postman-Token", postman);
                return headers;
            }
        };
        requestQueue.add(req);

    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {

            setTitle("DashBoard");
            fm.beginTransaction().replace(R.id.fragment_framelayout, new DashboardFragment()).commit();

        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.Home) {
            // Handle the camera action
            setTitle("DashBoard");
            fm.beginTransaction().replace(R.id.fragment_framelayout, new DashboardFragment()).commit();

        } else if(id == R.id.Exercise) {
            setTitle("Active");
            fm.beginTransaction().replace(R.id.fragment_framelayout, new ExerciseFragment()).commit();



        } else if(id == R.id.Rest){

            setTitle("Rest");
            fm.beginTransaction().replace(R.id.fragment_framelayout, new RestFragment()).commit();


        }else if(id == R.id.Play){

            setTitle("Play");
            fm.beginTransaction().replace(R.id.fragment_framelayout, new PlayFragment()).commit();


        }
        else if(id == R.id.Average){

            setTitle("Play");
            fm.beginTransaction().replace(R.id.fragment_framelayout, new AverageFragment()).commit();


        } else if(id == R.id.Sync){

            setTitle("Sync");


            Intent intent = new Intent(this, APIAuthenticate.class);
            startActivity(intent);
            finish();

        }else{


            setTitle("Settings");
            fm.beginTransaction().replace(R.id.fragment_framelayout, new SettingsActivity()).commit();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

}
