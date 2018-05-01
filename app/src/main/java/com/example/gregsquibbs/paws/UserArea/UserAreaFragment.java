package com.example.gregsquibbs.paws.UserArea;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import com.example.gregsquibbs.paws.ChangePassword.ChangePasswordActivity;
import com.example.gregsquibbs.paws.IntentConstants;
import com.example.gregsquibbs.paws.R;


/**
 * Created by gregsquibbs on 09/02/2018.
 */

public class UserAreaFragment extends Fragment {

    private String userID;
    private String name;
    private String username;
    private String email;
    private String password;
    private TextView tvPassword;

    public UserAreaFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle bundle = this.getArguments();
        if (bundle != null) {
            userID = bundle.getString("userID");
            name = bundle.getString("name");
            username = bundle.getString("username");
            email = bundle.getString("email");
            password = bundle.getString("password");
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.user_area_fragment, container, false);

        // Initializing elements
        final TextView tvName = (TextView) view.findViewById(R.id.tvName);
        final TextView tvUsername = (TextView) view.findViewById(R.id.tvUsername);
        final TextView tvEmail = (TextView) view.findViewById(R.id.tvEmail);
        final Button bEdit = (Button) view.findViewById(R.id.bEdit);




        // Setting text fields with relevant info
        tvName.setText("Name: "+name);
        tvUsername.setText("Username: "+username);
        tvEmail.setText("Email: "+email);
      //  starPassword();

        bEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), ChangePasswordActivity.class);
                intent.putExtra("password", password);
                intent.putExtra("username", username);
                startActivityForResult(intent, IntentConstants.INTENT_REQUEST_CODE);

            }
        });





        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode,resultCode,data);
        if(data.getExtras().containsKey(IntentConstants.INTENT_NEW_PASSWORD)) {
            password = data.getStringExtra(IntentConstants.INTENT_NEW_PASSWORD);
           Toast t = Toast.makeText(getActivity(), "Password Changed", Toast.LENGTH_SHORT);
           t.show();

        }
    }





}
