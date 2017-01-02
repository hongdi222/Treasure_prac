package com.feicuiedu.hunttreasure.user.register;

import android.os.AsyncTask;

/**
 * Created by gqq on 17/1/2.
 */

public class RegisterPresenter {

    private RegisterView mRegisterView;

    public RegisterPresenter(RegisterView registerView) {
        mRegisterView = registerView;
    }

    public void register(){
        new AsyncTask<Void, Integer, Void>() {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();

                mRegisterView.showProgress();

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

                mRegisterView.hideProgress();
                mRegisterView.showMessage("注册成功");
                mRegisterView.navigationToHome();

            }
        }.execute();
    }
}
