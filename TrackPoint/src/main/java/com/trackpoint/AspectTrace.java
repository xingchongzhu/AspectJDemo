package com.trackpoint;

import android.util.Log;
import android.widget.Toast;

import com.trackpoint.annotation.AspectAnalyze;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.aspectj.lang.reflect.SourceLocation;

@SuppressWarnings("unused")
@Aspect
public class AspectTrace {
    private final String TAG = "AspectTrace";

    @Around("call(* android.widget.Toast.setText(java.lang.CharSequence))")
    public void handleToastText(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        Log.d(TAG," start handleToastText");
        proceedingJoinPoint.proceed(new Object[]{"处理过的toast"}); //这里把它的参数换了
        Log.d(TAG," end handleToastText");
    }

    @Before("call(* android.widget.Toast.show())")
    public void changeToast(JoinPoint joinPoint) throws Throwable {
        Toast toast = (Toast) joinPoint.getTarget();
        toast.setText("修改后的toast");
        Log.d(TAG, " --> changeToast");
    }

    /**
     * 在MainActivity的所有生命周期的方法中打印log
     * @param joinPoint
     * @throws Throwable
     */
    @Before("execution(* android.app.Activity.**(..))")
    public void method(JoinPoint joinPoint) throws Throwable {
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        String className = joinPoint.getThis().getClass().getSimpleName();
        Log.e(TAG, "class:" + className+" method:" + methodSignature.getName());
    }

}

