package com.example.kent.letshare;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.kent.letshare.network.MsgHandler;
import com.example.kent.letshare.util.Task;

public class LobbyActivity extends NetworkActivity {

    public TextView searchBar;
    public ImageButton searchBtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_lobby);

        searchBar = findViewById(R.id.conn_name_txt);
        searchBtn = findViewById(R.id.search_btn);


    }

    @Override
    protected void onResume() {
        super.onResume();

        client.SetActivity(this);
        STAT = LOBBY_STAT;
    }

    public void Search(final View view)
    {
        Log.d("lobby", "Search: ");
        client.SetWriteMsg(MsgHandler.SendConnect(searchBar.getText().toString()));
        client.Send();
        STAT = TRY_CONN_STAT;

    }

    public static class ConnectResult extends Task {
        Dialog dialog;
        TextView msgTxt;
        String msg;
        TextView countTxt;
        Button btn;
        boolean type;
        public ConnectResult(Activity activity, String msg, boolean type)
        {
            super(activity);
            this.msg = msg;
            this.type = type;
        }
        @Override
        public void run() {
            dialog = new Dialog(act_ref);
            dialog.setContentView(R.layout.connect_view);

            msgTxt = dialog.findViewById(R.id.dialogMsg_txt);
            countTxt = dialog.findViewById(R.id.count_txt);
            btn = dialog.findViewById(R.id.dialog_btn);

            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.show();
            msgTxt.setText(msg);
            if (type)
            {
                btn.setText("取消");
//                new CountDownTimer(10000, 1000) {
//                    @Override
//                    public void onTick(long l) {
//                        countTxt.setText(String.valueOf((int) (l / 1000)));
//                    }
//
//                    @Override
//                    public void onFinish() {
//                        STAT = LOBBY_STAT;
//                        dialog.dismiss();
//                    }
//                }.start();
            }
            else
            {
                btn.setText("確認");
                countTxt.setVisibility(View.INVISIBLE);
            }
            btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    STAT = LOBBY_STAT;
                    dialog.dismiss();
                }
            });
        }
    }

    public static class ConnectResponse extends Task
    {
        Dialog dialog;
        String name;
        TextView inviter;
        Button acceptBtn;
        Button refuseBtn;
        public ConnectResponse(Activity activity, String name) {
            super(activity);
            this.name = name;
        }

        @Override
        public void run() {
            dialog = new Dialog(act_ref);
            dialog.setContentView(R.layout.response_view);

            inviter = dialog.findViewById(R.id.inviter_txt);
            acceptBtn = dialog.findViewById(R.id.respTrue_btn);
            refuseBtn = dialog.findViewById(R.id.respFalse_btn);

            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.show();

            inviter.setText(name);
            acceptBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    client.SetWriteMsg(MsgHandler.ReplyConnect("T"));
                    client.Send();
                    Intent intent = new Intent(act_ref, ViewActivity.class);
                    act_ref.startActivity(intent);
                }
            });
            refuseBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    client.SetWriteMsg(MsgHandler.ReplyConnect("F"));
                    client.Send();
                }
            });

            STAT = SHARING_STAT;
        }
    }

    public static class StartDrawActivity extends Task{
        public StartDrawActivity(Activity activity) {
            super(activity);
        }

        @Override
        public void run() {
            Intent intent = new Intent(act_ref, DrawActivity.class);
            act_ref.startActivity(intent);
        }
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right);
    }

    @Override
    protected void onDestroy() {
        Intent intent = new Intent(this, ShotService.class);
        stopService(intent);
        super.onDestroy();
    }
}
