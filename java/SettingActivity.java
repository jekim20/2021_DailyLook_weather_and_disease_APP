package com.example.swipeex;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Switch;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class SettingActivity extends AppCompatActivity {

    String userID;
    Button btn_noti;
    Button btn_notiCan;
    String channelID="channelID";
    NotificationManager notiManager;
    NotificationChannel channel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        Intent intent = getIntent();
        userID = intent.getStringExtra("userID");

        Button btn_logout = findViewById(R.id.btn_logout); //로그아웃 버튼
        btn_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(SettingActivity.this);
                        builder.setTitle("로그아웃").setMessage("로그아웃 하시겠습니까?")
                        .setPositiveButton("로그아웃", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent i = new Intent(SettingActivity.this, LoginActivity.class);
                                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_SINGLE_TOP);
                                startActivity(i);

                                //자동 로그인
                                SharedPreferences auto = getSharedPreferences("auto", Activity.MODE_PRIVATE);
                                SharedPreferences.Editor editor = auto.edit();
                                editor.clear();
                                editor.commit();
                                Toast.makeText(getApplicationContext(), "로그아웃 되었습니다.", Toast.LENGTH_SHORT).show();
                                finish();
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

        Button btn_deleteAccount = findViewById(R.id.btn_deleteAccount); //계정 탈퇴 버튼
        btn_deleteAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                AlertDialog.Builder builder = new AlertDialog.Builder(SettingActivity.this);
                builder.setTitle("계정탈퇴").setMessage("탈퇴 하시겠습니까?")
                        .setPositiveButton("계정탈퇴", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                Response.Listener<String> responseListener = new Response.Listener<String>() {
                                    @Override
                                    public void onResponse(String response) {
                                        try {
                                            JSONObject jsonObject = new JSONObject(response);
                                            boolean success = jsonObject.getBoolean("success");
                                            if(success) {
                                                SharedPreferences auto = getSharedPreferences("auto", Activity.MODE_PRIVATE);
                                                SharedPreferences.Editor editor = auto.edit();
                                                editor.clear();
                                                editor.commit();
                                                finish();
                                                Toast.makeText(getApplicationContext(), "회원 탈퇴에 성공하였습니다", Toast.LENGTH_SHORT).show();
                                                Intent intent = new Intent(SettingActivity.this, LoginActivity.class);
                                                startActivity(intent);


                                            } else {
                                                Toast.makeText(getApplicationContext(), "회원 탈퇴에 실패하였습니다.", Toast.LENGTH_SHORT).show();
                                                return;
                                            }
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                };
                                //서버로 Volley를 이용해서 요청을 함
                                DeleteRequest deleteRequest = new DeleteRequest(userID, responseListener);
                                RequestQueue queue = Volley.newRequestQueue(SettingActivity.this);
                                queue.add(deleteRequest);
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
                Intent intent = new Intent(SettingActivity.this, MainActivity.class);
                //intent.addFlags(Intent.FLAT_ACTIVITY_NO_ANIMATION);
                intent.putExtra("userID", userID);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                overridePendingTransition(0, 0);
                finish();
            }
        });

        Button bt_changeUserInfo = findViewById(R.id.btn_changeUserInfo); //개인 정보 변경 버튼
        bt_changeUserInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SettingActivity.this, ChangeUserInfo.class);
                intent.putExtra("userID", userID);
                startActivity(intent);
                overridePendingTransition(0, 0);

            }
        });

        btn_noti = findViewById(R.id.btn_noti);
        btn_notiCan = findViewById(R.id.btn_notiCan);

        Bitmap largeIcon = BitmapFactory.decodeResource(getResources(),R.drawable.ic_launcher_background);
        PendingIntent pendingIntent = PendingIntent.getActivity(SettingActivity.this,0,new Intent(getApplicationContext(),LoginActivity.class),PendingIntent.FLAG_UPDATE_CURRENT);

        btn_noti.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NotificationCompat.Builder builder;

                // OS 버전이 오레오 이상인 경우
                if (Build.VERSION.SDK_INT >= 26) {
                    channel = new NotificationChannel(channelID, "고정 알림", NotificationManager.IMPORTANCE_DEFAULT);
                    ((NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE)).createNotificationChannel(channel);
                    builder = new NotificationCompat.Builder(SettingActivity.this, channelID);
                } else {
                    builder = new NotificationCompat.Builder(SettingActivity.this);
                }

                // notification 설정
                builder.setSmallIcon(R.drawable.logo)
                        .setContentTitle("도씨 :)")
                        .setContentText("오늘의 데일리룩을 확인하세요!")
                        .setDefaults(Notification.DEFAULT_SOUND)
                        .setLargeIcon(largeIcon)
                        .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                        .setOngoing(true)
                        .setContentIntent(pendingIntent);
                notiManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                notiManager.notify(0, builder.build());
                Toast.makeText(getApplicationContext(), "전원이 꺼진 뒤에는 다시 설정해주세요", Toast.LENGTH_LONG).show();
            }
        });

        btn_notiCan.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                NotificationCompat.Builder builder;

                // OS 버전이 오레오 이상인 경우
                if (Build.VERSION.SDK_INT >= 26) {
                    channel = new NotificationChannel(channelID, "고정 알림", NotificationManager.IMPORTANCE_DEFAULT);
                    ((NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE)).createNotificationChannel(channel);
                    builder = new NotificationCompat.Builder(SettingActivity.this, channelID);
                } else {
                    builder = new NotificationCompat.Builder(SettingActivity.this);
                }

                // notification 설정
                builder.setSmallIcon(R.drawable.logo)
                        .setContentTitle("도씨 :)")
                        .setContentText("오늘의 데일리룩을 확인하세요!")
                        .setDefaults(Notification.DEFAULT_SOUND)
                        .setLargeIcon(largeIcon)
                        .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                        .setOngoing(true)
                        .setContentIntent(pendingIntent);
                notiManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                notiManager.cancel(0);
            }
        });
    }
}