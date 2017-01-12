package com.feicuiedu.hunttreasure.net;

import android.hardware.Camera;

import com.feicuiedu.hunttreasure.treasure.Area;
import com.feicuiedu.hunttreasure.treasure.Treasure;
import com.feicuiedu.hunttreasure.user.User;
import com.feicuiedu.hunttreasure.user.account.Update;
import com.feicuiedu.hunttreasure.user.account.UpdateResult;
import com.feicuiedu.hunttreasure.user.account.UploadResult;
import com.feicuiedu.hunttreasure.user.login.LoginResult;
import com.feicuiedu.hunttreasure.user.register.RegisterResult;

import java.io.File;
import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

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

    @Multipart
    @POST("/Handler/UserLoadPicHandler1.ashx")
    Call<UploadResult> upload(@Part("file\";filename=\"image.png\"") RequestBody part);

    @POST("/Handler/UserHandler.ashx?action=update")
    Call<UpdateResult> update(@Body Update update);


}
