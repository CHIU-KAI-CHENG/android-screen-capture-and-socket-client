package com.example.kent.letshare.util;


import android.app.Activity;
import android.widget.Toast;

public  class ToastMsg extends Task {

    private String msg;
    public ToastMsg(Activity activity, String msg) {
        super(activity);
        this.msg = msg;
    }

    @Override
    public void run() {
        Toast.makeText(act_ref, msg, Toast.LENGTH_SHORT).show();
    }
}
