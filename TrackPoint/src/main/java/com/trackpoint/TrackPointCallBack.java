package com.trackpoint;

import android.view.MotionEvent;
import android.view.View;

public interface TrackPointCallBack {

    void onClick(String pageClassName, String viewIdName);
    void onTouch(String pageClassName, View v, MotionEvent event);
}
