
package com.example.stl;

import android.app.Activity;
import android.content.Intent;
import android.os.RemoteException;
import android.preference.ListPreference;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import org.altbeacon.beacon.Beacon;
import org.altbeacon.beacon.BeaconConsumer;
import org.altbeacon.beacon.BeaconManager;
import org.altbeacon.beacon.BeaconParser;
import org.altbeacon.beacon.RangeNotifier;
import org.altbeacon.beacon.Region;
import org.altbeacon.beacon.service.ArmaRssiFilter;

import android.os.*;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.GlideDrawableImageViewTarget;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Locale;

public class GuideActivity extends AppCompatActivity implements BeaconConsumer {

    private final GuideHandler ghandler=new GuideHandler();

    private ArrayList<RoadData> road_list;
    private TextView pra;
    private BeaconManager beaconManager;
    private List<Beacon> beaconList=new ArrayList<>();

    //음성 출력
    private TextToSpeech helpme;

    private ImageView image_pre=null;
    private Beacon mini_beacon;
    private Beacon n_pos=null;
    private Beacon b_pos=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide);
        pra=(TextView)findViewById(R.id.Guide_pra);
        image_pre=(ImageView)findViewById(R.id.guide_image);
        image_pre.setImageResource(R.mipmap.loading);

        //gif
        GlideDrawableImageViewTarget gifImage=new GlideDrawableImageViewTarget(image_pre);
        Glide.with(this).load(R.mipmap.loading).into(gifImage);

        pra.setText("경로 탐색 중");

        // 실제로 비콘을 탐지하기 위한 비콘매니저 객체를 초기화
        beaconManager = BeaconManager.getInstanceForApplication(this);

        // 여기가 중요한데, 기기에 따라서 setBeaconLayout 안의 내용을 바꿔줘야 하는듯 싶다.
        beaconManager.getBeaconParsers().add(new BeaconParser().
                setBeaconLayout("m:2-3=0215,i:4-19,i:20-21,i:22-23,p:24-24,d:25-25"));

        beaconManager.setRssiFilterImplClass(ArmaRssiFilter.class);     //ARMA 필터

        road_list=(ArrayList<RoadData>) getIntent().getSerializableExtra("pass_array");

        helpme=new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if(status!=TextToSpeech.ERROR){
                    helpme.setLanguage(Locale.KOREAN);
                }
            }
        });


        beaconManager.bind(this);

        ghandler.sendEmptyMessage(0);


    }

    @Override
    public void onBeaconServiceConnect() {
        beaconManager.addRangeNotifier(new RangeNotifier() {
            @Override
            // 비콘이 감지되면 해당 함수가 호출된다. Collection<Beacon> beacons에는 감지된 비콘의 리스트가,
            // region에는 비콘들에 대응하는 Region 객체가 들어온다.
            public void didRangeBeaconsInRegion(Collection<Beacon> beacons, Region region) {
                if (beacons.size() > 0) {
                    beaconList.clear();
                    for (Beacon beacon : beacons) {
                        beaconList.add(beacon);
                    }
                }
            }
        });
        try {
            beaconManager.startRangingBeaconsInRegion(new Region("myRangingUniqueId", null, null, null));
        } catch (RemoteException e) { }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(helpme!=null){
            helpme.stop();
            helpme.shutdown();  //tts stop
        }
        beaconManager.unbind(this);
        ghandler.removeMessages(0);
    }

    private class GuideHandler extends Handler{
        public GuideHandler(){

        }

        @Override
        public void handleMessage(Message msg){
            if(msg.what==1){
                {
                    for(Beacon beacon : beaconList){
                        //pra.append("ID : " + beacon.getId2() + " / " + "Distance : " + Double.parseDouble(String.format("%.3f", beacon.getDistance())) + "m\n");
                        if(beacon.getDistance()<mini_beacon.getDistance() && beacon.getDistance()<2.0){
                            mini_beacon=beacon;
                        }
                    }


                    if(mini_beacon.getDistance()<0.5){
                        Log.d("젭알","조건 만족 "+mini_beacon.getDistance());
                        Intent intent=new Intent(getApplicationContext(),EndActivity.class);
                        startActivity(intent);
                        finish();
                        ghandler.removeMessages(0);
                    }else{
                        Log.d("젭알","조건 불 만족 "+n_pos.getDistance());
                        ghandler.sendEmptyMessageDelayed(1,1000);
                    }

                }

            }

            //pra.setText("");
            int i=0;
            String b_pos_id;
            String n_pos_id;
            boolean flag=true;

            if(beaconList.size()!=0){

                mini_beacon=beaconList.get(0);

                // 비콘의 아이디와 거리를 측정하여 textView에 넣는다.
                for(Beacon beacon : beaconList){
                    //pra.append("ID : " + beacon.getId2() + " / " + "Distance : " + Double.parseDouble(String.format("%.3f", beacon.getDistance())) + "m\n");
                    if(beacon.getDistance()<mini_beacon.getDistance() && beacon.getDistance()<2.0){
                        mini_beacon=beacon;
                    }
                }

                if(b_pos==null){
                    Log.d("젭알","null null mini_beacon: " + mini_beacon.getId2());
                }else{
                    Log.d("젭알","b_pos : " +b_pos.getId2()+"n_pos : " +n_pos.getId2()+
                            "mini_beacon"+mini_beacon.getId2());
                }

                if(b_pos==null) {
                    Log.d("젭알", "b_pos==null 통과");
                    b_pos = mini_beacon;
                    n_pos = mini_beacon;
                    flag = false;
                }else{
                    String n_beacon_id_else=String.format("%s",n_pos.getId2());
                    String mini_beacon_id_else=String.format("%s",mini_beacon.getId2());

                    if(!(n_beacon_id_else.equals(mini_beacon_id_else))) {


                        Log.d("젭알", "n_pos!=mini_beacon 통과");
                        Log.d("젭알", "n_beacon_id : " +n_beacon_id_else +" mini_beacon_id : "+
                                mini_beacon_id_else);


                        b_pos = n_pos;
                        n_pos = mini_beacon;
                        flag = false;

                    }
                }


                Log.d("젭알","b_pos : " +b_pos.getId2()+"n_pos : " +n_pos.getId2());

                if(!flag){
                    b_pos_id=String.format("%s",b_pos.getId2());
                    n_pos_id=String.format("%s",n_pos.getId2());
                    //pra.setText("");

                    //Log.d("sibal","b_pos_id : " +b_pos_id+" n_pos_id : "
                    //      +n_pos_id);

                    //pra.append("\nb_pos_id : " +b_pos_id + "n_pos_id : "+n_pos_id+"\n");

                    for(i=0;i<road_list.size();i++){
                        RoadData semi=road_list.get(i);

                        String b_pos=semi.getMember_b_pos();
                        String n_pos=semi.getMember_n_pos();
                        String Guide=semi.getMember_Guide();

                        Log.d("젭알",i+b_pos+n_pos);


                        if(b_pos.equals(b_pos_id) && n_pos.equals(n_pos_id)){
                            //pra.append(b_pos_id+" "+n_pos_id);
                            //pra.append(Guide);

                            Log.d("젭알",i+b_pos+n_pos);
                            char Guide_0=Guide.charAt(0);


                            switch (Guide_0){
                                case '오':
                                    pra.setText(Guide);
                                    Log.d("젭알",i+b_pos_id+n_pos_id+"speak 우회전하세요");
                                    helpme.speak(Guide,TextToSpeech.QUEUE_FLUSH,null,null);
                                    image_pre.setImageResource(R.mipmap.right_arrow);
                                    break;
                                case '왼':
                                    pra.setText(Guide);
                                    Log.d("젭알",i+b_pos_id+n_pos_id+"speak 좌회전");
                                    helpme.speak(Guide,TextToSpeech.QUEUE_FLUSH,null,null);
                                    image_pre.setImageResource(R.mipmap.left);
                                    break;
                                case '잠':
                                    Log.d("젭알",i+b_pos_id+n_pos_id+"speak 도착");
                                    pra.setText(Guide);
                                    helpme.speak(Guide,TextToSpeech.QUEUE_FLUSH,null,null);
                                    image_pre.setImageResource(R.mipmap.pin_mini);
                                    Log.d("젭알",String.format("%s",mini_beacon.getDistance()));

                                    ghandler.sendEmptyMessage(1);
                                    break;
                                default:
                                    pra.setText(Guide);
                                    helpme.speak(Guide,TextToSpeech.QUEUE_FLUSH,null,null);
                                    Log.d("젭알",i+b_pos_id+n_pos_id+"speak 직진");
                                    image_pre.setImageResource(R.mipmap.go);
                                    break;
                            }
                            break;
                        }
                    }

                    /*if(i==road_list.size())
                    {

                        Log.d("젭알", "i : "+i+"road_list.size : "+road_list.size());
                        Intent intent=new Intent(getApplicationContext(),LostActivity.class);
                        startActivity(intent);
                        finish();
                    }*/
                }

            }
            // 자기 자신을 3초마다 호출
            ghandler.sendEmptyMessageDelayed(0, 3000);
        }
    }



}
