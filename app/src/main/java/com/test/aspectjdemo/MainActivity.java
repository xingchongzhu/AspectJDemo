package com.test.aspectjdemo;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.trackpoint.annotation.AspectAnalyze;
import com.trackpoint.annotation.AspectDebugLog;

import net.codepig.aspectjdemo.R;

public class MainActivity extends Activity implements View.OnTouchListener {
    private Button myButton;
    private final String TAG= this.getClass().getSimpleName();

    @AspectAnalyze(name = "MainActivity.onCreate")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        myButton=findViewById(R.id.myButton);
        myButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(MainActivity.this,"text",Toast.LENGTH_LONG).show();
                onNameClick();
            }
        });
        findViewById(R.id.line1).setOnTouchListener(this);
    }

    @AspectDebugLog
    @AspectAnalyze(name = "onNameClick")
    public void onNameClick() {
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @AspectAnalyze(name = "MainActivity.onDestroy")
    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        Log.d(TAG,"onTouch event "+event.getAction());
        return false;
    }

    @Override
    protected void onResume() {
        super.onResume();
    }
}
