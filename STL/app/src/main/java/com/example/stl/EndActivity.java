package com.example.stl;

import android.os.Handler;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import org.w3c.dom.Text;

import java.util.Locale;

//길안내를 종료할 시 실행되는 Activity
public class EndActivity extends AppCompatActivity {


    private TextToSpeech end_tts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_end);

        Button end_button=(Button)findViewById(R.id.end_end);
        Button menu_button=(Button)findViewById(R.id.end_back);

        //음성인식 설정
        end_tts=new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if(status!=TextToSpeech.ERROR){
                    end_tts.setLanguage(Locale.KOREAN);
                    end_tts.speak("도착했습니다", TextToSpeech.QUEUE_FLUSH,null,null);
                }
            }
        });



        //앱 전체 종료
        end_button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                end_tts.speak("앱을 종료합니다.",TextToSpeech.QUEUE_FLUSH,null,null);
                finish();
                // 현재 어플을 백그라운드로 넘김 - > 종료된게 아님!
                moveTaskToBack(true);
                // 현재의 프로세스 및 서비스를 종료시킴
                android.os.Process.killProcess(android.os.Process.myPid());
            }
        });


        //MainActivity로 이동한다.
        menu_button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                end_tts.speak("메인 메뉴로 이동합니다.",TextToSpeech.QUEUE_FLUSH,null,null);
                finish();

            }
        });



    }

    //activity destory시 tts가 멈추어야 한다.
    @Override
    protected void onDestroy(){     //tts 초기화
        if(end_tts!=null){
            end_tts.stop();
            end_tts.shutdown();  //tts stop
        }
        super.onDestroy();
    }
}
