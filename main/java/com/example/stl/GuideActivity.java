
package com.example.stl;

import android.content.Intent;
import android.media.Image;
import android.os.RemoteException;
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
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.*;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.target.GlideDrawableImageViewTarget;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class GuideActivity extends AppCompatActivity implements BeaconConsumer {

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
        image_pre=(ImageView)findViewById(R.id.Guide_image);
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

        handler.sendEmptyMessage(0);


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
    }


    Handler handler = new Handler() {


        public void handleMessage(Message msg) {
            //pra.setText("");

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
                    b_pos=mini_beacon;
                    n_pos=mini_beacon;
                    flag=false;
                }else if (n_pos!=mini_beacon){
                    b_pos=n_pos;
                    n_pos=mini_beacon;
                    flag=false;
                }

                if(!flag){
                    b_pos_id=String.format("%s",b_pos.getId2());
                    n_pos_id=String.format("%s",n_pos.getId2());
                    //pra.setText("");

                    //Log.d("sibal","b_pos_id : " +b_pos_id+" n_pos_id : "
                      //      +n_pos_id);

                    //pra.append("\nb_pos_id : " +b_pos_id + "n_pos_id : "+n_pos_id+"\n");

                    for(int i=0;i<road_list.size();i++){
                        RoadData semi=road_list.get(i);

                        String b_pos=semi.getMember_b_pos();
                        String n_pos=semi.getMember_n_pos();
                        String Guide=semi.getMember_Guide();

                        //Log.d("sibal",i+" "+b_pos+" "+n_pos+" "+Guide);

                        if(b_pos.equals(b_pos_id) && n_pos.equals(n_pos_id)){
                            //pra.append(b_pos_id+" "+n_pos_id);
                            //pra.append(Guide);

                            //Log.d("sibal","if문 안");

                            switch (Guide){
                                case "직진하세요!":
                                    pra.setText("직진하세요!");
                                    helpme.speak("직진하세요!",TextToSpeech.QUEUE_FLUSH,null,null);
                                    image_pre.setImageResource(R.mipmap.go);
                                    break;
                                case "우회전하세요!":
                                    pra.setText("우회전하세요!");
                                    helpme.speak("우회전하세요!",TextToSpeech.QUEUE_FLUSH,null,null);
                                    image_pre.setImageResource(R.mipmap.right_arrow);
                                    break;
                                case "좌회전하세요!":
                                    pra.setText("좌회전하세요!");
                                    helpme.speak("좌회전하세요!",TextToSpeech.QUEUE_FLUSH,null,null);
                                    image_pre.setImageResource(R.mipmap.left);
                                    break;
                                case "도착했습니다!":
                                    pra.setText("도착했습니다!");
                                    helpme.speak("도착!",TextToSpeech.QUEUE_FLUSH,null,null);
                                    Intent intent=new Intent(getApplicationContext(),EndActivity.class);
                                    startActivity(intent);
                                    finish();
                            }
                        }
                    }

                }

            }


            // 자기 자신을 1초마다 호출
            handler.sendEmptyMessageDelayed(0, 1000);
        }
    };



}
