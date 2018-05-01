package com.example.gregsquibbs.paws.Register.UserInformation;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.text.method.PasswordTransformationMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.example.gregsquibbs.paws.Dashboard.DashboardActivity;
import com.example.gregsquibbs.paws.Login.LoginActivity;
import com.example.gregsquibbs.paws.R;
import com.example.gregsquibbs.paws.Register.DogInformation.RegisterDogActivity;
import com.example.gregsquibbs.paws.Register.DogInformation.RegisterDogFragment;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by gregsquibbs on 09/02/2018.
 */

public class RegisterFragment extends Fragment {

    private RegisterActivity registerActivity;

    public RegisterFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.register_fragment, container, false);

        // This code is ensuring the password hint font is normal whilst ensuring password dots
        EditText password = (EditText) view.findViewById(R.id.etPassword);
        password.setTypeface(Typeface.DEFAULT);
        password.setTransformationMethod(new PasswordTransformationMethod());

        // Initializing elements
        final EditText etName = (EditText) view.findViewById(R.id.etName);
        final EditText etUsername = (EditText) view.findViewById(R.id.etUsername);
        final EditText etEmail = (EditText) view.findViewById(R.id.etEmail);
        final EditText etPassword = (EditText) view.findViewById(R.id.etPassword);
        final Button bRegister = (Button) view.findViewById(R.id.bRegister);
        final TextView loginLink = (TextView) view.findViewById(R.id.tvLogin);

        // Switching to login activity when clicked text
        loginLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getContext(), LoginActivity.class);
                startActivity(intent);
            }
        });

        bRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String name = etName.getText().toString();
                final String username = etUsername.getText().toString();
                final String email = etEmail.getText().toString();
                final String password = etPassword.getText().toString();

                Response.Listener<String> responseListener = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonResponse = new JSONObject(response);
                            boolean success = jsonResponse.getBoolean("success");

                            if (success) {

                                String userID = jsonResponse.getString("user_id");
                                String name = jsonResponse.getString("name");
                                String username = jsonResponse.getString("username");
                                String email = jsonResponse.getString("email");
                                String password = jsonResponse.getString("password");

                                Intent intent = new Intent(getContext(), RegisterDogActivity.class);
                                intent.putExtra("userID", userID);
                                intent.putExtra("name", name);
                                intent.putExtra("username", username);
                                intent.putExtra("email", email);
                                intent.putExtra("password", password);
                                startActivity(intent);

                            } else {
                                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
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

                RegisterRequest registerRequest = new RegisterRequest(name, username, email, password, responseListener);
                RequestQueue queue = Volley.newRequestQueue(getContext());
                queue.add(registerRequest);


            }
        });



        return view;
    }
}
