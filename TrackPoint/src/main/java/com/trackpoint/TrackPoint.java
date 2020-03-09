package com.trackpoint;

import android.view.MotionEvent;
import android.view.View;

public class TrackPoint {

    private static TrackPointCallBack trackPointCallBack;

    private TrackPoint() {
    }

    public static void init(TrackPointCallBack callBack) {
        trackPointCallBack = callBack;
    }

    public static void onClick(String pageClassName, String viewIdName) {
        if (trackPointCallBack == null) {
            return;
        }
        trackPointCallBack.onClick(pageClassName, viewIdName);
    }


    public static void onTouch(String className, View v, MotionEvent event) {
        if (trackPointCallBack == null) {
            return;
        }
        trackPointCallBack.onTouch(className,v,event);
    }
}
