package com.example.kent.letshare;

import android.os.Handler;
import android.support.v7.app.AppCompatActivity;

import com.example.kent.letshare.network.Client;


public abstract class NetworkActivity extends AppCompatActivity {
    protected static Handler handler;
    public static Client client;


    public static int STAT;

    public static final int LOGIN_STAT = 0;
    public static final int REGISTER_STAT = 1;
    public static final int LOBBY_STAT = 2;
    public static final int TRY_CONN_STAT = 3;
    public static final int SHARING_STAT = 4;
    public static final int RECEIVING_STAT = 5;

}
