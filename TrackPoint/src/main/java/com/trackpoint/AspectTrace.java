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
    private static AspectTraceListener aspectTraceListener;

    /**
     * 针对所有继承 Activity 类的 onCreate 方法
     */
    @Pointcut("execution(* android.app.Activity+.onCreate(..))")
    public void activityOnCreatePointcut() {

    }
    /**
     * 针对带有AspectAnalyze注解的方法
     */
    @Pointcut("execution(@com.trackpoint.annotation.AspectAnalyze * *(..))")
    public void aspectAnalyzeAnnotation() {
    }
    /**
     * 针对带有AspectAnalyze注解的方法
     */
    @Pointcut("execution(@com.trackpoint.annotation.AspectDebugLog * *(..))")
    public void aspectDebugLogAnnotation() {
    }

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
    /**
     * 针对前面 aspectAnalyzeAnnotation() 的配置
     */
    @Around("aspectAnalyzeAnnotation()")
    public void aroundJoinAspectAnalyze(final ProceedingJoinPoint joinPoint) throws Throwable {
        Object target = joinPoint.getTarget();
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        AspectAnalyze aspectAnalyze = methodSignature.getMethod().getAnnotation(AspectAnalyze.class);
        long startTimeMillis = System.currentTimeMillis();
        joinPoint.proceed();
        if (aspectTraceListener != null) {
            aspectTraceListener.onAspectAnalyze(joinPoint, aspectAnalyze, methodSignature, System.currentTimeMillis() - startTimeMillis);
        }
    }
    /**
     * 针对前面 aspectDebugLogAnnotation() 或 activityOnCreatePointcut() 的配置
     */
    @Around("aspectDebugLogAnnotation() || activityOnCreatePointcut()")
    public void aroundJoinAspectDebugLog(final ProceedingJoinPoint joinPoint) throws Throwable {
        long startTimeMillis = System.currentTimeMillis();
        joinPoint.proceed();
        long duration = System.currentTimeMillis() - startTimeMillis;
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        SourceLocation location = joinPoint.getSourceLocation();
        String message = String.format("%s(%s:%s) [%sms]", methodSignature.getMethod().getName(), location.getFileName(), location.getLine(), duration);
        if (aspectTraceListener != null) {
            aspectTraceListener.logger("AspectTrace", message);
        } else {
            Log.e("AspectTrace", message);
        }
    }

    public static void setAspectTraceListener(AspectTraceListener aspectTraceListener) {
        AspectTrace.aspectTraceListener = aspectTraceListener;
    }

    public interface AspectTraceListener {
        void logger(String tag, String message);

        void onAspectAnalyze(ProceedingJoinPoint joinPoint, AspectAnalyze aspectAnalyze, MethodSignature methodSignature, long duration);
    }
}

