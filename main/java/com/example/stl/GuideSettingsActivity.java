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

    // RoadData array
    private ArrayList<RoadData> road_array;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide_settings);


        start=(EditText)findViewById(R.id.input_start);
        destination=(EditText)findViewById(R.id.input_destination);
        input_data=(Button)findViewById(R.id.button_ok);

        road_array=new ArrayList<>();

        // 길안내 시작 버튼을 누르면 출발지와 목적지를 검색목록.php에 넣고
        //
        input_data.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){

                road_array.clear();

                String input_start=start.getText().toString();
                String input_destination=destination.getText().toString();
                String macAddress = getMACAddress("wlan0");

                InsertData task_insert=new InsertData();
                task_insert.execute("http://"+IP_ADDRESS+"/insert.php",input_start,input_destination,macAddress);

                GetData task_get=new GetData();
                task_get.execute("http://"+IP_ADDRESS+"/queryGuide.php",input_start,input_destination);
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
            Log.d("sibal", "POST response  - " + result);

        }


        @Override
        protected String doInBackground(String... params) {
            String serverURL = (String)params[0];
            String id = (String)params[1];           //start
            String name = (String)params[2];     //destination
            String country=(String)params[3];        //userid
            String postParameters = "id=" + id + "&name=" + name+"&country="+country;

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
                return new String("Error: " + e.getMessage());
            }

        }
    }

   private class GetData extends AsyncTask<String,Void,String>{

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            Log.d("sibal", "POST response  - " + result);
            if(result!=null){
                road_json_string=result;
                get_list();
                Log.d("sibal_json_str",road_json_string);

                Intent intent=new Intent(getApplicationContext(),GuideActivity.class);
                intent.putExtra("pass_array",road_array);
                startActivity(intent);
            }
        }

        @Override
        protected void onCancelled(){
            super.onCancelled();
            Toast.makeText(getApplicationContext(),"경로가 잘 못 되었습니다. 다시 입력하여" +
                    "주십시오",Toast.LENGTH_LONG).show();
        }
        @Override
        protected String doInBackground(String... strings) {

            String serverURL = strings[0];
            String postParameters = "Start=" + strings[1] + "&Goal=" + strings[2];

            try {
                int index=0;
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
                    index++;
                    sb.append(line);
                }

                bufferedReader.close();

                if(index==0)
                    cancel(true);

                return sb.toString().trim();


            } catch (Exception e) {

                Log.d("sibal", "InsertData: Error ", e);
                return null;
            }
        }
    }

    private void get_list(){
        String TAG_JSON="search_db";
        String TAG_b_pos = "b_pos";
        String TAG_n_pos = "n_pos";
        String TAG_Guide = "Guide";

        try {
            JSONObject jsonObject = new JSONObject(road_json_string);
            JSONArray jsonArray = jsonObject.getJSONArray(TAG_JSON);

            for (int i = 0; i < jsonArray.length(); i++) {

                JSONObject item = jsonArray.getJSONObject(i);

                String b_pos=item.getString(TAG_b_pos);
                String n_pos = item.getString(TAG_n_pos);
                String Guide = item.getString(TAG_Guide);

                RoadData roadData = new RoadData();

                roadData.setMember_b_pos(b_pos);
                roadData.setMember_n_pos(n_pos);
                roadData.setMember_Guide(Guide);

                road_array.add(roadData);

                Log.d("sibal","확인!");
            }
        }catch (JSONException e){
            Log.d("sibal","error : ", e);
        }

    }

}
