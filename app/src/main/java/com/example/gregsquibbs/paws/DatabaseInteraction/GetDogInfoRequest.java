package com.example.gregsquibbs.paws.DatabaseInteraction;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by gregsquibbs on 28/02/2018.
 */

public class GetDogInfoRequest extends StringRequest {

    private static final String REQUEST_URL ="http://theweekendmatters.com/GetDogInfoRequest.php";
    private Map<String, String> params;

    public GetDogInfoRequest(int userID, Response.Listener<String> listener) {
        super(Method.POST, REQUEST_URL, listener, null);
        params = new HashMap<>();
        params.put("userID", String.valueOf(userID));

    }

    public Map<String, String> getParams() {
        return params;
    }
}