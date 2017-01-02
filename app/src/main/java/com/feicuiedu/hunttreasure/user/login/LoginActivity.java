package com.feicuiedu.hunttreasure.user.login;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;

import com.feicuiedu.hunttreasure.MainActivity;
import com.feicuiedu.hunttreasure.R;
import com.feicuiedu.hunttreasure.commons.ActivityUtils;
import com.feicuiedu.hunttreasure.commons.RegexUtils;
import com.feicuiedu.hunttreasure.components.AlertDialogFragment;
import com.feicuiedu.hunttreasure.home.HomeActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LoginActivity extends AppCompatActivity implements LoginView{

    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.et_Username)
    EditText mEtUsername;
    @BindView(R.id.et_Password)
    EditText mEtPassword;
    @BindView(R.id.btn_Login)
    Button mBtnLogin;
    private String mUserName;
    private String mPassword;

    private ActivityUtils mActivityUtils;
    private ProgressDialog mDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

    }

    @Override
    public void onContentChanged() {
        super.onContentChanged();
        ButterKnife.bind(this);
        mActivityUtils = new ActivityUtils(this);

        // 1. 处理Toolbar的展示和返回按钮的监听
        setSupportActionBar(mToolbar);
        if (getSupportActionBar() != null) {

            // 激活Home(左上角,内部使用的选项菜单处理的),设置其title
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle(R.string.login);
        }

        // 给两个输入框设置监听事件
        mEtUsername.addTextChangedListener(textWatcher);
        mEtPassword.addTextChangedListener(textWatcher);

    }

    private TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            mUserName = mEtUsername.getText().toString();
            mPassword = mEtPassword.getText().toString();
            boolean canLogin = !(TextUtils.isEmpty(mUserName) || TextUtils.isEmpty(mPassword));
            mBtnLogin.setEnabled(canLogin);
        }
    };

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * 2. 点击按钮做登陆之前，需要处理用户名和密码的输入
     * 2.1. 监听输入，确定可以点击按钮
     * 2.2. 可以点击之后，处理用户名和密码输入有误的情况：弹出一个对话框，自定义一个对话框
     *
     * 3. 自定义一个对话框：AlertDialogFragment
     *
     * 4. 没有问题，去做登陆的操作，因为未加入接口等，所以模拟场景进行
     */
    @OnClick(R.id.btn_Login)
    public void onClick() {
        mActivityUtils.hideSoftKeyboard();

        if (RegexUtils.verifyUsername(mUserName)!=RegexUtils.VERIFY_SUCCESS){

            // 显示用户名错误
            AlertDialogFragment.getInstances(
                    getString(R.string.username_error),
                    getString(R.string.username_rules))
                    .show(getFragmentManager(),"userNameError");
            return;
        }

        if (RegexUtils.verifyPassword(mPassword)!=RegexUtils.VERIFY_SUCCESS){

            // 显示密码错误
            AlertDialogFragment.getInstances(
                    getString(R.string.password_error),
                    getString(R.string.password_rules))
                    .show(getFragmentManager(),"passwordError");

            return;
        }

        // 都没错，进行登陆的操作
        new LoginPresenter(this).login();
    }

    @Override
    public void navigationToHome() {
        mActivityUtils.startActivity(HomeActivity.class);
        finish();

        /**
         * 登陆之后处理一下MainActivity页面关闭的问题使用本地广播来进行
         * 发送一个广播，在Main页面接受到之后关闭自己
         */

        Intent intent = new Intent(MainActivity.MAIN_ACTION);
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
    }

    @Override
    public void showMessage(String msg) {
        mActivityUtils.showToast(msg);
    }

    @Override
    public void hideProgress() {
        if (mDialog!=null){
            mDialog.dismiss();
        }
    }

    @Override
    public void showProgress() {
        mDialog = ProgressDialog.show(this, "登陆", "正在登陆中，请稍等～");
    }
}
