package com.star.aop_activitylifecyclecallbacks.activities;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.star.aop_activitylifecyclecallbacks.annotations.ActivityLifeCycle;

public abstract class AOP_BaseActivity extends AppCompatActivity {
    protected Context mContext;

    @ActivityLifeCycle()
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
    }

    @ActivityLifeCycle()
    @Override
    protected void onResume() {
        super.onResume();
    }

    @ActivityLifeCycle()
    @Override
    protected void onPause() {
        super.onPause();
    }

    @ActivityLifeCycle()
    @Override
    protected void onStop() {
        super.onStop();
    }

    @ActivityLifeCycle()
    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
