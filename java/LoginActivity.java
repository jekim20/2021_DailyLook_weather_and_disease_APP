package com.example.swipeex;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class LoginActivity extends AppCompatActivity {
    private EditText et_id, et_pass;
    private Button btn_login, btn_register;
    String userID, userPass;
    String loginId, loginPwd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        et_id = findViewById(R.id.et_id);
        et_pass = findViewById(R.id.et_pass);
        btn_login = findViewById(R.id.btn_login);
        btn_register = findViewById(R.id.btn_register);

        SharedPreferences auto = getSharedPreferences("auto", Activity.MODE_PRIVATE);
        loginId = auto.getString("inputId", null);
        // loginPwd = auto.getString("inputPwd", null);

        if(loginId != null) {
            Toast.makeText(LoginActivity.this, loginId + "님 자동로그인 되었습니다.", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            intent.putExtra("userID", loginId);
            startActivity(intent);
            finish();
        }
        else if(loginId == null && loginPwd == null) {
            //로그인 버튼을 클릭시 수행
            btn_login.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //EditText에 현재 입력되어있는 값을 get(가져온다)해온다
                    String userID = et_id.getText().toString();
                    String userPass = et_pass.getText().toString();

                    Response.Listener<String> responseListener = new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                boolean success = jsonObject.getBoolean("success");
                                if(success) { //로그인에 성공한 경우

                                     SharedPreferences auto = getSharedPreferences("auto", Activity.MODE_PRIVATE);
                                    SharedPreferences.Editor autoLogin = auto.edit();
                                    autoLogin.putString("inputId", et_id.getText().toString());
                                    // autoLogin.putString("inputPwd", et_pass.getText().toString());
                                    autoLogin.commit();

                                    String userID = jsonObject.getString("userID");

                                    Toast.makeText(getApplicationContext(), "로그인에 성공하였습니다", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                    //로그인에 성공하면 로그인을 할 때 사용한 id와 password를 메인 액티비티에 보내줌
                                   intent.putExtra("userID", userID);
                                   // intent.putExtra("userPass", userPass);


                                    startActivity(intent);
                                    finish();


                                } else { //로그인에 실패한 경우
                                    Toast.makeText(getApplicationContext(), "로그인에 실패하였습니다", Toast.LENGTH_SHORT).show();
                                    return;
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    };
                    LoginRequest loginRequest = new LoginRequest(userID, userPass, responseListener);
                    RequestQueue queue = Volley.newRequestQueue(LoginActivity.this);
                    queue.add(loginRequest);
                }
            });
        }

        SharedPreferences setting = getSharedPreferences("setting", 0);
        SharedPreferences.Editor editor = setting.edit();

        //자동 로그인
        if(setting.getBoolean("Auto_Login_enabled", false)) {
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            intent.putExtra("userID", userID);
            intent.putExtra("userPass", userPass);
            //cb_autoLogin.setChecked(true);
            startActivity(intent);
        }

        //회원가입 버튼을 클릭시 수행
        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });

    }
}