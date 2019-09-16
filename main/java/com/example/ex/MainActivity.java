package com.example.ex;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
    public void InfoonClicked(View v){
        Intent intent=new Intent(getApplicationContext(),InfoActivity.class);
        startActivity(intent);
    }

    public void RoadonClicked(View v){
        Intent intent2=new Intent(this,RoadActivity.class);
        startActivity(intent2);
    }


}
