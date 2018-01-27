package com.example.kent.letshare.network;


import android.app.Activity;
import android.os.Handler;
import android.util.Log;

import com.example.kent.letshare.LobbyActivity;
import com.example.kent.letshare.LoginActivity;
import com.example.kent.letshare.RegisterActivity;
import com.example.kent.letshare.ViewActivity;
import com.example.kent.letshare.util.ToastMsg;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.DataOutputStream;
import java.net.Socket;

import static com.example.kent.letshare.NetworkActivity.*;

public class Client {
    private Activity currentActivity;

    private Socket socket;
    private BufferedReader reader;
    private DataOutputStream writer;

    private Handler handler;

    private String wr_msg;
    private String rd_msg;
    private String msg_content;


    private Runnable Connect = new Runnable() {
        @Override
        public void run() {
            try {
                socket = new Socket("10.201.9.31", 8000);
                reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                writer = new DataOutputStream(socket.getOutputStream());

                StringBuilder sb = new StringBuilder();

                int number = 0;
                String picture = "";

                while (true)
                {
                    rd_msg = reader.readLine();
                    Log.d("receive", rd_msg);


                    if (!rd_msg.equals("") && rd_msg.contains("|"))
                    {
                        if (rd_msg.contains(MsgHandler.LOGIN)  && STAT == LOGIN_STAT)
                        {
                            msg_content = rd_msg.split("[|]")[1];
                            if (msg_content.equals("T"))
                            {
                                LoginActivity.EnterLobby enterLobby = new LoginActivity.EnterLobby(currentActivity);
                                handler.post(enterLobby);
                            }
                            else
                                break;
                        }
                        else if (rd_msg.contains(MsgHandler.REGISTER) && STAT == REGISTER_STAT)
                        {
                            msg_content = rd_msg.split("[|]")[1];

                            if (msg_content.equals("T"))
                            {
                                ToastMsg toastMsg = new ToastMsg(currentActivity, "註冊成功!");
                                handler.post(toastMsg);
                                RegisterActivity.ReturnLogin returnLogin = new RegisterActivity.ReturnLogin(currentActivity);
                                handler.post(returnLogin);
                            }
                            else
                            {
                                ToastMsg toastMsg = new ToastMsg(currentActivity, "註冊失敗，請重新申請帳號");
                                handler.post(toastMsg);
                            }
                        }
                        else if (rd_msg.contains(MsgHandler.SEND_CONNECT) && STAT == TRY_CONN_STAT)
                        {
                            msg_content = rd_msg.split("[|]")[1];

                            if (msg_content.equals("online"))
                            {
                                LobbyActivity.ConnectResult connectResult = new LobbyActivity.ConnectResult(currentActivity,
                                        "正在等候回應",
                                        true);
                                handler.post(connectResult);
                            }
                            else
                            {
                                LobbyActivity.ConnectResult connectResult = new LobbyActivity.ConnectResult(currentActivity,
                                        "不在線上或不存在的用戶",
                                        false);
                                handler.post(connectResult);
                            }
                        }
                        else if (rd_msg.contains(MsgHandler.RECEIVE_CONNECT) && STAT == LOBBY_STAT)
                        {
                            msg_content = rd_msg.split("[|]")[1];
                            LobbyActivity.ConnectResponse connectResponse = new LobbyActivity.ConnectResponse(currentActivity,
                                    msg_content);
                            handler.post(connectResponse);
                        }
                        else  if (rd_msg.contains(MsgHandler.RESULT_CONNECT) && STAT == TRY_CONN_STAT)
                        {
                            msg_content = rd_msg.split("[|]")[1];
                            if (msg_content.equals("T"))
                            {
                                LobbyActivity.StartDrawActivity startDrawActivity = new LobbyActivity.StartDrawActivity(currentActivity);
                                handler.post(startDrawActivity);
                            }
                            else
                            {

                            }
                        }
                        else if(STAT == RECEIVING_STAT){
                            String [] strings = rd_msg.split("[|]");
                            if (strings[0].equals(MsgHandler.PICTURE_RECV))
                            {
                                number = Integer.valueOf(strings[1]);
                                if (strings[2].length() == number)
                                {
                                    picture = MsgHandler.Decode(strings[2]);
                                    ViewActivity.OnUpdate onUpdate = new ViewActivity.OnUpdate(currentActivity, picture);
                                    handler.post(onUpdate);
                                }
                                else
                                {
                                    number -= strings[2].length();
                                    picture = strings[2];
                                }
                            }
                            else
                            {
                                if (number == strings[0].length())
                                {
                                    picture += strings[0];
                                    picture = MsgHandler.Decode(picture);
                                    ViewActivity.OnUpdate onUpdate = new ViewActivity.OnUpdate(currentActivity, picture);
                                    handler.post(onUpdate);
                                }
                            }

                        }

                    }
                    else
                    {
                        Log.d("leaving", "run: ");
                        break;
                    }
                }


            } catch (IOException e) {
                e.printStackTrace();
            }
            Log.d("exit", "run: ");
        }
    };

    public Client(Handler handler)
    {
        this.handler = handler;
    }

    public void Start()
    {
        new Thread(Connect).start();
    }

    public void SetActivity(Activity activity)
    {
        this.currentActivity = activity;
    }

    public void SetWriteMsg(String msg)
    {
        wr_msg = msg;
        Log.d("writing", msg);
    }

    public void Send()
    {
        try {
            Log.d(wr_msg, "Send: ");
            writer.writeBytes(wr_msg);
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }





}
