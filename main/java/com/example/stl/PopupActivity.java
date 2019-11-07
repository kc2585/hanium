package com.example.stl;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

//Internet, Bluetooth, Gps 연결 요구
public class PopupActivity extends AppCompatActivity {

    //~을 연결해주세요 가 나올 TextView
    private TextView information_text;
    private Button try_button;
    private Button end_button;
    private String intent_string;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_popup);

        information_text=(TextView)findViewById(R.id.popup_info);
        try_button=(Button)findViewById(R.id.popup_try);
        end_button=(Button)findViewById(R.id.popup_end);

        //데이터 받아오기
        Intent intent=getIntent();
        intent_string=intent.getStringExtra("check_popup");
        information_text.setText(intent_string);

        //재시도 다시 Splash Activity로 이동해서 connect check
        try_button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                startActivity(new Intent(PopupActivity.this,SplashActivity.class));
                finish();
            }
        });

        //앱 전체를 종료하는 코드
        end_button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                // 코드가 속해 있는 Activity를 종료시킨다.
                finish();
                // 현재 어플을 백그라운드로 넘긴다. 현재 실행되고 있는 어플이
                // 이 어플 하나라면 홈 화면으로 간다. 하지만 종료된거는 아님
                moveTaskToBack(true);
                // 현재의 프로세스 및 서비스를 종료한다.
                android.os.Process.killProcess(android.os.Process.myPid());
            }
        });
    }
}
