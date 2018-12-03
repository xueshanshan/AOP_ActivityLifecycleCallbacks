### 功能

#### 1、这是一个使用AOP切入Activity声明周期的方式来判断当前应用进入前后台，并打印相应日志

打印的日志为：

```
12-03 18:28:48.385 18001-18001/? E/ActivityAnnotationAspect: after MainActivity->onCreate
12-03 18:28:48.435 18001-18001/? E/ActivityAnnotationAspect: after MainActivity->onResume
12-03 18:28:48.435 18001-18001/? D/ActivityAnnotationAspect: app become foreground
12-03 18:28:52.665 18001-18001/? E/ActivityAnnotationAspect: after MainActivity->onPause
12-03 18:28:52.685 18001-18001/? E/ActivityAnnotationAspect: after TestActivity->onCreate
12-03 18:28:52.695 18001-18001/? E/ActivityAnnotationAspect: after TestActivity->onResume
12-03 18:28:53.185 18001-18001/? E/ActivityAnnotationAspect: after MainActivity->onStop
12-03 18:28:56.125 18001-18001/? E/ActivityAnnotationAspect: after TestActivity->onPause
12-03 18:28:56.465 18001-18001/? E/ActivityAnnotationAspect: after TestActivity->onStop
12-03 18:28:56.625 18001-18001/? D/ActivityAnnotationAspect: app become background
12-03 18:29:03.535 18001-18001/? E/ActivityAnnotationAspect: after TestActivity->onResume
12-03 18:29:03.535 18001-18001/? D/ActivityAnnotationAspect: app become foreground
```

#### 2、可以注册Activity切前后台的回调

```java
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
```

#### 3、也提供了`LifecycleUtil implements Application.ActivityLifecycleCallbacks`来监听activity生命周期的回调

```java
//在Application中使用
LifecycleUtil.init(this);
LifecycleUtil.get().registerAppVisiableChangedListener(this);
```


