package com.feicuiedu.hunttreasure.user.login;

/**
 * Created by gqq on 17/1/2.
 */

public interface LoginView {

    void showProgress();

    void hideProgress();

    void showMessage(String msg);

    void navigationToHome();

}
