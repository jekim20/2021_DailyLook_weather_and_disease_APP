package com.example.swipeex;

import androidx.fragment.app.Fragment;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.LocationManager;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import android.provider.Settings;
import android.util.Log;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import androidx.annotation.NonNull;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import static android.content.Context.LOCATION_SERVICE;


public class SecondFragment extends Fragment {

    private static final String TAG = "SecondFragment";

    int topRandomNum, top2RandomNum, bottomRandomNum, bottomShortRandNum, shoesRandomNum;

    private GpsTracker gpsTracker;

    private static final int GPS_ENABLE_REQUEST_CODE = 2001;
    private static final int PERMISSIONS_REQUEST_CODE = 100;
    String[] REQUIRED_PERMISSIONS = {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION};


    TextView dateView, cityView, weatherView, tempView;
    ImageButton ib_refresh;
    androidx.constraintlayout.widget.ConstraintLayout background;
    ImageView iv_background;

    ImageView iv_sex;

    ImageView iv_top, iv_top2, iv_bottom, iv_hat, iv_shoes, iv_umbrella, iv_parasol, iv_mask;
    String topIcon, top2Icon, bottomIcon, hatIcon, shoesIcon, umbrellaIcon, parasolIcon;


    static RequestQueue requestQueue;

    double latitude, lat;
    double longitude, lng;

    String address;
    String id, disease_1st, disease_2nd, disease_3rd, disease_4th, disease_5th, disease_6th, disease_7th, disease_8th;
    int c;

    int tempDo, data_uvi, data_pop, hourly_pop, data_temp;

    ArrayList<String> diseaseList = new ArrayList<String>();
    ArrayList<String> diseaseList2 = new ArrayList<String>();
    ArrayList<String> diseaseList3 = new ArrayList<String>();
    ArrayList<String> diseaseList4 = new ArrayList<String>();
    ArrayList<String> diseaseList5 = new ArrayList<String>();
    ArrayList<String> diseaseList6 = new ArrayList<String>();
    ArrayList<String> diseaseList7 = new ArrayList<String>();
    ArrayList<String> diseaseList8 = new ArrayList<String>();
    ArrayList<String> diseaseList9 = new ArrayList<String>();

