package com.star.aop_activitylifecyclecallbacks;

import android.app.Application;
import android.util.Log;

import com.star.aop_activitylifecyclecallbacks.aspects.ActivityAnnotationAspect;
import com.star.aop_activitylifecyclecallbacks.listeners.OnAppVisibleListener;
import com.star.aop_activitylifecyclecallbacks.utils.LifecycleUtil;

/**
 * @author xueshanshan
 * @date 2018/12/3
 */
public class MyApp extends Application implements OnAppVisibleListener {
    private static final String TAG = MyApp.class.getSimpleName();

    @Override
    public void onCreate() {
        super.onCreate();
        ActivityAnnotationAspect.registerAppVisiableChangedListener(this);

//        LifecycleUtil.init(this);
//        LifecycleUtil.get().registerAppVisiableChangedListener(this);
    }

    @Override
    public void becomeForeground() {
        Log.e(TAG, "becomeForeground");
    }

    @Override
    public void becomeBackground() {
        Log.e(TAG, "becomeBackground");
    }
}
