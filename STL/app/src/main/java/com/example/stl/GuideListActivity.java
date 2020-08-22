package com.example.stl;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Handler;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Locale;

public class GuideListActivity extends AppCompatActivity {

    // 우리 IP 주소
    private static String IP_ADDRESS = "15.164.217.175";

    private String Start;
    private String Goal;

    private RecyclerView Guide_view;
    private RoadAdapter road_adapter;
    private String road_json_string;

    // RoadData array
    private ArrayList<RoadData> road_array;
    private String info = "";
    private TextToSpeech GuideList_tts;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide_list);

        Log.d("젭알", "?");


        //UI
        Guide_view = (RecyclerView) findViewById(R.id.guide_list_route);
        Button start_button = (Button) findViewById(R.id.guide_list_start);

        road_array = new ArrayList<>();
        road_adapter = new RoadAdapter(this, road_array);
        Guide_view.setAdapter(road_adapter);

        Guide_view.setLayoutManager(new LinearLayoutManager(this));
        //구분선
        Guide_view.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));


        start_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), GuideActivity.class);
                intent.putExtra("pass_array", road_array);
                startActivity(intent);
                finish();
            }
        });

        Intent intent = getIntent();
        Start = intent.getExtras().getString("start");
        Goal = intent.getExtras().getString("goal");

        Log.d("젭알", Start + " " + Goal);


        road_array.clear();
        road_adapter.notifyDataSetChanged();

        //음성 설정
        GuideList_tts = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                GuideList_tts.setLanguage(Locale.KOREAN);
                GetData task_get = new GetData();
                task_get.execute("http://" + IP_ADDRESS + "/queryGuide.php", Start, Goal);
            }
        });

    }

    @Override
    protected void onDestroy() {
        if (GuideList_tts != null) {
            GuideList_tts.stop();
            GuideList_tts.shutdown();
        }
        super.onDestroy();
    }


    private class GetData extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            GuideList_tts.speak(Start + " " + Goal + "경로를 출력합니다.", TextToSpeech.QUEUE_FLUSH, null, null);

            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            if (result != null) {
                Log.d("젭알 result", result);
                info = Start + " " + Goal + "경로를 출력합니다.";
                road_json_string = result;
                if (get_list() == false) {
                    GuideList_tts.speak("경로가 존재하지 않습니다. 다시 입력하여 주십시오.",TextToSpeech.QUEUE_FLUSH,null,null);
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            Intent intent=new Intent(getApplicationContext(),GuideSettingsActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    },5000);
                } else {
                    GuideList_tts.speak(info, TextToSpeech.QUEUE_FLUSH, null, null);
                }
            }

        }

        /*@Override
        protected void onCancelled(){
            super.onCancelled();
            Toast.makeText(getApplicationContext(),"경로가 잘 못 되었습니다. 다시 입력하여" +
                    "주십시오",Toast.LENGTH_LONG).show();
            Intent intent=new Intent(getApplicationContext(),GuideSettingsActivity.class);
            startActivity(intent);
            finish();
        }*/
        @Override
        protected String doInBackground(String... strings) {

            String serverURL = strings[0];
            String postParameters = "Start=" + strings[1] + "&Goal=" + strings[2];

            try {
                int index = 0;
                URL url = new URL(serverURL);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();

                //inputStream 읽어 오는 Timeout 시간 설정
                httpURLConnection.setReadTimeout(5000);

                //서버에 연결되는 Timeout 시간 설정
                httpURLConnection.setConnectTimeout(5000);
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoInput(true);
                httpURLConnection.connect();
                OutputStream outputStream = httpURLConnection.getOutputStream();
                outputStream.write(postParameters.getBytes("UTF-8"));
                outputStream.flush();
                outputStream.close();

                int responseStatusCode = httpURLConnection.getResponseCode();
                InputStream inputStream;

                if (responseStatusCode == HttpURLConnection.HTTP_OK) {
                    // 데이터 가져오자
                    inputStream = httpURLConnection.getInputStream();
                } else {
                    //error!
                    inputStream = httpURLConnection.getErrorStream();
                }

                InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "UTF-8");
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

                StringBuilder sb = new StringBuilder();
                String line;


                while ((line = bufferedReader.readLine()) != null) {
                    index++;
                    sb.append(line);
                }

                bufferedReader.close();

                if (index == 0)
                    cancel(true);

                return sb.toString().trim();


            } catch (Exception e) {
                Log.d("젭알 GetData error", e.getMessage());
                return null;
            }
        }
    }

    private boolean get_list() {
        String TAG_JSON = "search_db";
        String TAG_b_pos = "b_pos";
        String TAG_n_pos = "n_pos";
        String TAG_Guide = "Guide";

        try {
            JSONObject jsonObject = new JSONObject(road_json_string);
            JSONArray jsonArray = jsonObject.getJSONArray(TAG_JSON);

            for (int i = 0; i < jsonArray.length(); i++) {

                JSONObject item = jsonArray.getJSONObject(i);

                String b_pos = item.getString(TAG_b_pos);
                String n_pos = item.getString(TAG_n_pos);
                String Guide = item.getString(TAG_Guide);

                RoadData roadData = new RoadData();

                roadData.setMember_b_pos(b_pos);
                roadData.setMember_n_pos(n_pos);
                roadData.setMember_Guide(Guide);

                road_array.add(roadData);
                road_adapter.notifyDataSetChanged();

                info += " " + Guide;
            }
        } catch (JSONException e) {
            Log.d("젭알 getlist_error", e.getMessage());
            return false;
        }

        return true;

    }

}
