package com.example.stl;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
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

public class SearchListActivity extends AppCompatActivity {

    private static String IP_ADDRESS="15.164.217.175";

    private RecyclerView search_view;
    private ArrayList<PersonalData> search_array;
    private UsersAdapter search_adapter;
    private String search_json_string;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_list);
        search_view=(RecyclerView)findViewById(R.id.recycle_search_list);

        search_view.setLayoutManager(new LinearLayoutManager(this));
        //구분선
        search_view.addItemDecoration(new DividerItemDecoration(getApplicationContext(),1));

        search_array=new ArrayList<>();
        search_adapter=new UsersAdapter(this,search_array);

        search_view.setAdapter(search_adapter);

        search();
    }

    private void search(){
        search_array.clear();
        search_adapter.notifyDataSetChanged();

        String macAddress = getMACAddress("wlan0");

        GetData task=new GetData();
        task.execute("http://"+IP_ADDRESS+"/query.php",macAddress);
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


    //AsyncTask의 자료형은 순서대로 doInBackground, onProgressUpdate, onPostExecute 의 매개변수
    private class GetData extends AsyncTask<String,Void,String>{

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            if(result!=null){
                search_json_string=result;
                show_list();
            }
        }

        @Override
        protected String doInBackground(String... strings) {

            String serverURL = strings[0];
            String postParameters = "country=" + strings[1];

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

                return sb.toString().trim();


            } catch (Exception e) {

                return null;
            }
        }
    }

    private void show_list(){
        String TAG_JSON="webnautes";
        String TAG_ID = "id";
        String TAG_NAME = "name";
        String TAG_COUNTRY ="country";

       try {
           JSONObject jsonObject = new JSONObject(search_json_string);
           JSONArray jsonArray = jsonObject.getJSONArray(TAG_JSON);

           for (int i = 0; i < jsonArray.length(); i++) {

               JSONObject item = jsonArray.getJSONObject(i);

               String id = item.getString(TAG_ID);
               String name = item.getString(TAG_NAME);
               String address = item.getString(TAG_COUNTRY);

               PersonalData personalData = new PersonalData();

               personalData.setMember_id(id);
               personalData.setMember_name(name);
               personalData.setMember_address(address);

               search_array.add(personalData);
               search_adapter.notifyDataSetChanged();
           }
       }catch (JSONException e){
       }

    }

}
