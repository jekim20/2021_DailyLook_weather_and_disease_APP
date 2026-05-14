package com.example.swipeex;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

import me.relex.circleindicator.CircleIndicator;

public class MainActivity extends AppCompatActivity {
    FragmentPagerAdapter adapterViewPager;

    String userID;
    int topRandomNum, top2RandomNum, bottomRandomNum, bottomShortRandNum, shoesRandomNum;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ViewPager vpPager = (ViewPager) findViewById(R.id.vpPager);
        adapterViewPager = new MyPagerAdapter(getSupportFragmentManager());
        vpPager.setAdapter(adapterViewPager);
        vpPager.setCurrentItem(1);

        CircleIndicator indicator = (CircleIndicator) findViewById(R.id.indicator);
        indicator.setViewPager(vpPager);

        getRandNum();

        //로그인 페이지에서 보낸 id를 받음
        Intent intent = getIntent();
        userID = intent.getStringExtra("userID");

        Button button3 = findViewById(R.id.button3);
        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, SettingActivity.class);
                intent.putExtra("userID", userID);
                startActivity(intent);
                overridePendingTransition(0, 0);
            }
        });
    }

    public void getRandNum() { //ment_all 테이블에서 옷의 color가 0일 때 랜덤한 옷 색을 보여주기 위해 랜덤한 수를 생성해 주는 함수
        Random random = new Random();
        topRandomNum = random.nextInt(3);
        top2RandomNum = random.nextInt(2);
        bottomRandomNum = random.nextInt(2);
        bottomShortRandNum = random.nextInt(5);
        shoesRandomNum = random.nextInt(2);
    }

    public static class MyPagerAdapter extends FragmentPagerAdapter {
        private static  int NUM_ITEMS = 3;

        public MyPagerAdapter(FragmentManager fragmentManager) {
            super(fragmentManager);
        }

        @Override
        public int getCount() {
            return  NUM_ITEMS;
        }

        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return FirstFragment.newInstance();
                case 1:
                    return SecondFragment.newInstance();
                case 2:
                    return ThirdFragment.newInstance();
                default:
                    return null;
            }
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return "Page " + position;
        }
    }
 }