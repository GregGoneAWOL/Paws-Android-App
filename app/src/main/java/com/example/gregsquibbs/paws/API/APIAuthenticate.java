package com.example.gregsquibbs.paws.API;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import com.example.gregsquibbs.paws.R;

/**
 * Created by gregsquibbs on 23/04/2018.
 */

public class APIAuthenticate extends AppCompatActivity {

    private String clientId = "911ce76a438c18142c15065be22aff080b4784c0a35ba728ac7f0ac4036a08e3";
    private String clientSecret ="b43251eb06e1475a5445c225a8839e2a605a5cf0e34a2223ccd3d80587b73ea2";
    private String redirectUri = "futurestudio://callback";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.fragment_container);

        Intent intent = new Intent(
                Intent.ACTION_VIEW,
                Uri.parse("https://app.fitbark.com/oauth/authorize" + "?response_type=code&client_credentials&" + "client_id=" + clientId + "&redirect_uri=" + redirectUri));
                startActivity(intent);

    }



}
