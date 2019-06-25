package com.example.kisar.jsondene1;

import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Process;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.ref.WeakReference;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    ProgressBar progressBar;
    TextView txtDeneme;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        progressBar= (ProgressBar) findViewById(R.id.prBAr);
        startAsyncTask();

        txtDeneme= (TextView) findViewById(R.id.txtDeneme);



    }
    public void startAsyncTask(){
        new JSONHelper(this).execute("https://api.canlidoviz.com/web/items?marketId=1&type=0");
    }
    private static class JSONHelper extends AsyncTask<String,Integer,String> {
        //Arka plan işlemleri  bittikten sonre Ram i temizleyip gereksiz alan işgalini engeeliyoruz
        private WeakReference<MainActivity> activityWeakReference;

        public JSONHelper(MainActivity activity) {
            this.activityWeakReference = new WeakReference<MainActivity>(activity);
        }

        @Override
        protected void onPreExecute() {
            MainActivity activity=activityWeakReference.get();
            if(activity==null||activity.isFinishing()){
                return;
            }
            activity.progressBar.setVisibility(View.VISIBLE);
            activity.progressBar.setBackgroundColor(Color.BLUE);



            super.onPreExecute();

        }

        @Override
        protected String doInBackground(String... strings) {
            try {


                StringBuilder sb = new StringBuilder();
                URL uri = null;
                uri = new URL(strings[0]);

                HttpURLConnection urlConnection = (HttpURLConnection) uri.openConnection();
                InputStream in = new BufferedInputStream(urlConnection.getInputStream());
                BufferedReader bin = new BufferedReader(new InputStreamReader(in));
                String inputLine;
                while ((inputLine = bin.readLine()) != null) {

                    Thread.sleep(9000);


                    sb.append(inputLine);

                }
                return sb.toString();

            } catch (MalformedURLException e) {

                e.printStackTrace();
                return null;
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            } catch (InterruptedException e) {
                e.printStackTrace();
                return null;
            }


        }

        @Override
        protected void onPostExecute(String s) {
            MainActivity activity=activityWeakReference.get();

            Gson gson=new Gson();
            Type DovizModelTyp = new TypeToken<ArrayList<Doviz>>(){}.getType();
            List<Doviz> dovizList = gson.fromJson(s, DovizModelTyp);
            // Log.e("list object",);
            activity.txtDeneme.setText(dovizList.get(0).getName());
            activity.progressBar.setProgress(0);
            activity.progressBar.setVisibility(View.INVISIBLE);

        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);

        }


    }



}
