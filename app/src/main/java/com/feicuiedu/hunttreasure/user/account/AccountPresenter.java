package com.feicuiedu.hunttreasure.user.account;

import android.util.Log;
import android.widget.Toast;

import com.feicuiedu.hunttreasure.net.NetClient;
import com.feicuiedu.hunttreasure.user.UserPrefs;

import java.io.File;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.http.Multipart;

/**
 * Created by gqq on 2017/1/12.
 */

public class AccountPresenter  {

    public void uploadPhoto(File file){
//        RequestBody body = RequestBody.create(null, file);
//        MultipartBody.Part part = MultipartBody.Part.createFormData("image", "photo.png", body);

        RequestBody body = MultipartBody.create(null,file);

        NetClient.getInstances().getTreasureApi().upload(body).enqueue(mUploadResultCallback);
    }

    private Callback<UploadResult> mUploadResultCallback = new Callback<UploadResult>() {
        @Override
        public void onResponse(Call<UploadResult> call, Response<UploadResult> response) {
            if (response.isSuccessful()){
                UploadResult uploadResult = response.body();
                if (uploadResult==null){
                    return;
                }
                if (uploadResult.getCount()!=1){
                    return;
                }
                String url = uploadResult.getUrl();
                UserPrefs.getInstance().setPhoto(url);

                String photo = url.substring(url.lastIndexOf("/")+1,url.length());
                Log.i("TAG","url:"+url+","+photo);
                Update update = new Update(UserPrefs.getInstance().getTokenid(),photo);
                NetClient.getInstances().getTreasureApi().update(update).enqueue(mResultCallback);

            }
        }

        @Override
        public void onFailure(Call<UploadResult> call, Throwable t) {

        }
    };

    private Callback<UpdateResult> mResultCallback= new Callback<UpdateResult>() {
        @Override
        public void onResponse(Call<UpdateResult> call, Response<UpdateResult> response) {
            if (response.isSuccessful()){

            }
        }

        @Override
        public void onFailure(Call<UpdateResult> call, Throwable t) {

        }
    };
}
