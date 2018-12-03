package com.star.aop_activitylifecyclecallbacks.aspect;

import android.os.Handler;
import android.util.Log;

import com.star.aop_activitylifecyclecallbacks.annotations.ActivityLifeCycle;
import com.star.aop_activitylifecyclecallbacks.annotations.LifeCycleMethod;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;

/**
 * @author xueshanshan
 * @date 2018/12/3
 */
@Aspect
public class ActivityAnnotationAspect {
    private static final String TAG = ActivityAnnotationAspect.class.getSimpleName();
    private boolean foreground = false;
    private boolean paused = true;
    private Handler handler = new Handler();
    private static final long CHECK_DELAY = 500L;
    private Runnable pauseRunnable = new Runnable() {
        @Override
        public void run() {
            if (!paused) {
                return;
            }
            if (foreground) {
                foreground = false;
                notifyStatusChanged(false);
            }
        }
    };

    @Pointcut("execution(@*..ActivityLifeCycle * *(..)) && @annotation(activityLifeCycle)")
    public void activitylifeCycle(ActivityLifeCycle activityLifeCycle) {}


    @After("activitylifeCycle(activityLifeCycle)")
    public void afterActivitylifeCycle(JoinPoint joinPoint, ActivityLifeCycle activityLifeCycle) {
        String methodName = joinPoint.getSignature().getName();
        Class<?> aClass = joinPoint.getThis().getClass();
        String className = aClass.getSimpleName();
        Log.e(TAG, "after " + className + "->" + methodName);
        switch (methodName) {
            case LifeCycleMethod.LIFECYCLE_METHOD_ON_CREATE:
                break;
            case LifeCycleMethod.LIFECYCLE_METHOD_ON_RESUME:
                if (!foreground) {
                    notifyStatusChanged(true);
                }
                foreground = true;
                paused = false;
                handler.removeCallbacks(pauseRunnable);
                break;
            case LifeCycleMethod.LIFECYCLE_METHOD_ON_PAUSE:
                paused = true;
                handler.removeCallbacks(pauseRunnable);
                handler.postDelayed(pauseRunnable, CHECK_DELAY);
                break;
            case LifeCycleMethod.LIFECYCLE_METHOD_ON_STOP:
                break;
            case LifeCycleMethod.LIFECYCLE_METHOD_ON_DESTROY:
                break;
            default:
                break;
        }
    }

    private void notifyStatusChanged(boolean foreground) {
        if (foreground) {
            Log.d(TAG, "app become foreground");
        } else {
            Log.d(TAG, "app become background");
        }
    }
}
