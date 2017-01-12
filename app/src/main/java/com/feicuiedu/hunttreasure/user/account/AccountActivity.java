package com.feicuiedu.hunttreasure.user.account;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.feicuiedu.hunttreasure.R;
import com.feicuiedu.hunttreasure.components.IconSelectWindow;
import com.feicuiedu.hunttreasure.user.UserPrefs;
import com.pkmmte.view.CircularImageView;

import org.hybridsquad.android.library.CropHandler;
import org.hybridsquad.android.library.CropHelper;
import org.hybridsquad.android.library.CropParams;

import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AccountActivity extends AppCompatActivity {

    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.iv_userIcon)
    ImageView mIvUserIcon;
    private IconSelectWindow mSelectWindow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);

        // 每次重新新入个人中心，更新用户头像
        String photoUrl = UserPrefs.getInstance().getPhoto();
        if (photoUrl != null) {
            Glide.with(this)
                    .load(photoUrl)
                    .placeholder(R.mipmap.user_icon)
                    .error(R.mipmap.user_icon)
                    .into(mIvUserIcon);
        }
    }

    @Override
    public void onContentChanged() {
        super.onContentChanged();
        ButterKnife.bind(this);

        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(getTitle());

    }

    @Override public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @OnClick(R.id.iv_userIcon)
    public void clickPhoto(){
        if (mSelectWindow==null) {
            mSelectWindow = new IconSelectWindow(this, listener);
        }
        if (mSelectWindow.isShowing()){
            mSelectWindow.dismiss();
            return;
        }
        mSelectWindow.show();
    }

    private IconSelectWindow.Listener listener = new IconSelectWindow.Listener() {

        @Override
        public void toGallery() {
            CropHelper.clearCachedCropFile(cropHandler.getCropParams().uri);
            Intent intent = CropHelper.buildCropFromGalleryIntent(cropHandler.getCropParams());
            startActivityForResult(intent, CropHelper.REQUEST_CROP);
        }

        @Override
        public void toCamera() {
            CropHelper.clearCachedCropFile(cropHandler.getCropParams().uri);
            Intent intent = CropHelper.buildCaptureIntent(cropHandler.getCropParams().uri);
            startActivityForResult(intent, CropHelper.REQUEST_CAMERA);
        }
    };

    private CropHandler cropHandler = new CropHandler() {

        // 完成
        @Override
        public void onPhotoCropped(Uri uri) {
            File file = new File(uri.getPath());
            new AccountPresenter().uploadPhoto(file);
        }

        // 取消
        @Override
        public void onCropCancel() {
            Toast.makeText(AccountActivity.this, "取消", Toast.LENGTH_SHORT).show();
        }

        // 失败
        @Override
        public void onCropFailed(String message) {
            Toast.makeText(AccountActivity.this, "失败", Toast.LENGTH_SHORT).show();

        }

        // 剪切参数
        @Override
        public CropParams getCropParams() {
            CropParams cropParams = new CropParams();
            // 文件的路径是在外部存储卡路径+crop_cache_file.jpg
            cropParams.aspectX = 300;
            cropParams.aspectY = 300;
            return cropParams;
        }

        // 上下文
        @Override
        public Activity getContext() {
            return AccountActivity.this;
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        CropHelper.handleResult(cropHandler, requestCode, resultCode, data);
    }

    @Override
    protected void onDestroy() {
        if (cropHandler.getCropParams() != null)
            CropHelper.clearCachedCropFile(cropHandler.getCropParams().uri);
        super.onDestroy();
    }
}
