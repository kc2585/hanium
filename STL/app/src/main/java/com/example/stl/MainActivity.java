package com.example.stl;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;


//1. 퍼미션이 허용되어 있는지 확인하고 없을시 퍼미션 요구
//2. 길안내 버튼과 검색 목록 버튼을 만들어서 클릭 이벤트시 Activity 이동
// 이동 시 MainActivity는 종료하지 않아서 다른 Activity가 종료되면 MainActivity 로 이동할 수 있게 한다.
// 길 안내 버튼 - > GuideSettingsActivity
// 검색 목록 버튼 - > SearchListActivity
public class MainActivity extends AppCompatActivity
{
    private ImageButton guide_button;
    private ImageButton list_button;
    private ImageButton info_button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        checkPermissions(MainActivity.this,this);

        list_button=(ImageButton)findViewById(R.id.search_list);
        guide_button=(ImageButton)findViewById(R.id.road_find);
        info_button=(ImageButton)findViewById(R.id.main_use_image);

        //버튼 클릭 이벤트
        list_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent search_intent=new Intent(MainActivity.this,SearchListActivity.class);
                startActivity(search_intent);
            }
        });

        guide_button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent guide_intent=new Intent(MainActivity.this,GuideSettingsActivity.class);
                startActivity(guide_intent);
            }
        });


        info_button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent use_intent=new Intent(MainActivity.this,UseActivity.class);
                startActivity(use_intent);
            }
        });
    }

    //최초의 권한 설정 , API 몇 이후로 이것을 필수로 해야된다고 함. 거절 시 앱 이용 못함
    public void checkPermissions(Activity activity, Context context){
        int PERMISSION_ALL = 1;
        String[] PERMISSIONS = {
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.BLUETOOTH,
                Manifest.permission.BLUETOOTH_ADMIN,
                Manifest.permission.INTERNET,
                Manifest.permission.RECORD_AUDIO,
        };

        //권한이 없다면 퍼미션 허용을 요구한다.
        if(!hasPermissions(context, PERMISSIONS)){
            ActivityCompat.requestPermissions( activity, PERMISSIONS, PERMISSION_ALL);
        }
    }

    //권한이 있는지 없는지를 확인한다.
    public static boolean hasPermissions(Context context, String... permissions) {
        if (context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }

}
