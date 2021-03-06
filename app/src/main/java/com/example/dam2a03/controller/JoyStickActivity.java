package com.example.dam2a03.controller;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.net.Socket;

import static android.os.Build.HOST;

public class JoyStickActivity extends AppCompatActivity {


    private Socket sock;
    private BufferedReader in; // i/o for the client
    private PrintWriter out;
    private String lastMove;
    private JoyStickClass js;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.controller);

        Intent intent = getIntent();
        String[] split = intent.getStringExtra(MainActivity.CONNECTION).split("&");
        String HOST = split[0];
        int PORT = Integer.parseInt(split[1]);

        makeContact(HOST, PORT);
        sendCommand("cntpad");

        String[] color = intent.getStringExtra(MainActivity.COLOR).split("&");
        String r = color[0];
        String g = color[1];
        String b = color[2];
        sendCommand("color&" + r + "&" + g + "&" + b);

        RelativeLayout layout_joystick;

        final TextView textView5 = (TextView) findViewById(R.id.textView5);

        layout_joystick = (RelativeLayout) findViewById(R.id.layout_joystick);

        js = new JoyStickClass(getApplicationContext()
                , layout_joystick, R.drawable.image_button);
        js.setStickSize(150, 150);
        js.setLayoutSize(500, 500);
        js.setLayoutAlpha(150);
        js.setStickAlpha(100);
        js.setOffset(90);
        js.setMinimumDistance(50);

        layout_joystick.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View arg0, MotionEvent arg1) {
                js.drawStick(arg1);
                if (arg1.getAction() == MotionEvent.ACTION_DOWN
                        || arg1.getAction() == MotionEvent.ACTION_MOVE) {
                    int direction = js.get8Direction();
                    if (direction == JoyStickClass.STICK_UP) {
                        textView5.setText("Direction : Up");
                        if (lastMove != "up") {
                            sendCommand("up");
                            lastMove = "up";
                        }

                    } else if (direction == JoyStickClass.STICK_UPRIGHT) {
                        textView5.setText("Direction : Up Right");
                        if (lastMove != "upr") {
                            sendCommand("upr");
                            lastMove = "upr";
                        }
                    } else if (direction == JoyStickClass.STICK_RIGHT) {
                        textView5.setText("Direction : Right");
                        if (lastMove != "ri") {
                            sendCommand("ri");
                            lastMove = "ri";
                        }
                    } else if (direction == JoyStickClass.STICK_DOWNRIGHT) {
                        textView5.setText("Direction : Down Right");
                        if (lastMove != "dor") {
                            sendCommand("dor");
                            lastMove = "dor";
                        }
                    } else if (direction == JoyStickClass.STICK_DOWN) {
                        textView5.setText("Direction : Down");
                        if (lastMove != "do") {
                            sendCommand("do");
                            lastMove = "do";
                        }
                    } else if (direction == JoyStickClass.STICK_DOWNLEFT) {
                        textView5.setText("Direction : Down Left");
                        if (lastMove != "dol") {
                            sendCommand("dol");
                            lastMove = "dol";
                        }
                    } else if (direction == JoyStickClass.STICK_LEFT) {
                        textView5.setText("Direction : Left");
                        sendCommand("le");
                        lastMove = "le";
                    } else if (direction == JoyStickClass.STICK_UPLEFT) {
                        textView5.setText("Direction : Up Left");
                        sendCommand("upl");
                        lastMove = "upl";
                    } else if (direction == JoyStickClass.STICK_NONE) {
                        textView5.setText("Direction : Center");
                        if (lastMove != "nm") {
                            sendCommand("nm");
                            lastMove = "nm";
                        }
                    }
                } else if (arg1.getAction() == MotionEvent.ACTION_UP) {
                    textView5.setText("Direction :");
                    if (lastMove != "nm") {
                        sendCommand("nm");
                        lastMove = "nm";
                    }
                }
                return true;
            }
        });
    }

    public void sendCommand(String comand) {

        try {
            out.println(comand);
            System.out.println("Command sended");
        } catch (Exception ex) {
            System.out.println("Problem sending object\n");
            System.out.println(ex);
        }


    }

    public void shoot(View v) {
        sendCommand("shoot");
    }

    private void makeContact(String HOST, int PORT) {
        try {
            sock = new Socket(HOST, PORT);
            in = new BufferedReader(new InputStreamReader(sock.getInputStream()));
            out = new PrintWriter(sock.getOutputStream(), true);

        } catch (Exception e) {
            System.out.println(e);
        }
    }

}

