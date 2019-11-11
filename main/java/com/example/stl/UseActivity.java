package com.example.stl;

import android.media.Image;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.stl.R;

import java.util.Locale;

public class UseActivity extends AppCompatActivity {

    private Button re_voice;
    private ImageView speak_image;
    private TextView use_info;
    private TextToSpeech use_tts;
    private String info;

    private boolean speak_check=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_use);



        info="1. STL 앱은 인터넷, 블루투스, gps 연결을 필요로 합니다." +
                " 인터넷 연결은 와이파이가 아니라 모바일 데이터로만 가능한 점 주의해 주세요!\n\n" +
                "2. 메인 메뉴에서 길 안내 메뉴를 누르고 출발지와 목적지를 입력하시면 경로를 탐색해 드려요." +
                " 탐색한 경로로 길 안내를 원하신다면 길 안내 시작 버튼을 누르시면 길 안내가 시작됩니다.\n\n" +
                "3. 메인 메뉴에서 검색 목록 메뉴를 누르시면 이용하시면서 검색하신 경로의 목록들을 볼 수 있어요." +
                " 검색 목록에서 경로 탐색을 원하시는 출발지 목적지 목록이 있다면 클릭해주세요!" +
                " 그러면 경로를 탐색해드립니다.";

        use_info=(TextView)findViewById(R.id.use_text);
        //re_voice=(Button)findViewById(R.id.use_re);
        speak_image=(ImageView)findViewById(R.id.use_image);

        speak_image.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                if(speak_check==false){
                    speak_image.setImageResource(R.mipmap.speaker);
                    use_speak();
                    speak_check=true;
                }else{
                    speak_image.setImageResource(R.mipmap.mute);
                    tts_stop();
                    speak_check=false;
                }
            }
        });


        /*re_voice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(speak_check==true){
                    use_speak();
                }
            }
        });*/

        use_info.setText(info);

        //음성인식 설정
        use_tts=new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if(status!=TextToSpeech.ERROR){
                    use_tts.setLanguage(Locale.KOREAN);
                }
            }
        });

    }

    protected void use_speak(){
        use_tts.speak(info,TextToSpeech.QUEUE_FLUSH,null,null);
    }



    protected void tts_stop(){
        if(use_tts!=null){
            use_tts.stop();
        }
    }

    @Override
    protected void onDestroy(){
        tts_stop();
        use_tts.shutdown();
        super.onDestroy();
    }

}
