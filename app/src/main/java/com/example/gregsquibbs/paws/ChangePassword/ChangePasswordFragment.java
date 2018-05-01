package com.example.gregsquibbs.paws.ChangePassword;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.gregsquibbs.paws.IntentConstants;
import com.example.gregsquibbs.paws.R;


public class ChangePasswordFragment extends Fragment {

    private String password;
    private EditText etOldPassword;
    private EditText etNewPassword;
    private EditText etConfirmPassword;



    public ChangePasswordFragment() {
        // Empty constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle bundle = this.getArguments();
        if (bundle != null) {
            password = bundle.getString("password");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.change_password_fragment, container, false);

        etOldPassword = (EditText) view.findViewById(R.id.etOldPassword);
        etNewPassword = (EditText) view.findViewById(R.id.etNewPassword);
        etConfirmPassword = (EditText) view.findViewById(R.id.etConfirmPassword);
        final Button bSave = (Button) view.findViewById(R.id.bSave);

        return view;
    }

    public String getOldPassword() {
        return etOldPassword.getText().toString();
    }

    public String getNewPassword() {
        return etNewPassword.getText().toString();
    }

    public String getConfirmPassword() {
        return etConfirmPassword.getText().toString();
    }

}

