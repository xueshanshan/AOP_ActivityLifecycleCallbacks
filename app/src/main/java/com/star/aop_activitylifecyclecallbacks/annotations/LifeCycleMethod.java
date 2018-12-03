package com.star.aop_activitylifecyclecallbacks.annotations;

import android.support.annotation.StringDef;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author xueshanshan
 * @date 2018/12/3
 */
public class LifeCycleMethod {
    public static final String LIFECYCLE_METHOD_ON_CREATE = "onCreate";
    public static final String LIFECYCLE_METHOD_ON_RESUME = "onResume";
    public static final String LIFECYCLE_METHOD_ON_PAUSE = "onPause";
    public static final String LIFECYCLE_METHOD_ON_STOP = "onStop";
    public static final String LIFECYCLE_METHOD_ON_DESTROY = "onDestroy";

    @Retention(RetentionPolicy.SOURCE)
    @Target(ElementType.METHOD)
    @StringDef({LIFECYCLE_METHOD_ON_CREATE,
            LIFECYCLE_METHOD_ON_RESUME,
            LIFECYCLE_METHOD_ON_PAUSE,
            LIFECYCLE_METHOD_ON_STOP,
            LIFECYCLE_METHOD_ON_DESTROY})
    public @interface MethodName {

    }
}
