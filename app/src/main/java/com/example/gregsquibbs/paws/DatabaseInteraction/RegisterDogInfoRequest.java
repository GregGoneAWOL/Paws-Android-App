package com.example.gregsquibbs.paws.DatabaseInteraction;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by gregsquibbs on 11/01/2018.
 */

public class RegisterDogInfoRequest extends StringRequest {

    private static final String REGISTER_REQUEST_URL ="http://theweekendmatters.com/dogregister.php";
    private Map<String, String> params;

    public RegisterDogInfoRequest(String userID, String name, String age, String weight, Response.Listener<String> listener) {
        super(Method.POST, REGISTER_REQUEST_URL, listener, null);
        params = new HashMap<>();
        params.put("userID", userID);
        params.put("dog_name", name);
        params.put("dog_age", age);
        params.put("dog_weight", weight);
    }

    public Map<String, String> getParams() {
        return params;
    }
}
