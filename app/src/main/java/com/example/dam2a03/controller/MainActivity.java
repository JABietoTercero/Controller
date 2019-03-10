package com.example.dam2a03.controller;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import static android.os.Build.HOST;

public class MainActivity extends AppCompatActivity {

    public static final String CONNECTION = "connection";
    public static final String COLOR = "color";
    private static int PORT; // server details
    private static String HOST;
    private Socket sock;
    private BufferedReader in; // i/o for the client
    private PrintWriter out;
    private EditText ip;
    private EditText editText_port;

    private EditText rt;
    private EditText gt;
    private EditText bt;

    private String r;
    private String b;
    private String g;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.ip = (EditText) findViewById(R.id.editIp);
        this.editText_port = (EditText) findViewById(R.id.editPort);

        SharedPreferences settings =
                getSharedPreferences(CONNECTION, MODE_PRIVATE);

        String ip = settings.getString("IP", "192.168.1.37");
        String port = settings.getString("port", "1234");
        this.ip.setText(ip);
        this.editText_port.setText(port);

        this.rt = (EditText) findViewById(R.id.editR);
        this.gt = (EditText) findViewById(R.id.editG);
        this.bt = (EditText) findViewById(R.id.editB);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();

        StrictMode.setThreadPolicy(policy);


    }

    public String getR() {
        return r;
    }

    public String getB() {
        return b;
    }

    public String getG() {
        return g;
    }

    public static int getPORT() {
        return PORT;
    }

    public static String getHOST() {
        return HOST;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        this.closeLink();
    }

    public void connect(View v) {
        HOST = this.ip.getText().toString();
        PORT = Integer.parseInt(this.editText_port.getText().toString());
        r = this.rt.getText().toString();
        g = this.gt.getText().toString();
        b = this.bt.getText().toString();
        savePreferences();
        makeContact();

    }

    private void makeContact() {
        try {
            sock = new Socket(HOST, PORT);
            in = new BufferedReader(new InputStreamReader(sock.getInputStream()));
            out = new PrintWriter(sock.getOutputStream(), true);

        } catch (Exception e) {
            System.out.println(e);
        }
    }

    private void closeLink() {
        try {
            out.println("bye"); // tell server
            sock.close();
            System.out.println("Socket cerrado");
        } catch (Exception e) {
            System.out.println(e);
        }
        //System.exit(0);
    }


    private void savePreferences(){
        String ip = String.valueOf(this.ip.getText());
        String port = String.valueOf(this.editText_port.getText());

        SharedPreferences settings =
                getSharedPreferences(CONNECTION, MODE_PRIVATE);
        SharedPreferences.Editor prefEditor = settings.edit();
        prefEditor.putString("IP", ip);
        prefEditor.putString("port", port);
        prefEditor.commit();
    }


    public void start(View v) {
        Intent intent = new Intent(this, JoyStickActivity.class);
        String connection = this.getHOST() + "&" + this.getPORT();
        String color = this.getR() + "&" + this.getG() + "&" + this.getB();
        intent.putExtra(CONNECTION, connection);
        intent.putExtra(COLOR, color);
        startActivity(intent);

    }

}
