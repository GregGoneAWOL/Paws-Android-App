package com.example.gregsquibbs.paws.Login;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
import com.example.gregsquibbs.paws.R;
import com.example.gregsquibbs.paws.Register.DogInformation.RegisterDogActivity;
import com.example.gregsquibbs.paws.Register.UserInformation.RegisterActivity;
import com.example.gregsquibbs.paws.UserArea.UserAreaActivity;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by gregsquibbs on 09/02/2018.
 */

public class LoginFragment extends Fragment {

    private EditText etUsername;
    private EditText etPassword;

    public LoginFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.login_fragment, container, false);

        // This code is ensuring the password hint font is normal whilst ensuring password dots
        EditText password = (EditText) view.findViewById(R.id.etPassword);
        password.setTypeface(Typeface.DEFAULT);
        password.setTransformationMethod(new PasswordTransformationMethod());

        // Initializing elements
        etUsername = (EditText) view.findViewById(R.id.etUsername);
        etPassword = (EditText) view.findViewById(R.id.etPassword);
        final Button bLogin = (Button) view.findViewById(R.id.bLogin);
        final TextView registerLink = (TextView) view.findViewById(R.id.tvRegister);

        // Switching to register activity when clicked text
        registerLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent registerIntent = new Intent(getContext(), RegisterActivity.class);
                startActivity(registerIntent);

            }
        });

        bLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String username = etUsername.getText().toString();
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


                                Intent intent = new Intent(getContext(), DashboardActivity.class);

                                // Changed to Link to the Dashboard


                                SharedPreferences myprefs = getActivity().getSharedPreferences("PawsPrefs", Context.MODE_PRIVATE);
                                SharedPreferences.Editor editor = myprefs.edit();

                                editor.putString("userID", userID);
                                editor.putString("name", name);
                                editor.putString("username", username);
                                editor.putString("email", email);
                                editor.putString("password", password);
                                editor.commit();


                                startActivity(intent);








                            } else {
                                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                                builder.setMessage("Login was unsuccessful")
                                        .setNegativeButton("Retry", null)
                                        .create()
                                        .show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                };


                LoginRequest loginRequest = new LoginRequest(username, password, responseListener);
                RequestQueue queue = Volley.newRequestQueue(getContext());
                queue.add(loginRequest);
            }
        });
        return view;
    }

}
