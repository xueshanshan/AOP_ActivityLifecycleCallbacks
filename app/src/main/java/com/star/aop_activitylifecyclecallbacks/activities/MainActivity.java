package com.star.aop_activitylifecyclecallbacks.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.star.aop_activitylifecyclecallbacks.R;

public class MainActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.tv_test).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, TestActivity.class);
                startActivity(intent);
            }
        });
    }
}
