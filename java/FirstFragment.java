package com.example.swipeex;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

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

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Random;

public class FirstFragment extends Fragment {
    private GpsTracker gpsTracker;
    double lat;
    double lng;
    String address;

    TextView tv_ment1, tv_ment_disease;
    TextView tv_time1, tv_time2, tv_time3, tv_time4, tv_time5, tv_time6, tv_time7, tv_time8;
    TextView tv_temp1, tv_temp2, tv_temp3, tv_temp4, tv_temp5, tv_temp6, tv_temp7, tv_temp8;
    ImageView iv_icon1, iv_icon2, iv_icon3, iv_icon4, iv_icon5, iv_icon6, iv_icon7, iv_icon8;
    androidx.constraintlayout.widget.ConstraintLayout background;

    TextView tv_updateTime, tv_city, tv_currentTemp, tv_currentMinMax;
    ImageView iv_currentIcon;
    ImageButton ib_update;
    String currentIcon;
    int tempDo, uvi;

    int topRandomNum, top2RandomNum, bottomRandomNum, bottomShortRandNum, shoesRandomNum;
    ImageView iv_top, iv_top2, iv_bottom, iv_hat, iv_parasol, iv_umbrella;
    String topIcon, top2Icon, bottomIcon, hatIcon, parasolIcon, umbrellaIcon;

    static RequestQueue requestQueue;

    String id, disease_1st, disease_2nd, disease_3rd, disease_4th, disease_5th, disease_6th, disease_7th, disease_8th;
    String UserDisease, ment_disease_1, ment_weather_1, ment_weather_2, ment_weather_3, ment_weather_4;
    String ment_disease_2 = "";
    String ment_disease_3 = "";
    String ment_disease_4 = "";
    String ment_disease_5 = "";
    String ment_disease_6 = "";
    int c;

    int data_temp, data_uvi, data_pop;

    ArrayList<String> diseaseList = new ArrayList<String>();
    ArrayList<String> diseaseList2 = new ArrayList<String>();
    ArrayList<String> diseaseList3 = new ArrayList<String>();
    ArrayList<String> diseaseList4 = new ArrayList<String>();
    ArrayList<String> diseaseList5 = new ArrayList<String>();
    ArrayList<String> diseaseList6 = new ArrayList<String>();
    ArrayList<String> diseaseList7 = new ArrayList<String>();
    ArrayList<String> diseaseList8 = new ArrayList<String>();
    ArrayList<String> diseaseList9 = new ArrayList<String>();


