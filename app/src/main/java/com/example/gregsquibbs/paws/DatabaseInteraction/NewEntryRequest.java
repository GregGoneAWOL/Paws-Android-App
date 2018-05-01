package com.example.gregsquibbs.paws.DatabaseInteraction;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by gregsquibbs on 27/02/2018.
 */

public class NewEntryRequest extends StringRequest {

    private static final String REQUEST_URL ="http://theweekendmatters.com/NewEntry.php";
    private Map<String, String> params;

    public NewEntryRequest(int userID, int hourly_average, int min_play, int min_active, int min_rest, Response.Listener<String> listener) {
        super(Method.POST, REQUEST_URL, listener, null);
        params = new HashMap<>();
        params.put("userID", String.valueOf(userID));
        params.put("hourly_average", String.valueOf(hourly_average));
        params.put("min_play", String.valueOf(min_play));
        params.put("min_active", String.valueOf(min_active));
        params.put("min_rest", String.valueOf(min_rest));

    }

    public Map<String, String> getParams() {
        return params;
    }
}
