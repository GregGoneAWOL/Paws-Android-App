package com.example.gregsquibbs.paws.Register.DogInformation;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.example.gregsquibbs.paws.DatabaseInteraction.DatabasePOJO;
import com.example.gregsquibbs.paws.Login.LoginActivity;
import com.example.gregsquibbs.paws.R;
import com.example.gregsquibbs.paws.Register.UserInformation.RegisterActivity;
import com.example.gregsquibbs.paws.Register.UserInformation.RegisterRequest;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by George PC on 20/03/2018.
 */

public class RegisterDogFragment extends Fragment {

    private String userID;

    public RegisterDogFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle bundle = this.getArguments();
        if (bundle != null) {
            userID = bundle.getString("userID");
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.registerdog_fragment, container, false);

        // Initializing elements
        final EditText etDogName = (EditText) view.findViewById(R.id.etDogName);
        final EditText etDogAge = (EditText) view.findViewById(R.id.etDogsAge);
        final EditText etDogWeight = (EditText) view.findViewById(R.id.etDogsWeight);
        final Button bNext = (Button) view.findViewById(R.id.bNext);

        bNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String name = etDogName.getText().toString();
                final String age = etDogAge.getText().toString();
                final String weight = etDogWeight.getText().toString();

                // The dogs name should not change so save it to user pefrences to reduce network use //

                final SharedPreferences myprefs = getContext().getSharedPreferences("PawsPrefs", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = myprefs.edit();
                editor.putString("Dogs_Name", name);
                editor.commit();


                DatabasePOJO db = new DatabasePOJO(getContext());
                db.registerDogInfo(userID,name,age,weight);
                Intent intent = new Intent(getContext(), LoginActivity.class);
                startActivity(intent);
            }
        });


        return view;



    }
}
