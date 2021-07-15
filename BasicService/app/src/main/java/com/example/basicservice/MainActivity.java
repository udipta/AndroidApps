package com.example.basicservice;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    // button objects
    private Button buttonStart;
    private Button buttonStop;
    private Button buttonLogCat;

    //initialize log
    private StringBuilder log;

    @Override
    public void onClick(View view) {
        if (view == buttonStart) {
            // start the service here
            startService(new Intent(this, BgService.class));
            Log.d("TAG", "Job Scheduling");

        } else if (view == buttonStop) {
            // stop the service here
            stopService(new Intent(this, BgService.class));
            Log.d("TAG", "Job Canceling");

        } else if (view == buttonLogCat) {
            // write log in file
            Toast.makeText(this, "Writing Log", Toast.LENGTH_LONG).show();
            writeLogCat();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // getting buttons from xml
        buttonStart = findViewById(R.id.buttonStart);
        buttonStop = findViewById(R.id.buttonStop);
        buttonLogCat = findViewById(R.id.buttonLogCat);

        // attaching OnClickListener to buttons
        buttonStart.setOnClickListener(this);
        buttonStop.setOnClickListener(this);
        buttonLogCat.setOnClickListener(this);

    }

    protected void writeLogCat() {
        // Create a new instance of a JSONArray
        JSONArray jsonLogArray = new JSONArray();
        // Create a new instance of a JSONObject
        JSONObject jsonLog = new JSONObject();

        try {
            Process process = Runtime.getRuntime().exec("logcat -d");
            BufferedReader bufferedReader = new BufferedReader(
                    new InputStreamReader(process.getInputStream()));

            log = new StringBuilder();
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                log.append(line).append("\n");
                jsonLogArray.put(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        // convert log to string
        final String logString = log.toString();

        // standard request
        com.example.basicservice.MySingleton mySingleton = new com.example.basicservice.MySingleton(this);

        try {
            // Add the JSONArray to the JSONObject
            jsonLog.put("jsonLogArray", jsonLogArray);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        mySingleton.postLog(jsonLog);
        // create text file in Internal Storage
//        File dir = new File(getFilesDir(), "myLogcat");

        // create text file in SDCard
        File sdCard = Environment.getExternalStorageDirectory();
        File dir = new File (sdCard.getAbsolutePath() + File.separator + "myLogcat");

        File file = new File(dir, "logcat.txt");

        String txt = String.format("File %s created %s%n", file.getAbsolutePath(), dir.mkdirs());
        Toast.makeText(this, txt, Toast.LENGTH_LONG).show();

        try {
            // to write logcat in text file
            FileOutputStream fOut = new FileOutputStream(file);
            OutputStreamWriter osw = new OutputStreamWriter(fOut);

            // Write the string to the file
            osw.write(logString);
            osw.flush();
            osw.close();
        } catch(FileNotFoundException e)
        {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
