package com.example.gregsquibbs.paws.Register.DogInformation;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;

import com.example.gregsquibbs.paws.R;
import com.example.gregsquibbs.paws.Register.UserInformation.RegisterFragment;
import com.example.gregsquibbs.paws.UserArea.UserAreaFragment;

/**
 * Created by George PC on 20/03/2018.
 */

public class RegisterDogActivity extends AppCompatActivity {

    private String userID;
    private RegisterDogFragment dogFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_container);

        Intent intent = getIntent();
        userID = intent.getStringExtra("userID");


        FragmentManager fm = getSupportFragmentManager();
        Fragment fragment = fm.findFragmentById(R.id.contentFrameContainer);

        if (fragment == null){
            dogFragment = new RegisterDogFragment();
            fm.beginTransaction()
                    .add(R.id.contentFrameContainer, dogFragment)
                    .commit();
        }

        Bundle bundle = new Bundle();
        bundle.putString("userID", userID);
        dogFragment.setArguments(bundle);

    }








}
