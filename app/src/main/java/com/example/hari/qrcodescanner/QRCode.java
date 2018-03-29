package com.example.hari.qrcodescanner;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.os.AsyncTask;
import android.widget.TextView;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;

public class QRCode extends AppCompatActivity {

    private IntentIntegrator qrScan;

    private static final String  BASE_URL = "base-url";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qrcode);
        Button button = findViewById(R.id.launch_qrcode);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                qrScan = new IntentIntegrator(QRCode.this);
                qrScan.initiateScan();
            }
        });

    }


    private class DownloadTask extends AsyncTask<String, Long, String> {
        protected String doInBackground(String... urls) {
            try {
                HttpRequest request =  HttpRequest.get(urls[0]);
                String result = null;
                if (request.ok()) {
                    Log.d("OK", "Request is ok");
                    result = request.body();
                }
                return result;
            } catch (HttpRequest.HttpRequestException exception) {
                return null;
            }
        }

        protected void onProgressUpdate(Long... progress) {
            Log.d("MyApp", "Downloaded bytes: " + progress[0]);
        }

        protected void onPostExecute(String file) {
            if (file != null) {
                Log.d("Result", file);
                try {
                    JSONObject reader = new JSONObject();
                    JSONObject res = reader.getJSONObject(file);
                } catch (JSONException e) {

                }
            } else {
                Log.d("Bad response", "Null");
            }
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            if (result.getContents() != null) {
                Log.d("Working", result.getContents());
                // add code for making http request
                String response = new DownloadTask().execute("https://tickets.gtindiaclub.com/api/holishow/availability?code").toString();
                Log.d("Result", response);
            }
        }
    }
}
