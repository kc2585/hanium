package com.example.stl;


import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
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
import java.util.UUID;


public class GuideSettingsActivity extends AppCompatActivity {

    // 우리 IP 주소
    private static String IP_ADDRESS="15.164.217.175";

    private EditText start;
    private EditText destination;
    private Button input_data;
    private String road_json_string;
    private String input_start;
    private String input_destination;

    // RoadData array
    private ArrayList<RoadData> road_array;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide_settings);


        start=(EditText)findViewById(R.id.guide_start);
        destination=(EditText)findViewById(R.id.guide_goal);
        input_data=(Button)findViewById(R.id.guide_road_start);




        road_array=new ArrayList<>();

        // 길안내 시작 버튼을 누르면 출발지와 목적지를 검색목록.php에 넣고
        //
        input_data.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){

                road_array.clear();

                input_start=start.getText().toString();
                input_destination=destination.getText().toString();
                String macAddress = getMACAddress("wlan0");

                Log.d("젭알",input_start);
                Log.d("젭알",input_destination);

                InsertData task_insert=new InsertData();
                task_insert.execute("http://"+IP_ADDRESS+"/insert.php",macAddress,input_start,input_destination);


            }
        });
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


    class InsertData extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            Log.d("젭알",result);
            Log.d("젭알","onPostExecute 통과");

            Intent searchlist_guide=new Intent(getApplicationContext(),GuideListActivity.class);
            searchlist_guide.putExtra("start",input_start);
            searchlist_guide.putExtra("goal",input_destination);
            startActivity(searchlist_guide);
            Log.d("젭알","ㅠㅠㅠㅠㅠㅠㅠㅠㅠㅠㅠ");
            finish();

        }


        @Override
        protected String doInBackground(String... params) {
            String serverURL = (String)params[0];
            String id = (String)params[1];
            String Start = (String)params[2];
            String Goal=(String)params[3];
            String postParameters = "id=" + id + "&Start=" + Start +"&Goal="+ Goal;

            try {
                URL url = new URL(serverURL);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setReadTimeout(5000);
                httpURLConnection.setConnectTimeout(5000);
                httpURLConnection.setRequestMethod("POST");
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
                String line = null;

                while((line = bufferedReader.readLine()) != null){
                    sb.append(line);
                }


                bufferedReader.close();


                return sb.toString();


            } catch (Exception e) {
                Log.d("젭알",e.getMessage());
                return new String("Error: " + e.getMessage());
            }

        }
    }


}
