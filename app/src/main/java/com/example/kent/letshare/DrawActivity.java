package com.example.kent.letshare;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.kent.letshare.network.MsgHandler;
import com.example.kent.letshare.util.ScreenShot;

import java.io.File;
import java.util.Date;

public class DrawActivity extends NetworkActivity {
    TextView et;
    private CanvasView canvasView;

    public static ScreenShot screenShot;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_draw);
        et = (EditText) findViewById(R.id.editText);
        canvasView = findViewById(R.id.canvas);

        et.setText("EMPTY");

        //surfaceHolder = surfaceView.getHolder();

        screenShot = new ScreenShot();
        screenShot.onCreate(savedInstanceState, this);

        screenShot.startMediaProjection();
        screenShot.test = "Here is data";

    }

    public void onBC(View view) {
        if (takeScreenshot()) {
            Toast.makeText(this, "Screenshot taken!", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Failed to take screenshot!", Toast.LENGTH_LONG).show();
        }
    }

    public void onBcClear(View view) {
        canvasView.clearCanvas();
    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
        screenShot.onSaveInstanceState(outState);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        screenShot.onActivityResult(requestCode, resultCode, data);

        Intent intent = new Intent(this, ShotService.class);
        startService(intent);
    }

    @Override
    protected void onPause() {
        super.onPause();
        //screenShot.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
       // screenShot.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
        STAT = SHARING_STAT;
        client.SetActivity(this);

    }

    private void openScreenshot(File imageFile) {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        Uri uri = Uri.fromFile(imageFile);
        intent.setDataAndType(uri, "image/*");
        startActivity(intent);
    }

    public boolean takeScreenshot() {
        Date now = new Date();
        android.text.format.DateFormat.format("yyyy-MM-dd_hh:mm:ss", now);

        try {
            //View v1 = getWindow().getDecorView().getRootView();
            //v1.setDrawingCacheEnabled(true);
            //Bitmap bitmap = Bitmap.createBitmap(v1.getDrawingCache());
            //v1.setDrawingCacheEnabled(false);

            Bitmap bitmap = screenShot.getScreenBitmap();

            String picture = MsgHandler.BitMapToString(bitmap);
            if (bitmap == null)
                return false;

            picture = MsgHandler.Encode(picture);
            picture = MsgHandler.SendPicture(picture);

            Log.d(picture, "takeScreenshot: ");
            client.SetWriteMsg(picture);
            client.Send();


            //Canvas canvas = surfaceHolder.lockCanvas();
            //canvas.drawColor(Color.WHITE);
            //Rect sq = new Rect(0,0,surfaceView.getMeasuredWidth(),surfaceView.getMeasuredHeight());
            //Paint paint = new Paint();
            //paint.setFilterBitmap(true);
            //canvas.drawBitmap(bitmap,null,sq,paint);
            //surfaceHolder.unlockCanvasAndPost(canvas);
            //-----------^-------測試

            return true;
        } catch (Throwable e) {
            e.printStackTrace();
            return false;
        }
    }




}
