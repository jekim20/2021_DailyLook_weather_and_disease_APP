package com.example.swipeex;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.ImageView;
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

public class ThirdFragment extends Fragment {
    private GpsTracker gpsTracker;
    double lat;
    double lng;
    String address;
    int data_pop;

    TextView cityView, dateView;
    TextView tv_weather1, tv_weather2, tv_weather3, tv_weather4, tv_weather5, tv_weather6, tv_weather7;
    TextView tv_MinMax1, tv_MinMax2, tv_MinMax3, tv_MinMax4, tv_MinMax5, tv_MinMax6, tv_MinMax7;
    TextView tv_pop1, tv_pop2, tv_pop3, tv_pop4, tv_pop5, tv_pop6, tv_pop7;
    ImageView iv_icon1, iv_icon2, iv_icon3, iv_icon4, iv_icon5, iv_icon6, iv_icon7;
    ImageButton ib_update;
    androidx.constraintlayout.widget.ConstraintLayout background;
    static RequestQueue requestQueue;

    ArrayList<String> maxList = new ArrayList<String>();
    ArrayList<Integer> uviList = new ArrayList<Integer>();

    int topRandomNum, top2RandomNum, bottomRandomNum, bottomShortRandNum, shoesRandomNum;

    public static ThirdFragment newInstance() {
        ThirdFragment fragment = new ThirdFragment();
        return fragment;
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_third, container, false);

        background = view.findViewById(R.id.background);
        dateView = view.findViewById(R.id.dateVIew);
        cityView = view.findViewById(R.id.cityView);
        ib_update = view.findViewById(R.id.ib_update);

        tv_weather1 = view.findViewById(R.id.tv_weather1);
        tv_weather2 = view.findViewById(R.id.tv_weather2);
        tv_weather3 = view.findViewById(R.id.tv_weather3);
        tv_weather4 = view.findViewById(R.id.tv_weather4);
        tv_weather5 = view.findViewById(R.id.tv_weather5);
        tv_weather6 = view.findViewById(R.id.tv_weather6);
        tv_weather7 = view.findViewById(R.id.tv_weather7);

        tv_MinMax1 = view.findViewById(R.id.tv_MinMax1);
        tv_MinMax2 = view.findViewById(R.id.tv_MinMax2);
        tv_MinMax3 = view.findViewById(R.id.tv_MinMax3);
        tv_MinMax4 = view.findViewById(R.id.tv_MinMax4);
        tv_MinMax5 = view.findViewById(R.id.tv_MinMax5);
        tv_MinMax6 = view.findViewById(R.id.tv_MinMax6);
        tv_MinMax7 = view.findViewById(R.id.tv_MinMax7);

        tv_pop1 = view.findViewById(R.id.tv_pop1);
        tv_pop2 = view.findViewById(R.id.tv_pop2);
        tv_pop3 = view.findViewById(R.id.tv_pop3);
        tv_pop4 = view.findViewById(R.id.tv_pop4);
        tv_pop5 = view.findViewById(R.id.tv_pop5);
        tv_pop6 = view.findViewById(R.id.tv_pop6);
        tv_pop7 = view.findViewById(R.id.tv_pop7);

        iv_icon1 = view.findViewById(R.id.iv_icon1);
        iv_icon2 = view.findViewById(R.id.iv_icon2);
        iv_icon3 = view.findViewById(R.id.iv_icon3);
        iv_icon4 = view.findViewById(R.id.iv_icon4);
        iv_icon5 = view.findViewById(R.id.iv_icon5);
        iv_icon6 = view.findViewById(R.id.iv_icon6);
        iv_icon7 = view.findViewById(R.id.iv_icon7);

        gpsTracker = new GpsTracker(getActivity());

        double latitude = gpsTracker.getLatitude();
        double longitude = gpsTracker.getLongitude();
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

        HourlyCall();

