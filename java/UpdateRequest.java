package com.example.swipeex;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class UpdateRequest extends StringRequest {
    final static private String URL = "YOUR_SERVER_DOMAIN/Update.php";
    private Map<String, String> parameters;

    public UpdateRequest(String userPassword, String userDisease_1st, String userDisease_2nd, String userDisease_3rd, String userDisease_4th, String userDisease_5th, String userDisease_6th, String userDisease_7th, String userDisease_8th, String userSex, int userAge, String userID, Response.Listener<String> listener){
        super(Request.Method.POST, URL, listener, null);//Post방식임
        parameters = new HashMap<>();//해쉬맵 생성후 parameters 변수에 값을 넣어줌
        parameters.put("userPassword", userPassword);
        parameters.put("userDisease_1st", userDisease_1st);
        parameters.put("userDisease_2nd", userDisease_2nd);
        parameters.put("userDisease_3rd", userDisease_3rd);
        parameters.put("userDisease_4th", userDisease_4th);
        parameters.put("userDisease_5th", userDisease_5th);
        parameters.put("userDisease_6th", userDisease_6th);
        parameters.put("userDisease_7th", userDisease_7th);
        parameters.put("userDisease_8th", userDisease_8th);
        parameters.put("userSex", userSex);
        parameters.put("userAge", userAge + "");
        parameters.put("userID", userID);

    }

    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        return parameters;
    }
}
