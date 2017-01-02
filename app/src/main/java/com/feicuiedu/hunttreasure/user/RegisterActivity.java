package com.feicuiedu.hunttreasure.user;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.EditText;

import com.feicuiedu.hunttreasure.R;
import com.feicuiedu.hunttreasure.commons.ActivityUtils;
import com.feicuiedu.hunttreasure.commons.RegexUtils;
import com.feicuiedu.hunttreasure.components.AlertDialogFragment;
import com.feicuiedu.hunttreasure.home.HomeActivity;

import org.w3c.dom.Text;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class RegisterActivity extends AppCompatActivity {

    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.et_Username)
    EditText mEtUsername;
    @BindView(R.id.et_Password)
    EditText mEtPassword;
    @BindView(R.id.et_Confirm)
    EditText mEtConfirm;
    @BindView(R.id.btn_Register)
    Button mBtnRegister;

    private ActivityUtils mActivityUtils;
    private String mUsername;
    private String mPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
    }

    @Override
    public void onContentChanged() {
        super.onContentChanged();
        ButterKnife.bind(this);
        mActivityUtils = new ActivityUtils(this);

        setSupportActionBar(mToolbar);
        if (getSupportActionBar()!=null){
            getSupportActionBar().setTitle(R.string.register);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        mEtUsername.addTextChangedListener(textWatcher);
        mEtPassword.addTextChangedListener(textWatcher);
        mEtConfirm.addTextChangedListener(textWatcher);

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
            mUsername = mEtUsername.getText().toString();
            mPassword = mEtPassword.getText().toString();
            String confirm = mEtConfirm.getText().toString();
            boolean canRegister = !(TextUtils.isEmpty(mUsername)|| TextUtils.isEmpty(mPassword))&& mPassword.equals(confirm);
            mBtnRegister.setEnabled(canRegister);
        }
    };

    @OnClick(R.id.btn_Register)
    public void onClick() {

        mActivityUtils.hideSoftKeyboard();

        if (RegexUtils.verifyUsername(mUsername)!=RegexUtils.VERIFY_SUCCESS){

            // 用户名错误
            AlertDialogFragment.getInstances(
                    getString(R.string.username_error),
                    getString(R.string.username_rules))
                    .show(getFragmentManager(),"usernameError");

            return;
        }

        if (RegexUtils.verifyPassword(mPassword)!=RegexUtils.VERIFY_SUCCESS){

            AlertDialogFragment.getInstances(
                    getString(R.string.password_error),
                    getString(R.string.password_rules))
                    .show(getFragmentManager(),"passwordError");

            return;

        }

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
                showMessage("注册成功");
                navigationToHome();

            }
        }.execute();

    }

    private ProgressDialog mDialog;

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
