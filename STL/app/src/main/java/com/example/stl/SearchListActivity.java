package com.example.stl;

import android.app.ProgressDialog;
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
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.NetworkInterface;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

public class SearchListActivity extends AppCompatActivity {

    private static String IP_ADDRESS="15.164.217.175";

    private RecyclerView search_view;
    private ArrayList<PersonalData> search_array;
    private UsersAdapter search_adapter;
    private String search_json_string;

    //private TextToSpeech searchlist_tts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_list);
        search_view=(RecyclerView)findViewById(R.id.recycle_search_list);

        search_view.setLayoutManager(new LinearLayoutManager(this));
        //구분선
        search_view.addItemDecoration(new DividerItemDecoration(this,LinearLayoutManager.VERTICAL));

        search_array=new ArrayList<>();
        search_adapter=new UsersAdapter(this,search_array);

        search_view.setAdapter(search_adapter);

        //음성출력 설정
        //searchlist_tts=new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
        //    @Override
        //    public void onInit(int status) {
        //        searchlist_tts.setLanguage(Locale.KOREAN);
        //    }
        //});

        //Touch Event 처리
        search_adapter.setOnItemClickListener(new UsersAdapter.OnItemClickListener() {
            @Override
            public void GetItem(View v, int position) {
                String Start;
                String Goal;
                PersonalData Get=search_array.get(position);

                Start=Get.getMember_Start();
                Goal=Get.getMember_Goal();

                //searchlist_tts.speak(Start+" "+Goal+"경로를 출력합니다.",TextToSpeech.QUEUE_FLUSH,null,null);

                Intent searchlist_guide=new Intent(getApplicationContext(),GuideListActivity.class);
                searchlist_guide.putExtra("start",Start);
                searchlist_guide.putExtra("goal",Goal);
                startActivity(searchlist_guide);
                finish();

                //new Handler().postDelayed(new Runnable() {
                 //   @Override
                 //   public void run() {
                 //       finish();
                 //   }
                //},2000);

            }
        });

        search();
    }

    private void search(){
        search_array.clear();
        search_adapter.notifyDataSetChanged();

        String macAddress = getMACAddress("wlan0");

        GetData task=new GetData();
        task.execute("http://"+IP_ADDRESS+"/querySearch.php",macAddress);
    }

    public static String getMACAddress(String interfaceName) {
        try {
            List<NetworkInterface> interfaces = Collections.list(NetworkInterface.getNetworkInterfaces());
            for (NetworkInterface intf : interfaces) {
                if (interfaceName != null) {
                    if (!intf.getName().equalsIgnoreCase(interfaceName)) continue;
                }
                byte[] mac = intf.getHardwareAddress();
                if (mac==null) return "";
                StringBuilder buf = new StringBuilder();
                for (int idx=0; idx<mac.length; idx++)
                    buf.append(String.format("%02X:", mac[idx]));
                if (buf.length()>0) buf.deleteCharAt(buf.length()-1);
                return buf.toString();
            }
        } catch (Exception ex) { } // for now eat exceptions
        return "";
    }


    //activity destory시 tts가 멈추어야 한다.
    //@Override
    //protected void onDestroy(){     //tts 초기화
     //   if(searchlist_tts!=null){
      //      searchlist_tts.stop();
       //     searchlist_tts.shutdown();  //tts stop
       // }
       // super.onDestroy();
    //}

    //AsyncTask의 자료형은 순서대로 doInBackground, onProgressUpdate, onPostExecute 의 매개변수
    private class GetData extends AsyncTask<String,Void,String>{

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            Log.d("젭알",result);
            Log.d("젭알","onPostExecute 통과");

            if(result!=null){

                Log.d("젭알","여기 통과하니?");
                search_json_string=result;
                show_list();
            }
        }

        @Override
        protected String doInBackground(String... strings) {

            String serverURL = strings[0];
            String postParameters = "id=" + strings[1];

            try {
                URL url = new URL(serverURL);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setReadTimeout(5000);
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

                if(responseStatusCode == HttpURLConnection.HTTP_OK) {
                    inputStream = httpURLConnection.getInputStream();
                }
                else{
                    inputStream = httpURLConnection.getErrorStream();
                }

                InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "UTF-8");
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

                StringBuilder sb = new StringBuilder();
                String line;

                while((line = bufferedReader.readLine()) != null){
                    sb.append(line);
                }

                bufferedReader.close();


                Log.d("젭알",sb.toString().trim());

                return sb.toString().trim();


            } catch (Exception e) {
                Log.d("젭알",e.getMessage());
                return null;
            }
        }
    }

    private void show_list(){
        String TAG_JSON="log_db";
        String TAG_ID = "id";
        String TAG_Start = "Start";
        String TAG_Goal ="Goal";

       try {
           JSONObject jsonObject = new JSONObject(search_json_string);
           JSONArray jsonArray = jsonObject.getJSONArray(TAG_JSON);

           for (int i = 0; i < jsonArray.length(); i++) {

               JSONObject item = jsonArray.getJSONObject(i);

               String id = item.getString(TAG_ID);
               String Start = item.getString(TAG_Start);
               String Goal = item.getString(TAG_Goal);

               PersonalData personalData = new PersonalData();

               personalData.setMember_id(id);
               personalData.setMember_Start(Start);
               personalData.setMember_Goal(Goal);

               Log.d("젭알",id+" "+Start+" "+Goal);

               search_array.add(personalData);
               search_adapter.notifyDataSetChanged();
           }
       }catch (JSONException e){
       }

    }


}
