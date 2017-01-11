package com.feicuiedu.hunttreasure.net;

import android.hardware.Camera;

import com.feicuiedu.hunttreasure.treasure.Area;
import com.feicuiedu.hunttreasure.treasure.Treasure;
import com.feicuiedu.hunttreasure.user.User;
import com.feicuiedu.hunttreasure.user.login.LoginResult;
import com.feicuiedu.hunttreasure.user.register.RegisterResult;

import java.util.List;

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

    @POST("/Handler/TreasureHandler.ashx?action=show")
    Call<List<Treasure>> getTreasureInArea(@Body Area body);


}
