package com.feicuiedu.hunttreasure.user;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;

import com.feicuiedu.hunttreasure.R;
import com.feicuiedu.hunttreasure.commons.ActivityUtils;
import com.feicuiedu.hunttreasure.commons.RegexUtils;
import com.feicuiedu.hunttreasure.components.AlertDialogFragment;
import com.feicuiedu.hunttreasure.home.HomeActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LoginActivity extends AppCompatActivity {

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
        /**
         * 3个泛型：
         * 3. 1. 启动任务输入的参数：请求的地址、上传的数据等
         * 3. 2. 后台任务执行的进度：一般是Integer类型(int的包装类)
         * 3. 3. 后台返回的结果类型：比如String类型、Void等
         *
         */

        new AsyncTask<Void, Integer, Void>() {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();

                showProgress();

            }

            @Override
            protected Void doInBackground(Void... params) {

                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);

                hideProgress();
                showMessage("登陆成功");
                navigationToHome();

            }
        }.execute();
    }

    private void navigationToHome() {
        mActivityUtils.startActivity(HomeActivity.class);
        finish();
    }

    private void showMessage(String msg) {
        mActivityUtils.showToast(msg);
    }

    private void hideProgress() {
        if (mDialog!=null){
            mDialog.dismiss();
        }
    }

    private void showProgress() {
        mDialog = ProgressDialog.show(this, "登陆", "正在登陆中，请稍等～");
    }
}
