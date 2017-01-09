package com.feicuiedu.hunttreasure.net;

import com.feicuiedu.hunttreasure.user.User;
import com.feicuiedu.hunttreasure.user.login.LoginResult;
import com.feicuiedu.hunttreasure.user.register.RegisterResult;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * Created by gqq on 17/1/8.
 */

public interface TreasureApi {

    @POST("/Handler/UserHandler.ashx?action=register")
    Call<RegisterResult> register(@Body User user);

    @POST("/Handler/UserHandler.ashx?action=login")
    Call<LoginResult> login(@Body User user);



}
