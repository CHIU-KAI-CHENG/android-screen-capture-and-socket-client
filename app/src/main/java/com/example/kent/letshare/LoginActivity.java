package com.example.kent.letshare;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;

import com.example.kent.letshare.network.Client;
import com.example.kent.letshare.network.MsgHandler;
import com.example.kent.letshare.util.Task;

public class LoginActivity extends NetworkActivity {


    private EditText account;
    private EditText password;

    Dialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_login);


        account = findViewById(R.id.acc_txt);
        password = findViewById(R.id.pass_txt);

        handler = new Handler();

        client = new Client(handler);
        client.Start();

    }

    @Override
    protected void onResume() {
        super.onResume();
        client.SetActivity(this);
        STAT = LOGIN_STAT;
    }

    public void Login(View view)
    {
        client.SetWriteMsg(MsgHandler.Login(account.getText().toString(), password.getText().toString()));
        client.Send();
//        LobbyActivity.StartDrawActivity startDrawActivity = new LobbyActivity.StartDrawActivity(LoginActivity.this);
//        handler.post(startDrawActivity);
    }

    public void Register(View view)
    {
        Intent register = new Intent(LoginActivity.this, RegisterActivity.class);
        startActivity(register);
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
    }

    public static class EnterLobby extends Task {
        public EnterLobby(Activity activity)
        {
            super(activity);
        }
        @Override
        public void run()//post
        {
            Intent enterLobby = new Intent(act_ref, LobbyActivity.class);
            act_ref.startActivity(enterLobby);
            act_ref.overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
        }
    }
}
