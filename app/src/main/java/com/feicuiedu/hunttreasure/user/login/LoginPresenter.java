package com.feicuiedu.hunttreasure.user.login;

import android.os.AsyncTask;

import com.feicuiedu.hunttreasure.net.NetClient;
import com.feicuiedu.hunttreasure.user.User;
import com.feicuiedu.hunttreasure.user.UserPrefs;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by gqq on 17/1/2.
 */

public class LoginPresenter {

    private LoginView mLoginView;
    private Call<LoginResult> mLoginCall;

    /**
     * 两种方式拿到视图方法：
     * 1. Activity：要达到视图和业务分离，持有Activity的对象，并没有达到分离的效果
     * 2. 接口回调：
     */

    public LoginPresenter(LoginView loginView) {

        this.mLoginView = loginView;
    }

    public void login(User user){
        // 完成登陆
        mLoginView.showProgress();

        if (mLoginCall!=null){
            mLoginCall.cancel();
        }

        mLoginCall = NetClient.getInstances().getTreasureApi().login(user);
        mLoginCall.enqueue(loginCallBack);
    }

    private Callback<LoginResult> loginCallBack = new Callback<LoginResult>() {

        @Override
        public void onResponse(Call<LoginResult> call, Response<LoginResult> response) {

            mLoginView.hideProgress();
            if (response.isSuccessful()){
                LoginResult result = response.body();
                if (result==null){
                    mLoginView.showMessage("未知错误");
                    return;
                }

                mLoginView.showMessage(result.getMsg());
                if (result.getCode()==1){
                    UserPrefs.getInstance().setPhoto(NetClient.BASE_URL+result.getIconUrl());
                    UserPrefs.getInstance().setTokenid(result.getTokenId());
                    mLoginView.navigationToHome();
                }

                return;
            }
        }

        @Override
        public void onFailure(Call<LoginResult> call, Throwable t) {
            mLoginView.showMessage("请求失败"+t.getMessage());
            mLoginView.hideProgress();
        }
    };

}
