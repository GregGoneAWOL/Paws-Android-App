package com.example.gregsquibbs.paws.Register.UserInformation;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.gregsquibbs.paws.R;

public class RegisterActivity extends AppCompatActivity {

    private RegisterFragment registerFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_container);

        FragmentManager fm = getSupportFragmentManager();
        Fragment fragment = fm.findFragmentById(R.id.contentFrameContainer);

        if (fragment == null){
            registerFragment = new RegisterFragment();
            fm.beginTransaction()
                    .add(R.id.contentFrameContainer, registerFragment)
                    .commit();
        }

    }

}
