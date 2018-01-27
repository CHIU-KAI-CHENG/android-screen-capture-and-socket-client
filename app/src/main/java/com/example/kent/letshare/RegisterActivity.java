package com.example.kent.letshare;


import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;

import com.example.kent.letshare.network.MsgHandler;
import com.example.kent.letshare.util.Task;

public class RegisterActivity extends NetworkActivity {
    private EditText account;
    private EditText password;
    private EditText name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_register);


        account = findViewById(R.id.regAcc_txt);
        password = findViewById(R.id.regPass_txt);
        name = findViewById(R.id.regName_txt);
    }

    @Override
    protected void onResume() {
        super.onResume();

        client.SetActivity(this);
        STAT = REGISTER_STAT;
    }

    public void Register(View view)
    {
        client.SetWriteMsg(MsgHandler.Register(account.getText().toString(),
                password.getText().toString(),
                name.getText().toString()));
        client.Send();
    }

    public void Cancel(View view)
    {
        finish();
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
    }

    public static class ReturnLogin extends Task{

        public ReturnLogin(Activity activity) {
            super(activity);
        }

        @Override
        public void run() {
            act_ref.finish();
            act_ref.overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
    }
}
