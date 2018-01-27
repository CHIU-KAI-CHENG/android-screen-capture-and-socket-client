package com.example.kent.letshare.network;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Base64;
import android.util.Log;

import java.io.ByteArrayOutputStream;

public class MsgHandler {
    public static final String LOGIN = "login";
    public static final String REGISTER = "register";
    public static final String SEND_CONNECT = "connectSend";
    public static final String RECEIVE_CONNECT = "connectRecv";
    public static final String RESULT_CONNECT = "connectResult";
    public static final String PICTURE_RECV = "pictureRecv";
    public static final String PICTURE_SEND = "pictureSend";
    public static final String PICTURE_END = "terminate";
    public static final String SCREEN_SHARE = "screenshare";
    static private StringBuilder sb = new StringBuilder();

    @NonNull
    public static String Login(String account, String password)
    {
        sb.setLength(0);
        sb.append(LOGIN).append('|').append(account).append('|').append(password);
        return sb.toString();
    }

    @NonNull
    public static String Register(String account, String register, String name)
    {
        sb.setLength(0);
        sb.append(REGISTER).append('|').append(account).append('|').append(register).append('|').append(name);
        return sb.toString();
    }

    @NonNull
    public static String SendConnect(String account)
    {
        sb.setLength(0);
        sb.append(SEND_CONNECT).append('|').append(account);
        return sb.toString();
    }

    @NonNull
    public static String ReplyConnect(String reply)
    {
        sb.setLength(0);
        sb.append(RECEIVE_CONNECT).append('|').append(reply);
        return sb.toString();
    }
    @NonNull
    public static String SendPicture(String picture)
    {
        sb.setLength(0);
        sb.append(SCREEN_SHARE).append(PICTURE_SEND).append("|").append(Integer.toString(picture.length())).append("|").append(picture);
        return sb.toString();
    }
    @NonNull
    public static String BitMapToString(Bitmap bitmap){
        ByteArrayOutputStream baos=new  ByteArrayOutputStream();
        //在這裡設定格式
        Bitmap.CompressFormat cf =Bitmap.CompressFormat.PNG;
        bitmap.compress(cf,100, baos);

        byte [] b=baos.toByteArray();
        String temp = Base64.encodeToString(b, Base64.DEFAULT);
        return temp;
    }

    @Nullable
    public static Bitmap StringToBitMap(String encodedString){
        try {
            byte [] encodeByte=Base64.decode(encodedString,Base64.DEFAULT);
            Bitmap bitmap= BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
            return bitmap;
        } catch(Exception e) {
            e.getMessage();
            return null;
        }
    }

    @Nullable
    public static String Encode(String src)
    {
         if(src.contains("\n"))
        {
            src = src.replaceAll("\n", "&");
        }

        return src;
    }

    @Nullable
    public static String Decode(String src)
    {
         if(src.contains("&"))
        {
            src = src.replaceAll("&","\n");
        }
        return src;
    }
    public static String[] CutString(String src, int index)
    {
        String [] parts = new String[2];
        parts[0] = src.substring(0, index);
        parts[1] = src.substring(index);

        return parts;
    }
}