    public static FirstFragment newInstance() {
        FirstFragment fragment = new FirstFragment();
        return fragment;
    }


    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_first, container, false);

        background = view.findViewById(R.id.background);
        tv_city = view.findViewById(R.id.tv_city);
        tv_currentTemp = view.findViewById(R.id.tv_currentTemp);
        tv_currentMinMax = view.findViewById(R.id.tv_currentMinMax);
        tv_updateTime = view.findViewById(R.id.tv_updateTime);
        iv_currentIcon = view.findViewById(R.id.iv_currentIcon);
        ib_update = view.findViewById(R.id.ib_update);

        tv_time1 = view.findViewById(R.id.tv_time1);
        tv_time2 = view.findViewById(R.id.tv_time2);
        tv_time3 = view.findViewById(R.id.tv_time3);
        tv_time4 = view.findViewById(R.id.tv_time4);
        tv_time5 = view.findViewById(R.id.tv_time5);
        tv_time6 = view.findViewById(R.id.tv_time6);
        tv_time7 = view.findViewById(R.id.tv_time7);
        tv_time8 = view.findViewById(R.id.tv_time8);

        tv_temp1 = view.findViewById(R.id.tv_temp1);
        tv_temp2 = view.findViewById(R.id.tv_temp2);
        tv_temp3 = view.findViewById(R.id.tv_temp3);
        tv_temp4 = view.findViewById(R.id.tv_temp4);
        tv_temp5 = view.findViewById(R.id.tv_temp5);
        tv_temp6 = view.findViewById(R.id.tv_temp6);
        tv_temp7 = view.findViewById(R.id.tv_temp7);
        tv_temp8 = view.findViewById(R.id.tv_temp8);

        iv_icon1 = view.findViewById(R.id.iv_icon1);
        iv_icon2 = view.findViewById(R.id.iv_icon2);
        iv_icon3 = view.findViewById(R.id.iv_icon3);
        iv_icon4 = view.findViewById(R.id.iv_icon4);
        iv_icon5 = view.findViewById(R.id.iv_icon5);
        iv_icon6 = view.findViewById(R.id.iv_icon6);
        iv_icon7 = view.findViewById(R.id.iv_icon7);
        iv_icon8 = view.findViewById(R.id.iv_icon8);

        tv_ment1 = view.findViewById(R.id.tv_ment1);
        tv_ment_disease = view.findViewById(R.id.tv_ment_disease);

        iv_top2 = view.findViewById(R.id.iv_top2);
        iv_top = view.findViewById(R.id.iv_top);
        iv_bottom = view.findViewById(R.id.iv_bottom);
        iv_hat = view.findViewById(R.id.iv_hat);
        iv_parasol = view.findViewById(R.id.iv_parasol);
        iv_umbrella = view.findViewById(R.id.iv_umbrella);

        topRandomNum = ((MainActivity)getActivity()).topRandomNum;
        top2RandomNum = ((MainActivity)getActivity()).top2RandomNum;
        bottomRandomNum = ((MainActivity)getActivity()).bottomRandomNum;
        bottomShortRandNum = ((MainActivity)getActivity()).bottomShortRandNum;
        shoesRandomNum = ((MainActivity)getActivity()).shoesRandomNum;

        gpsTracker = new GpsTracker(getActivity());

        double latitude = gpsTracker.getLatitude();
        double longitude = gpsTracker.getLongitude();
        lat = latitude;
        lng = longitude;

        id = ((MainActivity) getActivity()).userID;

        address = getCurrentAddress(latitude, longitude);
        tv_city.setText(address);

        dateViewMethod();
        changeBackground();

        if (requestQueue == null) {
            requestQueue = Volley.newRequestQueue(getActivity().getApplicationContext());
        }

        if (requestQueue == null) {
            requestQueue = Volley.newRequestQueue(getActivity().getApplicationContext());
        }

        CurrentCall();
        HourlyCall();

        ib_update.setOnClickListener(new View.OnClickListener() { //업데이트 버튼
            @Override
            public void onClick(View v) {
                dateViewMethod();
                changeBackground();
                CurrentCall();
                DailyCall();
                HourlyCall();

                double latitude = gpsTracker.getLatitude();
                double longitude = gpsTracker.getLongitude();
                lat = latitude;
                lng = longitude;
                address = getCurrentAddress(latitude, longitude);
                tv_city.setText(address);

                Toast.makeText(getActivity(), "업데이트", Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }

    public void changeBackground() { //배경 변경
        long now = System.currentTimeMillis();
        Date date = new Date(now);
        SimpleDateFormat AMPM = new SimpleDateFormat("a");
        SimpleDateFormat time = new SimpleDateFormat("h");
        String bg_getAMPM = AMPM.format(date);
        String bg_getTime = time.format(date);

        if (data_pop < 40) {
            if ((bg_getAMPM.equals("오전") && (4 < Integer.parseInt(bg_getTime) && 12 > Integer.parseInt(bg_getTime))) || (bg_getAMPM.equals("오후") && Integer.parseInt(bg_getTime) < 7) || (bg_getAMPM.equals("오후") && Integer.parseInt(bg_getTime) == 12)) {
                background.setBackgroundColor(Color.parseColor("#6CC2FF"));
            } else if ((bg_getAMPM.equals("오전") && 4 >= Integer.parseInt(bg_getTime)) || (bg_getAMPM.equals("오전") && Integer.parseInt(bg_getTime) == 12) || (bg_getAMPM.equals("오후") && (Integer.parseInt(bg_getTime) >= 7 && Integer.parseInt(bg_getTime) < 12))) {
                background.setBackgroundColor(Color.parseColor("#011B34"));
            }
        } else {
            background.setBackgroundColor(Color.parseColor("#4B78A4"));
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

        if (addresses == null || addresses.size() == 0) {
            Toast.makeText(getActivity(), "주소 미발견", Toast.LENGTH_LONG).show();
            return "주소 미발견";
        }

        Address address = addresses.get(0);
        return address.getLocality().toString() + "\n";
    }

    public void dateViewMethod() { //업데이트된 시간 표시
        long now = System.currentTimeMillis();
        Date date = new Date(now);
        SimpleDateFormat simpleDateFormatDay = new SimpleDateFormat("M/d a");
        SimpleDateFormat simpleDateFormatTime = new SimpleDateFormat("h:mm");
        String getDay = simpleDateFormatDay.format(date);
        String getTime = simpleDateFormatTime.format(date);

        String getDate = getDay + " " + getTime;

        tv_updateTime.setText("업데이트 " + getDate);

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

                    tempDo = (int) (Math.round((subJsonObject.getDouble("temp") - 273.15) * 100) / 100);
                    tv_currentTemp.setText(tempDo + "°");

                    JSONArray arr2 = subJsonObject.getJSONArray("weather");

                    uvi = (int) (subJsonObject.getDouble("uvi"));

                    JSONObject d = arr2.getJSONObject(0);
                    currentIcon = d.getString("icon");

                    String currentIconUrl1 = "http://openweathermap.org/img/wn/" + currentIcon + "@2x.png";
                    Glide.with(getActivity()).load(currentIconUrl1).into(iv_currentIcon);

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

                    //시간설정
                    JSONArray jsonArray = jsonObject.getJSONArray("hourly");
                    ArrayList<String> list = new ArrayList<String>();
                    ArrayList<String> list2 = new ArrayList<String>();
                    ArrayList<String> list3 = new ArrayList<String>();

                    JSONObject subJsonObject = jsonArray.getJSONObject(0);
                    data_pop = (int) (subJsonObject.getDouble("pop") * 100);

                    for (int i = 0; i < jsonArray.length(); i++) {
                        jsonObject = jsonArray.getJSONObject(i);
                        JSONObject a = jsonArray.getJSONObject(i);

                        list.add(jsonObject.getString("dt"));

                        JSONArray arr2 = a.getJSONArray("weather");
                        JSONObject b = arr2.getJSONObject(0);
                        list2.add(b.getString("icon"));

                        list3.add(jsonObject.getString("temp"));

                    }

                    String time1 = getTimestampToDate(list.get(0));
                    String time2 = getTimestampToDate(list.get(3));
                    String time3 = getTimestampToDate(list.get(6));
                    String time4 = getTimestampToDate(list.get(9));
                    String time5 = getTimestampToDate(list.get(12));
                    String time6 = getTimestampToDate(list.get(15));
                    String time7 = getTimestampToDate(list.get(18));
                    String time8 = getTimestampToDate(list.get(21));

                    tv_time1.setText(time1);
                    tv_time2.setText(time2);
                    tv_time3.setText(time3);
                    tv_time4.setText(time4);
                    tv_time5.setText(time5);
                    tv_time6.setText(time6);
                    tv_time7.setText(time7);
                    tv_time8.setText(time8);

                    String iconUrl1 = "http://openweathermap.org/img/wn/" + list2.get(0) + "@2x.png";
                    Glide.with(getActivity()).load(iconUrl1).into(iv_icon1);

                    String iconUrl2 = "http://openweathermap.org/img/wn/" + list2.get(3) + "@2x.png";
                    Glide.with(getActivity()).load(iconUrl2).into(iv_icon2);

                    String iconUrl3 = "http://openweathermap.org/img/wn/" + list2.get(6) + "@2x.png";
                    Glide.with(getActivity()).load(iconUrl3).into(iv_icon3);

                    String iconUrl4 = "http://openweathermap.org/img/wn/" + list2.get(9) + "@2x.png";
                    Glide.with(getActivity()).load(iconUrl4).into(iv_icon4);

                    String iconUrl5 = "http://openweathermap.org/img/wn/" + list2.get(12) + "@2x.png";
                    Glide.with(getActivity()).load(iconUrl5).into(iv_icon5);

                    String iconUrl6 = "http://openweathermap.org/img/wn/" + list2.get(15) + "@2x.png";
                    Glide.with(getActivity()).load(iconUrl6).into(iv_icon6);

                    String iconUrl7 = "http://openweathermap.org/img/wn/" + list2.get(18) + "@2x.png";
                    Glide.with(getActivity()).load(iconUrl7).into(iv_icon7);

                    String iconUrl8 = "http://openweathermap.org/img/wn/" + list2.get(21) + "@2x.png";
                    Glide.with(getActivity()).load(iconUrl8).into(iv_icon8);

                    int temp1 = Integer.parseInt(String.valueOf(Math.round((Double.parseDouble(list3.get(0)) - 273.15) * 100) / 100));
                    tv_temp1.setText(temp1 + "°");

                    int temp2 = Integer.parseInt(String.valueOf(Math.round((Double.parseDouble(list3.get(3)) - 273.15) * 100) / 100));
                    tv_temp2.setText(temp2 + "°");

                    int temp3 = Integer.parseInt(String.valueOf(Math.round((Double.parseDouble(list3.get(6)) - 273.15) * 100) / 100));
                    tv_temp3.setText(temp3 + "°");

                    int temp4 = Integer.parseInt(String.valueOf(Math.round((Double.parseDouble(list3.get(9)) - 273.15) * 100) / 100));
                    tv_temp4.setText(temp4 + "°");

                    int temp5 = Integer.parseInt(String.valueOf(Math.round((Double.parseDouble(list3.get(12)) - 273.15) * 100) / 100));
                    tv_temp5.setText(temp5 + "°");

                    int temp6 = Integer.parseInt(String.valueOf(Math.round((Double.parseDouble(list3.get(15)) - 273.15) * 100) / 100));
                    tv_temp6.setText(temp6 + "°");

                    int temp7 = Integer.parseInt(String.valueOf(Math.round((Double.parseDouble(list3.get(18)) - 273.15) * 100) / 100));
                    tv_temp7.setText(temp7 + "°");

                    int temp8 = Integer.parseInt(String.valueOf(Math.round((Double.parseDouble(list3.get(21)) - 273.15) * 100) / 100));
                    tv_temp8.setText(temp8 + "°");

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
        + "&exclude=current,minutely,daily,alerts"
        + "&appid=" + BuildConfig.OPEN_WEATHER_API_KEY;
        StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onResponse(String response) {
                try {

                    JSONObject jsonObject = new JSONObject(response);

                    JSONArray jsonArray = jsonObject.getJSONArray("daily");
                    jsonObject = jsonArray.getJSONObject(0);
                    JSONObject a = jsonArray.getJSONObject(0);
                    JSONObject b = jsonArray.getJSONObject(0);

                    a = jsonObject.getJSONObject("temp");
                    b = jsonObject.getJSONObject("temp");

                    //최저기온 설정
                    int tempMin = Integer.parseInt(String.valueOf(Math.round((Double.parseDouble(a.getString("min")) - 273.15) * 100) / 100));

                    //최고기온 설정
                    int tempMax = Integer.parseInt(String.valueOf(Math.round((Double.parseDouble(b.getString("max")) - 273.15) * 100) / 100));
                    data_temp = tempMax;

                    data_uvi = (int) (jsonObject.getDouble("uvi"));

                    tv_currentMinMax.setText(tempMin + " / " + tempMax);

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

    private void mentData() { //멘트 데이터 가져오기
        diseaseMent();
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
                    ArrayList<String> list8 = new ArrayList<String>();
                    ArrayList<String> list9 = new ArrayList<String>();

                    for (int i = 0; i < jsonArray.length(); i++) {
                        jsonObject = jsonArray.getJSONObject(i);

                        list.add(jsonObject.getString("COL 1"));
                        list2.add(jsonObject.getString("COL 5"));
                        list3.add(jsonObject.getString("COL 7"));
                        list4.add(jsonObject.getString("COL 9"));
                        list5.add(jsonObject.getString("COL 13"));
                        list6.add(jsonObject.getString("COL 17"));
                        list7.add(jsonObject.getString("COL 19"));
                        list8.add(jsonObject.getString("COL 21"));
                        list9.add(jsonObject.getString("COL 23"));

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

                    String result8_1 = list8.get(2); String result8_2 = list8.get(3); String result8_3 = list8.get(4); String result8_4 = list8.get(5); String result8_5 = list8.get(6);
                    String result8_6 = list8.get(7); String result8_7 = list8.get(8); String result8_8 = list8.get(9); String result8_9 = list8.get(10); String result8_10 = list8.get(11);
                    String result8_11 = list8.get(12); String result8_12 = list8.get(13); String result8_13 = list8.get(14); String result8_14 = list8.get(15); String result8_15 = list8.get(16);
                    String result8_16 = list8.get(17); String result8_17 = list8.get(18); String result8_18 = list8.get(19); String result8_19 = list8.get(20); String result8_20 = list8.get(21);
                    String result8_21 = list8.get(22); String result8_22 = list8.get(23); String result8_23 = list8.get(24); String result8_24 = list8.get(25);

                    String result9_1 = list9.get(2); String result9_2 = list9.get(3); String result9_3 = list9.get(4); String result9_4 = list9.get(5); String result9_5 = list9.get(6);
                    String result9_6 = list9.get(7); String result9_7 = list9.get(8); String result9_8 = list9.get(9); String result9_9 = list9.get(10); String result9_10 = list9.get(11);
                    String result9_11 = list9.get(12); String result9_12 = list9.get(13); String result9_13 = list9.get(14); String result9_14 = list9.get(15); String result9_15 = list9.get(16);
                    String result9_16 = list9.get(17); String result9_17 = list9.get(18); String result9_18 = list9.get(19); String result9_19 = list9.get(20); String result9_20 = list9.get(21);
                    String result9_21 = list9.get(22); String result9_22 = list9.get(23); String result9_23 = list9.get(24); String result9_24 = list9.get(25);


                    if (!disease_1st.equals("없음")) { //질병이 있을 때 -> 질병을 기준으로 한 멘트 데이터를 가져와 텍스트로 보여줌
                        if (diseaseList.get(1).equals(disease_1st)) {
                            ment_disease_1 = UserDisease + " 보유하고 계신 사용자께는 오늘의 날씨에 " + diseaseList2.get(1) + " " + diseaseList3.get(1);

                            if (ment_disease_2.equals("")) ment_disease_2 = diseaseList4.get(1);
                            if (ment_disease_4.equals("")) ment_disease_4 = diseaseList6.get(1);
                            if (ment_disease_6.equals(""))
                                ment_disease_6 = " " + diseaseList8.get(1) + " " + diseaseList9.get(1);

                            if (disease_2nd.equals("없음")) {
                                if (ment_disease_2.equals("")) ment_disease_2 = diseaseList4.get(1);
                                if (ment_disease_4.equals("")) ment_disease_4 = diseaseList6.get(1);
                                if (ment_disease_6.equals(""))
                                    ment_disease_6 = " " + diseaseList8.get(1) + " " + diseaseList9.get(1);
                            }
                            if (diseaseList.get(2).equals(disease_2nd) || diseaseList.get(2).equals(disease_3rd) || diseaseList.get(2).equals(disease_4th) || diseaseList.get(2).equals(disease_5th) || diseaseList.get(2).equals(disease_6th) || diseaseList.get(2).equals(disease_7th) || diseaseList.get(2).equals(disease_8th)) {
                                if (ment_disease_3.equals("")) ment_disease_3 = diseaseList5.get(2);
                                if (ment_disease_5.equals("")) ment_disease_5 = diseaseList7.get(2);
                            } else if (!(diseaseList.get(2).equals(disease_2nd) && diseaseList.get(2).equals(disease_3rd) && diseaseList.get(2).equals(disease_4th) && diseaseList.get(2).equals(disease_5th) && diseaseList.get(2).equals(disease_6th) && diseaseList.get(2).equals(disease_7th) && diseaseList.get(2).equals(disease_8th))) {
                            }

                            if (diseaseList.get(3).equals(disease_2nd) || diseaseList.get(3).equals(disease_3rd) || diseaseList.get(3).equals(disease_4th) || diseaseList.get(3).equals(disease_5th) || diseaseList.get(3).equals(disease_6th) || diseaseList.get(3).equals(disease_7th) || diseaseList.get(3).equals(disease_8th)) {
                                if (ment_disease_3.equals("")) ment_disease_3 = diseaseList5.get(3);
                            } else if (!(diseaseList.get(3).equals(disease_2nd) && diseaseList.get(3).equals(disease_3rd) && diseaseList.get(3).equals(disease_4th) && diseaseList.get(3).equals(disease_5th) && diseaseList.get(3).equals(disease_6th) && diseaseList.get(3).equals(disease_7th) && diseaseList.get(3).equals(disease_8th))) {
                            }

                            if (diseaseList.get(4).equals(disease_2nd) || diseaseList.get(4).equals(disease_3rd) || diseaseList.get(4).equals(disease_4th) || diseaseList.get(4).equals(disease_5th) || diseaseList.get(4).equals(disease_6th) || diseaseList.get(4).equals(disease_7th) || diseaseList.get(4).equals(disease_8th)) {
                                if (ment_disease_6.equals(""))
                                    ment_disease_6 = " " + diseaseList8.get(4) + " " + diseaseList9.get(4);
                            } else if (!(diseaseList.get(4).equals(disease_2nd) && diseaseList.get(4).equals(disease_3rd) && diseaseList.get(4).equals(disease_4th) && diseaseList.get(4).equals(disease_5th) && diseaseList.get(4).equals(disease_6th) && diseaseList.get(4).equals(disease_7th) && diseaseList.get(4).equals(disease_8th))) {
                            }

                            if (diseaseList.get(5).equals(disease_2nd) || diseaseList.get(5).equals(disease_3rd) || diseaseList.get(5).equals(disease_4th) || diseaseList.get(5).equals(disease_5th) || diseaseList.get(5).equals(disease_6th) || diseaseList.get(5).equals(disease_7th) || diseaseList.get(5).equals(disease_8th)) {
                                if (ment_disease_3.equals("")) ment_disease_3 = diseaseList5.get(5);
                                if (ment_disease_5.equals("")) ment_disease_5 = diseaseList7.get(5);
                            } else if (!(diseaseList.get(5).equals(disease_2nd) && diseaseList.get(5).equals(disease_3rd) && diseaseList.get(5).equals(disease_4th) && diseaseList.get(5).equals(disease_5th) && diseaseList.get(5).equals(disease_6th) && diseaseList.get(5).equals(disease_7th) && diseaseList.get(5).equals(disease_8th))) {
                            }

                            if (diseaseList.get(6).equals(disease_2nd) || diseaseList.get(6).equals(disease_3rd) || diseaseList.get(6).equals(disease_4th) || diseaseList.get(6).equals(disease_5th) || diseaseList.get(6).equals(disease_6th) || diseaseList.get(6).equals(disease_7th) || diseaseList.get(6).equals(disease_8th)) {
                                if (ment_disease_3.equals("")) ment_disease_3 = diseaseList5.get(6);
                                if (ment_disease_5.equals("")) ment_disease_5 = diseaseList7.get(6);
                            } else if (!(diseaseList.get(6).equals(disease_2nd) && diseaseList.get(6).equals(disease_3rd) && diseaseList.get(6).equals(disease_4th) && diseaseList.get(6).equals(disease_5th) && diseaseList.get(6).equals(disease_6th) && diseaseList.get(6).equals(disease_7th) && diseaseList.get(6).equals(disease_8th))) {
                            }

                            if (diseaseList.get(7).equals(disease_2nd) || diseaseList.get(7).equals(disease_3rd) || diseaseList.get(7).equals(disease_4th) || diseaseList.get(7).equals(disease_5th) || diseaseList.get(7).equals(disease_6th) || diseaseList.get(7).equals(disease_7th) || diseaseList.get(7).equals(disease_8th)) {
                                if (ment_disease_3.equals("")) ment_disease_3 = diseaseList5.get(7);
                            } else if (!(diseaseList.get(7).equals(disease_2nd) && diseaseList.get(7).equals(disease_3rd) && diseaseList.get(7).equals(disease_4th) && diseaseList.get(7).equals(disease_5th) && diseaseList.get(7).equals(disease_6th) && diseaseList.get(7).equals(disease_7th) && diseaseList.get(7).equals(disease_8th))) {
                            }

                            if (diseaseList.get(8).equals(disease_2nd) || diseaseList.get(8).equals(disease_3rd) || diseaseList.get(8).equals(disease_4th) || diseaseList.get(8).equals(disease_5th) || diseaseList.get(8).equals(disease_6th) || diseaseList.get(8).equals(disease_7th) || diseaseList.get(8).equals(disease_8th)) {
                                if (ment_disease_3.equals("")) ment_disease_3 = diseaseList5.get(8);
                                if (ment_disease_5.equals("")) ment_disease_5 = diseaseList7.get(8);
                            } else if (!(diseaseList.get(8).equals(disease_2nd) && diseaseList.get(8).equals(disease_3rd) && diseaseList.get(8).equals(disease_4th) && diseaseList.get(8).equals(disease_5th) && diseaseList.get(8).equals(disease_6th) && diseaseList.get(8).equals(disease_7th) && diseaseList.get(8).equals(disease_8th))) {
                            }
                        }

                        if (diseaseList.get(2).equals(disease_1st)) {
                            ment_disease_1 = UserDisease + " 보유하고 계신 사용자께는 오늘의 날씨에 " + diseaseList2.get(2) + " " + diseaseList3.get(2);

                            if (diseaseList.get(1).equals(disease_2nd) || diseaseList.get(1).equals(disease_3rd) || diseaseList.get(1).equals(disease_4th) || diseaseList.get(1).equals(disease_5th) || diseaseList.get(1).equals(disease_6th) || diseaseList.get(1).equals(disease_7th) || diseaseList.get(1).equals(disease_8th)) {
                                if (ment_disease_2.equals("")) ment_disease_2 = diseaseList4.get(1);
                                if (ment_disease_4.equals("")) ment_disease_4 = diseaseList6.get(1);
                                if (ment_disease_6.equals(""))
                                    ment_disease_6 = " " + diseaseList8.get(1) + " " + diseaseList9.get(1);
                            } else if (!(diseaseList.get(1).equals(disease_2nd) && diseaseList.get(1).equals(disease_3rd) && diseaseList.get(1).equals(disease_4th) && diseaseList.get(1).equals(disease_5th) && diseaseList.get(1).equals(disease_6th) && diseaseList.get(1).equals(disease_7th) && diseaseList.get(1).equals(disease_8th))) {
                            }

                            if (diseaseList.get(3).equals(disease_2nd) || diseaseList.get(3).equals(disease_3rd) || diseaseList.get(3).equals(disease_4th) || diseaseList.get(3).equals(disease_5th) || diseaseList.get(3).equals(disease_6th) || diseaseList.get(3).equals(disease_7th) || diseaseList.get(3).equals(disease_8th)) {
                                if (ment_disease_3.equals("")) ment_disease_3 = diseaseList5.get(3);
                            } else if (!(diseaseList.get(3).equals(disease_2nd) && diseaseList.get(3).equals(disease_3rd) && diseaseList.get(3).equals(disease_4th) && diseaseList.get(3).equals(disease_5th) && diseaseList.get(3).equals(disease_6th) && diseaseList.get(3).equals(disease_7th) && diseaseList.get(3).equals(disease_8th))) {
                            }

                            if (diseaseList.get(4).equals(disease_2nd) || diseaseList.get(4).equals(disease_3rd) || diseaseList.get(4).equals(disease_4th) || diseaseList.get(4).equals(disease_5th) || diseaseList.get(4).equals(disease_6th) || diseaseList.get(4).equals(disease_7th) || diseaseList.get(4).equals(disease_8th)) {
                                if (ment_disease_6.equals(""))
                                    ment_disease_6 = " " + diseaseList8.get(4) + " " + diseaseList9.get(4);
                            } else if (!(diseaseList.get(4).equals(disease_2nd) && diseaseList.get(4).equals(disease_3rd) && diseaseList.get(4).equals(disease_4th) && diseaseList.get(4).equals(disease_5th) && diseaseList.get(4).equals(disease_6th) && diseaseList.get(4).equals(disease_7th) && diseaseList.get(4).equals(disease_8th))) {
                            }

                            if (diseaseList.get(5).equals(disease_2nd) || diseaseList.get(5).equals(disease_3rd) || diseaseList.get(5).equals(disease_4th) || diseaseList.get(5).equals(disease_5th) || diseaseList.get(5).equals(disease_6th) || diseaseList.get(5).equals(disease_7th) || diseaseList.get(5).equals(disease_8th)) {
                                if (ment_disease_3.equals("")) ment_disease_3 = diseaseList5.get(5);
                                if (ment_disease_5.equals("")) ment_disease_5 = diseaseList7.get(5);
                            } else if (!(diseaseList.get(5).equals(disease_2nd) && diseaseList.get(5).equals(disease_3rd) && diseaseList.get(5).equals(disease_4th) && diseaseList.get(5).equals(disease_5th) && diseaseList.get(5).equals(disease_6th) && diseaseList.get(5).equals(disease_7th) && diseaseList.get(5).equals(disease_8th))) {
                            }

                            if (diseaseList.get(6).equals(disease_2nd) || diseaseList.get(6).equals(disease_3rd) || diseaseList.get(6).equals(disease_4th) || diseaseList.get(6).equals(disease_5th) || diseaseList.get(6).equals(disease_6th) || diseaseList.get(6).equals(disease_7th) || diseaseList.get(6).equals(disease_8th)) {
                                if (ment_disease_3.equals("")) ment_disease_3 = diseaseList5.get(6);
                                if (ment_disease_5.equals("")) ment_disease_5 = diseaseList7.get(6);
                            } else if (!(diseaseList.get(6).equals(disease_2nd) && diseaseList.get(6).equals(disease_3rd) && diseaseList.get(6).equals(disease_4th) && diseaseList.get(6).equals(disease_5th) && diseaseList.get(6).equals(disease_6th) && diseaseList.get(6).equals(disease_7th) && diseaseList.get(6).equals(disease_8th))) {
                            }

                            if (diseaseList.get(7).equals(disease_2nd) || diseaseList.get(7).equals(disease_3rd) || diseaseList.get(7).equals(disease_4th) || diseaseList.get(7).equals(disease_5th) || diseaseList.get(7).equals(disease_6th) || diseaseList.get(7).equals(disease_7th) || diseaseList.get(7).equals(disease_8th)) {
                                if (ment_disease_3.equals("")) ment_disease_3 = diseaseList5.get(7);
                            } else if (!(diseaseList.get(7).equals(disease_2nd) && diseaseList.get(7).equals(disease_3rd) && diseaseList.get(7).equals(disease_4th) && diseaseList.get(7).equals(disease_5th) && diseaseList.get(7).equals(disease_6th) && diseaseList.get(7).equals(disease_7th) && diseaseList.get(7).equals(disease_8th))) {
                            }

                            if (diseaseList.get(8).equals(disease_2nd) || diseaseList.get(8).equals(disease_3rd) || diseaseList.get(8).equals(disease_4th) || diseaseList.get(8).equals(disease_5th) || diseaseList.get(8).equals(disease_6th) || diseaseList.get(8).equals(disease_7th) || diseaseList.get(8).equals(disease_8th)) {
                                if (ment_disease_3.equals("")) ment_disease_3 = diseaseList5.get(8);
                                if (ment_disease_5.equals("")) ment_disease_5 = diseaseList7.get(8);
                            } else if (!(diseaseList.get(8).equals(disease_2nd) && diseaseList.get(8).equals(disease_3rd) && diseaseList.get(8).equals(disease_4th) && diseaseList.get(8).equals(disease_5th) && diseaseList.get(8).equals(disease_6th) && diseaseList.get(8).equals(disease_7th) && diseaseList.get(8).equals(disease_8th))) {
                            }
                        }

                        if (diseaseList.get(3).equals(disease_1st)) {
                            ment_disease_1 = UserDisease + " 보유하고 계신 사용자께는 오늘의 날씨에 " + diseaseList2.get(3) + " " + diseaseList3.get(3);

                            if (disease_2nd.equals("없음")) if (ment_disease_3.equals(""))
                                ment_disease_3 = diseaseList5.get(3) + " 옷";
                            if (disease_2nd.equals("백반증")) if (ment_disease_3.equals(""))
                                ment_disease_3 = diseaseList5.get(3) + " 옷,";
                            if (ment_disease_3.equals("")) ment_disease_3 = diseaseList5.get(3);

                            if (diseaseList.get(1).equals(disease_2nd) || diseaseList.get(1).equals(disease_3rd) || diseaseList.get(1).equals(disease_4th) || diseaseList.get(1).equals(disease_5th) || diseaseList.get(1).equals(disease_6th) || diseaseList.get(1).equals(disease_7th) || diseaseList.get(1).equals(disease_8th)) {
                                if (ment_disease_2.equals("")) ment_disease_2 = diseaseList4.get(1);
                                if (ment_disease_4.equals("")) ment_disease_4 = diseaseList6.get(1);
                                if (ment_disease_6.equals(""))
                                    ment_disease_6 = " " + diseaseList8.get(1) + " " + diseaseList9.get(1);
                            } else if (!(diseaseList.get(1).equals(disease_2nd) && diseaseList.get(1).equals(disease_3rd) && diseaseList.get(1).equals(disease_4th) && diseaseList.get(1).equals(disease_5th) && diseaseList.get(1).equals(disease_6th) && diseaseList.get(1).equals(disease_7th) && diseaseList.get(1).equals(disease_8th))) {
                            }

                            if (diseaseList.get(2).equals(disease_2nd) || diseaseList.get(2).equals(disease_3rd) || diseaseList.get(2).equals(disease_4th) || diseaseList.get(2).equals(disease_5th) || diseaseList.get(2).equals(disease_6th) || diseaseList.get(2).equals(disease_7th) || diseaseList.get(2).equals(disease_8th)) {
                                if (ment_disease_3.equals("")) ment_disease_3 = diseaseList5.get(2);
                                if (ment_disease_5.equals("")) ment_disease_5 = diseaseList7.get(2);
                            } else if (!(diseaseList.get(2).equals(disease_2nd) && diseaseList.get(2).equals(disease_3rd) && diseaseList.get(2).equals(disease_4th) && diseaseList.get(2).equals(disease_5th) && diseaseList.get(2).equals(disease_6th) && diseaseList.get(2).equals(disease_7th) && diseaseList.get(2).equals(disease_8th))) {
                            }

                            if (diseaseList.get(4).equals(disease_2nd) || diseaseList.get(4).equals(disease_3rd) || diseaseList.get(4).equals(disease_4th) || diseaseList.get(4).equals(disease_5th) || diseaseList.get(4).equals(disease_6th) || diseaseList.get(4).equals(disease_7th) || diseaseList.get(4).equals(disease_8th)) {
                                if (ment_disease_6.equals(""))
                                    ment_disease_6 = " " + diseaseList8.get(4) + " " + diseaseList9.get(4);
                            } else if (!(diseaseList.get(4).equals(disease_2nd) && diseaseList.get(4).equals(disease_3rd) && diseaseList.get(4).equals(disease_4th) && diseaseList.get(4).equals(disease_5th) && diseaseList.get(4).equals(disease_6th) && diseaseList.get(4).equals(disease_7th) && diseaseList.get(4).equals(disease_8th))) {
                            }

                            if (diseaseList.get(5).equals(disease_2nd) || diseaseList.get(5).equals(disease_3rd) || diseaseList.get(5).equals(disease_4th) || diseaseList.get(5).equals(disease_5th) || diseaseList.get(5).equals(disease_6th) || diseaseList.get(5).equals(disease_7th) || diseaseList.get(5).equals(disease_8th)) {
                                if (ment_disease_3.equals("")) ment_disease_3 = diseaseList5.get(5);
                                if (ment_disease_5.equals("")) ment_disease_5 = diseaseList7.get(5);
                            } else if (!(diseaseList.get(5).equals(disease_2nd) && diseaseList.get(5).equals(disease_3rd) && diseaseList.get(5).equals(disease_4th) && diseaseList.get(5).equals(disease_5th) && diseaseList.get(5).equals(disease_6th) && diseaseList.get(5).equals(disease_7th) && diseaseList.get(5).equals(disease_8th))) {
                            }

                            if (diseaseList.get(6).equals(disease_2nd) || diseaseList.get(6).equals(disease_3rd) || diseaseList.get(6).equals(disease_4th) || diseaseList.get(6).equals(disease_5th) || diseaseList.get(6).equals(disease_6th) || diseaseList.get(6).equals(disease_7th) || diseaseList.get(6).equals(disease_8th)) {
                                if (ment_disease_3.equals("")) ment_disease_3 = diseaseList5.get(6);
                                if (ment_disease_5.equals("")) ment_disease_5 = diseaseList7.get(6);
                            } else if (!(diseaseList.get(6).equals(disease_2nd) && diseaseList.get(6).equals(disease_3rd) && diseaseList.get(6).equals(disease_4th) && diseaseList.get(6).equals(disease_5th) && diseaseList.get(6).equals(disease_6th) && diseaseList.get(6).equals(disease_7th) && diseaseList.get(6).equals(disease_8th))) {
                            }

                            if (diseaseList.get(7).equals(disease_2nd) || diseaseList.get(7).equals(disease_3rd) || diseaseList.get(7).equals(disease_4th) || diseaseList.get(7).equals(disease_5th) || diseaseList.get(7).equals(disease_6th) || diseaseList.get(7).equals(disease_7th) || diseaseList.get(7).equals(disease_8th)) {
                                if (ment_disease_3.equals("")) ment_disease_3 = diseaseList5.get(7);
                            } else if (!(diseaseList.get(7).equals(disease_2nd) && diseaseList.get(7).equals(disease_3rd) && diseaseList.get(7).equals(disease_4th) && diseaseList.get(7).equals(disease_5th) && diseaseList.get(7).equals(disease_6th) && diseaseList.get(7).equals(disease_7th) && diseaseList.get(7).equals(disease_8th))) {
                            }

                            if (diseaseList.get(8).equals(disease_2nd) || diseaseList.get(8).equals(disease_3rd) || diseaseList.get(8).equals(disease_4th) || diseaseList.get(8).equals(disease_5th) || diseaseList.get(8).equals(disease_6th) || diseaseList.get(8).equals(disease_7th) || diseaseList.get(8).equals(disease_8th)) {
                                if (ment_disease_3.equals("")) ment_disease_3 = diseaseList5.get(8);
                                if (ment_disease_5.equals("")) ment_disease_5 = diseaseList7.get(8);
                            } else if (!(diseaseList.get(8).equals(disease_2nd) && diseaseList.get(8).equals(disease_3rd) && diseaseList.get(8).equals(disease_4th) && diseaseList.get(8).equals(disease_5th) && diseaseList.get(8).equals(disease_6th) && diseaseList.get(8).equals(disease_7th) && diseaseList.get(8).equals(disease_8th))) {
                            }
                        }

                        if (diseaseList.get(4).equals(disease_1st)) {
                            ment_disease_1 = UserDisease + " 보유하고 계신 사용자께는 오늘의 날씨에 " + diseaseList2.get(4) + " " + diseaseList3.get(4);

                            if (ment_disease_6.equals("")) ment_disease_6 = " " + diseaseList8.get(1) + " " + diseaseList9.get(1);

                            if (diseaseList.get(1).equals(disease_2nd) || diseaseList.get(1).equals(disease_3rd) || diseaseList.get(1).equals(disease_4th) || diseaseList.get(1).equals(disease_5th) || diseaseList.get(1).equals(disease_6th) || diseaseList.get(1).equals(disease_7th) || diseaseList.get(1).equals(disease_8th)) {
                                if (ment_disease_2.equals("")) ment_disease_2 = diseaseList4.get(1);
                                if (ment_disease_4.equals("")) ment_disease_4 = diseaseList6.get(1);
                                if (ment_disease_6.equals(""))
                                    ment_disease_6 = " " + diseaseList8.get(1) + " " + diseaseList9.get(1);
                            } else if (diseaseList.get(2).equals(disease_2nd) || diseaseList.get(2).equals(disease_3rd) || diseaseList.get(2).equals(disease_4th) || diseaseList.get(2).equals(disease_5th) || diseaseList.get(2).equals(disease_6th) || diseaseList.get(2).equals(disease_7th) || diseaseList.get(2).equals(disease_8th)) {
                                if (ment_disease_3.equals("")) ment_disease_3 = diseaseList5.get(2);
                                if (ment_disease_5.equals("")) ment_disease_5 = diseaseList7.get(2) + ",";
                            } else if (diseaseList.get(3).equals(disease_2nd) || diseaseList.get(3).equals(disease_3rd) || diseaseList.get(3).equals(disease_4th) || diseaseList.get(3).equals(disease_5th) || diseaseList.get(3).equals(disease_6th) || diseaseList.get(3).equals(disease_7th) || diseaseList.get(3).equals(disease_8th)) {
                                if (ment_disease_3.equals("")) ment_disease_3 = diseaseList5.get(3);
                            } else if (diseaseList.get(5).equals(disease_2nd) || diseaseList.get(5).equals(disease_3rd) || diseaseList.get(5).equals(disease_4th) || diseaseList.get(5).equals(disease_5th) || diseaseList.get(5).equals(disease_6th) || diseaseList.get(5).equals(disease_7th) || diseaseList.get(5).equals(disease_8th)) {
                                if (ment_disease_3.equals("")) ment_disease_3 = diseaseList5.get(5);
                                if (ment_disease_5.equals("")) ment_disease_5 = diseaseList7.get(5) + ",";
                            } else if (diseaseList.get(6).equals(disease_2nd) || diseaseList.get(6).equals(disease_3rd) || diseaseList.get(6).equals(disease_4th) || diseaseList.get(6).equals(disease_5th) || diseaseList.get(6).equals(disease_6th) || diseaseList.get(6).equals(disease_7th) || diseaseList.get(6).equals(disease_8th)) {
                                if (ment_disease_3.equals("")) ment_disease_3 = diseaseList5.get(6);
                                if (ment_disease_5.equals("")) ment_disease_5 = diseaseList7.get(6) + ",";
                            } else if (diseaseList.get(7).equals(disease_2nd) || diseaseList.get(7).equals(disease_3rd) || diseaseList.get(7).equals(disease_4th) || diseaseList.get(7).equals(disease_5th) || diseaseList.get(7).equals(disease_6th) || diseaseList.get(7).equals(disease_7th) || diseaseList.get(7).equals(disease_8th)) {
                                if (ment_disease_3.equals("")) ment_disease_3 = diseaseList5.get(7);
                            } else if (diseaseList.get(8).equals(disease_2nd) || diseaseList.get(8).equals(disease_3rd) || diseaseList.get(8).equals(disease_4th) || diseaseList.get(8).equals(disease_5th) || diseaseList.get(8).equals(disease_6th) || diseaseList.get(8).equals(disease_7th) || diseaseList.get(8).equals(disease_8th)) {
                                if (ment_disease_3.equals("")) ment_disease_3 = diseaseList5.get(8);
                                if (ment_disease_5.equals("")) ment_disease_5 = diseaseList7.get(8) + ",";
                            }
                        }

                        if (diseaseList.get(5).equals(disease_1st)) {
                            ment_disease_1 = UserDisease + " 보유하고 계신 사용자께는 오늘의 날씨에 " + diseaseList2.get(5) + " " + diseaseList3.get(5);

                            if (diseaseList.get(1).equals(disease_2nd) || diseaseList.get(1).equals(disease_3rd) || diseaseList.get(1).equals(disease_4th) || diseaseList.get(1).equals(disease_5th) || diseaseList.get(1).equals(disease_6th) || diseaseList.get(1).equals(disease_7th) || diseaseList.get(1).equals(disease_8th)) {
                                if (ment_disease_2.equals("")) ment_disease_2 = diseaseList4.get(1);
                                if (ment_disease_4.equals("")) ment_disease_4 = diseaseList6.get(1);
                                if (ment_disease_6.equals(""))
                                    ment_disease_6 = " " + diseaseList8.get(1) + " " + diseaseList9.get(1);
                            } else if (!(diseaseList.get(1).equals(disease_2nd) && diseaseList.get(1).equals(disease_3rd) && diseaseList.get(1).equals(disease_4th) && diseaseList.get(1).equals(disease_5th) && diseaseList.get(1).equals(disease_6th) && diseaseList.get(1).equals(disease_7th) && diseaseList.get(1).equals(disease_8th))) {
                            }

                            if (diseaseList.get(2).equals(disease_2nd) || diseaseList.get(2).equals(disease_3rd) || diseaseList.get(2).equals(disease_4th) || diseaseList.get(2).equals(disease_5th) || diseaseList.get(2).equals(disease_6th) || diseaseList.get(2).equals(disease_7th) || diseaseList.get(2).equals(disease_8th)) {
                                if (ment_disease_3.equals("")) ment_disease_3 = diseaseList5.get(2);
                                if (ment_disease_5.equals("")) ment_disease_5 = diseaseList7.get(2);
                            } else if (!(diseaseList.get(2).equals(disease_2nd) && diseaseList.get(2).equals(disease_3rd) && diseaseList.get(2).equals(disease_4th) && diseaseList.get(2).equals(disease_5th) && diseaseList.get(2).equals(disease_6th) && diseaseList.get(2).equals(disease_7th) && diseaseList.get(2).equals(disease_8th))) {
                            }

                            if (diseaseList.get(3).equals(disease_2nd) || diseaseList.get(3).equals(disease_3rd) || diseaseList.get(3).equals(disease_4th) || diseaseList.get(3).equals(disease_5th) || diseaseList.get(3).equals(disease_6th) || diseaseList.get(3).equals(disease_7th) || diseaseList.get(3).equals(disease_8th)) {
                                if (ment_disease_3.equals("")) ment_disease_3 = diseaseList5.get(3);
                            } else if (!(diseaseList.get(3).equals(disease_2nd) && diseaseList.get(3).equals(disease_3rd) && diseaseList.get(3).equals(disease_4th) && diseaseList.get(3).equals(disease_5th) && diseaseList.get(3).equals(disease_6th) && diseaseList.get(3).equals(disease_7th) && diseaseList.get(3).equals(disease_8th))) {
                            }

                            if (diseaseList.get(4).equals(disease_2nd) || diseaseList.get(4).equals(disease_3rd) || diseaseList.get(4).equals(disease_4th) || diseaseList.get(4).equals(disease_5th) || diseaseList.get(4).equals(disease_6th) || diseaseList.get(4).equals(disease_7th) || diseaseList.get(4).equals(disease_8th)) {
                                if (ment_disease_6.equals(""))
                                    ment_disease_6 = " " + diseaseList8.get(4) + " " + diseaseList9.get(4);
                            } else if (!(diseaseList.get(4).equals(disease_2nd) && diseaseList.get(4).equals(disease_3rd) && diseaseList.get(4).equals(disease_4th) && diseaseList.get(4).equals(disease_5th) && diseaseList.get(4).equals(disease_6th) && diseaseList.get(4).equals(disease_7th) && diseaseList.get(4).equals(disease_8th))) {
                            }

                            if (diseaseList.get(6).equals(disease_2nd) || diseaseList.get(6).equals(disease_3rd) || diseaseList.get(6).equals(disease_4th) || diseaseList.get(6).equals(disease_5th) || diseaseList.get(6).equals(disease_6th) || diseaseList.get(6).equals(disease_7th) || diseaseList.get(6).equals(disease_8th)) {
                                if (ment_disease_3.equals("")) ment_disease_3 = diseaseList5.get(6);
                                if (ment_disease_5.equals("")) ment_disease_5 = diseaseList7.get(6);
                            } else if (!(diseaseList.get(6).equals(disease_2nd) && diseaseList.get(6).equals(disease_3rd) && diseaseList.get(6).equals(disease_4th) && diseaseList.get(6).equals(disease_5th) && diseaseList.get(6).equals(disease_6th) && diseaseList.get(6).equals(disease_7th) && diseaseList.get(6).equals(disease_8th))) {
                            }

                            if (diseaseList.get(7).equals(disease_2nd) || diseaseList.get(7).equals(disease_3rd) || diseaseList.get(7).equals(disease_4th) || diseaseList.get(7).equals(disease_5th) || diseaseList.get(7).equals(disease_6th) || diseaseList.get(7).equals(disease_7th) || diseaseList.get(7).equals(disease_8th)) {
                                if (ment_disease_3.equals("")) ment_disease_3 = diseaseList5.get(7);
                            } else if (!(diseaseList.get(7).equals(disease_2nd) && diseaseList.get(7).equals(disease_3rd) && diseaseList.get(7).equals(disease_4th) && diseaseList.get(7).equals(disease_5th) && diseaseList.get(7).equals(disease_6th) && diseaseList.get(7).equals(disease_7th) && diseaseList.get(7).equals(disease_8th))) {
                            }

                            if (diseaseList.get(8).equals(disease_2nd) || diseaseList.get(8).equals(disease_3rd) || diseaseList.get(8).equals(disease_4th) || diseaseList.get(8).equals(disease_5th) || diseaseList.get(8).equals(disease_6th) || diseaseList.get(8).equals(disease_7th) || diseaseList.get(8).equals(disease_8th)) {
                                if (ment_disease_3.equals("")) ment_disease_3 = diseaseList5.get(8);
                                if (ment_disease_5.equals("")) ment_disease_5 = diseaseList7.get(8);
                            } else if (!(diseaseList.get(8).equals(disease_2nd) && diseaseList.get(8).equals(disease_3rd) && diseaseList.get(8).equals(disease_4th) && diseaseList.get(8).equals(disease_5th) && diseaseList.get(8).equals(disease_6th) && diseaseList.get(8).equals(disease_7th) && diseaseList.get(8).equals(disease_8th))) {
                            }
                        }

                        if (diseaseList.get(6).equals(disease_1st)) {
                            ment_disease_1 = UserDisease + " 보유하고 계신 사용자께는 오늘의 날씨에 " + diseaseList2.get(6) + " " + diseaseList3.get(6);

                            if (diseaseList.get(1).equals(disease_2nd) || diseaseList.get(1).equals(disease_3rd) || diseaseList.get(1).equals(disease_4th) || diseaseList.get(1).equals(disease_5th) || diseaseList.get(1).equals(disease_6th) || diseaseList.get(1).equals(disease_7th) || diseaseList.get(1).equals(disease_8th)) {
                                if (ment_disease_2.equals("")) ment_disease_2 = diseaseList4.get(1);
                                if (ment_disease_4.equals("")) ment_disease_4 = diseaseList6.get(1);
                                if (ment_disease_6.equals(""))
                                    ment_disease_6 = " " + diseaseList8.get(1) + " " + diseaseList9.get(1);
                            } else if (!(diseaseList.get(1).equals(disease_2nd) && diseaseList.get(1).equals(disease_3rd) && diseaseList.get(1).equals(disease_4th) && diseaseList.get(1).equals(disease_5th) && diseaseList.get(1).equals(disease_6th) && diseaseList.get(1).equals(disease_7th) && diseaseList.get(1).equals(disease_8th))) {
                            }

                            if (diseaseList.get(2).equals(disease_2nd) || diseaseList.get(2).equals(disease_3rd) || diseaseList.get(2).equals(disease_4th) || diseaseList.get(2).equals(disease_5th) || diseaseList.get(2).equals(disease_6th) || diseaseList.get(2).equals(disease_7th) || diseaseList.get(2).equals(disease_8th)) {
                                if (ment_disease_3.equals("")) ment_disease_3 = diseaseList5.get(2);
                                if (ment_disease_5.equals("")) ment_disease_5 = diseaseList7.get(2);
                            } else if (!(diseaseList.get(2).equals(disease_2nd) && diseaseList.get(2).equals(disease_3rd) && diseaseList.get(2).equals(disease_4th) && diseaseList.get(2).equals(disease_5th) && diseaseList.get(2).equals(disease_6th) && diseaseList.get(2).equals(disease_7th) && diseaseList.get(2).equals(disease_8th))) {
                            }

                            if (diseaseList.get(3).equals(disease_2nd) || diseaseList.get(3).equals(disease_3rd) || diseaseList.get(3).equals(disease_4th) || diseaseList.get(3).equals(disease_5th) || diseaseList.get(3).equals(disease_6th) || diseaseList.get(3).equals(disease_7th) || diseaseList.get(3).equals(disease_8th)) {
                                if (ment_disease_3.equals("")) ment_disease_3 = diseaseList5.get(3);
                            } else if (!(diseaseList.get(3).equals(disease_2nd) && diseaseList.get(3).equals(disease_3rd) && diseaseList.get(3).equals(disease_4th) && diseaseList.get(3).equals(disease_5th) && diseaseList.get(3).equals(disease_6th) && diseaseList.get(3).equals(disease_7th) && diseaseList.get(3).equals(disease_8th))) {
                            }

                            if (diseaseList.get(4).equals(disease_2nd) || diseaseList.get(4).equals(disease_3rd) || diseaseList.get(4).equals(disease_4th) || diseaseList.get(4).equals(disease_5th) || diseaseList.get(4).equals(disease_6th) || diseaseList.get(4).equals(disease_7th) || diseaseList.get(4).equals(disease_8th)) {
                                if (ment_disease_6.equals(""))
                                    ment_disease_6 = " " + diseaseList8.get(4) + " " + diseaseList9.get(4);
                            } else if (!(diseaseList.get(4).equals(disease_2nd) && diseaseList.get(4).equals(disease_3rd) && diseaseList.get(4).equals(disease_4th) && diseaseList.get(4).equals(disease_5th) && diseaseList.get(4).equals(disease_6th) && diseaseList.get(4).equals(disease_7th) && diseaseList.get(4).equals(disease_8th))) {
                            }

                            if (diseaseList.get(5).equals(disease_2nd) || diseaseList.get(5).equals(disease_3rd) || diseaseList.get(5).equals(disease_4th) || diseaseList.get(5).equals(disease_5th) || diseaseList.get(5).equals(disease_6th) || diseaseList.get(5).equals(disease_7th) || diseaseList.get(5).equals(disease_8th)) {
                                if (ment_disease_3.equals("")) ment_disease_3 = diseaseList5.get(5);
                                if (ment_disease_5.equals("")) ment_disease_5 = diseaseList7.get(5);
                            } else if (!(diseaseList.get(5).equals(disease_2nd) && diseaseList.get(5).equals(disease_3rd) && diseaseList.get(5).equals(disease_4th) && diseaseList.get(5).equals(disease_5th) && diseaseList.get(5).equals(disease_6th) && diseaseList.get(5).equals(disease_7th) && diseaseList.get(5).equals(disease_8th))) {
                            }

                            if (diseaseList.get(7).equals(disease_2nd) || diseaseList.get(7).equals(disease_3rd) || diseaseList.get(7).equals(disease_4th) || diseaseList.get(7).equals(disease_5th) || diseaseList.get(7).equals(disease_6th) || diseaseList.get(7).equals(disease_7th) || diseaseList.get(7).equals(disease_8th)) {
                                if (ment_disease_3.equals("")) ment_disease_3 = diseaseList5.get(7);
                            } else if (!(diseaseList.get(7).equals(disease_2nd) && diseaseList.get(7).equals(disease_3rd) && diseaseList.get(7).equals(disease_4th) && diseaseList.get(7).equals(disease_5th) && diseaseList.get(7).equals(disease_6th) && diseaseList.get(7).equals(disease_7th) && diseaseList.get(7).equals(disease_8th))) {
                            }

                            if (diseaseList.get(8).equals(disease_2nd) || diseaseList.get(8).equals(disease_3rd) || diseaseList.get(8).equals(disease_4th) || diseaseList.get(8).equals(disease_5th) || diseaseList.get(8).equals(disease_6th) || diseaseList.get(8).equals(disease_7th) || diseaseList.get(8).equals(disease_8th)) {
                                if (ment_disease_3.equals("")) ment_disease_3 = diseaseList5.get(8);
                                if (ment_disease_5.equals("")) ment_disease_5 = diseaseList7.get(8);
                            } else if (!(diseaseList.get(8).equals(disease_2nd) && diseaseList.get(8).equals(disease_3rd) && diseaseList.get(8).equals(disease_4th) && diseaseList.get(8).equals(disease_5th) && diseaseList.get(8).equals(disease_6th) && diseaseList.get(8).equals(disease_7th) && diseaseList.get(8).equals(disease_8th))) {
                            }
                        }

                        if (diseaseList.get(7).equals(disease_1st)) {
                            ment_disease_1 = UserDisease + " 보유하고 계신 사용자께는 오늘의 날씨에 " + diseaseList2.get(7) + " " + diseaseList3.get(7);

                            if (disease_2nd.equals("없음")) if (ment_disease_3.equals(""))
                                ment_disease_3 = diseaseList5.get(7) + " 옷";
                            if (disease_2nd.equals("백반증")) if (ment_disease_3.equals(""))
                                ment_disease_3 = diseaseList5.get(7) + " 옷,";
                            if (ment_disease_3.equals("")) ment_disease_3 = diseaseList5.get(7);

                            if (diseaseList.get(1).equals(disease_2nd) || diseaseList.get(1).equals(disease_3rd) || diseaseList.get(1).equals(disease_4th) || diseaseList.get(1).equals(disease_5th) || diseaseList.get(1).equals(disease_6th) || diseaseList.get(1).equals(disease_7th) || diseaseList.get(1).equals(disease_8th)) {
                                if (ment_disease_2.equals("")) ment_disease_2 = diseaseList4.get(1);
                                if (ment_disease_4.equals("")) ment_disease_4 = diseaseList6.get(1);
                                if (ment_disease_6.equals(""))
                                    ment_disease_6 = " " + diseaseList8.get(1) + " " + diseaseList9.get(1);
                            } else if (!(diseaseList.get(1).equals(disease_2nd) && diseaseList.get(1).equals(disease_3rd) && diseaseList.get(1).equals(disease_4th) && diseaseList.get(1).equals(disease_5th) && diseaseList.get(1).equals(disease_6th) && diseaseList.get(1).equals(disease_7th) && diseaseList.get(1).equals(disease_8th))) {
                            }

                            if (diseaseList.get(2).equals(disease_2nd) || diseaseList.get(2).equals(disease_3rd) || diseaseList.get(2).equals(disease_4th) || diseaseList.get(2).equals(disease_5th) || diseaseList.get(2).equals(disease_6th) || diseaseList.get(2).equals(disease_7th) || diseaseList.get(2).equals(disease_8th)) {
                                if (ment_disease_3.equals("")) ment_disease_3 = diseaseList5.get(2);
                                if (ment_disease_5.equals("")) ment_disease_5 = diseaseList7.get(2);
                            } else if (!(diseaseList.get(2).equals(disease_2nd) && diseaseList.get(2).equals(disease_3rd) && diseaseList.get(2).equals(disease_4th) && diseaseList.get(2).equals(disease_5th) && diseaseList.get(2).equals(disease_6th) && diseaseList.get(2).equals(disease_7th) && diseaseList.get(2).equals(disease_8th))) {
                            }

                            if (diseaseList.get(3).equals(disease_2nd) || diseaseList.get(3).equals(disease_3rd) || diseaseList.get(3).equals(disease_4th) || diseaseList.get(3).equals(disease_5th) || diseaseList.get(3).equals(disease_6th) || diseaseList.get(3).equals(disease_7th) || diseaseList.get(3).equals(disease_8th)) {
                                if (ment_disease_3.equals("")) ment_disease_3 = diseaseList5.get(3);
                            } else if (!(diseaseList.get(3).equals(disease_2nd) && diseaseList.get(3).equals(disease_3rd) && diseaseList.get(3).equals(disease_4th) && diseaseList.get(3).equals(disease_5th) && diseaseList.get(3).equals(disease_6th) && diseaseList.get(3).equals(disease_7th) && diseaseList.get(3).equals(disease_8th))) {
                            }

                            if (diseaseList.get(4).equals(disease_2nd) || diseaseList.get(4).equals(disease_3rd) || diseaseList.get(4).equals(disease_4th) || diseaseList.get(4).equals(disease_5th) || diseaseList.get(4).equals(disease_6th) || diseaseList.get(4).equals(disease_7th) || diseaseList.get(4).equals(disease_8th)) {
                                if (ment_disease_6.equals(""))
                                    ment_disease_6 = " " + diseaseList8.get(4) + " " + diseaseList9.get(4);
                            } else if (!(diseaseList.get(4).equals(disease_2nd) && diseaseList.get(4).equals(disease_3rd) && diseaseList.get(4).equals(disease_4th) && diseaseList.get(4).equals(disease_5th) && diseaseList.get(4).equals(disease_6th) && diseaseList.get(4).equals(disease_7th) && diseaseList.get(4).equals(disease_8th))) {
                            }

                            if (diseaseList.get(5).equals(disease_2nd) || diseaseList.get(5).equals(disease_3rd) || diseaseList.get(5).equals(disease_4th) || diseaseList.get(5).equals(disease_5th) || diseaseList.get(5).equals(disease_6th) || diseaseList.get(5).equals(disease_7th) || diseaseList.get(5).equals(disease_8th)) {
                                if (ment_disease_3.equals("")) ment_disease_3 = diseaseList5.get(5);
                                if (ment_disease_5.equals("")) ment_disease_5 = diseaseList7.get(5);
                            } else if (!(diseaseList.get(5).equals(disease_2nd) && diseaseList.get(5).equals(disease_3rd) && diseaseList.get(5).equals(disease_4th) && diseaseList.get(5).equals(disease_5th) && diseaseList.get(5).equals(disease_6th) && diseaseList.get(5).equals(disease_7th) && diseaseList.get(5).equals(disease_8th))) {
                            }

                            if (diseaseList.get(6).equals(disease_2nd) || diseaseList.get(6).equals(disease_3rd) || diseaseList.get(6).equals(disease_4th) || diseaseList.get(6).equals(disease_5th) || diseaseList.get(6).equals(disease_6th) || diseaseList.get(6).equals(disease_7th) || diseaseList.get(6).equals(disease_8th)) {
                                if (ment_disease_3.equals("")) ment_disease_3 = diseaseList5.get(6);
                                if (ment_disease_5.equals("")) ment_disease_5 = diseaseList7.get(6);
                            } else if (!(diseaseList.get(6).equals(disease_2nd) && diseaseList.get(6).equals(disease_3rd) && diseaseList.get(6).equals(disease_4th) && diseaseList.get(6).equals(disease_5th) && diseaseList.get(6).equals(disease_6th) && diseaseList.get(6).equals(disease_7th) && diseaseList.get(6).equals(disease_8th))) {
                            }

                            if (diseaseList.get(8).equals(disease_2nd) || diseaseList.get(8).equals(disease_3rd) || diseaseList.get(8).equals(disease_4th) || diseaseList.get(8).equals(disease_5th) || diseaseList.get(8).equals(disease_6th) || diseaseList.get(8).equals(disease_7th) || diseaseList.get(8).equals(disease_8th)) {
                                if (ment_disease_3.equals("")) ment_disease_3 = diseaseList5.get(8);
                                if (ment_disease_5.equals("")) ment_disease_5 = diseaseList7.get(8);
                            } else if (!(diseaseList.get(8).equals(disease_2nd) && diseaseList.get(8).equals(disease_3rd) && diseaseList.get(8).equals(disease_4th) && diseaseList.get(8).equals(disease_5th) && diseaseList.get(8).equals(disease_6th) && diseaseList.get(8).equals(disease_7th) && diseaseList.get(8).equals(disease_8th))) {
                            }
                        }

                        if (diseaseList.get(8).equals(disease_1st)) {
                            ment_disease_1 = UserDisease + " 보유하고 계신 사용자께는 오늘의 날씨에 " + diseaseList2.get(8) + " " + diseaseList3.get(8);

                            if (diseaseList.get(1).equals(disease_2nd) || diseaseList.get(1).equals(disease_3rd) || diseaseList.get(1).equals(disease_4th) || diseaseList.get(1).equals(disease_5th) || diseaseList.get(1).equals(disease_6th) || diseaseList.get(1).equals(disease_7th) || diseaseList.get(1).equals(disease_8th)) {
                                if (ment_disease_2.equals("")) ment_disease_2 = diseaseList4.get(1);
                                if (ment_disease_4.equals("")) ment_disease_4 = diseaseList6.get(1);
                                if (ment_disease_6.equals(""))
                                    ment_disease_6 = " " + diseaseList8.get(1) + " " + diseaseList9.get(1);
                            } else if (!(diseaseList.get(1).equals(disease_2nd) && diseaseList.get(1).equals(disease_3rd) && diseaseList.get(1).equals(disease_4th) && diseaseList.get(1).equals(disease_5th) && diseaseList.get(1).equals(disease_6th) && diseaseList.get(1).equals(disease_7th) && diseaseList.get(1).equals(disease_8th))) {
                            }

                            if (diseaseList.get(2).equals(disease_2nd) || diseaseList.get(2).equals(disease_3rd) || diseaseList.get(2).equals(disease_4th) || diseaseList.get(2).equals(disease_5th) || diseaseList.get(2).equals(disease_6th) || diseaseList.get(2).equals(disease_7th) || diseaseList.get(2).equals(disease_8th)) {
                                if (ment_disease_3.equals("")) ment_disease_3 = diseaseList5.get(2);
                                if (ment_disease_5.equals("")) ment_disease_5 = diseaseList7.get(2);
                            } else if (!(diseaseList.get(2).equals(disease_2nd) && diseaseList.get(2).equals(disease_3rd) && diseaseList.get(2).equals(disease_4th) && diseaseList.get(2).equals(disease_5th) && diseaseList.get(2).equals(disease_6th) && diseaseList.get(2).equals(disease_7th) && diseaseList.get(2).equals(disease_8th))) {
                            }

                            if (diseaseList.get(3).equals(disease_2nd) || diseaseList.get(3).equals(disease_3rd) || diseaseList.get(3).equals(disease_4th) || diseaseList.get(3).equals(disease_5th) || diseaseList.get(3).equals(disease_6th) || diseaseList.get(3).equals(disease_7th) || diseaseList.get(3).equals(disease_8th)) {
                                if (ment_disease_3.equals("")) ment_disease_3 = diseaseList5.get(3);
                            } else if (!(diseaseList.get(3).equals(disease_2nd) && diseaseList.get(3).equals(disease_3rd) && diseaseList.get(3).equals(disease_4th) && diseaseList.get(3).equals(disease_5th) && diseaseList.get(3).equals(disease_6th) && diseaseList.get(3).equals(disease_7th) && diseaseList.get(3).equals(disease_8th))) {
                            }

                            if (diseaseList.get(4).equals(disease_2nd) || diseaseList.get(4).equals(disease_3rd) || diseaseList.get(4).equals(disease_4th) || diseaseList.get(4).equals(disease_5th) || diseaseList.get(4).equals(disease_6th) || diseaseList.get(4).equals(disease_7th) || diseaseList.get(4).equals(disease_8th)) {
                                if (ment_disease_6.equals(""))
                                    ment_disease_6 = " " + diseaseList8.get(4) + " " + diseaseList9.get(4);
                            } else if (!(diseaseList.get(4).equals(disease_2nd) && diseaseList.get(4).equals(disease_3rd) && diseaseList.get(4).equals(disease_4th) && diseaseList.get(4).equals(disease_5th) && diseaseList.get(4).equals(disease_6th) && diseaseList.get(4).equals(disease_7th) && diseaseList.get(4).equals(disease_8th))) {
                            }

                            if (diseaseList.get(5).equals(disease_2nd) || diseaseList.get(5).equals(disease_3rd) || diseaseList.get(5).equals(disease_4th) || diseaseList.get(5).equals(disease_5th) || diseaseList.get(5).equals(disease_6th) || diseaseList.get(5).equals(disease_7th) || diseaseList.get(5).equals(disease_8th)) {
                                if (ment_disease_3.equals("")) ment_disease_3 = diseaseList5.get(5);
                                if (ment_disease_5.equals("")) ment_disease_5 = diseaseList7.get(5);
                            } else if (!(diseaseList.get(5).equals(disease_2nd) && diseaseList.get(5).equals(disease_3rd) && diseaseList.get(5).equals(disease_4th) && diseaseList.get(5).equals(disease_5th) && diseaseList.get(5).equals(disease_6th) && diseaseList.get(5).equals(disease_7th) && diseaseList.get(5).equals(disease_8th))) {
                            }

                            if (diseaseList.get(6).equals(disease_2nd) || diseaseList.get(6).equals(disease_3rd) || diseaseList.get(6).equals(disease_4th) || diseaseList.get(6).equals(disease_5th) || diseaseList.get(6).equals(disease_6th) || diseaseList.get(6).equals(disease_7th) || diseaseList.get(6).equals(disease_8th)) {
                                if (ment_disease_3.equals("")) ment_disease_3 = diseaseList5.get(6);
                                if (ment_disease_5.equals("")) ment_disease_5 = diseaseList7.get(6);
                            } else if (!(diseaseList.get(6).equals(disease_2nd) && diseaseList.get(6).equals(disease_3rd) && diseaseList.get(6).equals(disease_4th) && diseaseList.get(6).equals(disease_5th) && diseaseList.get(6).equals(disease_6th) && diseaseList.get(6).equals(disease_7th) && diseaseList.get(6).equals(disease_8th))) {
                            }

                            if (diseaseList.get(7).equals(disease_2nd) || diseaseList.get(7).equals(disease_3rd) || diseaseList.get(7).equals(disease_4th) || diseaseList.get(7).equals(disease_5th) || diseaseList.get(7).equals(disease_6th) || diseaseList.get(7).equals(disease_7th) || diseaseList.get(7).equals(disease_8th)) {
                                if (ment_disease_3.equals("")) ment_disease_3 = diseaseList5.get(7);
                            } else if (!(diseaseList.get(7).equals(disease_2nd) && diseaseList.get(7).equals(disease_3rd) && diseaseList.get(7).equals(disease_4th) && diseaseList.get(7).equals(disease_5th) && diseaseList.get(7).equals(disease_6th) && diseaseList.get(7).equals(disease_7th) && diseaseList.get(7).equals(disease_8th))) {
                            }
                        }

                        tv_ment1.setText(ment_disease_1 + " " + ment_disease_2 + " " + ment_disease_3 + " " + ment_disease_4 + ment_disease_5 + ment_disease_6 + "을/를 추천드립니다.");

                    } else { //질병이 없을 때 -> 날씨를 기준으로 한 멘트 데이터를 가져와 텍스트로 보여줌
                        if ((data_temp >= 20 && data_temp <= 23) && (data_uvi >= 0 && data_uvi <= 5) && (data_pop >= 0 && data_pop <= 39)) {

                            if (result4_1.equals("0")) result4_1 = "";
                            if (result5_1.equals("0")) result5_1 = "";

                            if (result4_1.equals("") && result5_1.equals("")) {
                                result3_1 = result3_1.replaceAll(",", "");
                                ment_weather_1 = result1_1 + "의 기온일 때는 " + result2_1 + " " + result3_1 + result4_1 + result5_1 + "를 추천드립니다.";
                            } else if (result4_1.equals("")) {
                                result5_1 = result5_1.replaceAll(",", "");
                                ment_weather_1 = result1_1 + "의 기온일 때는 " + result2_1 + " " + result3_1 + "\n" + result4_1 + result5_1 + "을 추천드립니다.";
                            } else if (result5_1.equals("")) {
                                result4_1 = result4_1.replaceAll(",", "");
                                ment_weather_1 = result1_1 + "의 기온일 때는 " + result2_1 + " " + result3_1 + "\n" + result4_1 + result5_1 + "를 추천드립니다.";
                            } else
                                ment_weather_1 = result1_1 + "의 기온일 때는 " + result2_1 + " " + result3_1 + "\n" + result4_1 + result5_1 + "을 추천드립니다.";

                            if (result6_1.equals("0") && result7_1.equals("0")) ment_weather_2 = "";
                            else ment_weather_2 = result6_1 + " " + result7_1 + "\n";

                            if (result8_1.equals("0")) ment_weather_3 = "";
                            else ment_weather_3 = result8_1 + "\n";

                            if (result9_1.equals("0")) ment_weather_4 = "";
                            else ment_weather_4 = result9_1;

                        } else if ((data_temp >= 24 && data_temp <= 27) && (data_uvi >= 0 && data_uvi <= 5) && (data_pop >= 0 && data_pop <= 39)) {

                            if (result4_2.equals("0")) result4_2 = "";
                            if (result5_2.equals("0")) result5_2 = "";

                            if (result4_2.equals("") && result5_2.equals("")) {
                                result3_2 = result3_2.replaceAll(",", "");
                                ment_weather_1 = result1_2 + "의 기온일 때는 " + result2_2 + " " + result3_2 + result4_2 + result5_2 + "를 추천드립니다.";
                            } else if (result4_2.equals("")) {
                                result5_2 = result5_2.replaceAll(",", "");
                                ment_weather_1 = result1_2 + "의 기온일 때는 " + result2_2 + " " + result3_2 + "\n" + result4_2 + result5_2 + "을 추천드립니다.";
                            } else if (result5_2.equals("")) {
                                result4_2 = result4_2.replaceAll(",", "");
                                ment_weather_1 = result1_2 + "의 기온일 때는 " + result2_2 + " " + result3_2 + "\n" + result4_2 + result5_2 + "를 추천드립니다.";
                            } else
                                ment_weather_1 = result1_2 + "의 기온일 때는 " + result2_2 + " " + result3_2 + "\n" + result4_2 + result5_2 + "을 추천드립니다.";

                            if (result6_2.equals("0") && result7_2.equals("0")) ment_weather_2 = "";
                            else ment_weather_2 = result6_2 + " " + result7_2 + "\n";

                            if (result8_2.equals("0")) ment_weather_3 = "";
                            else ment_weather_3 = result8_2 + "\n";

                            if (result9_2.equals("0")) ment_weather_4 = "";
                            else ment_weather_4 = result9_2;


                        } else if ((data_temp >= 28 && data_temp <= 31) && (data_uvi >= 0 && data_uvi <= 5) && (data_pop >= 0 && data_pop <= 39)) {

                            if (result4_3.equals("0")) result4_3 = "";
                            if (result5_3.equals("0")) result5_3 = "";

                            if (result4_3.equals("") && result5_3.equals("")) {
                                result3_3 = result3_1.replaceAll(",", "");
                                ment_weather_1 = result1_3 + "의 기온일 때는 " + result2_3 + " " + result3_3 + result4_3 + result5_3 + "를 추천드립니다.";
                            } else if (result4_3.equals("")) {
                                result5_3 = result5_3.replaceAll(",", "");
                                ment_weather_1 = result1_3 + "의 기온일 때는 " + result2_3 + " " + result3_3 + "\n" + result4_3 + result5_3 + "을 추천드립니다.";
                            } else if (result5_3.equals("")) {
                                result4_3 = result4_3.replaceAll(",", "");
                                ment_weather_1 = result1_3 + "의 기온일 때는 " + result2_3 + " " + result3_3 + "\n" + result4_3 + result5_3 + "를 추천드립니다.";
                            } else
                                ment_weather_1 = result1_3 + "의 기온일 때는 " + result2_3 + " " + result3_3 + "\n" + result4_3 + " " + result5_3 + "을 추천드립니다.";

                            if (result6_3.equals("0") && result7_3.equals("0")) ment_weather_2 = "";
                            else ment_weather_2 = result6_3 + " " + result7_3 + "\n";

                            if (result8_3.equals("0")) ment_weather_3 = "";
                            else ment_weather_3 = result8_3 + "\n";

                            if (result9_3.equals("0")) ment_weather_4 = "";
                            else ment_weather_4 = result9_3;


                        } else if ((data_temp >= 32) && (data_uvi >= 0 && data_uvi <= 5) && (data_pop >= 0 && data_pop <= 39)) {

                            if (result4_4.equals("0")) result4_4 = "";
                            if (result5_4.equals("0")) result5_4 = "";

                            if (result4_4.equals("") && result5_4.equals("")) {
                                result3_4 = result3_4.replaceAll(",", "");
                                ment_weather_1 = result1_4 + "의 기온일 때는 " + result2_4 + " " + result3_4 + result4_4 + result5_4 + "를 추천드립니다.";
                            } else if (result4_4.equals("")) {
                                result5_4 = result5_4.replaceAll(",", "");
                                ment_weather_1 = result1_4 + "의 기온일 때는 " + result2_4 + " " + result3_4 + "\n" + result4_4 + result5_4 + "을 추천드립니다.";
                            } else if (result5_4.equals("")) {
                                result4_4 = result4_4.replaceAll(",", "");
                                ment_weather_1 = result1_4 + "의 기온일 때는 " + result2_4 + " " + result3_4 + "\n" + result4_4 + result5_4 + "를 추천드립니다.";
                            } else
                                ment_weather_1 = result1_4 + "의 기온일 때는 " + result2_4 + " " + result3_4 + "\n" + result4_4 + result5_4 + "을 추천드립니다.";

                            if (result6_4.equals("0") && result7_4.equals("0")) ment_weather_2 = "";
                            else ment_weather_2 = result6_4 + " " + result7_4 + "\n";

                            if (result8_4.equals("0")) ment_weather_3 = "";
                            else ment_weather_3 = result8_4 + "\n";

                            if (result9_4.equals("0")) ment_weather_4 = "";
                            else ment_weather_4 = result9_4;

                        } else if ((data_temp >= 20 && data_temp <= 23) && (data_uvi >= 6 && data_uvi <= 8) && (data_pop >= 0 && data_pop <= 39)) {

                            if (result4_5.equals("0")) result4_5 = "";
                            if (result5_5.equals("0")) result5_5 = "";

                            if (result4_5.equals("") && result5_5.equals("")) {
                                result3_5 = result3_5.replaceAll(",", "");
                                ment_weather_1 = result1_5 + "의 기온일 때는 " + result2_5 + " " + result3_5 + result4_5 + result5_5 + "를 추천드립니다.";
                            } else if (result4_5.equals("")) {
                                result5_5 = result5_5.replaceAll(",", "");
                                ment_weather_1 = result1_5 + "의 기온일 때는 " + result2_5 + " " + result3_5 + "\n" + result4_5 + result5_5 + "을 추천드립니다.";
                            } else if (result5_5.equals("")) {
                                result4_5 = result4_5.replaceAll(",", "");
                                ment_weather_1 = result1_5 + "의 기온일 때는 " + result2_5 + " " + result3_5 + "\n" + result4_5 + result5_5 + "를 추천드립니다.";
                            } else
                                ment_weather_1 = result1_5 + "의 기온일 때는 " + result2_5 + " " + result3_5 + "\n" + result4_5 + result5_5 + "을 추천드립니다.";

                            if (result6_5.equals("0") && result7_5.equals("0")) ment_weather_2 = "";
                            else ment_weather_2 = result6_5 + " " + result7_5 + "\n";

                            if (result8_5.equals("0")) ment_weather_3 = "";
                            else ment_weather_3 = result8_5 + "\n";

                            if (result9_5.equals("0")) ment_weather_4 = "";
                            else ment_weather_4 = result9_5;

                        } else if ((data_temp >= 24 && data_temp <= 27) && (data_uvi >= 6 && data_uvi <= 8) && (data_pop >= 0 && data_pop <= 39)) {

                            if (result4_6.equals("0")) result4_6 = "";
                            if (result5_6.equals("0")) result5_6 = "";

                            if (result4_6.equals("") && result5_6.equals("")) {
                                result3_6 = result3_6.replaceAll(",", "");
                                ment_weather_1 = result1_6 + "의 기온일 때는 " + result2_6 + " " + result3_6 + result4_6 + result5_6 + "를 추천드립니다.";
                            } else if (result4_6.equals("")) {
                                result5_6 = result5_6.replaceAll(",", "");
                                ment_weather_1 = result1_6 + "의 기온일 때는 " + result2_6 + " " + result3_6 + "\n" + result4_6 + result5_6 + "을 추천드립니다.";
                            } else if (result5_6.equals("")) {
                                result4_6 = result4_6.replaceAll(",", "");
                                ment_weather_1 = result1_6 + "의 기온일 때는 " + result2_6 + " " + result3_6 + "\n" + result4_6 + result5_6 + "를 추천드립니다.";
                            } else
                                ment_weather_1 = result1_6 + "의 기온일 때는 " + result2_6 + " " + result3_6 + "\n" + result4_6 + result5_6 + "을 추천드립니다.";

                            if (result6_6.equals("0") && result7_6.equals("0")) ment_weather_2 = "";
                            else ment_weather_2 = result6_6 + " " + result7_6 + "\n";

                            if (result8_6.equals("0")) ment_weather_3 = "";
                            else ment_weather_3 = result8_6 + "\n";

                            if (result9_6.equals("0")) ment_weather_4 = "";
                            else ment_weather_4 = result9_6;


                        } else if ((data_temp >= 28 && data_temp <= 31) && (data_uvi >= 6 && data_uvi <= 8) && (data_pop >= 0 && data_pop <= 39)) {

                            if (result4_7.equals("0")) result4_7 = "";
                            if (result5_7.equals("0")) result5_7 = "";

                            if (result4_7.equals("") && result5_7.equals("")) {
                                result3_7 = result3_7.replaceAll(",", "");
                                ment_weather_1 = result1_7 + "의 기온일 때는 " + result2_7 + " " + result3_7 + result4_7 + result5_7 + "를 추천드립니다.";
                            } else if (result4_7.equals("")) {
                                result5_7 = result5_7.replaceAll(",", "");
                                ment_weather_1 = result1_7 + "의 기온일 때는 " + result2_7 + " " + result3_7 + "\n" + result4_7 + result5_7 + "을 추천드립니다.";
                            } else if (result5_7.equals("")) {
                                result4_7 = result4_7.replaceAll(",", "");
                                ment_weather_1 = result1_7 + "의 기온일 때는 " + result2_7 + " " + result3_7 + "\n" + result4_7 + result5_7 + "를 추천드립니다.";
                            } else
                                ment_weather_1 = result1_7 + "의 기온일 때는 " + result2_7 + " " + result3_7 + "\n" + result4_7 + result5_7 + "을 추천드립니다.";

                            if (result6_7.equals("0") && result7_7.equals("0")) ment_weather_2 = "";
                            else ment_weather_2 = result6_7 + " " + result7_7 + "\n";

                            if (result8_7.equals("0")) ment_weather_3 = "";
                            else ment_weather_3 = result8_7 + "\n";

                            if (result9_7.equals("0")) ment_weather_4 = "";
                            else ment_weather_4 = result9_7;


                        } else if ((data_temp >= 32) && (data_uvi >= 6 && data_uvi <= 8) && (data_pop >= 0 && data_pop <= 39)) {

                            if (result4_8.equals("0")) result4_8 = "";
                            if (result5_8.equals("0")) result5_8 = "";

                            if (result4_8.equals("") && result5_8.equals("")) {
                                result3_8 = result3_8.replaceAll(",", "");
                                ment_weather_1 = result1_8 + "의 기온일 때는 " + result2_8 + " " + result3_8 + result4_8 + result5_8 + "를 추천드립니다.";
                            } else if (result4_8.equals("")) {
                                result5_8 = result5_8.replaceAll(",", "");
                                ment_weather_1 = result1_8 + "의 기온일 때는 " + result2_8 + " " + result3_8 + "\n" + result4_8 + result5_8 + "을 추천드립니다.";
                            } else if (result5_8.equals("")) {
                                result4_8 = result4_8.replaceAll(",", "");
                                ment_weather_1 = result1_8 + "의 기온일 때는 " + result2_8 + " " + result3_8 + "\n" + result4_8 + result5_8 + "를 추천드립니다.";
                            } else
                                ment_weather_1 = result1_8 + "의 기온일 때는 " + result2_8 + " " + result3_8 + "\n" + result4_8 + result5_8 + "을 추천드립니다.";

                            if (result6_8.equals("0") && result7_8.equals("0")) ment_weather_2 = "";
                            else ment_weather_2 = result6_8 + " " + result7_8 + "\n";

                            if (result8_8.equals("0")) ment_weather_3 = "";
                            else ment_weather_3 = result8_8 + "\n";

                            if (result9_8.equals("0")) ment_weather_4 = "";
                            else ment_weather_4 = result9_8;


                        } else if ((data_temp >= 20 && data_temp <= 23) && (data_uvi >= 8) && (data_pop >= 0 && data_pop <= 39)) {

                            if (result4_9.equals("0")) result4_9 = "";
                            if (result5_9.equals("0")) result5_9 = "";

                            if (result4_9.equals("") && result5_9.equals("")) {
                                result3_9 = result3_9.replaceAll(",", "");
                                ment_weather_1 = result1_9 + "의 기온일 때는 " + result2_9 + " " + result3_9 + result4_9 + result5_9 + "를 추천드립니다.";
                            } else if (result4_9.equals("")) {
                                result5_9 = result5_9.replaceAll(",", "");
                                ment_weather_1 = result1_9 + "의 기온일 때는 " + result2_9 + " " + result3_9 + "\n" + result4_9 + result5_9 + "을 추천드립니다.";
                            } else if (result5_9.equals("")) {
                                result4_9 = result4_1.replaceAll(",", "");
                                ment_weather_1 = result1_9 + "의 기온일 때는 " + result2_9 + " " + result3_9 + "\n" + result4_9 + result5_9 + "를 추천드립니다.";
                            } else
                                ment_weather_1 = result1_9 + "의 기온일 때는 " + result2_9 + " " + result3_9 + "\n" + result4_9 + result5_9 + "을 추천드립니다.";

                            if (result6_9.equals("0") && result7_9.equals("0")) ment_weather_2 = "";
                            else ment_weather_2 = result6_9 + " " + result7_9 + "\n";

                            if (result8_9.equals("0")) ment_weather_3 = "";
                            else ment_weather_3 = result8_9 + "\n";

                            if (result9_9.equals("0")) ment_weather_4 = "";
                            else ment_weather_4 = result9_9;


                        } else if ((data_temp >= 24 && data_temp <= 27) && (data_uvi >= 8) && (data_pop >= 0 && data_pop <= 39)) {

                            if (result4_10.equals("0")) result4_10 = "";
                            if (result5_10.equals("0")) result5_10 = "";

                            if (result4_10.equals("") && result5_10.equals("")) {
                                result3_10 = result3_10.replaceAll(",", "");
                                ment_weather_1 = result1_10 + "의 기온일 때는 " + result2_10 + " " + result3_10 + result4_10 + result5_10 + "를 추천드립니다.";
                            } else if (result4_10.equals("")) {
                                result5_10 = result5_10.replaceAll(",", "");
                                ment_weather_1 = result1_10 + "의 기온일 때는 " + result2_10 + " " + result3_10 + "\n" + result4_10 + result5_10 + "을 추천드립니다.";
                            } else if (result5_10.equals("")) {
                                result4_10 = result4_10.replaceAll(",", "");
                                ment_weather_1 = result1_10 + "의 기온일 때는 " + result2_10 + " " + result3_10 + "\n" + result4_10 + result5_10 + "를 추천드립니다.";
                            } else
                                ment_weather_1 = result1_10 + "의 기온일 때는 " + result2_10 + " " + result3_10 + "\n" + result4_10 + result5_10 + "을 추천드립니다.";

                            if (result6_10.equals("0") && result7_10.equals("0"))
                                ment_weather_2 = "";
                            else ment_weather_2 = result6_10 + " " + result7_10 + "\n";

                            if (result8_10.equals("0")) ment_weather_3 = "";
                            else ment_weather_3 = result8_10 + "\n";

                            if (result9_10.equals("0")) ment_weather_4 = "";
                            else ment_weather_4 = result9_10;


                        } else if ((data_temp >= 28 && data_temp <= 31) && (data_uvi >= 8) && (data_pop >= 0 && data_pop <= 39)) {

                            if (result4_11.equals("0")) result4_11 = "";
                            if (result5_11.equals("0")) result5_11 = "";

                            if (result4_11.equals("") && result5_11.equals("")) {
                                result3_11 = result3_11.replaceAll(",", "");
                                ment_weather_1 = result1_11 + "의 기온일 때는 " + result2_11 + " " + result3_11 + result4_11 + result5_11 + "를 추천드립니다.";
                            } else if (result4_11.equals("")) {
                                result5_11 = result5_11.replaceAll(",", "");
                                ment_weather_1 = result1_11 + "의 기온일 때는 " + result2_11 + " " + result3_11 + "\n" + result4_11 + result5_11 + "을 추천드립니다.";
                            } else if (result5_11.equals("")) {
                                result4_11 = result4_11.replaceAll(",", "");
                                ment_weather_1 = result1_11 + "의 기온일 때는 " + result2_11 + " " + result3_11 + "\n" + result4_11 + result5_11 + "를 추천드립니다.";
                            } else
                                ment_weather_1 = result1_11 + "의 기온일 때는 " + result2_11 + " " + result3_11 + "\n" + result4_11 + result5_11 + "을 추천드립니다.";

                            if (result6_11.equals("0") && result7_11.equals("0"))
                                ment_weather_2 = "";
                            else ment_weather_2 = result6_11 + " " + result7_11 + "\n";

                            if (result8_11.equals("0")) ment_weather_3 = "";
                            else ment_weather_3 = result8_11 + "\n";

                            if (result9_11.equals("0")) ment_weather_4 = "";
                            else ment_weather_4 = result9_11;


                        } else if ((data_temp >= 32) && (data_uvi >= 8) && (data_pop >= 0 && data_pop <= 39)) {

                            if (result4_12.equals("0")) result4_12 = "";
                            if (result5_12.equals("0")) result5_12 = "";

                            if (result4_12.equals("") && result5_12.equals("")) {
                                result3_12 = result3_12.replaceAll(",", "");
                                ment_weather_1 = result1_12 + "의 기온일 때는 " + result2_12 + " " + result3_12 + result4_12 + result5_12 + "를 추천드립니다.";
                            } else if (result4_12.equals("")) {
                                result5_12 = result5_12.replaceAll(",", "");
                                ment_weather_1 = result1_12 + "의 기온일 때는 " + result2_12 + " " + result3_12 + "\n" + result4_12 + result5_12 + "을 추천드립니다.";
                            } else if (result5_12.equals("")) {
                                result4_12 = result4_12.replaceAll(",", "");
                                ment_weather_1 = result1_12 + "의 기온일 때는 " + result2_12 + " " + result3_12 + "\n" + result4_12 + result5_12 + "를 추천드립니다.";
                            } else
                                ment_weather_1 = result1_12 + "의 기온일 때는 " + result2_12 + " " + result3_12 + "\n" + result4_12 + result5_12 + "을 추천드립니다.";

                            if (result6_12.equals("0") && result7_12.equals("0"))
                                ment_weather_2 = "";
                            else ment_weather_2 = result6_12 + " " + result7_12 + "\n";

                            if (result8_12.equals("0")) ment_weather_3 = "";
                            else ment_weather_3 = result8_12 + "\n";

                            if (result9_12.equals("0")) ment_weather_4 = "";
                            else ment_weather_4 = result9_12;


                        } else if ((data_temp >= 20 && data_temp <= 23) && (data_uvi >= 0 && data_uvi <= 5) && (data_pop >= 40 && data_pop <= 100)) {

                            if (result4_13.equals("0")) result4_13 = "";
                            if (result5_13.equals("0")) result5_13 = "";

                            if (result4_13.equals("") && result5_13.equals("")) {
                                result3_13 = result3_13.replaceAll(",", "");
                                ment_weather_1 = result1_13 + "의 기온일 때는 " + result2_13 + " " + result3_13 + result4_13 + result5_13 + "를 추천드립니다.";
                            } else if (result4_13.equals("")) {
                                result5_13 = result5_13.replaceAll(",", "");
                                ment_weather_1 = result1_13 + "의 기온일 때는 " + result2_13 + " " + result3_13 + "\n" + result4_13 + result5_13 + "을 추천드립니다.";
                            } else if (result5_13.equals("")) {
                                result4_13 = result4_13.replaceAll(",", "");
                                ment_weather_1 = result1_13 + "의 기온일 때는 " + result2_13 + " " + result3_13 + "\n" + result4_13 + result5_13 + "를 추천드립니다.";
                            } else
                                ment_weather_1 = result1_13 + "의 기온일 때는 " + result2_13 + " " + result3_13 + "\n" + result4_13 + result5_13 + "을 추천드립니다.";

                            if (result6_13.equals("0") && result7_13.equals("0"))
                                ment_weather_2 = "";
                            else ment_weather_2 = result6_13 + " " + result7_13 + "\n";

                            if (result8_13.equals("0")) ment_weather_3 = "";
                            else ment_weather_3 = result8_13 + "\n";

                            if (result9_13.equals("0")) ment_weather_4 = "";
                            else ment_weather_4 = result9_13;


                        } else if ((data_temp >= 24 && data_temp <= 27) && (data_uvi >= 0 && data_uvi <= 5) && (data_pop >= 40 && data_pop <= 100)) {

                            if (result4_14.equals("0")) result4_14 = "";
                            if (result5_14.equals("0")) result5_14 = "";

                            if (result4_14.equals("") && result5_14.equals("")) {
                                result3_14 = result3_14.replaceAll(",", "");
                                ment_weather_1 = result1_14 + "의 기온일 때는 " + result2_14 + " " + result3_14 + result4_14 + result5_14 + "를 추천드립니다.";
                            } else if (result4_14.equals("")) {
                                result5_14 = result5_14.replaceAll(",", "");
                                ment_weather_1 = result1_14 + "의 기온일 때는 " + result2_14 + " " + result3_14 + "\n" + result4_14 + result5_14 + "을 추천드립니다.";
                            } else if (result5_14.equals("")) {
                                result4_14 = result4_14.replaceAll(",", "");
                                ment_weather_1 = result1_14 + "의 기온일 때는 " + result2_14 + " " + result3_14 + "\n" + result4_14 + result5_14 + "를 추천드립니다.";
                            } else
                                ment_weather_1 = result1_14 + "의 기온일 때는 " + result2_14 + " " + result3_14 + "\n" + result4_14 + result5_14 + "을 추천드립니다.";

                            if (result6_14.equals("0") && result7_14.equals("0"))
                                ment_weather_2 = "";
                            else ment_weather_2 = result6_14 + " " + result7_14 + "\n";

                            if (result8_14.equals("0")) ment_weather_3 = "";
                            else ment_weather_3 = result8_14 + "\n";

                            if (result9_14.equals("0")) ment_weather_4 = "";
                            else ment_weather_4 = result9_14;


                        } else if ((data_temp >= 28 && data_temp <= 31) && (data_uvi >= 0 && data_uvi <= 5) && (data_pop >= 40 && data_pop <= 100)) {

                            if (result4_15.equals("0")) result4_15 = "";
                            if (result5_15.equals("0")) result5_15 = "";

                            if (result4_15.equals("") && result5_15.equals("")) {
                                result3_15 = result3_15.replaceAll(",", "");
                                ment_weather_1 = result1_15 + "의 기온일 때는 " + result2_15 + " " + result3_15 + result4_15 + result5_15 + "를 추천드립니다.";
                            } else if (result4_15.equals("")) {
                                result5_15 = result5_15.replaceAll(",", "");
                                ment_weather_1 = result1_15 + "의 기온일 때는 " + result2_15 + " " + result3_15 + "\n" + result4_15 + result5_15 + "을 추천드립니다.";
                            } else if (result5_15.equals("")) {
                                result4_15 = result4_15.replaceAll(",", "");
                                ment_weather_1 = result1_15 + "의 기온일 때는 " + result2_15 + " " + result3_15 + "\n" + result4_15 + result5_15 + "를 추천드립니다.";
                            } else
                                ment_weather_1 = result1_15 + "의 기온일 때는 " + result2_15 + " " + result3_15 + "\n" + result4_15 + result5_15 + "을 추천드립니다.";

                            if (result6_15.equals("0") && result7_15.equals("0"))
                                ment_weather_2 = "";
                            else ment_weather_2 = result6_15 + " " + result7_15 + "\n";

                            if (result8_15.equals("0")) ment_weather_3 = "";
                            else ment_weather_3 = result8_15 + "\n";

                            if (result9_15.equals("0")) ment_weather_4 = "";
                            else ment_weather_4 = result9_15;


                        } else if ((data_temp >= 32) && (data_uvi >= 0 && data_uvi <= 5) && (data_pop >= 40 && data_pop <= 100)) {

                            if (result4_16.equals("0")) result4_16 = "";
                            if (result5_16.equals("0")) result5_16 = "";

                            if (result4_16.equals("") && result5_16.equals("")) {
                                result3_16 = result3_16.replaceAll(",", "");
                                ment_weather_1 = result1_16 + "의 기온일 때는 " + result2_16 + " " + result3_16 + result4_16 + result5_16 + "를 추천드립니다.";
                            } else if (result4_16.equals("")) {
                                result5_16 = result5_16.replaceAll(",", "");
                                ment_weather_1 = result1_16 + "의 기온일 때는 " + result2_16 + " " + result3_16 + "\n" + result4_16 + result5_16 + "을 추천드립니다.";
                            } else if (result5_16.equals("")) {
                                result4_16 = result4_16.replaceAll(",", "");
                                ment_weather_1 = result1_16 + "의 기온일 때는 " + result2_16 + " " + result3_16 + "\n" + result4_16 + result5_16 + "를 추천드립니다.";
                            } else
                                ment_weather_1 = result1_16 + "의 기온일 때는 " + result2_16 + " " + result3_16 + "\n" + result4_16 + result5_16 + "을 추천드립니다.";

                            if (result6_16.equals("0") && result7_16.equals("0"))
                                ment_weather_2 = "";
                            else ment_weather_2 = result6_16 + " " + result7_16 + "\n";

                            if (result8_16.equals("0")) ment_weather_3 = "";
                            else ment_weather_3 = result8_16 + "\n";

                            if (result9_16.equals("0")) ment_weather_4 = "";
                            else ment_weather_4 = result9_16;

                        } else if ((data_temp >= 20 && data_temp <= 23) && (data_uvi >= 6 && data_uvi <= 8) && (data_pop >= 40 && data_pop <= 100)) {

                            if (result4_17.equals("0")) result4_17 = "";
                            if (result5_17.equals("0")) result5_17 = "";

                            if (result4_17.equals("") && result5_17.equals("")) {
                                result3_17 = result3_17.replaceAll(",", "");
                                ment_weather_1 = result1_17 + "의 기온일 때는 " + result2_17 + " " + result3_17 + result4_17 + result5_17 + "를 추천드립니다.";
                            } else if (result4_17.equals("")) {
                                result5_17 = result5_17.replaceAll(",", "");
                                ment_weather_1 = result1_17 + "의 기온일 때는 " + result2_17 + " " + result3_17 + "\n" + result4_17 + result5_17 + "을 추천드립니다.";
                            } else if (result5_17.equals("")) {
                                result4_17 = result4_17.replaceAll(",", "");
                                ment_weather_1 = result1_17 + "의 기온일 때는 " + result2_17 + " " + result3_17 + "\n" + result4_17 + result5_17 + "를 추천드립니다.";
                            } else
                                ment_weather_1 = result1_17 + "의 기온일 때는 " + result2_17 + " " + result3_17 + "\n" + result4_17 + result5_17 + "을 추천드립니다.";

                            if (result6_17.equals("0") && result7_17.equals("0"))
                                ment_weather_2 = "";
                            else ment_weather_2 = result6_17 + " " + result7_17 + "\n";

                            if (result8_17.equals("0")) ment_weather_3 = "";
                            else ment_weather_3 = result8_17 + "\n";

                            if (result9_17.equals("0")) ment_weather_4 = "";
                            else ment_weather_4 = result9_17;

                        } else if ((data_temp >= 24 && data_temp <= 27) && (data_uvi >= 6 && data_uvi <= 8) && (data_pop >= 40 && data_pop <= 100)) {

                            if (result4_18.equals("0")) result4_18 = "";
                            if (result5_18.equals("0")) result5_18 = "";

                            if (result4_18.equals("") && result5_18.equals("")) {
                                result3_18 = result3_18.replaceAll(",", "");
                                ment_weather_1 = result1_18 + "의 기온일 때는 " + result2_18 + " " + result3_18 + result4_18 + result5_18 + "를 추천드립니다.";
                            } else if (result4_18.equals("")) {
                                result5_18 = result5_18.replaceAll(",", "");
                                ment_weather_1 = result1_18 + "의 기온일 때는 " + result2_18 + " " + result3_18 + "\n" + result4_18 + result5_18 + "을 추천드립니다.";
                            } else if (result5_18.equals("")) {
                                result4_18 = result4_18.replaceAll(",", "");
                                ment_weather_1 = result1_18 + "의 기온일 때는 " + result2_18 + " " + result3_18 + "\n" + result4_18 + result5_18 + "를 추천드립니다.";
                            } else
                                ment_weather_1 = result1_18 + "의 기온일 때는 " + result2_18 + " " + result3_18 + "\n" + result4_18 + result5_18 + "을 추천드립니다.";

                            if (result6_18.equals("0") && result7_18.equals("0"))
                                ment_weather_2 = "";
                            else ment_weather_2 = result6_18 + " " + result7_18 + "\n";

                            if (result8_18.equals("0")) ment_weather_3 = "";
                            else ment_weather_3 = result8_18 + "\n";

                            if (result9_18.equals("0")) ment_weather_4 = "";
                            else ment_weather_4 = result9_18;

                        } else if ((data_temp >= 28 && data_temp <= 31) && (data_uvi >= 6 && data_uvi <= 8) && (data_pop >= 40 && data_pop <= 100)) {

                            if (result4_19.equals("0")) result4_19 = "";
                            if (result5_19.equals("0")) result5_19 = "";

                            if (result4_19.equals("") && result5_19.equals("")) {
                                result3_19 = result3_19.replaceAll(",", "");
                                ment_weather_1 = result1_19 + "의 기온일 때는 " + result2_19 + " " + result3_19 + result4_19 + result5_19 + "를 추천드립니다.";
                            } else if (result4_19.equals("")) {
                                result5_19 = result5_19.replaceAll(",", "");
                                ment_weather_1 = result1_19 + "의 기온일 때는 " + result2_19 + " " + result3_19 + "\n" + result4_19 + result5_19 + "을 추천드립니다.";
                            } else if (result5_19.equals("")) {
                                result4_19 = result4_19.replaceAll(",", "");
                                ment_weather_1 = result1_19 + "의 기온일 때는 " + result2_19 + " " + result3_19 + "\n" + result4_19 + result5_19 + "를 추천드립니다.";
                            } else
                                ment_weather_1 = result1_19 + "의 기온일 때는 " + result2_19 + " " + result3_19 + "\n" + result4_19 + result5_19 + "을 추천드립니다.";

                            if (result6_19.equals("0") && result7_19.equals("0"))
                                ment_weather_2 = "";
                            else ment_weather_2 = result6_19 + " " + result7_19 + "\n";

                            if (result8_19.equals("0")) ment_weather_3 = "";
                            else ment_weather_3 = result8_19 + "\n";

                            if (result9_19.equals("0")) ment_weather_4 = "";
                            else ment_weather_4 = result9_19;

                        } else if ((data_temp >= 32) && (data_uvi >= 6 && data_uvi <= 8) && (data_pop >= 40 && data_pop <= 100)) {

                            if (result4_20.equals("0")) result4_20 = "";
                            if (result5_20.equals("0")) result5_20 = "";

                            if (result4_20.equals("") && result5_20.equals("")) {
                                result3_20 = result3_20.replaceAll(",", "");
                                ment_weather_1 = result1_20 + "의 기온일 때는 " + result2_20 + " " + result3_20 + result4_20 + result5_20 + "를 추천드립니다.";
                            } else if (result4_20.equals("")) {
                                result5_20 = result5_20.replaceAll(",", "");
                                ment_weather_1 = result1_20 + "의 기온일 때는 " + result2_20 + " " + result3_20 + "\n" + result4_20 + result5_20 + "을 추천드립니다.";
                            } else if (result5_20.equals("")) {
                                result4_20 = result4_20.replaceAll(",", "");
                                ment_weather_1 = result1_20 + "의 기온일 때는 " + result2_20 + " " + result3_20 + "\n" + result4_20 + result5_20 + "를 추천드립니다.";
                            } else
                                ment_weather_1 = result1_20 + "의 기온일 때는 " + result2_20 + " " + result3_20 + "\n" + result4_20 + result5_20 + "을 추천드립니다.";

                            if (result6_20.equals("0") && result7_20.equals("0"))
                                ment_weather_2 = "";
                            else ment_weather_2 = result6_20 + " " + result7_20 + "\n";

                            if (result8_20.equals("0")) ment_weather_3 = "";
                            else ment_weather_3 = result8_20 + "\n";

                            if (result9_20.equals("0")) ment_weather_4 = "";
                            else ment_weather_4 = result9_20;

                        } else if ((data_temp >= 20 && data_temp <= 23) && (data_uvi >= 8) && (data_pop >= 40 && data_pop <= 100)) {

                            if (result4_21.equals("0")) result4_21 = "";
                            if (result5_21.equals("0")) result5_21 = "";

                            if (result4_21.equals("") && result5_21.equals("")) {
                                result3_21 = result3_21.replaceAll(",", "");
                                ment_weather_1 = result1_21 + "의 기온일 때는 " + result2_21 + " " + result3_21 + result4_21 + result5_21 + "를 추천드립니다.";
                            } else if (result4_21.equals("")) {
                                result5_21 = result5_21.replaceAll(",", "");
                                ment_weather_1 = result1_21 + "의 기온일 때는 " + result2_21 + " " + result3_21 + "\n" + result4_21 + result5_21 + "을 추천드립니다.";
                            } else if (result5_21.equals("")) {
                                result4_21 = result4_21.replaceAll(",", "");
                                ment_weather_1 = result1_21 + "의 기온일 때는 " + result2_21 + " " + result3_21 + "\n" + result4_21 + result5_21 + "를 추천드립니다.";
                            } else
                                ment_weather_1 = result1_21 + "의 기온일 때는 " + result2_21 + " " + result3_21 + "\n" + result4_21 + result5_21 + "을 추천드립니다.";


                            if (result6_21.equals("0") && result7_21.equals("0"))
                                ment_weather_2 = "";
                            else ment_weather_2 = result6_21 + " " + result7_21 + "\n";

                            if (result8_21.equals("0")) ment_weather_3 = "";
                            else ment_weather_3 = result8_21 + "\n";

                            if (result9_21.equals("0")) ment_weather_4 = "";
                            else ment_weather_4 = result9_21;

                        } else if ((data_temp >= 24 && data_temp <= 27) && (data_uvi >= 8) && (data_pop >= 40 && data_pop <= 100)) {

                            if (result4_22.equals("0")) result4_22 = "";
                            if (result5_22.equals("0")) result5_22 = "";

                            if (result4_22.equals("") && result5_22.equals("")) {
                                result3_22 = result3_22.replaceAll(",", "");
                                ment_weather_1 = result1_22 + "의 기온일 때는 " + result2_22 + " " + result3_22 + result4_22 + result5_22 + "를 추천드립니다.";
                            } else if (result4_22.equals("")) {
                                result5_22 = result5_22.replaceAll(",", "");
                                ment_weather_1 = result1_22 + "의 기온일 때는 " + result2_22 + " " + result3_22 + "\n" + result4_22 + result5_22 + "을 추천드립니다.";
                            } else if (result5_22.equals("")) {
                                result4_22 = result4_22.replaceAll(",", "");
                                ment_weather_1 = result1_22 + "의 기온일 때는 " + result2_22 + " " + result3_22 + "\n" + result4_22 + result5_22 + "를 추천드립니다.";
                            } else
                                tv_ment1.setText(result1_22 + "의 기온일 때는 " + result2_22 + " " + result3_22 + "\n" + result4_22 + result5_22 + "을 추천드립니다.");

                            if (result6_22.equals("0") && result7_22.equals("0"))
                                ment_weather_2 = "";
                            else ment_weather_2 = result6_22 + " " + result7_22 + "\n";

                            if (result8_22.equals("0")) ment_weather_3 = "";
                            else ment_weather_3 = result8_22 + "\n";

                            if (result9_22.equals("0")) ment_weather_4 = "";
                            else ment_weather_3 = result9_22;

                        } else if ((data_temp >= 28 && data_temp <= 31) && (data_uvi >= 8) && (data_pop >= 40 && data_pop <= 100)) {

                            if (result4_23.equals("0")) result4_23 = "";
                            if (result5_23.equals("0")) result5_23 = "";

                            if (result4_23.equals("") && result5_23.equals("")) {
                                result3_23 = result3_23.replaceAll(",", "");
                                ment_weather_1 = result1_23 + "의 기온일 때는 " + result2_23 + " " + result3_23 + result4_23 + result5_23 + "를 추천드립니다.";
                            } else if (result4_23.equals("")) {
                                result5_23 = result5_23.replaceAll(",", "");
                                ment_weather_1 = result1_23 + "의 기온일 때는 " + result2_23 + " " + result3_23 + "\n" + result4_23 + result5_23 + "을 추천드립니다.";
                            } else if (result5_23.equals("")) {
                                result4_23 = result4_23.replaceAll(",", "");
                                ment_weather_1 = result1_23 + "의 기온일 때는 " + result2_23 + " " + result3_23 + "\n" + result4_23 + result5_23 + "를 추천드립니다.";
                            } else
                                ment_weather_1 = result1_23 + "의 기온일 때는 " + result2_23 + " " + result3_23 + "\n" + result4_23 + result5_23 + "을 추천드립니다.";

                            if (result6_23.equals("0") && result7_23.equals("0"))
                                ment_weather_2 = "";
                            else ment_weather_2 = result6_23 + " " + result7_23 + "\n";

                            if (result8_23.equals("0")) ment_weather_3 = "";
                            else ment_weather_3 = result8_23 + "\n";

                            if (result9_23.equals("0")) ment_weather_4 = "";
                            else ment_weather_4 = result9_23;

                        } else if ((data_temp >= 32) && (data_uvi >= 8) && (data_pop >= 40 && data_pop <= 100)) {

                            if (result4_24.equals("0")) result4_24 = "";
                            if (result5_24.equals("0")) result5_24 = "";

                            if (result4_24.equals("") && result5_24.equals("")) {
                                result3_24 = result3_24.replaceAll(",", "");
                                ment_weather_1 = result1_24 + "의 기온일 때는 " + result2_24 + " " + result3_24 + result4_24 + result5_24 + "를 추천드립니다.";
                            } else if (result4_24.equals("")) {
                                result5_24 = result5_24.replaceAll(",", "");
                                ment_weather_1 = result1_24 + "의 기온일 때는 " + result2_24 + " " + result3_24 + "\n" + result4_24 + result5_24 + "을 추천드립니다.";
                            } else if (result5_24.equals("")) {
                                result4_24 = result4_24.replaceAll(",", "");
                                ment_weather_1 = result1_24 + "의 기온일 때는 " + result2_24 + " " + result3_24 + "\n" + result4_24 + result5_24 + "를 추천드립니다.";
                            } else
                                ment_weather_1 = result1_24 + "의 기온일 때는 " + result2_24 + " " + result3_24 + "\n" + result4_24 + result5_24 + "을 추천드립니다.";

                            if (result6_24.equals("0") && result7_24.equals("0"))
                                ment_weather_2 = "";
                            else ment_weather_2 = result6_24 + " " + result7_24 + "\n";

                            if (result8_24.equals("0")) ment_weather_3 = "";
                            else ment_weather_3 = result8_24 + "\n";

                            if (result9_24.equals("0")) ment_weather_4 = "";
                            else ment_weather_4 = result9_24;
                        }
                        tv_ment1.setText(ment_weather_1 + "\n" + ment_weather_2 + ment_weather_3 + ment_weather_4);
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

    private void dataTest() { //url을 이용해 옷 이미지 가져오기
        diseaseMent();
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

                    if (!disease_1st.equals("없음")) {  // 질병이 있을 때 -> 질병을 기준으로 추천 옷 차림 데이터를 가져와 이미지로 보여줌
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
                                parasolIcon = "acc_parasol";

                            if(result7_24.equals("1"))
                                umbrellaIcon = "acc_umbrella";
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

                    String umbrellaImageURL = BuildConfig.BASE_SERVER_URL + "/" + umbrellaIcon + ".png";
                    Glide.with(getActivity()).load(umbrellaImageURL).into(iv_umbrella);

                    String parasolImageURL = BuildConfig.BASE_SERVER_URL + "/" + parasolIcon + ".png";
                    Glide.with(getActivity()).load(parasolImageURL).into(iv_parasol);

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

    private void diseaseMent() { //ment_disease 테이블에서 질병 멘트 데이터를 가져옴
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
    private void userDataTest() { //USER 테이블에서 사용자 정보를 가져옴 (id와 질병)
        String url = BuildConfig.BASE_SERVER_URL + "/PHP_userInfo.php";
        StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onResponse(String response) {
                try {

                    JSONObject jsonObject = new JSONObject(response);

                    JSONArray jsonArray = jsonObject.getJSONArray("result");
                    ArrayList<String> list = new ArrayList<String>();
                    ArrayList<String> list1 = new ArrayList<String>();
                    ArrayList<String> list2 = new ArrayList<String>();
                    ArrayList<String> list3 = new ArrayList<String>();
                    ArrayList<String> list4 = new ArrayList<String>();
                    ArrayList<String> list5 = new ArrayList<String>();
                    ArrayList<String> list6 = new ArrayList<String>();
                    ArrayList<String> list7 = new ArrayList<String>();
                    ArrayList<String> list8 = new ArrayList<String>();

                    for(int i = 0; i < jsonArray.length(); i++) {
                        jsonObject = jsonArray.getJSONObject(i);

                        list.add(jsonObject.getString("userID"));

                        list1.add(jsonObject.getString("userDisease_1st"));
                        list2.add(jsonObject.getString("userDisease_2nd"));
                        list3.add(jsonObject.getString("userDisease_3rd"));
                        list4.add(jsonObject.getString("userDisease_4th"));
                        list5.add(jsonObject.getString("userDisease_5th"));
                        list6.add(jsonObject.getString("userDisease_6th"));
                        list7.add(jsonObject.getString("userDisease_7th"));
                        list8.add(jsonObject.getString("userDisease_8th"));
                    }

                    for(int i = 0; i < jsonArray.length(); i++) {
                        if(list.get(i).equals(id)) {
                            c = i;
                            disease_1st = list1.get(c);
                            disease_2nd = list2.get(c);
                            disease_3rd = list3.get(c);
                            disease_4th = list4.get(c);
                            disease_5th = list5.get(c);
                            disease_6th = list6.get(c);
                            disease_7th = list7.get(c);
                            disease_8th = list8.get(c);

                            disease_1st = disease_1st.replaceAll("\n", "");
                            disease_2nd = disease_2nd.replaceAll("\n", "");
                            disease_2nd = disease_2nd.replaceAll("\n", "");
                            disease_2nd = disease_2nd.replaceAll("\n", "");
                            disease_2nd = disease_2nd.replaceAll("\n", "");
                            disease_2nd = disease_2nd.replaceAll("\n", "");
                            disease_2nd = disease_2nd.replaceAll("\n", "");
                            disease_2nd = disease_2nd.replaceAll("\n", "");

                            if(!disease_1st.equals("없음") && disease_2nd.equals("없음") && disease_3rd.equals("없음") && disease_4th.equals("없음") && disease_5th.equals("없음") && disease_6th.equals("없음") &&
                                    disease_7th.equals("없음") && disease_8th.equals("없음")) {
                                tv_ment_disease.setText(id + "님의 질병은 " + disease_1st + " 입니다.");
                                UserDisease = disease_1st + " 을/를";
                            }
                            else if(!disease_1st.equals("없음") && !disease_2nd.equals("없음") && disease_3rd.equals("없음") && disease_4th.equals("없음") && disease_5th.equals("없음") && disease_6th.equals("없음") &&
                                    disease_7th.equals("없음") && disease_8th.equals("없음")) {
                                tv_ment_disease.setText(id + "님의 질병은 " + disease_1st + ", "+ disease_2nd +" 입니다.");
                                UserDisease = disease_1st + ", "+ disease_2nd + " 을/를";
                            }
                            else if(!disease_1st.equals("없음") && !disease_2nd.equals("없음") && !disease_3rd.equals("없음") && disease_4th.equals("없음") && disease_5th.equals("없음") && disease_6th.equals("없음") &&
                                    disease_7th.equals("없음") && disease_8th.equals("없음")) {
                                tv_ment_disease.setText(id + "님의 질병은 " + disease_1st + ", "+ disease_2nd +", "+ disease_3rd +" 입니다.");
                                UserDisease = disease_1st + ", "+ disease_2nd +", "+ disease_3rd + " 을/를";
                            }
                            else if(!disease_1st.equals("없음") && !disease_2nd.equals("없음") &&!disease_3rd.equals("없음")&& !disease_4th.equals("없음") && disease_5th.equals("없음")&& disease_6th.equals("없음") &&
                                    disease_7th.equals("없음")&& disease_8th.equals("없음")) {
                                tv_ment_disease.setText(id + "님의 질병은 " + disease_1st + ", "+ disease_2nd +", "+ disease_3rd +", "+ disease_4th +" 입니다.");
                                UserDisease = disease_1st + ", "+ disease_2nd +", "+ disease_3rd +", "+ disease_4th + " 을/를";
                            }
                            else if(!disease_1st.equals("없음") && !disease_2nd.equals("없음") && !disease_3rd.equals("없음") && !disease_4th.equals("없음") && !disease_5th.equals("없음") && disease_6th.equals("없음") &&
                                    disease_7th.equals("없음") && disease_8th.equals("없음")) {
                                tv_ment_disease.setText(id + "님의 질병은 " + disease_1st + ", " + disease_2nd + ", " + disease_3rd + ", " + disease_4th + ", " + disease_5th + " 입니다.");
                                UserDisease = disease_1st + ", "+ disease_2nd +", "+ disease_3rd +", "+ disease_4th + ", " + disease_5th + " 을/를";
                            }
                            else if(!disease_1st.equals("없음") && !disease_2nd.equals("없음") && !disease_3rd.equals("없음") && !disease_4th.equals("없음") && !disease_5th.equals("없음") && !disease_6th.equals("없음") &&
                                    disease_7th.equals("없음") && disease_8th.equals("없음")) {
                                tv_ment_disease.setText(id + "님의 질병은 " + disease_1st + ", " + disease_2nd + ", " + disease_3rd + ", " + disease_4th + ", " + disease_5th + ", " + disease_6th + " 입니다.");
                                UserDisease = disease_1st + ", "+ disease_2nd +", "+ disease_3rd +", "+ disease_4th + ", " + disease_5th + ", " + disease_6th + " 을/를";
                            }
                            else if(!disease_1st.equals("없음") && !disease_2nd.equals("없음") && !disease_3rd.equals("없음") && !disease_4th.equals("없음") && !disease_5th.equals("없음") && !disease_6th.equals("없음") &&
                                    !disease_7th.equals("없음") && disease_8th.equals("없음")) {
                                tv_ment_disease.setText(id + "님의 질병은 " + disease_1st + ", " + disease_2nd + ", " + disease_3rd + ", " + disease_4th + ", " + disease_5th + ", " + disease_6th + ", " + disease_7th + " 입니다.");
                                UserDisease = disease_1st + ", "+ disease_2nd +", "+ disease_3rd +", "+ disease_4th + ", " + disease_5th + ", " + disease_6th + ", " + disease_7th + " 을/를";
                            }
                            else if(!disease_1st.equals("없음") && !disease_2nd.equals("없음") && !disease_3rd.equals("없음") && !disease_4th.equals("없음") && !disease_5th.equals("없음") && !disease_6th.equals("없음") &&
                                    !disease_7th.equals("없음") && !disease_8th.equals("없음")) {
                                tv_ment_disease.setText(id + "님의 질병은 " + disease_1st + ", " + disease_2nd + ", " + disease_3rd + ", " + disease_4th + ", " + disease_5th + ", " + disease_6th + ", "+ disease_7th + ", "+ disease_8th + " 입니다.");
                                UserDisease = disease_1st + ", "+ disease_2nd +", "+ disease_3rd +", "+ disease_4th + ", " + disease_5th + ", " + disease_6th + ", " + disease_7th + ", "+ disease_8th + " 을/를";
                            }

                        }

                    }
                    mentData();
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

    private static String getTimestampToDate(String timestampStr){
        long timestamp = Long.parseLong(timestampStr);
        Date date = new java.util.Date(timestamp*1000L);
        SimpleDateFormat sdf = new java.text.SimpleDateFormat("a h시");
        sdf.setTimeZone(java.util.TimeZone.getTimeZone("GMT+9"));
        String formattedDate = sdf.format(date);
        return formattedDate;
    }


}
