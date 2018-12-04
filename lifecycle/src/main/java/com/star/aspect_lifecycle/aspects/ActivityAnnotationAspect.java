package com.star.aspect_lifecycle.aspects;

import android.os.Handler;
import android.util.Log;

import com.star.aspect_lifecycle.BuildConfig;
import com.star.aspect_lifecycle.annotations.ActivityLifeCycle;
import com.star.aspect_lifecycle.annotations.LifeCycleMethod;
import com.star.aspect_lifecycle.listeners.OnAppVisibleListener;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;

import java.lang.ref.WeakReference;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 * @author xueshanshan
 * @date 2018/12/3
 */
@Aspect
public class ActivityAnnotationAspect {
    private static final String TAG = ActivityAnnotationAspect.class.getSimpleName();
    private static boolean foreground = false;
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

    private static Set<WeakReference<OnAppVisibleListener>> mOnAppVisibleListeners = new HashSet<>();

    @Pointcut("execution(@*..ActivityLifeCycle * *(..)) && @annotation(activityLifeCycle)")
    public void activitylifeCycle(ActivityLifeCycle activityLifeCycle) {}


    @After("activitylifeCycle(activityLifeCycle)")
    public void afterActivitylifeCycle(JoinPoint joinPoint, ActivityLifeCycle activityLifeCycle) {
        String methodName = joinPoint.getSignature().getName();
        Class<?> aClass = joinPoint.getThis().getClass();
        String className = aClass.getSimpleName();
        logD("after " + className + "->" + methodName);
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
            logD("app become foreground");
        } else {
            logD("app become background");
        }
        Iterator<WeakReference<OnAppVisibleListener>> iterator = mOnAppVisibleListeners.iterator();
        while (iterator.hasNext()) {
            WeakReference<OnAppVisibleListener> next = iterator.next();
            if (next.get() != null) {
                if (foreground) {
                    next.get().becomeForeground();
                } else {
                    next.get().becomeBackground();
                }
            }
        }
    }

    public static void registerAppVisiableChangedListener(OnAppVisibleListener onAppVisibleListener) {
        mOnAppVisibleListeners.add(new WeakReference<OnAppVisibleListener>(onAppVisibleListener));
    }

    public static void unregisterAppVisiableChangedListener(OnAppVisibleListener onAppVisibleListener) {
        Iterator<WeakReference<OnAppVisibleListener>> iterator = mOnAppVisibleListeners.iterator();
        while (iterator.hasNext()) {
            WeakReference<OnAppVisibleListener> next = iterator.next();
            if (next.get() != null && next.get() == onAppVisibleListener) {
                mOnAppVisibleListeners.remove(next);
                break;
            }
        }
    }

    private void logD(String message) {
        if (BuildConfig.DEBUG) {
            Log.d(TAG, message);
        }
    }

    public static boolean isForeground() {
        return foreground;
    }

    public static boolean isBackground() {
        return !foreground;
    }
}
