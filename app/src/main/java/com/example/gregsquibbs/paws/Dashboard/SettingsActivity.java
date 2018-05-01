package com.example.gregsquibbs.paws.Dashboard;


import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.gregsquibbs.paws.Login.LoginActivity;
import com.example.gregsquibbs.paws.R;
import com.example.gregsquibbs.paws.UserArea.UserAreaActivity;

public class SettingsActivity  extends Fragment {


    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        // Inflate the view to which contains the dashboard_fragement layout then return the view *******George LA

        View view = inflater.inflate(R.layout.settings_fragment, container, false);

        Button logout = (Button) view.findViewById(R.id.logout);
        Button about = (Button) view.findViewById(R.id.about);
        Button changepass = (Button) view.findViewById(R.id.usersettings);


        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getContext(), LoginActivity.class);
                startActivity(i);
                getActivity().finish();
            }
        });

        changepass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getContext(), UserAreaActivity.class);
                startActivity(i);

            }
        });

        about.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AlertDialog dialog = new AlertDialog.Builder(getContext()).create();
                dialog.setTitle("About");
                dialog.setMessage("App Built by Dream Team at Leeds Beckett University" +
                        " Incorporating MPAndroidChat Volley and Fitbark Api");
                dialog.setButton(AlertDialog.BUTTON_NEUTRAL, "Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });

                dialog.show();

            }
        });



        return view;
    }






    }