    public static SecondFragment newInstance() {
        SecondFragment fragment = new SecondFragment();
        return fragment;
    }


    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_second, container, false);
        dateView = view.findViewById(R.id.dateView);
        cityView = view.findViewById(R.id.cityView);
        weatherView = view.findViewById(R.id.weatherView);
        tempView = view.findViewById(R.id.tempView);
        ib_refresh = view.findViewById(R.id.ib_updateBtn);
        background = view.findViewById(R.id.background);
        iv_background = view.findViewById(R.id.iv_background);

        iv_sex = view.findViewById(R.id.iv_sex);
        iv_top = view.findViewById(R.id.iv_top);
        iv_top2 = view.findViewById(R.id.iv_top2);
        iv_bottom = view.findViewById(R.id.iv_bottom);
        iv_hat = view.findViewById(R.id.iv_hat);
        iv_shoes = view.findViewById(R.id.iv_shoes);
        iv_umbrella = view.findViewById(R.id.iv_umbrella);
        iv_parasol = view.findViewById(R.id.iv_parasol);
        iv_mask = view.findViewById(R.id.iv_mask);

        //메인페이지에서 생성한 랜덤 수를 가져옴
        topRandomNum = ((MainActivity)getActivity()).topRandomNum;
        top2RandomNum = ((MainActivity)getActivity()).top2RandomNum;
        bottomRandomNum = ((MainActivity)getActivity()).bottomRandomNum;
        bottomShortRandNum = ((MainActivity)getActivity()).bottomShortRandNum;
        shoesRandomNum = ((MainActivity)getActivity()).shoesRandomNum;

        id = ((MainActivity)getActivity()).userID;

        if(!checkLocationServicesStatus()) {
            showDialogForLocationServiceSetting();
        } else {
            checkRunTimePermission();
        }

        gpsTracker = new GpsTracker(getActivity());

        latitude = gpsTracker.getLatitude();
        longitude = gpsTracker.getLongitude();
        lat = latitude;
        lng = longitude;

        address = getCurrentAddress(latitude, longitude);
        cityView.setText(address);

        dateViewMethod();

        if(requestQueue == null) {
            requestQueue = Volley.newRequestQueue(getActivity().getApplicationContext());
        }

        if(requestQueue == null) {
            requestQueue = Volley.newRequestQueue(getActivity().getApplicationContext());
        }

        CurrentCall();
        HourlyCall();

        ib_refresh.setOnClickListener(new View.OnClickListener() { //업데이트 버튼
            @Override
            public void onClick(View v) {

                latitude = gpsTracker.getLatitude();
                longitude = gpsTracker.getLongitude();
                lat = latitude;
                lng = longitude;

                address = getCurrentAddress(latitude, longitude);
                cityView.setText(address);

                dateViewMethod();
                CurrentCall();
                HourlyCall();
                DailyCall();
            }
        });

        return view;
    }

    public void changeBackground() { //배경
        long now = System.currentTimeMillis();
        Date date = new Date(now);
        SimpleDateFormat AMPM = new SimpleDateFormat("a");
        SimpleDateFormat time = new SimpleDateFormat("h");
        String bg_getAMPM = AMPM.format(date);
        String bg_getTime = time.format(date);

        if(data_pop < 40) {
            if((bg_getAMPM.equals("오전") && (4 < Integer.parseInt(bg_getTime) && 12 > Integer.parseInt(bg_getTime))) || (bg_getAMPM.equals("오후") && Integer.parseInt(bg_getTime) < 7) || (bg_getAMPM.equals("오후") && Integer.parseInt(bg_getTime) == 12)) {
                background.setBackgroundColor(Color.parseColor("#6CC2FF"));
                iv_background.setBackgroundDrawable(getResources().getDrawable(R.drawable.main_afternoon));
            }
            else if((bg_getAMPM.equals("오전") && 4 >= Integer.parseInt(bg_getTime)) || (bg_getAMPM.equals("오전") && Integer.parseInt(bg_getTime) == 12) || (bg_getAMPM.equals("오후") && (Integer.parseInt(bg_getTime) >= 7 && Integer.parseInt(bg_getTime) < 12))) {
                background.setBackgroundColor(Color.parseColor("#011B34"));
                iv_background.setBackgroundDrawable(getResources().getDrawable(R.drawable.main_night));
            }
        }
        else {
            background.setBackgroundColor(Color.parseColor("#4B78A4"));
            iv_background.setBackgroundDrawable(getResources().getDrawable(R.drawable.main_rain));
        }


    }

    public void dateViewMethod() { //업데이트 된 시간 보여주기
        long now = System.currentTimeMillis();
        Date date = new Date(now);
        SimpleDateFormat simpleDateFormatDay = new SimpleDateFormat("M/d a");
        SimpleDateFormat simpleDateFormatTime = new SimpleDateFormat("h:mm");
        String getDay = simpleDateFormatDay.format(date);
        String getTime = simpleDateFormatTime.format(date);
        String getDate = getDay + " " + getTime;
        dateView.setText("업데이트 " + getDate);

    }

    @SuppressLint("MissingSuperCall")
    @Override
    public void onRequestPermissionsResult(int permsRequestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grandResults) {

        if (permsRequestCode == PERMISSIONS_REQUEST_CODE && grandResults.length == REQUIRED_PERMISSIONS.length) {
            boolean check_result = true;

            for (int result : grandResults) {
                if (result != PackageManager.PERMISSION_GRANTED) {
                    check_result = false;
                    break;
                }
            }

            if (check_result) {
                ;
            } else {
                if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), REQUIRED_PERMISSIONS[0])
                        || ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), REQUIRED_PERMISSIONS[1])) {
                    Toast.makeText(getActivity(), "퍼미션이 거부되었습니다. 앱을 다시 실행하여 퍼미션을 허용해주세요", Toast.LENGTH_LONG).show();
                    getActivity().finish();
                } else {
                    Toast.makeText(getActivity(), "퍼미션이 거부되었습니다. 설정(앱 정보)에서 퍼미션을 허용해야 합니다.", Toast.LENGTH_LONG).show();
                }
            }
        }
    }

    void checkRunTimePermission() { //위치 권한 설정
        int hasFineLocationPermission = ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION);
        int hasCoarseLocationPermission = ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION);

        if(hasFineLocationPermission == PackageManager.PERMISSION_GRANTED && hasCoarseLocationPermission == PackageManager.PERMISSION_GRANTED) {

        } else {
            if(ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), REQUIRED_PERMISSIONS[0])) {
                Toast.makeText(getActivity(), "이 앱을 실행하려면 위치 접근 권한이 필요합니다.", Toast.LENGTH_LONG).show();
                ActivityCompat.requestPermissions(getActivity(), REQUIRED_PERMISSIONS, PERMISSIONS_REQUEST_CODE);
            } else {
                ActivityCompat.requestPermissions(getActivity(), REQUIRED_PERMISSIONS, PERMISSIONS_REQUEST_CODE);
            }
        }
    }

    public String getCurrentAddress(double latitude, double longitude) { //위도, 경도를 얻어온 후 주소로 변환하여 리턴
        Geocoder geocoder = new Geocoder(getActivity(), Locale.getDefault());

        List<Address> addresses;

        try {
            addresses = geocoder.getFromLocation(
                    latitude,
                    longitude,
                    7);
        } catch (IOException ioException) {
            Toast.makeText(getActivity(), "지오코더 서비스 사용불가", Toast.LENGTH_LONG).show();
            return "지오코더 서비스 사용불가";
        } catch (IllegalArgumentException illegalArgumentException) {
            Toast.makeText(getActivity(), "잘못된 GPS 좌표", Toast.LENGTH_LONG).show();
            return "잘못된 GPS 좌표";
        }

        if(addresses == null || addresses.size() == 0) {
            Toast.makeText(getActivity(), "주소 미발견", Toast.LENGTH_LONG).show();
            return "주소 미발견";
        }

        Address address = addresses.get(0);
        return address.getLocality().toString() + "\n";
        //return address.getAddressLine(0).toString() + "\n";
    }

    private void showDialogForLocationServiceSetting() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("위치 서비스 비황성화");
        builder.setMessage("앱을 사용하기 위해서는 위치 서비스가 필요합니다.\n" + "위치 설정을 수정하실래요?");
        builder.setCancelable(true);
        builder.setPositiveButton("설정", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                Intent callGPSSettingIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivityForResult(callGPSSettingIntent, GPS_ENABLE_REQUEST_CODE);
            }
        });

        builder.setNegativeButton("취소", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
            }
        });
        builder.create().show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case GPS_ENABLE_REQUEST_CODE:

                if(checkLocationServicesStatus()) {
                    if(checkLocationServicesStatus()) {
                        Log.d("@@@", "onActivityResult : GPS 활성화 돼있음");
                        checkRunTimePermission();
                        return;
                    }
                }
                break;
        }
    }

    public boolean checkLocationServicesStatus() {
        LocationManager locationManager = (LocationManager) getActivity().getSystemService(LOCATION_SERVICE);

        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
                || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
    }


    private void CurrentCall() { //현재 날씨 정보
        String url = "https://api.openweathermap.org/data/2.5/onecall?"
        + "lat=" + lat
        + "&lon=" + lng
        + "&exclude=minutely,hourly,daily,alerts"
        + "&appid=" + BuildConfig.OPEN_WEATHER_API_KEY
        + "&lang=kr";
        StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onResponse(String response) {
                try {

                    JSONObject jsonObject = new JSONObject(response);


                    JSONObject subJsonObject = jsonObject.getJSONObject("current");

                    tempDo = (int)(Math.round((subJsonObject.getDouble("temp")-273.15)*100)/100);

                    tempView.setText(tempDo + "°");

                    JSONArray arr2 = subJsonObject.getJSONArray("weather");
                    JSONObject weatherObj = arr2.getJSONObject(0);
                    String weather = weatherObj.getString("description");
                    weatherView.setText(weather);


                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                return params;
            }
        };

        request.setShouldCache(false);
        requestQueue.add(request);
    }

    private void HourlyCall() { //시간별 날씨 정보
          String url = "https://api.openweathermap.org/data/2.5/onecall?"
        + "lat=" + lat
        + "&lon=" + lng
        + "&exclude=current,minutely,daily,alerts"
        + "&appid=" + BuildConfig.OPEN_WEATHER_API_KEY;
        StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onResponse(String response) {
                try {

                    JSONObject jsonObject = new JSONObject(response);

                    JSONArray jsonArray = jsonObject.getJSONArray("hourly");

                    JSONObject subJsonObject = jsonArray.getJSONObject(0);

                    data_pop = (int)(subJsonObject.getDouble("pop")*100);

                    DailyCall();
                    changeBackground();

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                return params;
            }
        };

        request.setShouldCache(false);
        requestQueue.add(request);
    }

    private void DailyCall() { //주간별 날씨 정보
        String url = "https://api.openweathermap.org/data/2.5/onecall?"
        + "lat=" + lat
        + "&lon=" + lng
        + "&exclude=current,minutely,hourly,alerts"
        + "&appid=" + BuildConfig.OPEN_WEATHER_API_KEY;

        StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onResponse(String response) {
                try {

                    JSONObject jsonObject = new JSONObject(response);

                    JSONArray jsonArray = jsonObject.getJSONArray("daily");
                    ArrayList<String> list = new ArrayList<String>();

                    jsonObject = jsonArray.getJSONObject(0);
                    JSONObject a = jsonArray.getJSONObject(0);
                    JSONObject b = jsonArray.getJSONObject(0);

                    a = jsonObject.getJSONObject("temp");

                    data_temp = Integer.parseInt(String.valueOf(Math.round((Double.parseDouble(a.getString("max"))-273.15)*100)/100));

                    data_uvi = (int)(jsonObject.getDouble("uvi"));

                    userDataTest();

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                return params;
            }
        };

        request.setShouldCache(false);
        requestQueue.add(request);
    }

    private void diseaseMent() { // ment_disease 테이블에서 멘트데이터를 가져옴
        String url = BuildConfig.BASE_SERVER_URL + "/ment_disease.php";
        StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onResponse(String response) {
                try {

                    JSONObject jsonObject = new JSONObject(response);

                    JSONArray jsonArray = jsonObject.getJSONArray("result");

                    for(int i = 0; i < jsonArray.length(); i++) {
                        jsonObject = jsonArray.getJSONObject(i);

                        diseaseList.add(jsonObject.getString("COL 1"));
                        diseaseList2.add(jsonObject.getString("COL 2"));
                        diseaseList3.add(jsonObject.getString("COL 3"));
                        diseaseList4.add(jsonObject.getString("COL 4"));
                        diseaseList5.add(jsonObject.getString("COL 5"));
                        diseaseList6.add(jsonObject.getString("COL 6"));
                        diseaseList7.add(jsonObject.getString("COL 7"));
                        diseaseList8.add(jsonObject.getString("COL 8"));
                        diseaseList9.add(jsonObject.getString("COL 9"));
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                return params;
            }
        };

        request.setShouldCache(false);
        requestQueue.add(request);
    }

    private void userDataTest() { //USER 테이블에서 사용자 정보를 가져옴
        String url = BuildConfig.BASE_SERVER_URL + "/PHP_userInfo.php";
        StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onResponse(String response) {
                try {

                    JSONObject jsonObject = new JSONObject(response);

                    JSONArray jsonArray = jsonObject.getJSONArray("result");
                    ArrayList<String> list = new ArrayList<String>();
                    ArrayList<String> list2 = new ArrayList<String>();

                    ArrayList<String> list3 = new ArrayList<String>();
                    ArrayList<String> list4 = new ArrayList<String>();
                    ArrayList<String> list5 = new ArrayList<String>();
                    ArrayList<String> list6 = new ArrayList<String>();
                    ArrayList<String> list7 = new ArrayList<String>();
                    ArrayList<String> list8 = new ArrayList<String>();
                    ArrayList<String> list9 = new ArrayList<String>();
                    ArrayList<String> list10 = new ArrayList<String>();

                    for(int i = 0; i < jsonArray.length(); i++) {
                        jsonObject = jsonArray.getJSONObject(i);

                        list.add(jsonObject.getString("userID"));

                        list2.add(jsonObject.getString("userSex"));

                        list3.add(jsonObject.getString("userDisease_1st"));
                        list4.add(jsonObject.getString("userDisease_2nd"));
                        list5.add(jsonObject.getString("userDisease_3rd"));
                        list6.add(jsonObject.getString("userDisease_4th"));
                        list7.add(jsonObject.getString("userDisease_5th"));
                        list8.add(jsonObject.getString("userDisease_6th"));
                        list9.add(jsonObject.getString("userDisease_7th"));
                        list10.add(jsonObject.getString("userDisease_8th"));

                    }

                    for(int i = 0; i < jsonArray.length(); i++) {
                        if(list.get(i).equals(id)) {
                            c = i;
                            String user_sex = list2.get(c);
                            user_sex = user_sex.replaceAll("\n", "");

                            String ImageURL = BuildConfig.BASE_SERVER_URL + "/" + user_sex + ".png";
                            Glide.with(getActivity()).load(ImageURL).into(iv_sex);

                            disease_1st = list3.get(c);
                            disease_2nd = list4.get(c);
                            disease_3rd = list5.get(c);
                            disease_4th = list6.get(c);
                            disease_5th = list7.get(c);
                            disease_6th = list8.get(c);
                            disease_7th = list9.get(c);
                            disease_8th = list10.get(c);

                            disease_1st = disease_1st.replaceAll("\n", "");
                            disease_2nd = disease_2nd.replaceAll("\n", "");
                            disease_3rd = disease_3rd.replaceAll("\n", "");
                            disease_4th = disease_4th.replaceAll("\n", "");
                            disease_5th = disease_5th.replaceAll("\n", "");
                            disease_6th = disease_6th.replaceAll("\n", "");
                            disease_7th = disease_7th.replaceAll("\n", "");
                            disease_8th = disease_8th.replaceAll("\n", "");

                        }

                    }

                    diseaseMent();
                    dataTest();

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                return params;
            }
        };

        request.setShouldCache(false);
        requestQueue.add(request);
    }


    private void dataTest() { //url을 이용해 옷 이미지 가져오기


        String url = BuildConfig.BASE_SERVER_URL + "/ment_all.php";
        StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onResponse(String response) {
                try {

                    JSONObject jsonObject = new JSONObject(response);

                    JSONArray jsonArray = jsonObject.getJSONArray("result");
                    ArrayList<String> list = new ArrayList<String>();
                    ArrayList<String> list2 = new ArrayList<String>();
                    ArrayList<String> list3 = new ArrayList<String>();
                    ArrayList<String> list4 = new ArrayList<String>();
                    ArrayList<String> list5 = new ArrayList<String>();
                    ArrayList<String> list6 = new ArrayList<String>();
                    ArrayList<String> list7 = new ArrayList<String>();

                    for(int i = 0; i < jsonArray.length(); i++) {
                        jsonObject = jsonArray.getJSONObject(i);

                        list.add(jsonObject.getString("COL 4")); //상의
                        list2.add(jsonObject.getString("COL 6")); //하의
                        list3.add(jsonObject.getString("COL 8")); //아우터
                        list4.add(jsonObject.getString("COL 12")); //색
                        list5.add(jsonObject.getString("COL 16")); //모자
                        list6.add(jsonObject.getString("COL 18")); //양산
                        list7.add(jsonObject.getString("COL 20")); //우산



                    }

                    String result1_1 = list.get(2); String result1_2 = list.get(3); String result1_3 = list.get(4); String result1_4 = list.get(5); String result1_5 = list.get(6);
                    String result1_6 = list.get(7); String result1_7 = list.get(8); String result1_8 = list.get(9); String result1_9 = list.get(10); String result1_10 = list.get(11);
                    String result1_11 = list.get(12); String result1_12 = list.get(13); String result1_13 = list.get(14); String result1_14 = list.get(15); String result1_15 = list.get(16);
                    String result1_16 = list.get(17); String result1_17 = list.get(18); String result1_18 = list.get(19); String result1_19 = list.get(20); String result1_20 = list.get(21);
                    String result1_21 = list.get(22); String result1_22 = list.get(23); String result1_23 = list.get(24); String result1_24 = list.get(25);

                    String result2_1 = list2.get(2); String result2_2 = list2.get(3); String result2_3 = list2.get(4); String result2_4 = list2.get(5); String result2_5 = list2.get(6);
                    String result2_6 = list2.get(7); String result2_7 = list2.get(8); String result2_8 = list2.get(9); String result2_9 = list2.get(10); String result2_10 = list2.get(11);
                    String result2_11 = list2.get(12); String result2_12 = list2.get(13); String result2_13 = list2.get(14); String result2_14 = list2.get(15); String result2_15 = list2.get(16);
                    String result2_16 = list2.get(17); String result2_17 = list2.get(18); String result2_18 = list2.get(19); String result2_19 = list2.get(20); String result2_20 = list2.get(21);
                    String result2_21 = list2.get(22); String result2_22 = list2.get(23); String result2_23 = list2.get(24); String result2_24 = list2.get(25);

                    String result3_1 = list3.get(2); String result3_2 = list3.get(3); String result3_3 = list3.get(4); String result3_4 = list3.get(5); String result3_5 = list3.get(6);
                    String result3_6 = list3.get(7); String result3_7 = list3.get(8); String result3_8 = list3.get(9); String result3_9 = list3.get(10); String result3_10 = list3.get(11);
                    String result3_11 = list3.get(12); String result3_12 = list3.get(13); String result3_13 = list3.get(14); String result3_14 = list3.get(15); String result3_15 = list3.get(16);
                    String result3_16 = list3.get(17); String result3_17 = list3.get(18); String result3_18 = list3.get(19); String result3_19 = list3.get(20); String result3_20 = list3.get(21);
                    String result3_21 = list3.get(22); String result3_22 = list3.get(23); String result3_23 = list3.get(24); String result3_24 = list3.get(25);

                    String result4_1 = list4.get(2); String result4_2 = list4.get(3); String result4_3 = list4.get(4); String result4_4 = list4.get(5); String result4_5 = list4.get(6);
                    String result4_6 = list4.get(7); String result4_7 = list4.get(8); String result4_8 = list4.get(9); String result4_9 = list4.get(10); String result4_10 = list4.get(11);
                    String result4_11 = list4.get(12); String result4_12 = list4.get(13); String result4_13 = list4.get(14); String result4_14 = list4.get(15); String result4_15 = list4.get(16);
                    String result4_16 = list4.get(17); String result4_17 = list4.get(18); String result4_18 = list4.get(19); String result4_19 = list4.get(20); String result4_20 = list4.get(21);
                    String result4_21 = list4.get(22); String result4_22 = list4.get(23); String result4_23 = list4.get(24); String result4_24 = list4.get(25);

                    String result5_1 = list5.get(2); String result5_2 = list5.get(3); String result5_3 = list5.get(4); String result5_4 = list5.get(5); String result5_5 = list5.get(6);
                    String result5_6 = list5.get(7); String result5_7 = list5.get(8); String result5_8 = list5.get(9); String result5_9 = list5.get(10); String result5_10 = list5.get(11);
                    String result5_11 = list5.get(12); String result5_12 = list5.get(13); String result5_13 = list5.get(14); String result5_14 = list5.get(15); String result5_15 = list5.get(16);
                    String result5_16 = list5.get(17); String result5_17 = list5.get(18); String result5_18 = list5.get(19); String result5_19 = list5.get(20); String result5_20 = list5.get(21);
                    String result5_21 = list5.get(22); String result5_22 = list5.get(23); String result5_23 = list5.get(24); String result5_24 = list5.get(25);

                    String result6_1 = list6.get(2); String result6_2 = list6.get(3); String result6_3 = list6.get(4); String result6_4 = list6.get(5); String result6_5 = list6.get(6);
                    String result6_6 = list6.get(7); String result6_7 = list6.get(8); String result6_8 = list6.get(9); String result6_9 = list6.get(10); String result6_10 = list6.get(11);
                    String result6_11 = list6.get(12); String result6_12 = list6.get(13); String result6_13 = list6.get(14); String result6_14 = list6.get(15); String result6_15 = list6.get(16);
                    String result6_16 = list6.get(17); String result6_17 = list6.get(18); String result6_18 = list6.get(19); String result6_19 = list6.get(20); String result6_20 = list6.get(21);
                    String result6_21 = list6.get(22); String result6_22 = list6.get(23); String result6_23 = list6.get(24); String result6_24 = list6.get(25);

                    String result7_1 = list7.get(2); String result7_2 = list7.get(3); String result7_3 = list7.get(4); String result7_4 = list7.get(5); String result7_5 = list7.get(6);
                    String result7_6 = list7.get(7); String result7_7 = list7.get(8); String result7_8 = list7.get(9); String result7_9 = list7.get(10); String result7_10 = list7.get(11);
                    String result7_11 = list7.get(12); String result7_12 = list7.get(13); String result7_13 = list7.get(14); String result7_14 = list7.get(15); String result7_15 = list7.get(16);
                    String result7_16 = list7.get(17); String result7_17 = list7.get(18); String result7_18 = list7.get(19); String result7_19 = list7.get(20); String result7_20 = list7.get(21);
                    String result7_21 = list7.get(22); String result7_22 = list7.get(23); String result7_23 = list7.get(24); String result7_24 = list7.get(25);

                    if (!disease_1st.equals("없음")) { // 질병이 있을 때 -> 질병을 기준으로 추천 옷 차림 데이터를 가져와 이미지로 보여줌
                        if (diseaseList.get(1).equals(disease_1st)) {
                            top2Icon = "top2_yellow";
                            topIcon = "top_long_black";
                            bottomIcon = "bottom_long_black";
                            hatIcon = "acc_cap";
                            parasolIcon = "acc_parasol";
                        }
                        else if (diseaseList.get(2).equals(disease_1st) || diseaseList.get(3).equals(disease_1st)) {
                            if(diseaseList.get(1).equals(disease_2nd) || diseaseList.get(1).equals(disease_3rd) || diseaseList.get(1).equals(disease_4th) || diseaseList.get(1).equals(disease_5th) || diseaseList.get(1).equals(disease_6th) || diseaseList.get(1).equals(disease_7th) || diseaseList.get(1).equals(disease_8th)) {
                                topIcon = "top_long_black";
                                bottomIcon = "bottom_long_black";
                            }
                            else {
                                if(topRandomNum == 0)
                                    topIcon = "top_long_rainbow";
                                else if(topRandomNum == 1)
                                    topIcon = "top_long_gray";
                                else if(topRandomNum == 2)
                                    topIcon = "top_long_wave";

                                if(bottomRandomNum == 0)
                                    bottomIcon = "bottom_long_sky";
                                else if(bottomRandomNum == 1)
                                    bottomIcon = "bottom_long_blue";
                            }
                        }
                        else if (diseaseList.get(4).equals(disease_1st)) {
                            if (diseaseList.get(1).equals(disease_2nd) || diseaseList.get(1).equals(disease_3rd) || diseaseList.get(1).equals(disease_4th) || diseaseList.get(1).equals(disease_5th) || diseaseList.get(1).equals(disease_6th) || diseaseList.get(1).equals(disease_7th) || diseaseList.get(1).equals(disease_8th)) {
                                topIcon = "top_long_black";
                                bottomIcon = "bottom_long_black";
                            }
                            else {
                                if(topRandomNum == 0)
                                    topIcon = "top_long_rainbow";
                                else if(topRandomNum == 1)
                                    topIcon = "top_long_gray";
                                else if(topRandomNum == 2)
                                    topIcon = "top_long_wave";

                                if(bottomRandomNum == 0)
                                    bottomIcon = "bottom_long_sky";
                                else if(bottomRandomNum == 1)
                                    bottomIcon = "bottom_long_blue";
                            }

                            hatIcon = "acc_cap";
                            parasolIcon = "acc_parasol";

                        }
                        else if (diseaseList.get(5).equals(disease_1st) || diseaseList.get(6).equals(disease_1st) || diseaseList.get(7).equals(disease_1st) || diseaseList.get(8).equals(disease_1st)) {

                            if(diseaseList.get(1).equals(disease_2nd) || diseaseList.get(1).equals(disease_3rd) || diseaseList.get(1).equals(disease_4th) || diseaseList.get(1).equals(disease_5th) || diseaseList.get(1).equals(disease_6th) || diseaseList.get(1).equals(disease_7th) || diseaseList.get(1).equals(disease_8th)) {
                                topIcon = "top_short_black";
                                if(bottomRandomNum == 0)
                                    bottomIcon = "bottom_short_black";
                                else if(bottomRandomNum == 1)
                                    bottomIcon = "bottom_short_black2";
                            }
                            else {
                                if(topRandomNum == 0)
                                    topIcon = "top_short_rainbow";
                                else if(topRandomNum == 1)
                                    topIcon = "top_short_gray";
                                else if(topRandomNum == 2)
                                    topIcon = "top_short_wave";

                                if(bottomShortRandNum == 0)
                                    bottomIcon = "bottom_short_sky";
                                else if(bottomShortRandNum == 1)
                                    bottomIcon = "bottom_short_blue";
                                else if(bottomShortRandNum == 2)
                                    bottomIcon = "bottom_short_blue2";
                                else if(bottomShortRandNum == 3)
                                    bottomIcon = "bottom_short_gray";
                                else if(bottomShortRandNum == 4)
                                    bottomIcon = "bottom_short_brown";
                            }
                        }

                        if (diseaseList.get(1).equals(disease_2nd) || diseaseList.get(1).equals(disease_3rd) || diseaseList.get(1).equals(disease_4th) || diseaseList.get(1).equals(disease_5th) || diseaseList.get(1).equals(disease_6th) || diseaseList.get(1).equals(disease_7th) || diseaseList.get(1).equals(disease_8th)) {
                            top2Icon = "top2_yellow";
                            hatIcon = "acc_cap";
                            parasolIcon = "acc_parasol";
                        }

                        if (diseaseList.get(4).equals(disease_2nd) || diseaseList.get(4).equals(disease_3rd) || diseaseList.get(4).equals(disease_4th) || diseaseList.get(4).equals(disease_5th) || diseaseList.get(4).equals(disease_6th) || diseaseList.get(4).equals(disease_7th) || diseaseList.get(4).equals(disease_8th)) {
                            hatIcon = "acc_cap";
                            parasolIcon = "acc_parasol";
                        }
                    }
                    else { // 질병이 없을 때 -> 질병을 기준으로 추천 옷 차림 데이터를 가져와 이미지로 보여줌
                        if((data_temp >= 20 && data_temp <= 23) && (data_uvi >= 0 && data_uvi <= 5) && (data_pop >= 0 && data_pop <= 39)) {

                            if(result1_1.equals("short") && result4_1.equals("white"))
                                topIcon = "top_short_white";
                            else if(result1_1.equals("short") && result4_1.equals("black"))
                                topIcon = "top_short_black";
                            else if(result1_1.equals("short") && result4_1.equals("0")) { // 색 랜덤으로
                                if(topRandomNum == 0)
                                    topIcon = "top_short_rainbow";
                                else if(topRandomNum == 1)
                                    topIcon = "top_short_gray";
                                else if(topRandomNum == 2)
                                    topIcon = "top_short_wave";
                            }
                            else if(result1_1.equals("long") && result4_1.equals("white"))
                                topIcon = "top_long_white";
                            else if(result1_1.equals("long") && result4_1.equals("black"))
                                topIcon = "top_long_black";
                            else if(result1_1.equals("long") && result4_1.equals("0")) {
                                if(topRandomNum == 0)
                                    topIcon = "top_long_rainbow";
                                else if(topRandomNum == 1)
                                    topIcon = "top_long_gray";
                                else if(topRandomNum == 2)
                                    topIcon = "top_long_wave";
                            }

                            if(result2_1.equals("short") && result4_1.equals("white")) {
                                if(bottomRandomNum == 0)
                                    bottomIcon = "bottom_short_white";
                                else if(bottomRandomNum == 1)
                                    bottomIcon = "bottom_short_white2";
                            }
                            else if(result2_1.equals("short") && result4_1.equals("black")) {
                                if(bottomRandomNum == 0)
                                    bottomIcon = "bottom_short_black";
                                else if(bottomRandomNum == 1)
                                    bottomIcon = "bottom_short_black2";
                            }
                            else if(result2_1.equals("short") && result4_1.equals("0")) {
                                if(bottomShortRandNum == 0)
                                    bottomIcon = "bottom_short_sky";
                                else if(bottomShortRandNum == 1)
                                    bottomIcon = "bottom_short_blue";
                                else if(bottomShortRandNum == 2)
                                    bottomIcon = "bottom_short_blue2";
                                else if(bottomShortRandNum == 3)
                                    bottomIcon = "bottom_short_gray";
                                else if(bottomShortRandNum == 4)
                                    bottomIcon = "bottom_short_brown";
                            }
                            else if(result2_1.equals("long") && result4_1.equals("white"))
                                bottomIcon = "bottom_long_white";
                            else if(result2_1.equals("long") && result4_1.equals("black"))
                                bottomIcon = "bottom_long_black";
                            else if(result2_1.equals("long") && result4_1.equals("0")) {
                                if(bottomRandomNum == 0)
                                    bottomIcon = "bottom_long_sky";
                                else if(bottomRandomNum == 1)
                                    bottomIcon = "bottom_long_blue";
                            }

                            if(result3_1.equals("1"))
                                top2Icon = "top2_yellow";


                            if(result5_1.equals("1"))
                                hatIcon = "acc_cap";

                            if(result6_1.equals("1"))
                                parasolIcon = "acc_parasol";

                            if(result7_1.equals("1"))
                                umbrellaIcon = "acc_umbrella";
                        }
                        else if((data_temp >= 24 && data_temp <= 27) && (data_uvi >= 0 && data_uvi <= 5) && (data_pop >= 0 && data_pop <= 39)) {

                            if(result1_2.equals("short") && result4_2.equals("white"))
                                topIcon = "top_short_white";
                            else if(result1_2.equals("short") && result4_2.equals("black"))
                                topIcon = "top_short_black";
                            else if(result1_2.equals("short") && result4_2.equals("0")) {
                                if(topRandomNum == 0)
                                    topIcon = "top_short_rainbow";
                                else if(topRandomNum == 1)
                                    topIcon = "top_short_gray";
                                else if(topRandomNum == 2)
                                    topIcon = "top_short_wave";
                            }
                            else if(result1_2.equals("long") && result4_2.equals("white"))
                                topIcon = "top_long_white";
                            else if(result1_2.equals("long") && result4_2.equals("black"))
                                topIcon = "top_long_black";
                            else if(result1_2.equals("long") && result4_2.equals("0")) {
                                if(topRandomNum == 0)
                                    topIcon = "top_long_rainbow";
                                else if(topRandomNum == 1)
                                    topIcon = "top_long_gray";
                                else if(topRandomNum == 2)
                                    topIcon = "top_long_wave";
                            }

                            if(result2_2.equals("short") && result4_2.equals("white")) {
                                if(bottomRandomNum == 0)
                                    bottomIcon = "bottom_short_white";
                                else if(bottomRandomNum == 1)
                                    bottomIcon = "bottom_short_white2";
                            }
                            else if(result2_2.equals("short") && result4_2.equals("black")) {
                                if(bottomRandomNum == 0)
                                    bottomIcon = "bottom_short_black";
                                else if(bottomRandomNum == 1)
                                    bottomIcon = "bottom_short_black2";
                            }
                            else if(result2_2.equals("short") && result4_2.equals("0")) {
                                if(bottomShortRandNum == 0)
                                    bottomIcon = "bottom_short_sky";
                                else if(bottomShortRandNum == 1)
                                    bottomIcon = "bottom_short_blue";
                                else if(bottomShortRandNum == 2)
                                    bottomIcon = "bottom_short_blue2";
                                else if(bottomShortRandNum == 3)
                                    bottomIcon = "bottom_short_gray";
                                else if(bottomShortRandNum == 4)
                                    bottomIcon = "bottom_short_brown";
                            }
                            else if(result2_2.equals("long") && result4_2.equals("white"))
                                bottomIcon = "bottom_long_white";
                            else if(result2_2.equals("long") && result4_2.equals("black"))
                                bottomIcon = "bottom_long_black";
                            else if(result2_2.equals("long") && result4_2.equals("0")) {
                                if(bottomRandomNum == 0)
                                    bottomIcon = "bottom_long_sky";
                                else if(bottomRandomNum == 1)
                                    bottomIcon = "bottom_long_blue";
                            }

                            if(result3_2.equals("1"))
                                top2Icon = "top2_yellow";


                            if(result5_2.equals("1"))
                                hatIcon = "acc_cap";

                            if(result6_2.equals("1"))
                                parasolIcon = "acc_parasol";

                            if(result7_2.equals("1"))
                                umbrellaIcon = "acc_umbrella";
                        }
                        else if((data_temp >= 28 && data_temp <= 31) && (data_uvi >= 0 && data_uvi <= 5) && (data_pop >= 0 && data_pop <= 39)) {

                            if(result1_3.equals("short") && result4_3.equals("white"))
                                topIcon = "top_short_white";
                            else if(result1_3.equals("short") && result4_3.equals("black"))
                                topIcon = "top_short_black";
                            else if(result1_3.equals("short") && result4_3.equals("0")) {
                                if(topRandomNum == 0)
                                    topIcon = "top_short_rainbow";
                                else if(topRandomNum == 1)
                                    topIcon = "top_short_gray";
                                else if(topRandomNum == 2)
                                    topIcon = "top_short_wave";
                            }

                            else if(result1_3.equals("long") && result4_3.equals("white"))
                                topIcon = "top_long_white";
                            else if(result1_3.equals("long") && result4_3.equals("black"))
                                topIcon = "top_long_black";
                            else if(result1_3.equals("long") && result4_3.equals("0")) {
                                if(topRandomNum == 0)
                                    topIcon = "top_long_rainbow";
                                else if(topRandomNum == 1)
                                    topIcon = "top_long_gray";
                                else if(topRandomNum == 2)
                                    topIcon = "top_long_wave";
                            }

                            if(result2_3.equals("short") && result4_3.equals("white")) {
                                if(bottomRandomNum == 0)
                                    bottomIcon = "bottom_short_white";
                                else if(bottomRandomNum == 1)
                                    bottomIcon = "bottom_short_white2";
                            }
                            else if(result2_3.equals("short") && result4_3.equals("black")) {
                                if(bottomRandomNum == 0)
                                    bottomIcon = "bottom_short_black";
                                else if(bottomRandomNum == 1)
                                    bottomIcon = "bottom_short_black2";
                            }
                            else if(result2_3.equals("short") && result4_3.equals("0")) {
                                if(bottomShortRandNum == 0)
                                    bottomIcon = "bottom_short_sky";
                                else if(bottomShortRandNum == 1)
                                    bottomIcon = "bottom_short_blue";
                                else if(bottomShortRandNum == 2)
                                    bottomIcon = "bottom_short_blue2";
                                else if(bottomShortRandNum == 3)
                                    bottomIcon = "bottom_short_gray";
                                else if(bottomShortRandNum == 4)
                                    bottomIcon = "bottom_short_brown";
                            }
                            else if(result2_3.equals("long") && result4_3.equals("white"))
                                bottomIcon = "bottom_long_white";
                            else if(result2_3.equals("long") && result4_3.equals("black"))
                                bottomIcon = "bottom_long_black";
                            else if(result2_3.equals("long") && result4_3.equals("0")) {
                                if (bottomRandomNum == 0)
                                    bottomIcon = "bottom_long_sky";
                                else if (bottomRandomNum == 1)
                                    bottomIcon = "bottom_long_blue";
                            }

                            if(result3_3.equals("1"))
                                top2Icon = "top2_yellow";


                            if(result5_3.equals("1"))
                                hatIcon = "acc_cap";

                            if(result6_3.equals("1"))
                                parasolIcon = "acc_parasol";

                            if(result7_3.equals("1"))
                                umbrellaIcon = "acc_umbrella";
                        }
                        else if((data_temp >= 32) && (data_uvi >= 0 && data_uvi <= 5) && (data_pop >= 0 && data_pop <= 39)) {

                            if(result1_4.equals("short") && result4_4.equals("white"))
                                topIcon = "top_short_white";
                            else if(result1_4.equals("short") && result4_4.equals("black"))
                                topIcon = "top_short_black";
                            else if(result1_4.equals("short") && result4_4.equals("0")) {
                                if(topRandomNum == 0)
                                    topIcon = "top_short_rainbow";
                                else if(topRandomNum == 1)
                                    topIcon = "top_short_gray";
                                else if(topRandomNum == 2)
                                    topIcon = "top_short_wave";
                            }

                            else if(result1_4.equals("long") && result4_4.equals("white"))
                                topIcon = "top_long_white";
                            else if(result1_4.equals("long") && result4_4.equals("black"))
                                topIcon = "top_long_black";
                            else if(result1_4.equals("long") && result4_4.equals("0")) {
                                if(topRandomNum == 0)
                                    topIcon = "top_long_rainbow";
                                else if(topRandomNum == 1)
                                    topIcon = "top_long_gray";
                                else if(topRandomNum == 2)
                                    topIcon = "top_long_wave";
                            }

                            if(result2_4.equals("short") && result4_4.equals("white")) {
                                if(bottomRandomNum == 0)
                                    bottomIcon = "bottom_short_white";
                                else if(bottomRandomNum == 1)
                                    bottomIcon = "bottom_short_white2";
                            }
                            else if(result2_4.equals("short") && result4_4.equals("black")) {
                                if(bottomRandomNum == 0)
                                    bottomIcon = "bottom_short_black";
                                else if(bottomRandomNum == 1)
                                    bottomIcon = "bottom_short_black2";
                            }
                            else if(result2_4.equals("short") && result4_4.equals("0")) {
                                if(bottomShortRandNum == 0)
                                    bottomIcon = "bottom_short_sky";
                                else if(bottomShortRandNum == 1)
                                    bottomIcon = "bottom_short_blue";
                                else if(bottomShortRandNum == 2)
                                    bottomIcon = "bottom_short_blue2";
                                else if(bottomShortRandNum == 3)
                                    bottomIcon = "bottom_short_gray";
                                else if(bottomShortRandNum == 4)
                                    bottomIcon = "bottom_short_brown";
                            }
                            else if(result2_4.equals("long") && result4_4.equals("white"))
                                bottomIcon = "bottom_long_white";
                            else if(result2_4.equals("long") && result4_4.equals("black"))
                                bottomIcon = "bottom_long_black";
                            else if(result2_4.equals("long") && result4_4.equals("0")) {
                                if (bottomRandomNum == 0)
                                    bottomIcon = "bottom_long_sky";
                                else if (bottomRandomNum == 1)
                                    bottomIcon = "bottom_long_blue";
                            }

                            if(result3_4.equals("1"))
                                top2Icon = "top2_yellow";

                            if(result5_4.equals("1"))
                                hatIcon = "acc_cap";

                            if(result6_4.equals("1"))
                                parasolIcon = "acc_parasol";

                            if(result7_4.equals("1"))
                                umbrellaIcon = "acc_umbrella";
                        }
                        else if((data_temp >= 20 && data_temp <= 23) && (data_uvi >= 6 && data_uvi <= 8) && (data_pop >= 0 && data_pop <= 39)) {

                            if(result1_5.equals("short") && result4_5.equals("white"))
                                topIcon = "top_short_white";
                            else if(result1_5.equals("short") && result4_5.equals("black"))
                                topIcon = "top_short_black";
                            else if(result1_5.equals("short") && result4_5.equals("0")) {
                                if(topRandomNum == 0)
                                    topIcon = "top_short_rainbow";
                                else if(topRandomNum == 1)
                                    topIcon = "top_short_gray";
                                else if(topRandomNum == 2)
                                    topIcon = "top_short_wave";
                            }

                            else if(result1_5.equals("long") && result4_5.equals("white"))
                                topIcon = "top_long_white";
                            else if(result1_5.equals("long") && result4_5.equals("black"))
                                topIcon = "top_long_black";
                            else if(result1_5.equals("long") && result4_5.equals("0")) {
                                if(topRandomNum == 0)
                                    topIcon = "top_long_rainbow";
                                else if(topRandomNum == 1)
                                    topIcon = "top_long_gray";
                                else if(topRandomNum == 2)
                                    topIcon = "top_long_wave";
                            }

                            if(result2_5.equals("short") && result4_5.equals("white")) {
                                if(bottomRandomNum == 0)
                                    bottomIcon = "bottom_short_white";
                                else if(bottomRandomNum == 1)
                                    bottomIcon = "bottom_short_white2";
                            }
                            else if(result2_5.equals("short") && result4_5.equals("black")) {
                                if(bottomRandomNum == 0)
                                    bottomIcon = "bottom_short_black";
                                else if(bottomRandomNum == 1)
                                    bottomIcon = "bottom_short_black2";
                            }
                            else if(result2_5.equals("short") && result4_5.equals("0")) {
                                if(bottomShortRandNum == 0)
                                    bottomIcon = "bottom_short_sky";
                                else if(bottomShortRandNum == 1)
                                    bottomIcon = "bottom_short_blue";
                                else if(bottomShortRandNum == 2)
                                    bottomIcon = "bottom_short_blue2";
                                else if(bottomShortRandNum == 3)
                                    bottomIcon = "bottom_short_gray";
                                else if(bottomShortRandNum == 4)
                                    bottomIcon = "bottom_short_brown";
                            }
                            else if(result2_5.equals("long") && result4_5.equals("white"))
                                bottomIcon = "bottom_long_white";
                            else if(result2_5.equals("long") && result4_5.equals("black"))
                                bottomIcon = "bottom_long_black";
                            else if(result2_5.equals("long") && result4_5.equals("0")) {
                                if (bottomRandomNum == 0)
                                    bottomIcon = "bottom_long_sky";
                                else if (bottomRandomNum == 1)
                                    bottomIcon = "bottom_long_blue";
                            }

                            if(result3_5.equals("1"))
                                top2Icon = "top2_yellow";


                            if(result5_5.equals("1"))
                                hatIcon = "acc_cap";

                            if(result6_5.equals("1"))
                                parasolIcon = "acc_parasol";

                            if(result7_5.equals("1"))
                                umbrellaIcon = "acc_umbrella";
                        }
                        else if((data_temp >= 24 && data_temp <= 27) && (data_uvi >= 6 && data_uvi <= 8) && (data_pop >= 0 && data_pop <= 39)) {
                            //test_top.setText(result1_6); test_bottom.setText(result2_6); test_top2.setText(result3_6); test_color.setText(result4_6);
                            //test_hat.setText(result5_6); test_parasol.setText(result6_6); test_umbrella.setText(result7_6);

                            if(result1_6.equals("short") && result4_6.equals("white"))
                                topIcon = "top_short_white";
                            else if(result1_6.equals("short") && result4_6.equals("black"))
                                topIcon = "top_short_black";
                            else if(result1_6.equals("short") && result4_6.equals("0")) {
                                if(topRandomNum == 0)
                                    topIcon = "top_short_rainbow";
                                else if(topRandomNum == 1)
                                    topIcon = "top_short_gray";
                                else if(topRandomNum == 2)
                                    topIcon = "top_short_wave";
                            }

                            else if(result1_6.equals("long") && result4_6.equals("white"))
                                topIcon = "top_long_white";
                            else if(result1_6.equals("long") && result4_6.equals("black"))
                                topIcon = "top_long_black";
                            else if(result1_6.equals("long") && result4_6.equals("0")) {
                                if(topRandomNum == 0)
                                    topIcon = "top_long_rainbow";
                                else if(topRandomNum == 1)
                                    topIcon = "top_long_gray";
                                else if(topRandomNum == 2)
                                    topIcon = "top_long_wave";
                            }

                            if(result2_6.equals("short") && result4_6.equals("white")) {
                                if(bottomRandomNum == 0)
                                    bottomIcon = "bottom_short_white";
                                else if(bottomRandomNum == 1)
                                    bottomIcon = "bottom_short_white2";
                            }
                            else if(result2_6.equals("short") && result4_6.equals("black")) {
                                if(bottomRandomNum == 0)
                                    bottomIcon = "bottom_short_black";
                                else if(bottomRandomNum == 1)
                                    bottomIcon = "bottom_short_black2";
                            }
                            else if(result2_6.equals("short") && result4_6.equals("0")) {
                                if(bottomShortRandNum == 0)
                                    bottomIcon = "bottom_short_sky";
                                else if(bottomShortRandNum == 1)
                                    bottomIcon = "bottom_short_blue";
                                else if(bottomShortRandNum == 2)
                                    bottomIcon = "bottom_short_blue2";
                                else if(bottomShortRandNum == 3)
                                    bottomIcon = "bottom_short_gray";
                                else if(bottomShortRandNum == 4)
                                    bottomIcon = "bottom_short_brown";
                            }
                            else if(result2_6.equals("long") && result4_6.equals("white"))
                                bottomIcon = "bottom_long_white";
                            else if(result2_6.equals("long") && result4_6.equals("black"))
                                bottomIcon = "bottom_long_black";
                            else if(result2_6.equals("long") && result4_6.equals("0")) {
                                if (bottomRandomNum == 0)
                                    bottomIcon = "bottom_long_sky";
                                else if (bottomRandomNum == 1)
                                    bottomIcon = "bottom_long_blue";
                            }

                            if(result3_6.equals("1"))
                                top2Icon = "top2_yellow";


                            if(result5_6.equals("1"))
                                hatIcon = "acc_cap";

                            if(result6_6.equals("1"))
                                parasolIcon = "acc_parasol";

                            if(result7_6.equals("1"))
                                umbrellaIcon = "acc_umbrella";

                        }
                        else if((data_temp >= 28 && data_temp <= 31) && (data_uvi >= 6 && data_uvi <= 8) && (data_pop >= 0 && data_pop <= 39)) {

                            if(result1_7.equals("short") && result4_7.equals("white"))
                                topIcon = "top_short_white";
                            else if(result1_7.equals("short") && result4_7.equals("black"))
                                topIcon = "top_short_black";
                            else if(result1_7.equals("short") && result4_7.equals("0")) {
                                if(topRandomNum == 0)
                                    topIcon = "top_short_rainbow";
                                else if(topRandomNum == 1)
                                    topIcon = "top_short_gray";
                                else if(topRandomNum == 2)
                                    topIcon = "top_short_wave";
                            }

                            else if(result1_7.equals("long") && result4_7.equals("white"))
                                topIcon = "top_long_white";
                            else if(result1_7.equals("long") && result4_7.equals("black"))
                                topIcon = "top_long_black";
                            else if(result1_7.equals("long") && result4_7.equals("0")) {
                                if(topRandomNum == 0)
                                    topIcon = "top_long_rainbow";
                                else if(topRandomNum == 1)
                                    topIcon = "top_long_gray";
                                else if(topRandomNum == 2)
                                    topIcon = "top_long_wave";
                            }

                            if(result2_7.equals("short") && result4_7.equals("white")) {
                                if(bottomRandomNum == 0)
                                    bottomIcon = "bottom_short_white";
                                else if(bottomRandomNum == 1)
                                    bottomIcon = "bottom_short_white2";
                            }
                            else if(result2_7.equals("short") && result4_7.equals("black")) {
                                if(bottomRandomNum == 0)
                                    bottomIcon = "bottom_short_black";
                                else if(bottomRandomNum == 1)
                                    bottomIcon = "bottom_short_black2";
                            }
                            else if(result2_7.equals("short") && result4_7.equals("0")) {
                                if(bottomShortRandNum == 0)
                                    bottomIcon = "bottom_short_sky";
                                else if(bottomShortRandNum == 1)
                                    bottomIcon = "bottom_short_blue";
                                else if(bottomShortRandNum == 2)
                                    bottomIcon = "bottom_short_blue2";
                                else if(bottomShortRandNum == 3)
                                    bottomIcon = "bottom_short_gray";
                                else if(bottomShortRandNum == 4)
                                    bottomIcon = "bottom_short_brown";
                            }
                            else if(result2_7.equals("long") && result4_7.equals("white"))
                                bottomIcon = "bottom_long_white";
                            else if(result2_7.equals("long") && result4_7.equals("black"))
                                bottomIcon = "bottom_long_black";
                            else if(result2_7.equals("long") && result4_7.equals("0")) {
                                if (bottomRandomNum == 0)
                                    bottomIcon = "bottom_long_sky";
                                else if (bottomRandomNum == 1)
                                    bottomIcon = "bottom_long_blue";
                            }

                            if(result3_7.equals("1"))
                                top2Icon = "top2_yellow";


                            if(result5_7.equals("1"))
                                hatIcon = "acc_cap";

                            if(result6_7.equals("1"))
                                parasolIcon = "acc_parasol";

                            if(result7_7.equals("1"))
                                umbrellaIcon = "acc_umbrella";

                        }
                        else if((data_temp >= 32) && (data_uvi >= 6 && data_uvi <= 8) && (data_pop >= 0 && data_pop <= 39)) {

                            if(result1_8.equals("short") && result4_8.equals("white"))
                                topIcon = "top_short_white";
                            else if(result1_8.equals("short") && result4_8.equals("black"))
                                topIcon = "top_short_black";
                            else if(result1_8.equals("short") && result4_8.equals("0")) {
                                if(topRandomNum == 0)
                                    topIcon = "top_short_rainbow";
                                else if(topRandomNum == 1)
                                    topIcon = "top_short_gray";
                                else if(topRandomNum == 2)
                                    topIcon = "top_short_wave";
                            }
                            else if(result1_8.equals("long") && result4_8.equals("white"))
                                topIcon = "top_long_white";
                            else if(result1_8.equals("long") && result4_8.equals("black"))
                                topIcon = "top_long_black";
                            else if(result1_8.equals("long") && result4_8.equals("0")) {
                                if(topRandomNum == 0)
                                    topIcon = "top_long_rainbow";
                                else if(topRandomNum == 1)
                                    topIcon = "top_long_gray";
                                else if(topRandomNum == 2)
                                    topIcon = "top_long_wave";
                            }

                            if(result2_8.equals("short") && result4_8.equals("white")) {
                                if(bottomRandomNum == 0)
                                    bottomIcon = "bottom_short_white";
                                else if(bottomRandomNum == 1)
                                    bottomIcon = "bottom_short_white2";
                            }
                            else if(result2_8.equals("short") && result4_8.equals("black")) {
                                if(bottomRandomNum == 0)
                                    bottomIcon = "bottom_short_black";
                                else if(bottomRandomNum == 1)
                                    bottomIcon = "bottom_short_black2";
                            }
                            else if(result2_8.equals("short") && result4_8.equals("0")) {
                                if(bottomShortRandNum == 0)
                                    bottomIcon = "bottom_short_sky";
                                else if(bottomShortRandNum == 1)
                                    bottomIcon = "bottom_short_blue";
                                else if(bottomShortRandNum == 2)
                                    bottomIcon = "bottom_short_blue2";
                                else if(bottomShortRandNum == 3)
                                    bottomIcon = "bottom_short_gray";
                                else if(bottomShortRandNum == 4)
                                    bottomIcon = "bottom_short_brown";
                            }
                            else if(result2_8.equals("long") && result4_8.equals("white"))
                                bottomIcon = "bottom_long_white";
                            else if(result2_8.equals("long") && result4_8.equals("black"))
                                bottomIcon = "bottom_long_black";
                            else if(result2_8.equals("long") && result4_8.equals("0")) {
                                if (bottomRandomNum == 0)
                                    bottomIcon = "bottom_long_sky";
                                else if (bottomRandomNum == 1)
                                    bottomIcon = "bottom_long_blue";
                            }

                            if(result3_8.equals("1"))
                                top2Icon = "top2_yellow";


                            if(result5_8.equals("1"))
                                hatIcon = "acc_cap";

                            if(result6_8.equals("1"))
                                parasolIcon = "acc_parasol";

                            if(result7_8.equals("1"))
                                umbrellaIcon = "acc_umbrella";

                        }
                        else if((data_temp >= 20 && data_temp <= 23) && (data_uvi >= 8) && (data_pop >= 0 && data_pop <= 39)) {

                            if(result1_9.equals("short") && result4_9.equals("white"))
                                topIcon = "top_short_white";
                            else if(result1_9.equals("short") && result4_9.equals("black"))
                                topIcon = "top_short_black";
                            else if(result1_9.equals("short") && result4_9.equals("0")) {
                                if(topRandomNum == 0)
                                    topIcon = "top_short_rainbow";
                                else if(topRandomNum == 1)
                                    topIcon = "top_short_gray";
                                else if(topRandomNum == 2)
                                    topIcon = "top_short_wave";
                            }
                            else if(result1_9.equals("long") && result4_9.equals("white"))
                                topIcon = "top_long_white";
                            else if(result1_9.equals("long") && result4_9.equals("black"))
                                topIcon = "top_long_black";
                            else if(result1_9.equals("long") && result4_9.equals("0")) {
                                if(topRandomNum == 0)
                                    topIcon = "top_long_rainbow";
                                else if(topRandomNum == 1)
                                    topIcon = "top_long_gray";
                                else if(topRandomNum == 2)
                                    topIcon = "top_long_wave";
                            }

                            if(result2_9.equals("short") && result4_9.equals("white")) {
                                if(bottomRandomNum == 0)
                                    bottomIcon = "bottom_short_white";
                                else if(bottomRandomNum == 1)
                                    bottomIcon = "bottom_short_white2";
                            }
                            else if(result2_9.equals("short") && result4_9.equals("black")) {
                                if(bottomRandomNum == 0)
                                    bottomIcon = "bottom_short_black";
                                else if(bottomRandomNum == 1)
                                    bottomIcon = "bottom_short_black2";
                            }
                            else if(result2_9.equals("short") && result4_9.equals("0")) {
                                if(bottomShortRandNum == 0)
                                    bottomIcon = "bottom_short_sky";
                                else if(bottomShortRandNum == 1)
                                    bottomIcon = "bottom_short_blue";
                                else if(bottomShortRandNum == 2)
                                    bottomIcon = "bottom_short_blue2";
                                else if(bottomShortRandNum == 3)
                                    bottomIcon = "bottom_short_gray";
                                else if(bottomShortRandNum == 4)
                                    bottomIcon = "bottom_short_brown";
                            }
                            else if(result2_9.equals("long") && result4_9.equals("white"))
                                bottomIcon = "bottom_long_white";
                            else if(result2_9.equals("long") && result4_9.equals("black"))
                                bottomIcon = "bottom_long_black";
                            else if(result2_9.equals("long") && result4_9.equals("0")) {
                                if (bottomRandomNum == 0)
                                    bottomIcon = "bottom_long_sky";
                                else if (bottomRandomNum == 1)
                                    bottomIcon = "bottom_long_blue";
                            }

                            if(result3_9.equals("1"))
                                top2Icon = "top2_yellow";


                            if(result5_9.equals("1"))
                                hatIcon = "acc_cap";

                            if(result6_9.equals("1"))
                                parasolIcon = "acc_parasol";

                            if(result7_9.equals("1"))
                                umbrellaIcon = "acc_umbrella";


                        }
                        else if((data_temp >= 24 && data_temp <= 27) && (data_uvi >= 8) && (data_pop >= 0 && data_pop <= 39)) {

                            if(result1_10.equals("short") && result4_10.equals("white"))
                                topIcon = "top_short_white";
                            else if(result1_10.equals("short") && result4_10.equals("black"))
                                topIcon = "top_short_black";
                            else if(result1_10.equals("short") && result4_10.equals("0")) {
                                if(topRandomNum == 0)
                                    topIcon = "top_short_rainbow";
                                else if(topRandomNum == 1)
                                    topIcon = "top_short_gray";
                                else if(topRandomNum == 2)
                                    topIcon = "top_short_wave";
                            }
                            else if(result1_10.equals("long") && result4_10.equals("white"))
                                topIcon = "top_long_white";
                            else if(result1_10.equals("long") && result4_10.equals("black"))
                                topIcon = "top_long_black";
                            else if(result1_10.equals("long") && result4_10.equals("0")) {
                                if(topRandomNum == 0)
                                    topIcon = "top_long_rainbow";
                                else if(topRandomNum == 1)
                                    topIcon = "top_long_gray";
                                else if(topRandomNum == 2)
                                    topIcon = "top_long_wave";
                            }

                            if(result2_10.equals("short") && result4_10.equals("white")) {
                                if(bottomRandomNum == 0)
                                    bottomIcon = "bottom_short_white";
                                else if(bottomRandomNum == 1)
                                    bottomIcon = "bottom_short_white2";
                            }
                            else if(result2_10.equals("short") && result4_10.equals("black")) {
                                if(bottomRandomNum == 0)
                                    bottomIcon = "bottom_short_black";
                                else if(bottomRandomNum == 1)
                                    bottomIcon = "bottom_short_black2";
                            }
                            else if(result2_10.equals("short") && result4_10.equals("0")) {
                                if(bottomShortRandNum == 0)
                                    bottomIcon = "bottom_short_sky";
                                else if(bottomShortRandNum == 1)
                                    bottomIcon = "bottom_short_blue";
                                else if(bottomShortRandNum == 2)
                                    bottomIcon = "bottom_short_blue2";
                                else if(bottomShortRandNum == 3)
                                    bottomIcon = "bottom_short_gray";
                                else if(bottomShortRandNum == 4)
                                    bottomIcon = "bottom_short_brown";
                            }
                            else if(result2_10.equals("long") && result4_10.equals("white"))
                                bottomIcon = "bottom_long_white";
                            else if(result2_10.equals("long") && result4_10.equals("black"))
                                bottomIcon = "bottom_long_black";
                            else if(result2_10.equals("long") && result4_10.equals("0")) {
                                if (bottomRandomNum == 0)
                                    bottomIcon = "bottom_long_sky";
                                else if (bottomRandomNum == 1)
                                    bottomIcon = "bottom_long_blue";
                            }

                            if(result3_10.equals("1"))
                                top2Icon = "top2_yellow";

                            if(result5_10.equals("1"))
                                hatIcon = "acc_cap";

                            if(result6_10.equals("1"))
                                parasolIcon = "acc_parasol";

                            if(result7_10.equals("1"))
                                umbrellaIcon = "acc_umbrella";
                        }
                        else if((data_temp >= 28 && data_temp <= 31) && (data_uvi >= 8) && (data_pop >= 0 && data_pop <= 39)) {

                            if(result1_11.equals("short") && result4_11.equals("white"))
                                topIcon = "top_short_white";
                            else if(result1_11.equals("short") && result4_11.equals("black"))
                                topIcon = "top_short_black";
                            else if(result1_11.equals("short") && result4_11.equals("0")) {
                                if(topRandomNum == 0)
                                    topIcon = "top_short_rainbow";
                                else if(topRandomNum == 1)
                                    topIcon = "top_short_gray";
                                else if(topRandomNum == 2)
                                    topIcon = "top_short_wave";
                            }
                            else if(result1_11.equals("long") && result4_11.equals("white"))
                                topIcon = "top_long_white";
                            else if(result1_11.equals("long") && result4_11.equals("black"))
                                topIcon = "top_long_black";
                            else if(result1_11.equals("long") && result4_11.equals("0")) {
                                if(topRandomNum == 0)
                                    topIcon = "top_long_rainbow";
                                else if(topRandomNum == 1)
                                    topIcon = "top_long_gray";
                                else if(topRandomNum == 2)
                                    topIcon = "top_long_wave";
                            }

                            if(result2_11.equals("short") && result4_11.equals("white")) {
                                if(bottomRandomNum == 0)
                                    bottomIcon = "bottom_short_white";
                                else if(bottomRandomNum == 1)
                                    bottomIcon = "bottom_short_white2";
                            }
                            else if(result2_11.equals("short") && result4_11.equals("black")) {
                                if(bottomRandomNum == 0)
                                    bottomIcon = "bottom_short_black";
                                else if(bottomRandomNum == 1)
                                    bottomIcon = "bottom_short_black2";
                            }
                            else if(result2_11.equals("short") && result4_11.equals("0")) {
                                if(bottomShortRandNum == 0)
                                    bottomIcon = "bottom_short_sky";
                                else if(bottomShortRandNum == 1)
                                    bottomIcon = "bottom_short_blue";
                                else if(bottomShortRandNum == 2)
                                    bottomIcon = "bottom_short_blue2";
                                else if(bottomShortRandNum == 3)
                                    bottomIcon = "bottom_short_gray";
                                else if(bottomShortRandNum == 4)
                                    bottomIcon = "bottom_short_brown";
                            }
                            else if(result2_11.equals("long") && result4_11.equals("white"))
                                bottomIcon = "bottom_long_white";
                            else if(result2_11.equals("long") && result4_11.equals("black"))
                                bottomIcon = "bottom_long_black";
                            else if(result2_11.equals("long") && result4_11.equals("0")) {
                                if (bottomRandomNum == 0)
                                    bottomIcon = "bottom_long_sky";
                                else if (bottomRandomNum == 1)
                                    bottomIcon = "bottom_long_blue";
                            }

                            if(result3_11.equals("1"))
                                top2Icon = "top2_yellow";

                            if(result5_11.equals("1"))
                                hatIcon = "acc_cap";

                            if(result6_11.equals("1"))
                                parasolIcon = "acc_parasol";

                            if(result7_11.equals("1"))
                                umbrellaIcon = "acc_umbrella";
                        }
                        else if((data_temp >= 32) && (data_uvi >= 8) && (data_pop >= 0 && data_pop <= 39)) {

                            if(result1_12.equals("short") && result4_12.equals("white"))
                                topIcon = "top_short_white";
                            else if(result1_12.equals("short") && result4_12.equals("black"))
                                topIcon = "top_short_black";
                            else if(result1_12.equals("short") && result4_12.equals("0")) {
                                if(topRandomNum == 0)
                                    topIcon = "top_short_rainbow";
                                else if(topRandomNum == 1)
                                    topIcon = "top_short_gray";
                                else if(topRandomNum == 2)
                                    topIcon = "top_short_wave";
                            }
                            else if(result1_12.equals("long") && result4_12.equals("white"))
                                topIcon = "top_long_white";
                            else if(result1_12.equals("long") && result4_12.equals("black"))
                                topIcon = "top_long_black";
                            else if(result1_12.equals("long") && result4_12.equals("0")) {
                                if(topRandomNum == 0)
                                    topIcon = "top_long_rainbow";
                                else if(topRandomNum == 1)
                                    topIcon = "top_long_gray";
                                else if(topRandomNum == 2)
                                    topIcon = "top_long_wave";
                            }

                            if(result2_12.equals("short") && result4_12.equals("white")) {
                                if(bottomRandomNum == 0)
                                    bottomIcon = "bottom_short_white";
                                else if(bottomRandomNum == 1)
                                    bottomIcon = "bottom_short_white2";
                            }
                            else if(result2_12.equals("short") && result4_12.equals("black")) {
                                if(bottomRandomNum == 0)
                                    bottomIcon = "bottom_short_black";
                                else if(bottomRandomNum == 1)
                                    bottomIcon = "bottom_short_black2";
                            }
                            else if(result2_12.equals("short") && result4_12.equals("0")) {
                                if(bottomShortRandNum == 0)
                                    bottomIcon = "bottom_short_sky";
                                else if(bottomShortRandNum == 1)
                                    bottomIcon = "bottom_short_blue";
                                else if(bottomShortRandNum == 2)
                                    bottomIcon = "bottom_short_blue2";
                                else if(bottomShortRandNum == 3)
                                    bottomIcon = "bottom_short_gray";
                                else if(bottomShortRandNum == 4)
                                    bottomIcon = "bottom_short_brown";
                            }
                            else if(result2_12.equals("long") && result4_12.equals("white"))
                                bottomIcon = "bottom_long_white";
                            else if(result2_12.equals("long") && result4_12.equals("black"))
                                bottomIcon = "bottom_long_black";
                            else if(result2_12.equals("long") && result4_12.equals("0")) {
                                if (bottomRandomNum == 0)
                                    bottomIcon = "bottom_long_sky";
                                else if (bottomRandomNum == 1)
                                    bottomIcon = "bottom_long_blue";
                            }

                            if(result3_12.equals("1"))
                                top2Icon = "top2_yellow";

                            if(result5_12.equals("1"))
                                hatIcon = "acc_cap";

                            if(result6_12.equals("1"))
                                parasolIcon = "acc_parasol";

                            if(result7_12.equals("1"))
                                umbrellaIcon = "acc_umbrella";
                        }
                        else if((data_temp >= 20 && data_temp <= 23) && (data_uvi >= 0 && data_uvi <= 5) && (data_pop >= 40 && data_pop <= 100)) {

                            if(result1_13.equals("short") && result4_13.equals("white"))
                                topIcon = "top_short_white";
                            else if(result1_13.equals("short") && result4_13.equals("black"))
                                topIcon = "top_short_black";
                            else if(result1_13.equals("short") && result4_13.equals("0")) {
                                if(topRandomNum == 0)
                                    topIcon = "top_short_rainbow";
                                else if(topRandomNum == 1)
                                    topIcon = "top_short_gray";
                                else if(topRandomNum == 2)
                                    topIcon = "top_short_wave";
                            }
                            else if(result1_13.equals("long") && result4_13.equals("white"))
                                topIcon = "top_long_white";
                            else if(result1_13.equals("long") && result4_13.equals("black"))
                                topIcon = "top_long_black";
                            else if(result1_13.equals("long") && result4_13.equals("0")) {
                                if(topRandomNum == 0)
                                    topIcon = "top_long_rainbow";
                                else if(topRandomNum == 1)
                                    topIcon = "top_long_gray";
                                else if(topRandomNum == 2)
                                    topIcon = "top_long_wave";
                            }

                            if(result2_13.equals("short") && result4_13.equals("white")) {
                                if(bottomRandomNum == 0)
                                    bottomIcon = "bottom_short_white";
                                else if(bottomRandomNum == 1)
                                    bottomIcon = "bottom_short_white2";
                            }
                            else if(result2_13.equals("short") && result4_13.equals("black")) {
                                if(bottomRandomNum == 0)
                                    bottomIcon = "bottom_short_black";
                                else if(bottomRandomNum == 1)
                                    bottomIcon = "bottom_short_black2";
                            }
                            else if(result2_13.equals("short") && result4_13.equals("0")) {
                                if(bottomShortRandNum == 0)
                                    bottomIcon = "bottom_short_sky";
                                else if(bottomShortRandNum == 1)
                                    bottomIcon = "bottom_short_blue";
                                else if(bottomShortRandNum == 2)
                                    bottomIcon = "bottom_short_blue2";
                                else if(bottomShortRandNum == 3)
                                    bottomIcon = "bottom_short_gray";
                                else if(bottomShortRandNum == 4)
                                    bottomIcon = "bottom_short_brown";
                            }
                            else if(result2_13.equals("long") && result4_13.equals("white"))
                                bottomIcon = "bottom_long_white";
                            else if(result2_13.equals("long") && result4_13.equals("black"))
                                bottomIcon = "bottom_long_black";
                            else if(result2_13.equals("long") && result4_13.equals("0")) {
                                if (bottomRandomNum == 0)
                                    bottomIcon = "bottom_long_sky";
                                else if (bottomRandomNum == 1)
                                    bottomIcon = "bottom_long_blue";
                            }

                            if(result3_13.equals("1"))
                                top2Icon = "top2_yellow";

                            if(result5_13.equals("1"))
                                hatIcon = "acc_cap";

                            if(result6_13.equals("1"))
                                parasolIcon = "acc_parasol";

                            if(result7_13.equals("1"))
                                umbrellaIcon = "acc_umbrella";
                        }
                        else if((data_temp >= 24 && data_temp <= 27) && (data_uvi >= 0 && data_uvi <= 5) && (data_pop >= 40 && data_pop <= 100)) {

                            if(result1_14.equals("short") && result4_14.equals("white"))
                                topIcon = "top_short_white";
                            else if(result1_14.equals("short") && result4_14.equals("black"))
                                topIcon = "top_short_black";
                            else if(result1_14.equals("short") && result4_14.equals("0")) {
                                if(topRandomNum == 0)
                                    topIcon = "top_short_rainbow";
                                else if(topRandomNum == 1)
                                    topIcon = "top_short_gray";
                                else if(topRandomNum == 2)
                                    topIcon = "top_short_wave";
                            }
                            else if(result1_14.equals("long") && result4_14.equals("white"))
                                topIcon = "top_long_white";
                            else if(result1_14.equals("long") && result4_14.equals("black"))
                                topIcon = "top_long_black";
                            else if(result1_14.equals("long") && result4_14.equals("0")) {
                                if(topRandomNum == 0)
                                    topIcon = "top_long_rainbow";
                                else if(topRandomNum == 1)
                                    topIcon = "top_long_gray";
                                else if(topRandomNum == 2)
                                    topIcon = "top_long_wave";
                            }

                            if(result2_14.equals("short") && result4_14.equals("white")) {
                                if(bottomRandomNum == 0)
                                    bottomIcon = "bottom_short_white";
                                else if(bottomRandomNum == 1)
                                    bottomIcon = "bottom_short_white2";
                            }
                            else if(result2_14.equals("short") && result4_14.equals("black")) {
                                if(bottomRandomNum == 0)
                                    bottomIcon = "bottom_short_black";
                                else if(bottomRandomNum == 1)
                                    bottomIcon = "bottom_short_black2";
                            }
                            else if(result2_14.equals("short") && result4_14.equals("0")) {
                                if(bottomShortRandNum == 0)
                                    bottomIcon = "bottom_short_sky";
                                else if(bottomShortRandNum == 1)
                                    bottomIcon = "bottom_short_blue";
                                else if(bottomShortRandNum == 2)
                                    bottomIcon = "bottom_short_blue2";
                                else if(bottomShortRandNum == 3)
                                    bottomIcon = "bottom_short_gray";
                                else if(bottomShortRandNum == 4)
                                    bottomIcon = "bottom_short_brown";
                            }
                            else if(result2_14.equals("long") && result4_14.equals("white"))
                                bottomIcon = "bottom_long_white";
                            else if(result2_14.equals("long") && result4_14.equals("black"))
                                bottomIcon = "bottom_long_black";
                            else if(result2_14.equals("long") && result4_14.equals("0")) {
                                if (bottomRandomNum == 0)
                                    bottomIcon = "bottom_long_sky";
                                else if (bottomRandomNum == 1)
                                    bottomIcon = "bottom_long_blue";
                            }

                            if(result3_14.equals("1"))
                                top2Icon = "top2_yellow";

                            if(result5_14.equals("1"))
                                hatIcon = "acc_cap";

                            if(result6_14.equals("1"))
                                parasolIcon = "acc_parasol";

                            if(result7_14.equals("1"))
                                umbrellaIcon = "acc_umbrella";
                        }
                        else if((data_temp >= 28 && data_temp <= 31) && (data_uvi >= 0 && data_uvi <= 5) && (data_pop >= 40 && data_pop <= 100)) {

                            if(result1_15.equals("short") && result4_15.equals("white"))
                                topIcon = "top_short_white";
                            else if(result1_15.equals("short") && result4_15.equals("black"))
                                topIcon = "top_short_black";
                            else if(result1_15.equals("short") && result4_15.equals("0")) {
                                if(topRandomNum == 0)
                                    topIcon = "top_short_rainbow";
                                else if(topRandomNum == 1)
                                    topIcon = "top_short_gray";
                                else if(topRandomNum == 2)
                                    topIcon = "top_short_wave";
                            }
                            else if(result1_15.equals("long") && result4_15.equals("white"))
                                topIcon = "top_long_white";
                            else if(result1_15.equals("long") && result4_15.equals("black"))
                                topIcon = "top_long_black";
                            else if(result1_15.equals("long") && result4_15.equals("0")) {
                                if(topRandomNum == 0)
                                    topIcon = "top_long_rainbow";
                                else if(topRandomNum == 1)
                                    topIcon = "top_long_gray";
                                else if(topRandomNum == 2)
                                    topIcon = "top_long_wave";
                            }

                            if(result2_15.equals("short") && result4_15.equals("white")) {
                                if(bottomRandomNum == 0)
                                    bottomIcon = "bottom_short_white";
                                else if(bottomRandomNum == 1)
                                    bottomIcon = "bottom_short_white2";
                            }
                            else if(result2_15.equals("short") && result4_15.equals("black")) {
                                if(bottomRandomNum == 0)
                                    bottomIcon = "bottom_short_black";
                                else if(bottomRandomNum == 1)
                                    bottomIcon = "bottom_short_black2";
                            }
                            else if(result2_15.equals("short") && result4_15.equals("0")) {
                                if(bottomShortRandNum == 0)
                                    bottomIcon = "bottom_short_sky";
                                else if(bottomShortRandNum == 1)
                                    bottomIcon = "bottom_short_blue";
                                else if(bottomShortRandNum == 2)
                                    bottomIcon = "bottom_short_blue2";
                                else if(bottomShortRandNum == 3)
                                    bottomIcon = "bottom_short_gray";
                                else if(bottomShortRandNum == 4)
                                    bottomIcon = "bottom_short_brown";
                            }
                            else if(result2_15.equals("long") && result4_15.equals("white"))
                                bottomIcon = "bottom_long_white";
                            else if(result2_15.equals("long") && result4_15.equals("black"))
                                bottomIcon = "bottom_long_black";
                            else if(result2_15.equals("long") && result4_15.equals("0")) {
                                if (bottomRandomNum == 0)
                                    bottomIcon = "bottom_long_sky";
                                else if (bottomRandomNum == 1)
                                    bottomIcon = "bottom_long_blue";
                            }

                            if(result3_15.equals("1"))
                                top2Icon = "top2_yellow";

                            if(result5_15.equals("1"))
                                hatIcon = "acc_cap";

                            if(result6_15.equals("1"))
                                parasolIcon = "acc_parasol";

                            if(result7_15.equals("1"))
                                umbrellaIcon = "acc_umbrella";
                        }
                        else if((data_temp >= 32) && (data_uvi >= 0 && data_uvi <= 5) && (data_pop >= 40 && data_pop <= 100)) {

                            if(result1_16.equals("short") && result4_16.equals("white"))
                                topIcon = "top_short_white";
                            else if(result1_16.equals("short") && result4_16.equals("black"))
                                topIcon = "top_short_black";
                            else if(result1_16.equals("short") && result4_16.equals("0")) {
                                if(topRandomNum == 0)
                                    topIcon = "top_short_rainbow";
                                else if(topRandomNum == 1)
                                    topIcon = "top_short_gray";
                                else if(topRandomNum == 2)
                                    topIcon = "top_short_wave";
                            }
                            else if(result1_16.equals("long") && result4_16.equals("white"))
                                topIcon = "top_long_white";
                            else if(result1_16.equals("long") && result4_16.equals("black"))
                                topIcon = "top_long_black";
                            else if(result1_16.equals("long") && result4_16.equals("0")) {
                                if(topRandomNum == 0)
                                    topIcon = "top_long_rainbow";
                                else if(topRandomNum == 1)
                                    topIcon = "top_long_gray";
                                else if(topRandomNum == 2)
                                    topIcon = "top_long_wave";
                            }

                            if(result2_16.equals("short") && result4_16.equals("white")) {
                                if(bottomRandomNum == 0)
                                    bottomIcon = "bottom_short_white";
                                else if(bottomRandomNum == 1)
                                    bottomIcon = "bottom_short_white2";
                            }
                            else if(result2_16.equals("short") && result4_16.equals("black")) {
                                if(bottomRandomNum == 0)
                                    bottomIcon = "bottom_short_black";
                                else if(bottomRandomNum == 1)
                                    bottomIcon = "bottom_short_black2";
                            }
                            else if(result2_16.equals("short") && result4_16.equals("0")) {
                                if(bottomShortRandNum == 0)
                                    bottomIcon = "bottom_short_sky";
                                else if(bottomShortRandNum == 1)
                                    bottomIcon = "bottom_short_blue";
                                else if(bottomShortRandNum == 2)
                                    bottomIcon = "bottom_short_blue2";
                                else if(bottomShortRandNum == 3)
                                    bottomIcon = "bottom_short_gray";
                                else if(bottomShortRandNum == 4)
                                    bottomIcon = "bottom_short_brown";
                            }
                            else if(result2_16.equals("long") && result4_16.equals("white"))
                                bottomIcon = "bottom_long_white";
                            else if(result2_16.equals("long") && result4_16.equals("black"))
                                bottomIcon = "bottom_long_black";
                            else if(result2_16.equals("long") && result4_16.equals("0")) {
                                if (bottomRandomNum == 0)
                                    bottomIcon = "bottom_long_sky";
                                else if (bottomRandomNum == 1)
                                    bottomIcon = "bottom_long_blue";
                            }

                            if(result3_16.equals("1"))
                                top2Icon = "top2_yellow";

                            if(result5_16.equals("1"))
                                hatIcon = "acc_cap";

                            if(result6_16.equals("1"))
                                parasolIcon = "acc_parasol";

                            if(result7_16.equals("1"))
                                umbrellaIcon = "acc_umbrella";
                        }
                        else if((data_temp >= 20 && data_temp <= 23) && (data_uvi >= 6 && data_uvi <= 8) && (data_pop >= 40 && data_pop <= 100)) {

                            if(result1_17.equals("short") && result4_17.equals("white"))
                                topIcon = "top_short_white";
                            else if(result1_17.equals("short") && result4_17.equals("black"))
                                topIcon = "top_short_black";
                            else if(result1_17.equals("short") && result4_17.equals("0")) {
                                if(topRandomNum == 0)
                                    topIcon = "top_short_rainbow";
                                else if(topRandomNum == 1)
                                    topIcon = "top_short_gray";
                                else if(topRandomNum == 2)
                                    topIcon = "top_short_wave";
                            }
                            else if(result1_17.equals("long") && result4_17.equals("white"))
                                topIcon = "top_long_white";
                            else if(result1_17.equals("long") && result4_17.equals("black"))
                                topIcon = "top_long_black";
                            else if(result1_17.equals("long") && result4_17.equals("0")) {
                                if(topRandomNum == 0)
                                    topIcon = "top_long_rainbow";
                                else if(topRandomNum == 1)
                                    topIcon = "top_long_gray";
                                else if(topRandomNum == 2)
                                    topIcon = "top_long_wave";
                            }

                            if(result2_17.equals("short") && result4_17.equals("white")) {
                                if(bottomRandomNum == 0)
                                    bottomIcon = "bottom_short_white";
                                else if(bottomRandomNum == 1)
                                    bottomIcon = "bottom_short_white2";
                            }
                            else if(result2_17.equals("short") && result4_17.equals("black")) {
                                if(bottomRandomNum == 0)
                                    bottomIcon = "bottom_short_black";
                                else if(bottomRandomNum == 1)
                                    bottomIcon = "bottom_short_black2";
                            }
                            else if(result2_17.equals("short") && result4_17.equals("0")) {
                                if(bottomShortRandNum == 0)
                                    bottomIcon = "bottom_short_sky";
                                else if(bottomShortRandNum == 1)
                                    bottomIcon = "bottom_short_blue";
                                else if(bottomShortRandNum == 2)
                                    bottomIcon = "bottom_short_blue2";
                                else if(bottomShortRandNum == 3)
                                    bottomIcon = "bottom_short_gray";
                                else if(bottomShortRandNum == 4)
                                    bottomIcon = "bottom_short_brown";
                            }
                            else if(result2_17.equals("long") && result4_17.equals("white"))
                                bottomIcon = "bottom_long_white";
                            else if(result2_17.equals("long") && result4_17.equals("black"))
                                bottomIcon = "bottom_long_black";
                            else if(result2_17.equals("long") && result4_17.equals("0")) {
                                if (bottomRandomNum == 0)
                                    bottomIcon = "bottom_long_sky";
                                else if (bottomRandomNum == 1)
                                    bottomIcon = "bottom_long_blue";
                            }

                            if(result3_17.equals("1"))
                                top2Icon = "top2_yellow";

                            if(result5_17.equals("1"))
                                hatIcon = "acc_cap";

                            if(result6_17.equals("1"))
                                parasolIcon = "acc_parasol";

                            if(result7_17.equals("1"))
                                umbrellaIcon = "acc_umbrella";
                        }
                        else if((data_temp >= 24 && data_temp <= 27) && (data_uvi >= 6 && data_uvi <= 8) && (data_pop >= 40 && data_pop <= 100)) {

                            if(result1_18.equals("short") && result4_18.equals("white"))
                                topIcon = "top_short_white";
                            else if(result1_18.equals("short") && result4_18.equals("black"))
                                topIcon = "top_short_black";
                            else if(result1_18.equals("short") && result4_18.equals("0")) {
                                if(topRandomNum == 0)
                                    topIcon = "top_short_rainbow";
                                else if(topRandomNum == 1)
                                    topIcon = "top_short_gray";
                                else if(topRandomNum == 2)
                                    topIcon = "top_short_wave";
                            }
                            else if(result1_18.equals("long") && result4_18.equals("white"))
                                topIcon = "top_long_white";
                            else if(result1_18.equals("long") && result4_18.equals("black"))
                                topIcon = "top_long_black";
                            else if(result1_18.equals("long") && result4_18.equals("0")) {
                                if(topRandomNum == 0)
                                    topIcon = "top_long_rainbow";
                                else if(topRandomNum == 1)
                                    topIcon = "top_long_gray";
                                else if(topRandomNum == 2)
                                    topIcon = "top_long_wave";
                            }

                            if(result2_18.equals("short") && result4_18.equals("white")) {
                                if(bottomRandomNum == 0)
                                    bottomIcon = "bottom_short_white";
                                else if(bottomRandomNum == 1)
                                    bottomIcon = "bottom_short_white2";
                            }
                            else if(result2_18.equals("short") && result4_18.equals("black")) {
                                if(bottomRandomNum == 0)
                                    bottomIcon = "bottom_short_black";
                                else if(bottomRandomNum == 1)
                                    bottomIcon = "bottom_short_black2";
                            }
                            else if(result2_18.equals("short") && result4_18.equals("0")) {
                                if(bottomShortRandNum == 0)
                                    bottomIcon = "bottom_short_sky";
                                else if(bottomShortRandNum == 1)
                                    bottomIcon = "bottom_short_blue";
                                else if(bottomShortRandNum == 2)
                                    bottomIcon = "bottom_short_blue2";
                                else if(bottomShortRandNum == 3)
                                    bottomIcon = "bottom_short_gray";
                                else if(bottomShortRandNum == 4)
                                    bottomIcon = "bottom_short_brown";
                            }
                            else if(result2_18.equals("long") && result4_18.equals("white"))
                                bottomIcon = "bottom_long_white";
                            else if(result2_18.equals("long") && result4_18.equals("black"))
                                bottomIcon = "bottom_long_black";
                            else if(result2_18.equals("long") && result4_18.equals("0")) {
                                if (bottomRandomNum == 0)
                                    bottomIcon = "bottom_long_sky";
                                else if (bottomRandomNum == 1)
                                    bottomIcon = "bottom_long_blue";
                            }

                            if(result3_18.equals("1"))
                                top2Icon = "top2_yellow";

                            if(result5_18.equals("1"))
                                hatIcon = "acc_cap";

                            if(result6_18.equals("1"))
                                parasolIcon = "acc_parasol";

                            if(result7_18.equals("1"))
                                umbrellaIcon = "acc_umbrella";
                        }
                        else if((data_temp >= 28 && data_temp <= 31) && (data_uvi >= 6 && data_uvi <= 8) && (data_pop >= 40 && data_pop <= 100)) {

                            if(result1_19.equals("short") && result4_19.equals("white"))
                                topIcon = "top_short_white";
                            else if(result1_19.equals("short") && result4_19.equals("black"))
                                topIcon = "top_short_black";
                            else if(result1_19.equals("short") && result4_19.equals("0")) {
                                if(topRandomNum == 0)
                                    topIcon = "top_short_rainbow";
                                else if(topRandomNum == 1)
                                    topIcon = "top_short_gray";
                                else if(topRandomNum == 2)
                                    topIcon = "top_short_wave";
                            }
                            else if(result1_19.equals("long") && result4_19.equals("white"))
                                topIcon = "top_long_white";
                            else if(result1_19.equals("long") && result4_19.equals("black"))
                                topIcon = "top_long_black";
                            else if(result1_19.equals("long") && result4_19.equals("0")) {
                                if(topRandomNum == 0)
                                    topIcon = "top_long_rainbow";
                                else if(topRandomNum == 1)
                                    topIcon = "top_long_gray";
                                else if(topRandomNum == 2)
                                    topIcon = "top_long_wave";
                            }

                            if(result2_19.equals("short") && result4_19.equals("white")) {
                                if(bottomRandomNum == 0)
                                    bottomIcon = "bottom_short_white";
                                else if(bottomRandomNum == 1)
                                    bottomIcon = "bottom_short_white2";
                            }
                            else if(result2_19.equals("short") && result4_19.equals("black")) {
                                if(bottomRandomNum == 0)
                                    bottomIcon = "bottom_short_black";
                                else if(bottomRandomNum == 1)
                                    bottomIcon = "bottom_short_black2";
                            }
                            else if(result2_19.equals("short") && result4_19.equals("0")) {
                                if(bottomShortRandNum == 0)
                                    bottomIcon = "bottom_short_sky";
                                else if(bottomShortRandNum == 1)
                                    bottomIcon = "bottom_short_blue";
                                else if(bottomShortRandNum == 2)
                                    bottomIcon = "bottom_short_blue2";
                                else if(bottomShortRandNum == 3)
                                    bottomIcon = "bottom_short_gray";
                                else if(bottomShortRandNum == 4)
                                    bottomIcon = "bottom_short_brown";
                            }
                            else if(result2_19.equals("long") && result4_19.equals("white"))
                                bottomIcon = "bottom_long_white";
                            else if(result2_19.equals("long") && result4_19.equals("black"))
                                bottomIcon = "bottom_long_black";
                            else if(result2_19.equals("long") && result4_19.equals("0")) {
                                if (bottomRandomNum == 0)
                                    bottomIcon = "bottom_long_sky";
                                else if (bottomRandomNum == 1)
                                    bottomIcon = "bottom_long_blue";
                            }

                            if(result3_19.equals("1"))
                                top2Icon = "top2_yellow";


                            if(result5_19.equals("1"))
                                hatIcon = "acc_cap";

                            if(result6_19.equals("1"))
                                parasolIcon = "acc_parasol";

                            if(result7_19.equals("1"))
                                umbrellaIcon = "acc_umbrella";

                        }
                        else if((data_temp >= 32) && (data_uvi >= 6 && data_uvi <= 8) && (data_pop >= 40 && data_pop <= 100)) {

                            if(result1_20.equals("short") && result4_20.equals("white"))
                                topIcon = "top_short_white";
                            else if(result1_20.equals("short") && result4_20.equals("black"))
                                topIcon = "top_short_black";
                            else if(result1_20.equals("short") && result4_20.equals("0")) {
                                if(topRandomNum == 0)
                                    topIcon = "top_short_rainbow";
                                else if(topRandomNum == 1)
                                    topIcon = "top_short_gray";
                                else if(topRandomNum == 2)
                                    topIcon = "top_short_wave";
                            }
                            else if(result1_20.equals("long") && result4_20.equals("white"))
                                topIcon = "top_long_white";
                            else if(result1_20.equals("long") && result4_20.equals("black"))
                                topIcon = "top_long_black";
                            else if(result1_20.equals("long") && result4_20.equals("0")) {
                                if(topRandomNum == 0)
                                    topIcon = "top_long_rainbow";
                                else if(topRandomNum == 1)
                                    topIcon = "top_long_gray";
                                else if(topRandomNum == 2)
                                    topIcon = "top_long_wave";
                            }

                            if(result2_20.equals("short") && result4_20.equals("white")) {
                                if(bottomRandomNum == 0)
                                    bottomIcon = "bottom_short_white";
                                else if(bottomRandomNum == 1)
                                    bottomIcon = "bottom_short_white2";
                            }
                            else if(result2_20.equals("short") && result4_20.equals("black")) {
                                if(bottomRandomNum == 0)
                                    bottomIcon = "bottom_short_black";
                                else if(bottomRandomNum == 1)
                                    bottomIcon = "bottom_short_black2";
                            }
                            else if(result2_20.equals("short") && result4_20.equals("0")) {
                                if(bottomShortRandNum == 0)
                                    bottomIcon = "bottom_short_sky";
                                else if(bottomShortRandNum == 1)
                                    bottomIcon = "bottom_short_blue";
                                else if(bottomShortRandNum == 2)
                                    bottomIcon = "bottom_short_blue2";
                                else if(bottomShortRandNum == 3)
                                    bottomIcon = "bottom_short_gray";
                                else if(bottomShortRandNum == 4)
                                    bottomIcon = "bottom_short_brown";
                            }
                            else if(result2_20.equals("long") && result4_20.equals("white"))
                                bottomIcon = "bottom_long_white";
                            else if(result2_20.equals("long") && result4_20.equals("black"))
                                bottomIcon = "bottom_long_black";
                            else if(result2_20.equals("long") && result4_20.equals("0")) {
                                if (bottomRandomNum == 0)
                                    bottomIcon = "bottom_long_sky";
                                else if (bottomRandomNum == 1)
                                    bottomIcon = "bottom_long_blue";
                            }

                            if(result3_20.equals("1"))
                                top2Icon = "top2_yellow";

                            if(result5_20.equals("1"))
                                hatIcon = "acc_cap";

                            if(result6_20.equals("1"))
                                parasolIcon = "acc_parasol";

                            if(result7_20.equals("1"))
                                umbrellaIcon = "acc_umbrella";

                        }
                        else if((data_temp >= 20 && data_temp <= 23) && (data_uvi >= 8) && (data_pop >= 40 && data_pop <= 100)) {

                            if(result1_21.equals("short") && result4_21.equals("white"))
                                topIcon = "top_short_white";
                            else if(result1_21.equals("short") && result4_21.equals("black"))
                                topIcon = "top_short_black";
                            else if(result1_21.equals("short") && result4_21.equals("0")) {
                                if(topRandomNum == 0)
                                    topIcon = "top_short_rainbow";
                                else if(topRandomNum == 1)
                                    topIcon = "top_short_gray";
                                else if(topRandomNum == 2)
                                    topIcon = "top_short_wave";
                            }
                            else if(result1_21.equals("long") && result4_21.equals("white"))
                                topIcon = "top_long_white";
                            else if(result1_21.equals("long") && result4_21.equals("black"))
                                topIcon = "top_long_black";
                            else if(result1_21.equals("long") && result4_21.equals("0")) {
                                if(topRandomNum == 0)
                                    topIcon = "top_long_rainbow";
                                else if(topRandomNum == 1)
                                    topIcon = "top_long_gray";
                                else if(topRandomNum == 2)
                                    topIcon = "top_long_wave";
                            }

                            if(result2_21.equals("short") && result4_21.equals("white")) {
                                if(bottomRandomNum == 0)
                                    bottomIcon = "bottom_short_white";
                                else if(bottomRandomNum == 1)
                                    bottomIcon = "bottom_short_white2";
                            }
                            else if(result2_21.equals("short") && result4_21.equals("black")) {
                                if(bottomRandomNum == 0)
                                    bottomIcon = "bottom_short_black";
                                else if(bottomRandomNum == 1)
                                    bottomIcon = "bottom_short_black2";
                            }
                            else if(result2_21.equals("short") && result4_21.equals("0")) {
                                if(bottomShortRandNum == 0)
                                    bottomIcon = "bottom_short_sky";
                                else if(bottomShortRandNum == 1)
                                    bottomIcon = "bottom_short_blue";
                                else if(bottomShortRandNum == 2)
                                    bottomIcon = "bottom_short_blue2";
                                else if(bottomShortRandNum == 3)
                                    bottomIcon = "bottom_short_gray";
                                else if(bottomShortRandNum == 4)
                                    bottomIcon = "bottom_short_brown";
                            }
                            else if(result2_21.equals("long") && result4_21.equals("white"))
                                bottomIcon = "bottom_long_white";
                            else if(result2_21.equals("long") && result4_21.equals("black"))
                                bottomIcon = "bottom_long_black";
                            else if(result2_21.equals("long") && result4_21.equals("0")) {
                                if (bottomRandomNum == 0)
                                    bottomIcon = "bottom_long_sky";
                                else if (bottomRandomNum == 1)
                                    bottomIcon = "bottom_long_blue";
                            }

                            if(result3_21.equals("1"))
                                top2Icon = "top2_yellow";

                            if(result5_21.equals("1"))
                                hatIcon = "acc_cap";

                            if(result6_21.equals("1"))
                                parasolIcon = "acc_parasol";

                            if(result7_21.equals("1"))
                                umbrellaIcon = "acc_umbrella";

                        }
                        else if((data_temp >= 24 && data_temp <= 27) && (data_uvi >= 8) && (data_pop >= 40 && data_pop <= 100)) {

                            if(result1_22.equals("short") && result4_22.equals("white"))
                                topIcon = "top_short_white";
                            else if(result1_22.equals("short") && result4_22.equals("black"))
                                topIcon = "top_short_black";
                            else if(result1_22.equals("short") && result4_22.equals("0")) {
                                if(topRandomNum == 0)
                                    topIcon = "top_short_rainbow";
                                else if(topRandomNum == 1)
                                    topIcon = "top_short_gray";
                                else if(topRandomNum == 2)
                                    topIcon = "top_short_wave";
                            }
                            else if(result1_22.equals("long") && result4_22.equals("white"))
                                topIcon = "top_long_white";
                            else if(result1_22.equals("long") && result4_22.equals("black"))
                                topIcon = "top_long_black";
                            else if(result1_22.equals("long") && result4_22.equals("0")) {
                                if(topRandomNum == 0)
                                    topIcon = "top_long_rainbow";
                                else if(topRandomNum == 1)
                                    topIcon = "top_long_gray";
                                else if(topRandomNum == 2)
                                    topIcon = "top_long_wave";
                            }

                            if(result2_22.equals("short") && result4_22.equals("white")) {
                                if(bottomRandomNum == 0)
                                    bottomIcon = "bottom_short_white";
                                else if(bottomRandomNum == 1)
                                    bottomIcon = "bottom_short_white2";
                            }
                            else if(result2_22.equals("short") && result4_22.equals("black")) {
                                if(bottomRandomNum == 0)
                                    bottomIcon = "bottom_short_black";
                                else if(bottomRandomNum == 1)
                                    bottomIcon = "bottom_short_black2";
                            }
                            else if(result2_22.equals("short") && result4_22.equals("0")) {
                                if(bottomShortRandNum == 0)
                                    bottomIcon = "bottom_short_sky";
                                else if(bottomShortRandNum == 1)
                                    bottomIcon = "bottom_short_blue";
                                else if(bottomShortRandNum == 2)
                                    bottomIcon = "bottom_short_blue2";
                                else if(bottomShortRandNum == 3)
                                    bottomIcon = "bottom_short_gray";
                                else if(bottomShortRandNum == 4)
                                    bottomIcon = "bottom_short_brown";
                            }
                            else if(result2_22.equals("long") && result4_22.equals("white"))
                                bottomIcon = "bottom_long_white";
                            else if(result2_22.equals("long") && result4_22.equals("black"))
                                bottomIcon = "bottom_long_black";
                            else if(result2_22.equals("long") && result4_22.equals("0")) {
                                if (bottomRandomNum == 0)
                                    bottomIcon = "bottom_long_sky";
                                else if (bottomRandomNum == 1)
                                    bottomIcon = "bottom_long_blue";
                            }

                            if(result3_22.equals("1"))
                                top2Icon = "top2_yellow";

                            if(result5_22.equals("1"))
                                hatIcon = "acc_cap";

                            if(result6_22.equals("1"))
                                parasolIcon = "acc_parasol";

                            if(result7_22.equals("1"))
                                umbrellaIcon = "acc_umbrella";
                        }
                        else if((data_temp >= 28 && data_temp <= 31) && (data_uvi >= 8) && (data_pop >= 40 && data_pop <= 100)) {

                            if(result1_23.equals("short") && result4_23.equals("white"))
                                topIcon = "top_short_white";
                            else if(result1_23.equals("short") && result4_23.equals("black"))
                                topIcon = "top_short_black";
                            else if(result1_23.equals("short") && result4_23.equals("0")) {
                                if(topRandomNum == 0)
                                    topIcon = "top_short_rainbow";
                                else if(topRandomNum == 1)
                                    topIcon = "top_short_gray";
                                else if(topRandomNum == 2)
                                    topIcon = "top_short_wave";
                            }
                            else if(result1_23.equals("long") && result4_23.equals("white"))
                                topIcon = "top_long_white";
                            else if(result1_23.equals("long") && result4_23.equals("black"))
                                topIcon = "top_long_black";
                            else if(result1_23.equals("long") && result4_23.equals("0")) {
                                if(topRandomNum == 0)
                                    topIcon = "top_long_rainbow";
                                else if(topRandomNum == 1)
                                    topIcon = "top_long_gray";
                                else if(topRandomNum == 2)
                                    topIcon = "top_long_wave";
                            }

                            if(result2_23.equals("short") && result4_23.equals("white")) {
                                if(bottomRandomNum == 0)
                                    bottomIcon = "bottom_short_white";
                                else if(bottomRandomNum == 1)
                                    bottomIcon = "bottom_short_white2";
                            }
                            else if(result2_23.equals("short") && result4_23.equals("black")) {
                                if(bottomRandomNum == 0)
                                    bottomIcon = "bottom_short_black";
                                else if(bottomRandomNum == 1)
                                    bottomIcon = "bottom_short_black2";
                            }
                            else if(result2_23.equals("short") && result4_23.equals("0")) {
                                if(bottomShortRandNum == 0)
                                    bottomIcon = "bottom_short_sky";
                                else if(bottomShortRandNum == 1)
                                    bottomIcon = "bottom_short_blue";
                                else if(bottomShortRandNum == 2)
                                    bottomIcon = "bottom_short_blue2";
                                else if(bottomShortRandNum == 3)
                                    bottomIcon = "bottom_short_gray";
                                else if(bottomShortRandNum == 4)
                                    bottomIcon = "bottom_short_brown";
                            }
                            else if(result2_23.equals("long") && result4_23.equals("white"))
                                bottomIcon = "bottom_long_white";
                            else if(result2_23.equals("long") && result4_23.equals("black"))
                                bottomIcon = "bottom_long_black";
                            else if(result2_23.equals("long") && result4_23.equals("0")) {
                                if (bottomRandomNum == 0)
                                    bottomIcon = "bottom_long_sky";
                                else if (bottomRandomNum == 1)
                                    bottomIcon = "bottom_long_blue";
                            }

                            if(result3_23.equals("1"))
                                top2Icon = "top2_yellow";

                            if(result5_23.equals("1"))
                                hatIcon = "acc_cap";

                            if(result6_23.equals("1"))
                                parasolIcon = "acc_parasol";

                            if(result7_23.equals("1"))
                                umbrellaIcon = "acc_umbrella";
                        }
                        else if((data_temp >= 32) && (data_uvi >= 8) && (data_pop >= 40 && data_pop <= 100)) {

                            if(result1_24.equals("short") && result4_24.equals("white"))
                                topIcon = "top_short_white";
                            else if(result1_24.equals("short") && result4_24.equals("black"))
                                topIcon = "top_short_black";
                            else if(result1_24.equals("short") && result4_24.equals("0")) {
                                if(topRandomNum == 0)
                                    topIcon = "top_short_rainbow";
                                else if(topRandomNum == 1)
                                    topIcon = "top_short_gray";
                                else if(topRandomNum == 2)
                                    topIcon = "top_short_wave";
                            }
                            else if(result1_24.equals("long") && result4_24.equals("white"))
                                topIcon = "top_long_white";
                            else if(result1_24.equals("long") && result4_24.equals("black"))
                                topIcon = "top_long_black";
                            else if(result1_24.equals("long") && result4_24.equals("0")) {
                                if(topRandomNum == 0)
                                    topIcon = "top_long_rainbow";
                                else if(topRandomNum == 1)
                                    topIcon = "top_long_gray";
                                else if(topRandomNum == 2)
                                    topIcon = "top_long_wave";
                            }

                            if(result2_24.equals("short") && result4_24.equals("white")) {
                                if(bottomRandomNum == 0)
                                    bottomIcon = "bottom_short_white";
                                else if(bottomRandomNum == 1)
                                    bottomIcon = "bottom_short_white2";
                            }
                            else if(result2_24.equals("short") && result4_24.equals("black")) {
                                if(bottomRandomNum == 0)
                                    bottomIcon = "bottom_short_black";
                                else if(bottomRandomNum == 1)
                                    bottomIcon = "bottom_short_black2";
                            }
                            else if(result2_24.equals("short") && result4_24.equals("0")) {
                                if(bottomShortRandNum == 0)
                                    bottomIcon = "bottom_short_sky";
                                else if(bottomShortRandNum == 1)
                                    bottomIcon = "bottom_short_blue";
                                else if(bottomShortRandNum == 2)
                                    bottomIcon = "bottom_short_blue2";
                                else if(bottomShortRandNum == 3)
                                    bottomIcon = "bottom_short_gray";
                                else if(bottomShortRandNum == 4)
                                    bottomIcon = "bottom_short_brown";
                            }
                            else if(result2_24.equals("long") && result4_24.equals("white"))
                                bottomIcon = "bottom_long_white";
                            else if(result2_24.equals("long") && result4_24.equals("black"))
                                bottomIcon = "bottom_long_black";
                            else if(result2_24.equals("long") && result4_24.equals("0")) {
                                if (bottomRandomNum == 0)
                                    bottomIcon = "bottom_long_sky";
                                else if (bottomRandomNum == 1)
                                    bottomIcon = "bottom_long_blue";
                            }

                            if(result3_24.equals("1"))
                                top2Icon = "top2_yellow";

                            if(result5_24.equals("1"))
                                hatIcon = "acc_cap";

                            if(result6_24.equals("1"))
                                umbrellaIcon = "acc_parasol";

                            if(result7_24.equals("1"))
                                parasolIcon = "acc_umbrella";
                        }
                    }

                    String topImageURL = BuildConfig.BASE_SERVER_URL + "/" + topIcon + ".png";
                    Glide.with(getActivity()).load(topImageURL).into(iv_top);

                    String bottomImageURL = BuildConfig.BASE_SERVER_URL + "/" + bottomIcon + ".png";
                    Glide.with(getActivity()).load(bottomImageURL).into(iv_bottom);

                    String top2ImageURL = BuildConfig.BASE_SERVER_URL + "/" + top2Icon + ".png";
                    Glide.with(getActivity()).load(top2ImageURL).into(iv_top2);

                    String hatImageURL = BuildConfig.BASE_SERVER_URL + "/" + hatIcon + ".png";
                    Glide.with(getActivity()).load(hatImageURL).into(iv_hat);

                    if(shoesRandomNum == 0)
                        shoesIcon = "acc_shoes";
                    else if(shoesRandomNum == 1)
                        shoesIcon = "acc_flop";

                    String shoesImageURL = BuildConfig.BASE_SERVER_URL + "/acc_shoes.png";
                    Glide.with(getActivity()).load(shoesImageURL).into(iv_shoes);

                    String umbrellaImageURL = BuildConfig.BASE_SERVER_URL + "/" + umbrellaIcon + ".png";
                    Glide.with(getActivity()).load(umbrellaImageURL).into(iv_umbrella);

                    String parasolImageURL = BuildConfig.BASE_SERVER_URL + "/" + parasolIcon + ".png";
                    Glide.with(getActivity()).load(parasolImageURL).into(iv_parasol);

                    String maskImageURL = BuildConfig.BASE_SERVER_URL + "/acc_mask.png";
                    Glide.with(getActivity()).load(maskImageURL).into(iv_mask);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                return params;
            }
        };

        request.setShouldCache(false);
        requestQueue.add(request);
    }

}

