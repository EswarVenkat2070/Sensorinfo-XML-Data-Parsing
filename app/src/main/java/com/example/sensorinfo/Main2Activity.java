package com.example.sensorinfo;
import android.content.Intent;
import android.content.res.XmlResourceParser;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.app.AlertDialog;
import android.content.DialogInterface;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;


import java.io.*;
import java.io.IOException;

import java.util.ArrayList;

public class Main2Activity extends AppCompatActivity {

    private TextView txt;
    Button browse;
    public static final int REQUEST_CODE = 1;
    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setMessage("Are you sure you want to exit?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Main2Activity.super.onBackPressed();
                    }
                })
                .setNegativeButton("No", null)
                .show();
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        txt = (TextView) findViewById(R.id.txt);
        browse = (Button) findViewById(R.id.button2);
        browse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                select();
            }
        });
    }

    public void select(){
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("file/*");
        startActivityForResult(intent, REQUEST_CODE);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data){


        try {
            String FilePath = data.getData().getPath();
              parseXML(FilePath);
             }
        catch (NullPointerException e)
        {

        }


    }

    private void parseXML(String FilePath) {
        XmlPullParserFactory parserFactory;
        Toast.makeText(getApplicationContext(),FilePath, Toast.LENGTH_LONG).show();
        try {
            parserFactory = XmlPullParserFactory.newInstance();
            XmlPullParser parser = parserFactory.newPullParser();
           //getDir(FilePath,MODE_WORLD_READABLE));

            InputStream is = getAssets().open("data.xml");
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES,true);
            parser.setInput(is,null);

            processParsing(parser);

        } catch (XmlPullParserException e) {
            Log.i("error ", "Parser Exception");
        } catch (IOException e) {
            Log.i("error ", " IO Exception");
        }
    }

    private void processParsing(XmlPullParser parser) throws IOException, XmlPullParserException{
        ArrayList<Player> players = new ArrayList<>();
        int eventType = parser.getEventType();
        Player currentPlayer = null;

        while (eventType != XmlPullParser.END_DOCUMENT) {
            String eltName = null;

            switch (eventType) {
                case XmlPullParser.START_TAG:
                    eltName = parser.getName();

                    if ("sensor".equals(eltName)) {
                        currentPlayer = new Player();
                        players.add(currentPlayer);
                    }
                    else if (currentPlayer != null) {
                        if ("name".equals(eltName)) {
                            currentPlayer.name = parser.nextText();
                        } else if ("para".equals(eltName)) {
                            currentPlayer.para = parser.nextText();
                            currentPlayer.space="--------------------";
                        }
                    }
                    break;
            }


            eventType = parser.next();
        }

        printPlayers(players);
    }

    private void printPlayers(ArrayList<Player> sensors) {
        StringBuilder builder = new StringBuilder();

        for (Player sensor : sensors) {
            builder.append(sensor.name).append("\n");
            builder.append(sensor.para).append("\n\n");
            builder.append(sensor.space).append("\n\n");


        }


        txt.setText(builder.toString());
    }

}