package com.example.kent.letshare;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.ImageView;

import com.example.kent.letshare.network.MsgHandler;
import com.example.kent.letshare.util.ScreenShot;
import com.example.kent.letshare.util.Task;


public class ViewActivity extends NetworkActivity  {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view);


    }



    @Override
    protected void onResume() {
        super.onResume();
        client.SetActivity(this);
        STAT = RECEIVING_STAT;
    }

    public static class OnUpdate extends Task
    {
        Bitmap bitmap ;

        public OnUpdate(Activity activity, String picture){
            super(activity);
            Log.d(picture, "OnUpdate: ");
            this.bitmap = MsgHandler.StringToBitMap(picture);
        }

        @Override
        public void run() {
            ImageView pic = act_ref.findViewById(R.id.pic_img);
            pic.setImageBitmap(bitmap);
        }
    }
}
