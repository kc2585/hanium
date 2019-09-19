package com.example.ex;

import android.speech.tts.TextToSpeech;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.speech.tts.TextToSpeech.OnInitListener;
import android.view.View;
import android.widget.Toast;
import java.util.Locale;


public class InfoActivity extends AppCompatActivity implements OnInitListener {

    private TextToSpeech myTTS;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);
        myTTS=new TextToSpeech(this,this);
    }


    public void onInit(int status){
        String myText1="안녕하세요 갓기호어플입니다.";
        myTTS.speak(myText1,TextToSpeech.QUEUE_FLUSH,null);
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
        myTTS.shutdown();
      //  finish();
    }

}
