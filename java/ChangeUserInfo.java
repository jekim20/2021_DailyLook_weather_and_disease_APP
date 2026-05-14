package com.example.swipeex;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;

public class ChangeUserInfo extends AppCompatActivity {

    String userID, userPass, userDisease_1st, userDisease_2nd, userDisease_3rd, userDisease_4th, userDisease_5th, userDisease_6th, userDisease_7th, userDisease_8th, userSex;
    int userAge;
    String checkPass, checkSex, checkAge;
    int birthYear;
    String pwPattern = "^(?=.*[A-Za-z])(?=.*[0-9])(?=.*[$@^!%*#?&]).{8,15}.$";
    String agePattern = "^\\d{4}\\d{2}\\d{2}$";
    private EditText et_pass, et_pass_confirm, et_age;
    private RadioGroup rg_sex;
    private RadioButton rb_woman, rb_man;
    private Spinner sp_Disease1st, sp_Disease2nd, sp_Disease3rd, sp_Disease4th, sp_Disease5th, sp_Disease6th, sp_Disease7th, sp_Disease8th;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_user_info);

        Intent intent = getIntent();
        userID = intent.getStringExtra("userID");

        et_pass = findViewById(R.id.et_pass);
        et_pass_confirm = findViewById(R.id.et_pass_confirm);
        et_age = findViewById(R.id.et_age);
        rg_sex = findViewById(R.id.rg_sex);
        rb_woman = findViewById(R.id.rb_woman);
        rb_man = findViewById(R.id.rb_man);

        final String[] data = getResources().getStringArray(R.array.disease);

        sp_Disease1st = findViewById(R.id.sp_Disease1st); //질병 1순위
        sp_Disease1st.setAdapter(new SpinnerAdapter(this, android.R.layout.simple_spinner_dropdown_item, data));
        sp_Disease1st.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                ((TextView)adapterView.getChildAt(0)).setTextColor(Color.BLACK);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        sp_Disease2nd = findViewById(R.id.sp_Disease2nd); //질병 2순위
        sp_Disease2nd.setAdapter(new SpinnerAdapter(this, android.R.layout.simple_spinner_dropdown_item, data));
        sp_Disease2nd.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                ((TextView)adapterView.getChildAt(0)).setTextColor(Color.BLACK);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        sp_Disease3rd = findViewById(R.id.sp_Disease3rd); //질병 3순위
        sp_Disease3rd.setAdapter(new SpinnerAdapter(this, android.R.layout.simple_spinner_dropdown_item, data));
        sp_Disease3rd.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                ((TextView)adapterView.getChildAt(0)).setTextColor(Color.BLACK);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        sp_Disease4th = findViewById(R.id.sp_Disease4th); //질병 4순위
        sp_Disease4th.setAdapter(new SpinnerAdapter(this, android.R.layout.simple_spinner_dropdown_item, data));
        sp_Disease4th.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                ((TextView)adapterView.getChildAt(0)).setTextColor(Color.BLACK);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        sp_Disease5th = findViewById(R.id.sp_Disease5th); //질병 5순위
        sp_Disease5th.setAdapter(new SpinnerAdapter(this, android.R.layout.simple_spinner_dropdown_item, data));
        sp_Disease5th.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                ((TextView)adapterView.getChildAt(0)).setTextColor(Color.BLACK);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        sp_Disease6th = findViewById(R.id.sp_Disease6th); //질병 6순위
        sp_Disease6th.setAdapter(new SpinnerAdapter(this, android.R.layout.simple_spinner_dropdown_item, data));
        sp_Disease6th.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                ((TextView)adapterView.getChildAt(0)).setTextColor(Color.BLACK);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        sp_Disease7th = findViewById(R.id.sp_Disease7th); //질병 7순위
        sp_Disease7th.setAdapter(new SpinnerAdapter(this, android.R.layout.simple_spinner_dropdown_item, data));
        sp_Disease7th.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                ((TextView)adapterView.getChildAt(0)).setTextColor(Color.BLACK);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        sp_Disease8th = findViewById(R.id.sp_Disease8th); //질병 8순위
        sp_Disease8th.setAdapter(new SpinnerAdapter(this, android.R.layout.simple_spinner_dropdown_item, data));
        sp_Disease8th.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                ((TextView)adapterView.getChildAt(0)).setTextColor(Color.BLACK);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        et_pass.addTextChangedListener(new TextWatcher() { //비밀번호 입력
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                checkPass = et_pass.getText().toString();
                if(checkPass.matches(pwPattern)) { //입력한 비밀번호 유효성 검사
                    et_pass.setError(null);
                } else {
                    et_pass.setError("비밀번호는 영문자, 숫자, 특수문자($@^!%*#?&) 조합으로 8~20자리를 사용해야 합니다.");
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        et_pass_confirm.addTextChangedListener(new TextWatcher() { //바꾼 비밀번호를 다시 입력했을 때 일치하지 않을 때
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(et_pass.getText().toString().equals(et_pass_confirm.getText().toString())) {
                    et_pass_confirm.setError(null);
                } else {
                    et_pass_confirm.setError("비밀번호가 일치하지 않습니다.");
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        rg_sex.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() { //성별 선택
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                switch (checkedId) {
                    //"여성"을 선택했을때
                    case R.id.rb_woman:
                        //checkSex에 "여성"을 넣음
                        checkSex = rb_woman.getText().toString();
                        break;

                    //"남성"을 선택했을때
                    case R.id.rb_man:
                        //checkSex에 "남성"을 넣음
                        checkSex = rb_man.getText().toString();
                        break;

                }
            }
        });

        et_age.addTextChangedListener(new TextWatcher() { //나이 입력
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                checkAge = et_age.getText().toString();
                if(checkAge.matches(agePattern)) {
                    et_age.setError(null);
                } else {
                    et_age.setError("생년월일은 8글자를 입력해주세요.\n예)19991019");
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        Button btn_confirm = findViewById(R.id.btn_confirm); //개인정보 변경 확인 버튼
        btn_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder builder = new AlertDialog.Builder(ChangeUserInfo.this);
                builder.setTitle("개인정보 변경").setMessage("개인정보를 변경 하시겠습니까?")
                        .setPositiveButton("변경", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                userPass = et_pass.getText().toString();
                                userSex = checkSex;

                                userDisease_1st = sp_Disease1st.getSelectedItem().toString();
                                userDisease_2nd = sp_Disease2nd.getSelectedItem().toString();
                                userDisease_3rd = sp_Disease3rd.getSelectedItem().toString();
                                userDisease_4th = sp_Disease4th.getSelectedItem().toString();
                                userDisease_5th = sp_Disease5th.getSelectedItem().toString();
                                userDisease_6th = sp_Disease6th.getSelectedItem().toString();
                                userDisease_7th = sp_Disease7th.getSelectedItem().toString();
                                userDisease_8th = sp_Disease8th.getSelectedItem().toString();

                                //우선순위가 높은 것을 먼저 선택 안 할 경우 오류 발생
                                if (userDisease_1st.equals("없음")&&userDisease_2nd.equals("없음")&&userDisease_3rd.equals("없음")&&
                                        userDisease_4th.equals("없음")&&userDisease_5th.equals("없음")&&userDisease_6th.equals("없음")&&
                                        userDisease_7th.equals("없음")&&userDisease_8th.equals("없음")){
                                    btn_confirm.setEnabled(true);
                                }
                                else if (!userDisease_1st.equals("없음")&&userDisease_2nd.equals("없음")&&userDisease_3rd.equals("없음")&&
                                        userDisease_4th.equals("없음")&&userDisease_5th.equals("없음")&&userDisease_6th.equals("없음")&&
                                        userDisease_7th.equals("없음")&&userDisease_8th.equals("없음")){
                                    btn_confirm.setEnabled(true);
                                }
                                else if (!userDisease_1st.equals("없음")&&!userDisease_2nd.equals("없음")&&userDisease_3rd.equals("없음")&&
                                        userDisease_4th.equals("없음")&&userDisease_5th.equals("없음")&&userDisease_6th.equals("없음")&&
                                        userDisease_7th.equals("없음")&&userDisease_8th.equals("없음")){
                                    btn_confirm.setEnabled(true);
                                }
                                else if (!userDisease_1st.equals("없음")&&!userDisease_2nd.equals("없음")&&!userDisease_3rd.equals("없음")&&
                                        userDisease_4th.equals("없음")&&userDisease_5th.equals("없음")&&userDisease_6th.equals("없음")&&
                                        userDisease_7th.equals("없음")&&userDisease_8th.equals("없음")){
                                    btn_confirm.setEnabled(true);
                                }
                                else if (!userDisease_1st.equals("없음")&&!userDisease_2nd.equals("없음")&&!userDisease_3rd.equals("없음")&&
                                        !userDisease_4th.equals("없음")&&userDisease_5th.equals("없음")&&userDisease_6th.equals("없음")&&
                                        userDisease_7th.equals("없음")&&userDisease_8th.equals("없음")){
                                    btn_confirm.setEnabled(true);
                                }
                                else if (!userDisease_1st.equals("없음")&&!userDisease_2nd.equals("없음")&&!userDisease_3rd.equals("없음")&&
                                        !userDisease_4th.equals("없음")&&!userDisease_5th.equals("없음")&&userDisease_6th.equals("없음")&&
                                        userDisease_7th.equals("없음")&&userDisease_8th.equals("없음")){
                                    btn_confirm.setEnabled(true);
                                }
                                else if (!userDisease_1st.equals("없음")&&!userDisease_2nd.equals("없음")&&!userDisease_3rd.equals("없음")&&
                                        !userDisease_4th.equals("없음")&&!userDisease_5th.equals("없음")&&!userDisease_6th.equals("없음")&&
                                        userDisease_7th.equals("없음")&&userDisease_8th.equals("없음")){
                                    btn_confirm.setEnabled(true);
                                }
                                else if (!userDisease_1st.equals("없음")&&!userDisease_2nd.equals("없음")&&!userDisease_3rd.equals("없음")&&
                                        !userDisease_4th.equals("없음")&&!userDisease_5th.equals("없음")&&!userDisease_6th.equals("없음")&&
                                        !userDisease_7th.equals("없음")&&userDisease_8th.equals("없음")){
                                    btn_confirm.setEnabled(true);
                                }
                                else if (!userDisease_1st.equals("없음")&&!userDisease_2nd.equals("없음")&&!userDisease_3rd.equals("없음")&&
                                        !userDisease_4th.equals("없음")&&!userDisease_5th.equals("없음")&&!userDisease_6th.equals("없음")&&
                                        !userDisease_7th.equals("없음")&&!userDisease_8th.equals("없음")){
                                    btn_confirm.setEnabled(true);
                                }
                                else {
                                    Toast.makeText(getApplicationContext(), "질병의 순위를 맞게 선택해주세요.", Toast.LENGTH_SHORT).show();
                                    return;
                                }

                                //입력한 생년월일을 이용하여 나이 구하기
                                //1. 현재 시간을 가져옴
                                long now = System.currentTimeMillis();
                                //2. Date형식으로 고침
                                Date mDate = new Date(now);
                                //3. 현재 날짜 중 연도만 가져 옴
                                SimpleDateFormat getYear = new SimpleDateFormat("yyyy");
                                String currentYear = getYear.format(mDate);
                                //4. 입력한 생년월일 중 연도만 가져옴
                                birthYear = Integer.parseInt(et_age.getText().toString().substring(0, 4));
                                //현재 연도 - 입력한 연도 + 1을 하여 나이 구함
                                userAge = (Integer.parseInt(currentYear) - birthYear) + 1;

                                Response.Listener<String> responseListener = new Response.Listener<String>() {
                                    @Override
                                    public void onResponse(String response) {
                                        try {
                                            JSONObject jsonObject = new JSONObject(response);
                                            boolean success = jsonObject.getBoolean("success");
                                            if(success) {
                                                Toast.makeText(getApplicationContext(), "개인정보 변경에 성공하였습니다", Toast.LENGTH_SHORT).show();
                                                Intent intent = new Intent(ChangeUserInfo.this, SettingActivity.class);
                                                intent.putExtra("userID", userID);
                                                startActivity(intent);

                                            } else {
                                                Toast.makeText(getApplicationContext(), "개인정보 변경에 실패하였습니다.", Toast.LENGTH_SHORT).show();
                                                return;
                                            }
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                };
                                //서버로 Volley를 이용해서 요청을 함
                                UpdateRequest updateRequest = new UpdateRequest(userPass, userDisease_1st, userDisease_2nd, userDisease_3rd, userDisease_4th, userDisease_5th, userDisease_6th, userDisease_7th, userDisease_8th, userSex, userAge, userID, responseListener);
                                RequestQueue queue = Volley.newRequestQueue(ChangeUserInfo.this);
                                queue.add(updateRequest);
                            }
                        })
                        .setNegativeButton("취소", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        })
                        .show();
            }
        });

        ImageButton btn_back = findViewById(R.id.btn_back); //뒤로가기 버튼
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ChangeUserInfo.this, SettingActivity.class);
                intent.putExtra("userID", userID);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                overridePendingTransition(0, 0);
                finish();
            }
        });
    }

    public class SpinnerAdapter extends ArrayAdapter<String> {
        Context context;
        String[] items = new String[]{};

        public  SpinnerAdapter(final Context context, final  int textViewResourceId, final String[] objects) {
            super(context, textViewResourceId, objects);
            this.items = objects;
            this.context = context;

        }

        @Override
        public  View getDropDownView(int position, View convertView, ViewGroup parent) {
            if(convertView == null) {
                LayoutInflater inflater = LayoutInflater.from(context);
                convertView = inflater.inflate(
                        android.R.layout.simple_spinner_dropdown_item, parent, false);
            }
            TextView tv = (TextView) convertView.findViewById(android.R.id.text1);
            tv.setText(items[position]);
            tv.setBackgroundColor(Color.WHITE);
            tv.setTextColor(Color.BLACK);
            tv.setTextSize(14);
            return convertView;
        }
    }

}