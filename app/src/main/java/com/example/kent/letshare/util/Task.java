package com.example.kent.letshare.util;


import android.app.Activity;

public abstract class Task implements Runnable{
    protected Activity act_ref;
    public Task(Activity activity)
    {
        act_ref = activity;
    }
}
