package com.test.aspectjdemo;

import android.app.Application;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import com.trackpoint.AnnotationAspectTrace;
import com.trackpoint.AspectTrace;
import com.trackpoint.TrackPoint;
import com.trackpoint.TrackPointCallBack;
import com.trackpoint.annotation.AspectAnalyze;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.reflect.MethodSignature;

public class MainApp extends Application {
    private static final String TAG = "LOGCAT";

    @Override
    public void onCreate() {
        super.onCreate();
        AnnotationAspectTrace.setAspectTraceListener(new AnnotationAspectTrace.AspectTraceListener() {
            @Override
            public void logger(String tag, String message) {
                Log.e(tag, message);
            }
            @Override
            public void onAspectAnalyze(ProceedingJoinPoint joinPoint, AspectAnalyze aspectAnalyze, MethodSignature methodSignature, long duration) {
                Log.e("onAspectAnalyze", aspectAnalyze.name());
            }
        });

        TrackPoint.init(new TrackPointCallBack() {
            @Override
            public void onClick(String pageClassName, String viewIdName) {
                Log.d(TAG, "onClick: " + pageClassName + "-" + viewIdName);
                //添加你的操作
            }

            @Override
            public void onTouch(String pageClassName, View v, MotionEvent event) {
                Log.d(TAG, "onTouch: " + pageClassName + "-" + event.getAction());
                //添加你的操作
            }
        });
    }
}
