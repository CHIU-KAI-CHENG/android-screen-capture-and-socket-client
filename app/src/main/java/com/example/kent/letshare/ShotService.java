package com.example.kent.letshare;

import android.app.Service;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.IBinder;
import android.util.Log;

import com.example.kent.letshare.network.MsgHandler;

import java.util.TimerTask;


public class ShotService extends Service {

    class ConstantRun extends TimerTask
    {
        @Override
        public void run() {
            Bitmap bitmap = DrawActivity.screenShot.getScreenBitmap();

            if (bitmap == null)
                return;
            String picture = MsgHandler.BitMapToString(bitmap);
            Log.d("service : ", picture);

            picture = MsgHandler.Encode(picture);
            picture = MsgHandler.SendPicture(picture);

            Log.d(picture, "takeScreenshot: ");
            NetworkActivity.client.SetWriteMsg(picture);
            NetworkActivity.client.Send();
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();

        new Thread(new Runnable() {
            @Override
            public void run() {
                while(true)
                {
                    Bitmap bitmap = DrawActivity.screenShot.getScreenBitmap();

                    if (bitmap == null)
                        continue;
                    String picture = MsgHandler.BitMapToString(bitmap);

                    picture = MsgHandler.Encode(picture);
                    picture = MsgHandler.SendPicture(picture);

                    NetworkActivity.client.SetWriteMsg(picture);
                    NetworkActivity.client.Send();
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();

        //new Timer().schedule(new ConstantRun(),10,500);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);

        return START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent)
    {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
