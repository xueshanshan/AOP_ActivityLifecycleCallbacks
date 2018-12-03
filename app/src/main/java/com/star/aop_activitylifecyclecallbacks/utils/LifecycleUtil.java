package com.star.aop_activitylifecyclecallbacks.utils;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import com.star.aop_activitylifecyclecallbacks.BuildConfig;
import com.star.aop_activitylifecyclecallbacks.listeners.OnAppVisibleListener;

import java.lang.ref.WeakReference;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class LifecycleUtil implements Application.ActivityLifecycleCallbacks {

    private static final String TAG = LifecycleUtil.class.getSimpleName();

    private static LifecycleUtil sInstance;
    private Activity curActivity;

    /**
     * 是否在前台，false 代表不在前台，初始化为false ，对应app初始打开
     * 对于exception的activity,打开时认为在后台 ,foreground = false;
     */
    private boolean foreground = false;
    private boolean paused = true;
    private Handler handler = new Handler();
    private static final long CHECK_DELAY = 500L;
    private Application mApp;
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

    private Set<WeakReference<OnAppVisibleListener>> mOnAppVisibleListeners = new HashSet<>();

    private LifecycleUtil() {}

    public static void init(Application app) {
        if (sInstance == null) {
            sInstance = new LifecycleUtil();
            sInstance.mApp = app;
            app.registerActivityLifecycleCallbacks(sInstance);
        }
    }

    public static LifecycleUtil get() {
        return sInstance;
    }

    public Activity getCurActivity() {
        return curActivity;
    }

    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {

    }

    @Override
    public void onActivityStarted(Activity activity) {

    }

    @Override
    public void onActivityResumed(Activity activity) {
        if (!foreground) {
            notifyStatusChanged(true);
        }
        foreground = true;
        paused = false;
        curActivity = activity;
        handler.removeCallbacks(pauseRunnable);
    }

    @Override
    public void onActivityPaused(Activity activity) {
        curActivity = null;
        paused = true;
        handler.removeCallbacks(pauseRunnable);
        handler.postDelayed(pauseRunnable, CHECK_DELAY);
    }

    @Override
    public void onActivityStopped(Activity activity) {

    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

    }

    @Override
    public void onActivityDestroyed(Activity activity) {

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

    public void registerAppVisiableChangedListener(OnAppVisibleListener onAppVisibleListener) {
        mOnAppVisibleListeners.add(new WeakReference<OnAppVisibleListener>(onAppVisibleListener));
    }

    public void unregisterAppVisiableChangedListener(OnAppVisibleListener onAppVisibleListener) {
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

    public boolean isForeground() {
        return foreground;
    }

    public boolean isBackground() {
        return !foreground;
    }
}
