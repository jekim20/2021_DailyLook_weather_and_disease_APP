package com.example.swipeex;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class RegisterRequest extends StringRequest {

    //서버 URL 설정 (PHP 파일 연동)
    final static private String URL = "YOUR_SERVER_DOMAIN/Register.php";
    private Map<String, String> map;

    public RegisterRequest(String userID, String userPassword, String userDisease_1st, String userDisease_2nd, String userDisease_3rd, String userDisease_4th, String userDisease_5th, String userDisease_6th, String userDisease_7th, String userDisease_8th, String userSex, int userAge, Response.Listener<String> listener) {
        super(Method.POST, URL, listener, null);

        map = new HashMap<>();
        map.put("userID", userID);
        map.put("userPassword", userPassword);
        map.put("userDisease_1st", userDisease_1st);
        map.put("userDisease_2nd", userDisease_2nd);
        map.put("userDisease_3rd", userDisease_3rd);
        map.put("userDisease_4th", userDisease_4th);
        map.put("userDisease_5th", userDisease_5th);
        map.put("userDisease_6th", userDisease_6th);
        map.put("userDisease_7th", userDisease_7th);
        map.put("userDisease_8th", userDisease_8th);
        map.put("userSex", userSex);
        map.put("userAge", userAge + "");
    }

    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        return map;
    }
}
