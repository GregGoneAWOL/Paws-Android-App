package com.example.gregsquibbs.paws.Register.UserInformation;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by gregsquibbs on 11/01/2018.
 */

public class RegisterRequest extends StringRequest {

    private static final String REGISTER_REQUEST_URL ="http://theweekendmatters.com/register.php";
    private Map<String, String> params;

    public RegisterRequest(String name, String username, String email, String password, Response.Listener<String> listener) {
        super(Method.POST, REGISTER_REQUEST_URL, listener, null);
        params = new HashMap<>();
        params.put("name", name);
        params.put("username", username);
        params.put("email", email);
        params.put("password", password);
    }

    public Map<String, String> getParams() {
        return params;
    }
}
