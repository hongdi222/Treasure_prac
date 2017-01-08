package com.feicuiedu.hunttreasure.user.register;

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

public class RegisterPresenter {

    private RegisterView mRegisterView;
    private Call<RegisterResult> mRegisterCall;

    public RegisterPresenter(RegisterView registerView) {
        mRegisterView = registerView;
    }

    public void register(User user){

        mRegisterView.showProgress();

        mRegisterCall = NetClient.getInstances().getTreasureApi().register(user);
        mRegisterCall.enqueue(mResultCallback);

    }

    private Callback<RegisterResult> mResultCallback = new Callback<RegisterResult>() {
        @Override
        public void onResponse(Call<RegisterResult> call, Response<RegisterResult> response) {
            mRegisterView.hideProgress();
            if (response.isSuccessful()){
                RegisterResult result = response.body();
                if (result==null){
                    mRegisterView.showMessage("未知错误");
                    return;
                }
                if (result.getCode()==1){
                    UserPrefs.getInstance().setTokenid(result.getTokenId());
                    mRegisterView.navigationToHome();
                }
                mRegisterView.showMessage(result.getMsg());
            }
        }

        @Override
        public void onFailure(Call<RegisterResult> call, Throwable t) {
            mRegisterView.hideProgress();
            mRegisterView.showMessage("请求失败"+t.getMessage());
        }
    };
}
