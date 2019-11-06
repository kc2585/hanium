package com.example.stl;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

// 첫번째 실행하는 Activity
// 이미지를 통해 화면에 logo를 출력함
// 이미지는 AndroidManifest.xml에서 Acitivty style를 통해서 함

// AppCompatActivity vs Activity
// AppCompatActivity는 3.0 미만의 안드로이드 하위 버전을 지원하는 액티비티이다.
// 근데 우리 앱은 최저 레벨이 안드로이드 5.0이기 때문에 AppCompatActivity를
// 쓸 필요는 없다. - > 나중에 수정해보고 잘 되면 Activity로 바꾸자.
public class SplashActivity extends AppCompatActivity {

    // Splash Activity - > CheckActivity 로 이동
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent=new Intent(this,CheckActivity.class);
        startActivity(intent);
        finish();
    }

}
