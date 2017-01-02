package com.feicuiedu.hunttreasure.user.register;

/**
 * Created by gqq on 17/1/2.
 */

public interface RegisterView {

    void showProgress();

    void hideProgress();

    void showMessage(String msg);

    void navigationToHome();
}
