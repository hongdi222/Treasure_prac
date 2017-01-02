package com.feicuiedu.hunttreasure;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.feicuiedu.hunttreasure.commons.ActivityUtils;
import com.feicuiedu.hunttreasure.user.login.LoginActivity;
import com.feicuiedu.hunttreasure.user.register.RegisterActivity;

import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class MainActivity extends AppCompatActivity {

    private Unbinder mBind;
    private ActivityUtils mActivityUtils;
    public static String MAIN_ACTION="navigation_to_home";

    private BroadcastReceiver receive = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            finish();
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mActivityUtils = new ActivityUtils(this);
        mBind = ButterKnife.bind(this);

        // 注册本地广播
        IntentFilter filter = new IntentFilter(MAIN_ACTION);
        LocalBroadcastManager.getInstance(this).registerReceiver(receive,filter);

    }

    // 跳转
    @OnClick({R.id.btn_Register, R.id.btn_Login})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_Register:
                mActivityUtils.startActivity(RegisterActivity.class);
                break;
            case R.id.btn_Login:
                mActivityUtils.startActivity(LoginActivity.class);
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mBind.unbind();
    }
}