        ib_update.setOnClickListener(new View.OnClickListener() { //업데이트 버튼
            @Override
            public void onClick(View v) {
                dateViewMethod();
                changeBackground();
                DailyCall();

                double latitude = gpsTracker.getLatitude();
                double longitude = gpsTracker.getLongitude();
                lat = latitude;
                lng = longitude;

                address = getCurrentAddress(latitude, longitude);
                cityView.setText(address);

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

        if(data_pop < 40) {
            if((bg_getAMPM.equals("오전") && (4 < Integer.parseInt(bg_getTime) && 12 > Integer.parseInt(bg_getTime))) || (bg_getAMPM.equals("오후") && Integer.parseInt(bg_getTime) < 7) || (bg_getAMPM.equals("오후") && Integer.parseInt(bg_getTime) == 12)) {
                background.setBackgroundColor(Color.parseColor("#6CC2FF"));
            }
            else if((bg_getAMPM.equals("오전") && 4 >= Integer.parseInt(bg_getTime)) || (bg_getAMPM.equals("오전") && Integer.parseInt(bg_getTime) == 12) || (bg_getAMPM.equals("오후") && (Integer.parseInt(bg_getTime) >= 7 && Integer.parseInt(bg_getTime) < 12))) {
                background.setBackgroundColor(Color.parseColor("#011B34"));
            }
        }
        else {
            background.setBackgroundColor(Color.parseColor("#4B78A4"));
        }


    }

    public void dateViewMethod() { //업데이트 한 시간 보여주기
        long now = System.currentTimeMillis();
        Date date = new Date(now);
        SimpleDateFormat simpleDateFormatDay = new SimpleDateFormat("M/d a");
        SimpleDateFormat simpleDateFormatTime = new SimpleDateFormat("h:mm");
        String getDay = simpleDateFormatDay.format(date);
        String getTime = simpleDateFormatTime.format(date);

        String getDate = getDay + " " + getTime;

        dateView.setText("업데이트 " + getDate);

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

                    //시간설정
                    JSONArray jsonArray = jsonObject.getJSONArray("daily");
                    ArrayList<String> list = new ArrayList<String>();
                    ArrayList<String> list2 = new ArrayList<String>();
                    //ArrayList<String> list3 = new ArrayList<String>();
                    ArrayList<String> list4 = new ArrayList<String>();
                    ArrayList<String> popList = new ArrayList<String>();

                    for(int i = 0; i < jsonArray.length(); i++) {
                        jsonObject = jsonArray.getJSONObject(i);
                        JSONObject a = jsonArray.getJSONObject(i);
                        JSONObject b = jsonArray.getJSONObject(i);
                        JSONObject c = jsonArray.getJSONObject(i);
                        JSONObject e = jsonArray.getJSONObject(i);

                        list.add(jsonObject.getString("dt"));

                        a = jsonObject.getJSONObject("temp");
                        list2.add(a.getString("min"));

                        b = jsonObject.getJSONObject("temp");
                        maxList.add(b.getString("max"));

                        JSONArray arr2 = c.getJSONArray("weather");
                        JSONObject d = arr2.getJSONObject(0);
                        list4.add(d.getString("icon"));

                        popList.add(Integer.toString((int)(e.getDouble("pop")*100)));

                        uviList.add((int)(jsonObject.getDouble("uvi")));

                    }

                    String day1 = getTimestampToDate(list.get(0));
                    String day2 = getTimestampToDate(list.get(1));
                    String day3 = getTimestampToDate(list.get(2));
                    String day4 = getTimestampToDate(list.get(3));
                    String day5 = getTimestampToDate(list.get(4));
                    String day6 = getTimestampToDate(list.get(5));
                    String day7 = getTimestampToDate(list.get(6));

                    tv_weather1.setText(day1);
                    tv_weather2.setText(day2);
                    tv_weather3.setText(day3);
                    tv_weather4.setText(day4);
                    tv_weather5.setText(day5);
                    tv_weather6.setText(day6);
                    tv_weather7.setText(day7);



                    //최저기온 설정
                    int tempMin1 = Integer.parseInt(String.valueOf(Math.round((Double.parseDouble(list2.get(0))-273.15)*100)/100));
                    int tempMin2 = Integer.parseInt(String.valueOf(Math.round((Double.parseDouble(list2.get(1))-273.15)*100)/100));
                    int tempMin3 = Integer.parseInt(String.valueOf(Math.round((Double.parseDouble(list2.get(2))-273.15)*100)/100));
                    int tempMin4 = Integer.parseInt(String.valueOf(Math.round((Double.parseDouble(list2.get(3))-273.15)*100)/100));
                    int tempMin5 = Integer.parseInt(String.valueOf(Math.round((Double.parseDouble(list2.get(4))-273.15)*100)/100));
                    int tempMin6 = Integer.parseInt(String.valueOf(Math.round((Double.parseDouble(list2.get(5))-273.15)*100)/100));
                    int tempMin7 = Integer.parseInt(String.valueOf(Math.round((Double.parseDouble(list2.get(6))-273.15)*100)/100));

                    //최고기온 설정
                    int tempMax1 = Integer.parseInt(String.valueOf(Math.round((Double.parseDouble(maxList.get(0))-273.15)*100)/100));
                    int tempMax2 = Integer.parseInt(String.valueOf(Math.round((Double.parseDouble(maxList.get(1))-273.15)*100)/100));
                    int tempMax3 = Integer.parseInt(String.valueOf(Math.round((Double.parseDouble(maxList.get(2))-273.15)*100)/100));
                    int tempMax4 = Integer.parseInt(String.valueOf(Math.round((Double.parseDouble(maxList.get(3))-273.15)*100)/100));
                    int tempMax5 = Integer.parseInt(String.valueOf(Math.round((Double.parseDouble(maxList.get(4))-273.15)*100)/100));
                    int tempMax6 = Integer.parseInt(String.valueOf(Math.round((Double.parseDouble(maxList.get(5))-273.15)*100)/100));
                    int tempMax7 = Integer.parseInt(String.valueOf(Math.round((Double.parseDouble(maxList.get(6))-273.15)*100)/100));

                    tv_MinMax1.setText(tempMin1 + "    /   " + tempMax1);
                    tv_MinMax2.setText(tempMin2 + "    /   " + tempMax2);
                    tv_MinMax3.setText(tempMin3 + "    /   " + tempMax3);
                    tv_MinMax4.setText(tempMin4 + "    /   " + tempMax4);
                    tv_MinMax5.setText(tempMin5 + "    /   " + tempMax5);
                    tv_MinMax6.setText(tempMin6 + "    /   " + tempMax6);
                    tv_MinMax7.setText(tempMin7 + "    /   " + tempMax7);

                    //강수확률 설정
                    tv_pop1.setText(popList.get(0) + "%");
                    tv_pop2.setText(popList.get(1) + "%");
                    tv_pop3.setText(popList.get(2) + "%");
                    tv_pop4.setText(popList.get(3) + "%");
                    tv_pop5.setText(popList.get(4) + "%");
                    tv_pop6.setText(popList.get(5) + "%");
                    tv_pop7.setText(popList.get(6) + "%");

                    //아이콘 설정
                    String iconUrl1 = "http://openweathermap.org/img/wn/" + list4.get(0) + "@2x.png";
                    Glide.with(getActivity()).load(iconUrl1).into(iv_icon1);

                    String iconUrl2 = "http://openweathermap.org/img/wn/" + list4.get(1) + "@2x.png";
                    Glide.with(getActivity()).load(iconUrl2).into(iv_icon2);

                    String iconUrl3 = "http://openweathermap.org/img/wn/" + list4.get(2) + "@2x.png";
                    Glide.with(getActivity()).load(iconUrl3).into(iv_icon3);

                    String iconUrl4 = "http://openweathermap.org/img/wn/" + list4.get(3) + "@2x.png";
                    Glide.with(getActivity()).load(iconUrl4).into(iv_icon4);

                    String iconUrl5 = "http://openweathermap.org/img/wn/" + list4.get(4) + "@2x.png";
                    Glide.with(getActivity()).load(iconUrl5).into(iv_icon5);

                    String iconUrl6 = "http://openweathermap.org/img/wn/" + list4.get(5) + "@2x.png";
                    Glide.with(getActivity()).load(iconUrl6).into(iv_icon6);

                    String iconUrl7 = "http://openweathermap.org/img/wn/" + list4.get(6) + "@2x.png";
                    Glide.with(getActivity()).load(iconUrl7).into(iv_icon7);

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
        SimpleDateFormat sdf = new java.text.SimpleDateFormat("MM.dd\nEE요일");
        sdf.setTimeZone(java.util.TimeZone.getTimeZone("GMT+9"));
        String formattedDate = sdf.format(date);
        return formattedDate;
    }

}