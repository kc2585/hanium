package com.example.stl;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

//GuideActivity - > LostActivity - > GuideSettingsActivity
//경로를 잃었을 시 실행되는 Activity 잘 안된다. 나중에 누가 만들어 주도록 하자.
public class LostActivity extends AppCompatActivity {

    //이 버튼을 누르면 현재 Activity를 종료하고
    // guidesettingsActivity로 가게 되어서 다시 경로탐색을 하게 해준다.
    private Button LostButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lost);

        LostButton=(Button)findViewById(R.id.research);

        LostButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent intent=new Intent(getApplicationContext(),GuideSettingsActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}
