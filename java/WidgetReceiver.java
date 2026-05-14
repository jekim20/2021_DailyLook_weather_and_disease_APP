package com.example.swipeex;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.widget.ImageView;
import android.widget.RemoteViews;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.InputSource;

import java.io.IOException;
import java.io.StringReader;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

public class WidgetReceiver extends AppWidgetProvider {

    private GpsTracker gpsTracker;
    double lat;
    double lng;

    private static final String SYNC_CLICKED = "automaticWidgetSyncButtonClick";

    RequestQueue queue;

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        RemoteViews remoteViews;
        ComponentName watchWidget;

        remoteViews = new RemoteViews(context.getPackageName(), R.layout.widjet_layout);
        watchWidget = new ComponentName(context, WidgetReceiver.class);

        appWidgetManager.updateAppWidget(watchWidget, remoteViews);
    }


    @Override
    public void onReceive(final Context context, Intent intent) {

        if (SYNC_CLICKED.equals(intent.getAction())) {

            long now = System.currentTimeMillis();
            Date date = new Date(now);
            SimpleDateFormat sdf = new SimpleDateFormat("MM/dd hh:mm:ss");
            String getTime = sdf.format(date);
            String refresh_time = "업데이트 " + getTime;
            final RemoteViews remoteViews2 = new RemoteViews(context.getPackageName(), R.layout.widjet_layout);
            remoteViews2.setTextViewText(R.id.text_refresh, refresh_time);

            AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);

            RemoteViews remoteViews;
            ComponentName watchWidget;

            remoteViews = new RemoteViews(context.getPackageName(), R.layout.widjet_layout);
            watchWidget = new ComponentName(context, WidgetReceiver.class);
            appWidgetManager.updateAppWidget(watchWidget, remoteViews);

        }

        gpsTracker = new GpsTracker(context);

        double latitude = gpsTracker.getLatitude();
        double longitude = gpsTracker.getLongitude();
        lat = latitude;
        lng = longitude;

        String address = getCurrentAddress(context, latitude, longitude);
        String url = "https://api.openweathermap.org/data/2.5/weather?"
        + "lat=" + lat
        + "&lon=" + lng
        + "&appid=" + BuildConfig.OPEN_WEATHER_API_KEY
        + "&mode=xml"
        + "&units=metric";
        if(intent.getStringExtra("mode") != null) {

            final RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.widjet_layout);
            StringRequest currentRequest = new StringRequest(Request.Method.POST, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {

                                DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
                                DocumentBuilder builder = factory.newDocumentBuilder();
                                Document doc = builder.parse(new InputSource(new StringReader(response)));

                                Element tempElement = (Element)(doc.getElementsByTagName("temperature").item(0));
                                String temperature = tempElement.getAttribute("value");
                                // 최고, 최소기온
                                String temp_min = tempElement.getAttribute("min");
                                String temp_max = tempElement.getAttribute("max");
                                double min = Float.parseFloat(temp_min);
                                double min2 = (double)Math.round(min);
                                int min3 = (int)min2;
                                String minResult = min3 + "";
                                double max = Float.parseFloat(temp_max);
                                double max2 = (double)Math.round(max);
                                int max3 = (int)max2;
                                String maxResult = max3 + "";
                                remoteViews.setTextViewText(R.id.min_max, "최고:" + maxResult + "℃ 최저:" + minResult + "℃");

                                long now = System.currentTimeMillis();
                                Date date = new Date(now);
                                SimpleDateFormat sdf = new SimpleDateFormat("MM/dd hh:mm");
                                String getTime = sdf.format(date);

                                Element updateElement = (Element)(doc.getElementsByTagName("lastupdate").item(0));
                                String lastUpdate = updateElement.getAttribute("value");
                                String update_month = lastUpdate.substring(5,7);
                                String update_date = lastUpdate.substring(8,10);
                                String update_time = lastUpdate.substring(11,16);
                                remoteViews.setTextViewText(R.id.text_refresh, "업데이트 " + getTime);


                                long now2 = System.currentTimeMillis();
                                Date date2 = new Date(now2);
                                SimpleDateFormat AMPM = new SimpleDateFormat("a");
                                SimpleDateFormat time = new SimpleDateFormat("h");
                                String bg_getAMPM = AMPM.format(date2);
                                String bg_getTime = time.format(date2);

                                //배경 바꾸기
                               if((bg_getAMPM.equals("오전") && (4 < Integer.parseInt(bg_getTime) && 12 > Integer.parseInt(bg_getTime))) || (bg_getAMPM.equals("오후") && Integer.parseInt(bg_getTime) < 7) || (bg_getAMPM.equals("오후") && Integer.parseInt(bg_getTime) == 12)) {
                                    remoteViews.setImageViewResource(R.id.iv_widgetBackground, R.drawable.widget_afternoon);
                                }
                                else if((bg_getAMPM.equals("오전") && 4 >= Integer.parseInt(bg_getTime)) || (bg_getAMPM.equals("오전") && Integer.parseInt(bg_getTime) == 12) || (bg_getAMPM.equals("오후") && (Integer.parseInt(bg_getTime) >= 7 && Integer.parseInt(bg_getTime) < 12))) {
                                    remoteViews.setImageViewResource(R.id.iv_widgetBackground, R.drawable.widget_night);
                                }

                                Element cityElement = (Element)(doc.getElementsByTagName("city").item(0));
                                String city = cityElement.getAttribute("name");
                                remoteViews.setTextViewText(R.id.city_view, address);

                                double temp = Float.parseFloat(temperature);
                                double temp2 = (double)Math.round(temp);
                                int temp3 = (int)temp2;
                                String tempResult = temp3 + "";
                                remoteViews.setTextViewText(R.id.lab2_text, tempResult + "°");

                                Element weatherElement =(Element)(doc.getElementsByTagName("weather").item(0));
                                String symbol = weatherElement.getAttribute("icon");

                                ImageRequest imageRequest = new ImageRequest("https://openweathermap.org/img/w/" + symbol + ".png", new Response.Listener<Bitmap>() {
                                    @Override
                                    public void onResponse(Bitmap response) {
                                        remoteViews.setImageViewBitmap(R.id.lab2_image, response);
                                        AppWidgetManager manager = AppWidgetManager.getInstance(context);
                                        manager.updateAppWidget(new ComponentName(context, WidgetReceiver.class), remoteViews);
                                    }
                                }, 0, 0, ImageView.ScaleType.CENTER_CROP, null, new Response.ErrorListener() {
                                    @Override
                                    public void onErrorResponse(VolleyError error) {

                                    }
                                });

                                queue.add(imageRequest);
                            } catch (Exception e) {

                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {

                        }
                    });

            queue = Volley.newRequestQueue(context);
            queue.add(currentRequest);

        }
        super.onReceive(context, intent);
    }

    @Override
    public void onEnabled(Context context) { // 최초 앱위젯이 실행되었을 때 실행되는 메소드
        super.onEnabled(context);
        Intent alntent = new Intent(context, WidgetReceiver.class);
        alntent.putExtra("mode", "data");
        PendingIntent pIntent = PendingIntent.getBroadcast(context, 11, alntent, PendingIntent.FLAG_UPDATE_CURRENT);

        AlarmManager am = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        am.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), 10000, pIntent);
        // 10초에 한 번씩 Broadcast Receiver가 실행되면서 위젯 데이터가 업데이트
    }

    @Override
    public void onDisabled(Context context) { // 위젯이 화면에서 제거되는 순간 실행되는 메소드
        super.onDisabled(context);
        Intent alntent = new Intent(context, WidgetReceiver.class);
        PendingIntent pIntent = PendingIntent.getBroadcast(context, 11, alntent, PendingIntent.FLAG_UPDATE_CURRENT);

        AlarmManager am = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        am.cancel(pIntent);
    }

    public String getCurrentAddress(Context context, double latitude, double longitude) {
        Geocoder geocoder = new Geocoder(context, Locale.getDefault());

        List<Address> addresses;

        try {
            addresses = geocoder.getFromLocation(
                    latitude,
                    longitude,
                    7);
        } catch (IOException ioException) {
            Toast.makeText(context, "지오코더 서비스 사용불가", Toast.LENGTH_LONG).show();
            return "지오코더 서비스 사용불가";
        } catch (IllegalArgumentException illegalArgumentException) {
            Toast.makeText(context, "잘못된 GPS 좌표", Toast.LENGTH_LONG).show();
            return "잘못된 GPS 좌표";
        }

        if(addresses == null || addresses.size() == 0) {
            Toast.makeText(context, "주소 미발견", Toast.LENGTH_LONG).show();
            return "주소 미발견";
        }

        Address address = addresses.get(0);
        return address.getLocality().toString() + "\n";
    }

    protected PendingIntent getPendingSelfIntent(Context context, String action) {
        Intent intent = new Intent(context, getClass());
        intent.setAction(action);
        return PendingIntent.getBroadcast(context, 0, intent, 0);
    }

}