package com.example.gregsquibbs.paws.ChangePassword;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.example.gregsquibbs.paws.IntentConstants;
import com.example.gregsquibbs.paws.R;
import com.example.gregsquibbs.paws.Register.ChangePasswordRequest;

import org.json.JSONException;
import org.json.JSONObject;

public class ChangePasswordActivity extends AppCompatActivity {

    // @@@@@@@@@@@@ THIS CLASS IS COUPLED TO THE FRAGMENT (SCROLL DOWN) @@@@@@@@@@@@

    private ChangePasswordFragment changePassFragment;
    private String password;
    private String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_container);

        FragmentManager fm = getSupportFragmentManager();
        Fragment fragment = fm.findFragmentById(R.id.contentFrameContainer);

        if (fragment == null){
            changePassFragment = new ChangePasswordFragment();
            fm.beginTransaction()
                    .add(R.id.contentFrameContainer, changePassFragment)
                    .commit();
        }

        Intent intent = getIntent();
        password = intent.getStringExtra("password");
        username = intent.getStringExtra("username");

        Bundle bundle = new Bundle();
        bundle.putString("password", password);
        changePassFragment.setArguments(bundle);

    }

    public void saveButtonClicked(View v) {

        // I HAVE COUPLED THE FRAGMENT HERE BY CALLING GETOLD/GETNEW/GETCONFIRM DIRECTLY TO JUST QUICKLY GET THE DATA
        // IT OBVIOUSLY NEEDS TO BE REPLACED WITH BEST PRACTISE - INTERFACES

        final String OldPassword = changePassFragment.getOldPassword();
        final String NewPassword = changePassFragment.getNewPassword();
        final String ConfirmNewPassword = changePassFragment.getConfirmPassword();


        if (password.equals(OldPassword) && NewPassword.equals(ConfirmNewPassword)) {
            //NEED TO UPDATE PASSWORD IN DB HERE

            Response.Listener<String> responseListener = new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {
                        JSONObject jsonResponse = new JSONObject(response);
                        boolean success = jsonResponse.getBoolean("success");

                        if (success) {
                            Intent intent = new Intent();
                            intent.putExtra(IntentConstants.INTENT_NEW_PASSWORD, NewPassword);
                            setResult(IntentConstants.INTENT_RESULT_CODE, intent);
                            finish();
                        } else {
                            AlertDialog.Builder builder = new AlertDialog.Builder(ChangePasswordActivity.this);
                            builder.setMessage("Password change was unsuccessful")
                                    .setNegativeButton("Retry", null)
                                    .create()
                                    .show();
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            };

            ChangePasswordRequest changePasswordRequest = new ChangePasswordRequest(username, NewPassword, responseListener);
            RequestQueue queue = Volley.newRequestQueue(ChangePasswordActivity.this);
            queue.add(changePasswordRequest);

        } else if(!(password.equals(OldPassword))) {
            AlertDialog.Builder builder = new AlertDialog.Builder(ChangePasswordActivity.this);
            builder.setMessage("Old password is incorrect")
                    .setNegativeButton("Retry", null)
                    .create()
                    .show();
        } else if(!(NewPassword.equals(ConfirmNewPassword))) {
            AlertDialog.Builder builder = new AlertDialog.Builder(ChangePasswordActivity.this);
            builder.setMessage("New passwords do not match")
                    .setNegativeButton("Retry", null)
                    .create()
                    .show();
        } else {
            AlertDialog.Builder builder = new AlertDialog.Builder(ChangePasswordActivity.this);
            builder.setMessage("Error")
                    .setNegativeButton("Retry", null)
                    .create()
                    .show();
        }
    }

}

