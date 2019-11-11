package com.example.stl;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;

// 첫번째 실행하는 Activity
// 이미지를 통해 화면에 logo를 출력함
// 이미지는 AndroidManifest.xml에서 Acitivty style를 통해서 함
// android:theme="@style/SplashTheme" 이었으나
// gif를 넣고싶다는 요구에 Handler를 사용한 delay 방식으로 바뀜

// AppCompatActivity vs Activity
// AppCompatActivity는 3.0 미만의 안드로이드 하위 버전을 지원하는 액티비티이다.
// 근데 우리 앱은 최저 레벨이 안드로이드 5.0이기 때문에 AppCompatActivity를
// 쓸 필요는 없다. - > 나중에 수정해보고 잘 되면 Activity로 바꾸자.

public class LoadingActivity extends AppCompatActivity {

    private ImageView splash_image;
    private int index=-1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading);

        splash_image=(ImageView)findViewById(R.id.loading_image);


        handler.sendEmptyMessage(0);

    }


    // gif를 만들지 못해서 handler를 사용했다.
    // 이게 무슨짓이지... 컴맹의 최후다다
   Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            index++;
            int i=index;
            switch (i) {
                case 0:
                    splash_image.setImageResource(R.mipmap.logo_1);
                    Log.d("젭알", index + " 0");
                    break;
                case 1:
                    splash_image.setImageResource(R.mipmap.logo_2);
                    Log.d("젭알", index + " 1");
                    break;
                case 2:
                    splash_image.setImageResource(R.mipmap.logo_3);
                    Log.d("젭알", index + " 2");
                    break;
                case 3:
                    splash_image.setImageResource(R.mipmap.logo_4);
                    Log.d("젭알", index + " 3");
                    break;
                case 4:
                    splash_image.setImageResource(R.mipmap.logo_5);
                    Log.d("젭알", index + " 4");
                    break;
                case 5:
                    splash_image.setImageResource(R.mipmap.logo_6);
                    Log.d("젭알", index + " 5");
                    break;
                case 6:
                    splash_image.setImageResource(R.mipmap.logo_7);
                    Log.d("젭알", index + " 6");
                    break;
                case 7:
                    splash_image.setImageResource(R.mipmap.logo_8);
                    Log.d("젭알", index + " 7");
                    break;
                case 8:
                    Log.d("젭알", index + " 8");
                    Intent intent = new Intent(getApplicationContext(), CheckActivity.class);
                    startActivity(intent);
                    finish();
                    break;
            }

            // 자기 자신을 1초마다 호출
            handler.sendEmptyMessageDelayed(0, 300);
        }
    };





}
