package com.example.stl;

import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;

import android.speech.tts.TextToSpeech;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.Locale;

// bluetooth, gps, internet(mobile) 연결 확인
public class CheckActivity extends AppCompatActivity  {

    private String intent_check="";
    private String info_message="연결을 확인해 주세요.\n";
    private String base_info="연결 후 아래 재시도 버튼을 눌러주세요.";



    // true면 연결이 되어 있고 false는 연결이 되어 있지 않을것
    // 처음 확인을 위해서 false로 초기화
    private boolean total_check=false;

    private TextToSpeech check_tts;

    private Button re_button;
    private TextView info_text;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check);

        info_text=(TextView)findViewById(R.id.ConstraintLayout);
        re_button=(Button)findViewById(R.id.check_re);

        //버튼 설정
        re_button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                check();
            }
        });

        //음성인식 설정
        check_tts=new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if(status!=TextToSpeech.ERROR){
                    check_tts.setLanguage(Locale.KOREAN);
                }
            }
        });

        // 음성출력을 바로 사용할 경우 첫번째 음성출력이 안될 경우의 문제를 해결하기 위해서
        // 해봣음

        // handler를 이용해서 500ms 대기를 한다.
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                check();
            }
        },500);



    }


    private void check(){

        //total_check를 false로 초기화
        intent_check="";
        total_check=false;

        if(!internet()){
            intent_check+=" 인터넷 ";
            total_check=true;
        }
        if(!bluetooth()){
            intent_check+=" 블루투스 ";
            total_check=true;
        }
        if(!gps()){
            intent_check+=" gps ";
            total_check=true;
        }

        //하나라도 연결이 안되어있다면 true
        if(total_check==true){
            intent_check+="\n"+info_message+base_info;
            info_text.setText(intent_check);
            check_tts.speak(intent_check,TextToSpeech.QUEUE_FLUSH,null,null);
        }
        // main activity 로 intent 넘김
        else{
            Intent mainIntent=new Intent(this, MainActivity.class);
            startActivity(mainIntent);
            finish();
        }

    }

    //블루투스 연결 확인
    private boolean bluetooth(){
        BluetoothAdapter bluetoothAdapter= BluetoothAdapter.getDefaultAdapter();

        // 블루투스가 연결되어있지 않으면
        if(!bluetoothAdapter.isEnabled()){
                return false;
        }
        return true;
    }

    //인터넷(모바일 데이터) 연결 확인
    private boolean internet(){
        ConnectivityManager connectivityManager=(ConnectivityManager)
                getSystemService(CONNECTIVITY_SERVICE);

        NetworkInfo networkInfo=null;

        if(connectivityManager!=null){
            networkInfo=connectivityManager.getActiveNetworkInfo();
        }

        if(networkInfo!=null && networkInfo.isConnected()){
            // networkInfo type을 받아와서 type_mobile인지 확인
            int type=networkInfo.getType();
            if(type==ConnectivityManager.TYPE_MOBILE){
                return true;
            }
        }
        return false;
    }

    //gps 상태 확인
    private boolean gps(){
        LocationManager manger=(LocationManager)
                getApplicationContext().getSystemService(Context.LOCATION_SERVICE);

        if(manger.isProviderEnabled(LocationManager.GPS_PROVIDER)){
            return true;
        }else{
            return false;
        }
    }

    //activity destory시 tts가 멈추어야 한다.
    @Override
    protected void onDestroy(){     //tts 초기화
        if(check_tts!=null){
            check_tts.stop();
            check_tts.shutdown();  //tts stop
        }
        super.onDestroy();
    }


}
