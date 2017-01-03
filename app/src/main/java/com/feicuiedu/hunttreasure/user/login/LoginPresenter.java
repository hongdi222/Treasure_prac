package com.feicuiedu.hunttreasure.user.login;

import android.os.AsyncTask;

/**
 * Created by gqq on 17/1/2.
 */

public class LoginPresenter {

    private LoginView mLoginView;

    /**
     * 两种方式拿到视图方法：
     * 1. Activity：要达到视图和业务分离，持有Activity的对象，并没有达到分离的效果
     * 2. 接口回调：
     */

    public LoginPresenter(LoginView loginView) {

        this.mLoginView = loginView;
    }

    public void login(){
        // 完成登陆

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

                mLoginView.showProgress();

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

                mLoginView.hideProgress();
                mLoginView.showMessage("登陆成功");
                mLoginView.navigationToHome();

            }
        }.execute();
    }

}
