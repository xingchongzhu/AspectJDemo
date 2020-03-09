package com.trackpoint.view;

import android.util.Log;
import android.view.View;

import com.trackpoint.TrackPoint;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;

@Aspect
public class ViewAspect {
    private final String TAG = "ViewAspect";

    @Pointcut("execution(void android.view.View.OnClickListener.onClick(..))")
    public void onClickPointcut() {
    }

    @Pointcut("execution(* *.*onTouch(..))")
    public void onTouchPointcut() {
        Log.d(TAG,"onTouchPointcut");
    }


    @Around("onClickPointcut()")
    public void aroundJoinClickPoint(final ProceedingJoinPoint joinPoint) throws Throwable {
        Object target = joinPoint.getTarget();
        String className = "";
        if (target != null) {
            className = target.getClass().getName();
        }
        //获取点击事件view对象及名称，可以对不同按钮的点击事件进行统计
        Object[] args = joinPoint.getArgs();
        if (args.length >= 1 && args[0] instanceof View) {
            View view = (View) args[0];
            int id = view.getId();
            String entryName = view.getResources().getResourceEntryName(id);
            TrackPoint.onClick(className, entryName);
        }
        joinPoint.proceed();//执行原来的代码
    }
}
